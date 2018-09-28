package org.swift.framework.util;


import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.*;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * excel文件转化为html格式的字符串，提供给前端预览
 * 提供 byte[]   InputStream   Workbook  三种方法
 * 支持 ratio 参数 设置缩放比例，默认为 1， 例如 0.5 为缩放一半  2 为方大一倍
 * 前端使用：document.getElementById('previewDetail').innerHTML = resultHtml;
 * create by zww
 **/
public class ExcelToHtmlUtil {

    private static final int DEFAULT_WIDTH = 10;    //乘数
    private static final int DEFAULT_HEIGHT = 15;   //除数

    /**
     * 把 Excel文件的byte[] 转换为html格式
     * @param buffer
     * @return
     */
    public static String getExcelInfo(byte[] buffer){
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        return getExcelInfo(bais, null);
    }

    //居中样式显式
    public static String getExcelInfo(byte[] buffer, HtmlViewType copy){
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        return getExcelInfo(bais, copy);
    }

    /**
     * @param ratio  前端缩放比例
     */
    public static String getExcelInfo(byte[] buffer, double ratio){
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        return getExcelInfo(bais, ratio, null);
    }

    /**
     * 把 Excel数据流 转化为html格式
     * @param is
     * @return
     */
    public static String getExcelInfo(InputStream is, HtmlViewType copy) {
        try {
            if (!is.markSupported()) {
                is = new PushbackInputStream(is, 8);
            }
            if (POIFSFileSystem.hasPOIFSHeader(is)) {   // Excel 2003  xls
                return getExcelInfo(Workbook.getWorkbook(is), 1, copy);
            }
            if (POIXMLDocument.hasOOXMLHeader(is)) {    // Excel 2007  xlsx
                XSSFWorkbook wb = new XSSFWorkbook(OPCPackage.open(is));
                return getExcelInfo(wb,1, copy);
            }
            System.out.println("excel版本目前poi解析不了");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * @param ratio  前端缩放比例
     */
    public static String getExcelInfo(InputStream is, double ratio, HtmlViewType copy) {
        try {
            if (!is.markSupported()) {
                is = new PushbackInputStream(is, 8);
            }
            if (POIFSFileSystem.hasPOIFSHeader(is)) {   // Excel 2003  xls
                return getExcelInfo(Workbook.getWorkbook(is), ratio, copy);
            }
            if (POIXMLDocument.hasOOXMLHeader(is)) {    // Excel 2007  xlsx
                XSSFWorkbook wb = new XSSFWorkbook(OPCPackage.open(is));
                return getExcelInfo(wb,ratio, copy);
            }
            System.out.println("excel版本目前poi解析不了");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 把 Workbook对象 转换为html样式的String
     * @param rwb
     * @return
     */
    public static String getExcelInfo(Workbook rwb, double ratio, HtmlViewType copy){
        Sheet sheet = null;
        //检查空文件
        if (rwb == null || (sheet = rwb.getSheet(0)) == null) {
            return null;
        }
        StringBuffer resultHtml = new StringBuffer();
        int colnumCount = sheet.getColumns();
        int rownumCount = sheet.getRows();
        int[] columnWidths = new int[colnumCount];//每列的宽
        int totilColumn = 0;    //table的宽
        int tempSize = 0;
        for (int i = 0; i < colnumCount; i++) {
            tempSize = (int) (sheet.getColumnWidth(i) * DEFAULT_WIDTH * ratio);    //乘10是为了对应html的宽
            columnWidths[i] = tempSize;
            totilColumn += tempSize;
        }

        Map<String, String> map[] = getMergedMap(sheet);    //获取合并单元格的信息
        resultHtml.append("<table align='center' border='1' cellspacing='0' frame='box' bgcolor='#FAEBD7' width='" + totilColumn + "'>");//表头.界面样式被覆盖
        //resultHtml.append("<table align='center' border='1' cellspacing='0' frame='box' width='" + totilColumn + "'>");//界面样式冲突 不能设置 颜色
        for (int row = 0; row < rownumCount; row++) {
            resultHtml.append("<tr height='" + (int) (sheet.getRowHeight(row)/DEFAULT_HEIGHT  * ratio) + "'>"); //行高
            for (int col = 0; col < colnumCount; col++) {
                Cell cell = sheet.getCell(col, row);
                String content = cell.getContents();    //单元格内容
                CellFormat cellFormat = cell.getCellFormat();   //单元格样式
                //判断是否属于首个合并单元格
                if (map[0].containsKey(row + "," + col)) {
                    //获得右下角单元格
                    String pointString = map[0].get(row + "," + col);
                    map[0].remove(row + "," + col);
                    int bottomeRow = Integer.valueOf(pointString.split(",")[0]);
                    int bottomeCol = Integer.valueOf(pointString.split(",")[1]);
                    int rowSpan = bottomeRow - row + 1; //合并的行数
                    int colSpan = bottomeCol - col + 1; //合并的列数
                    tempSize = 0;   //合并单元格的宽
                    for(int i = 0; i < colSpan; i++) {
                        tempSize += columnWidths[col + i];
                    }
                    resultHtml.append("<td width='" + tempSize + "' rowspan= '" + rowSpan + "' colspan= '" + colSpan + "' ");
                } else if (map[1].containsKey(row + "," + col)) {   //左上角之外的合并单元格
                    map[1].remove(row + "," + col);
                    continue;
                } else {
                    resultHtml.append("<td width='" + columnWidths[col] + "' ");
                }
                Font font = null;
                if (cellFormat != null) {
                    font = cellFormat.getFont();
                    Alignment alignment = cellFormat.getAlignment();
                    if (copy !=null && copy.getCode().equals(HtmlViewType.HORIZON_CENTER.getCode())) {
                        resultHtml.append("align='center' ");
                    } else {
                        resultHtml.append("align='" + convertToHtmlGrammer(alignment) + "' ");
                    }
                    VerticalAlignment verticalAlignment = cellFormat.getVerticalAlignment();
                    resultHtml.append("valign='" + convertToHtmlGrammer(verticalAlignment) + "' ");
                    //stringBuffer.append("style='color:" + getRgbByColor(cellFormat.getFont().getColour()) + " ");  //
                    //stringBuffer.append("bgcolor='" + getRgbByColor(cellFormat.getBackgroundColour()) + "'");  //从cell获取的颜色信息是错乱的，所以不使用

                    // stringBuffer.append("border-color:" + convertToHtmlGrammer(bottomColour) + ";");  //
                    //stringBuffer.append(" ");
                }
                resultHtml.append(">");

                //判断字体粗细
                if (font != null) {
                    int boldWeight = font.getBoldWeight();
                    //if (boldWeight > 550) {  //取值在  400 ---- 700  之间
                    resultHtml.append("<span style='border: 0px; padding: 0px; font-weight: " + boldWeight + "'>");  //设置字体粗细
                    //}
                }

                if (content == null || "".equals(content.trim())) {
                    resultHtml.append("   ");
                } else {
                    resultHtml.append(content);
                }
                if (font != null) {
                    resultHtml.append("</span></td>");
                } else {
                    resultHtml.append("</td>");
                }

            }
            resultHtml.append("</tr>");
        }
        resultHtml.append("</table>");
        rwb.close();
        System.out.println("预览文件：" + resultHtml);
        return resultHtml.toString();
    }

    public static String getExcelInfo(XSSFWorkbook rwb, double ratio, HtmlViewType copy){ //Excel 2007
        XSSFSheet sheet = null;
        //检查空文件
        if (rwb == null || (sheet = rwb.getSheetAt(0)) == null) {
            return null;
        }
        StringBuffer resultHtml = new StringBuffer();
        int colnumCount = sheet.getRow(0).getPhysicalNumberOfCells();
        int rownumCount = sheet.getLastRowNum();
        int[] columnWidths = new int[colnumCount];//每列的宽
        int totilColumn = 0;    //table的宽
        int tempSize = 0;
        for (int i = 0; i < colnumCount; i++) {
            tempSize = (int) (sheet.getColumnWidth(i)  / 25.6 * ratio);
            columnWidths[i] = tempSize;
            totilColumn += tempSize;
        }

        Map<String, String> map[] = getMergedMap(sheet);    //获取合并单元格的信息
        resultHtml.append("<table align='center' border='1' cellspacing='0' frame='box' bgcolor='#FAEBD7' width='" + totilColumn + "'>");//表头.界面样式被覆盖
        //resultHtml.append("<table align='center' border='1' cellspacing='0' frame='box' width='" + totilColumn + "'>");//界面样式冲突 不能设置 颜色
        for (int row = 0; row <= rownumCount; row++) {
            resultHtml.append("<tr height='" + (int)(sheet.getRow(row).getHeight() /DEFAULT_HEIGHT  * ratio) + "'>"); //行高
            for (int col = 0; col < colnumCount; col++) {
                XSSFCell cell = sheet.getRow(row).getCell(col);
                String content = getCellValue(cell);   //单元格内容
                XSSFCellStyle cellFormat = cell.getCellStyle();   //单元格样式
                //判断是否属于首个合并单元格
                if (map[0].containsKey(row + "," + col)) {
                    //获得右下角单元格
                    String pointString = map[0].get(row + "," + col);
                    map[0].remove(row + "," + col);
                    int bottomeRow = Integer.valueOf(pointString.split(",")[0]);
                    int bottomeCol = Integer.valueOf(pointString.split(",")[1]);
                    int rowSpan = bottomeRow - row + 1; //合并的行数
                    int colSpan = bottomeCol - col + 1; //合并的列数
                    tempSize = 0;   //合并单元格的宽
                    for(int i = 0; i < colSpan; i++) {
                        tempSize += columnWidths[col + i];
                    }
                    resultHtml.append("<td width='" + tempSize + "' rowspan= '" + rowSpan + "' colspan= '" + colSpan + "' ");
                } else if (map[1].containsKey(row + "," + col)) {   //左上角之外的合并单元格
                    map[1].remove(row + "," + col);
                    continue;
                } else {
                    resultHtml.append("<td width='" + columnWidths[col] + "' ");
                }
                XSSFFont font = null;
                if (cellFormat != null) {
                    font = cellFormat.getFont();
                    short alignment = cellFormat.getAlignment();
                    if (copy !=null && copy.getCode().equals(HtmlViewType.HORIZON_CENTER.getCode())) {
                        resultHtml.append("align='center' ");
                    } else {
                        resultHtml.append("align='" + convertToHtmlGrammer(alignment) + "' ");
                    }
                    short verticalAlignment = cellFormat.getVerticalAlignment();
                    resultHtml.append("valign='" + convertToHtmlGrammerVertical(verticalAlignment) + "' ");
                    //stringBuffer.append("style='color:" + getRgbByColor(cellFormat.getFont().getColour()) + " ");  //
                    //stringBuffer.append("bgcolor='" + getRgbByColor(cellFormat.getBackgroundColour()) + "'");  //从cell获取的颜色信息是错乱的，所以不使用

                    // stringBuffer.append("border-color:" + convertToHtmlGrammer(bottomColour) + ";");  //
                    //stringBuffer.append(" ");
                }
                resultHtml.append(">");

                //判断字体粗细
                if (font != null) {
                    int boldWeight = font.getBoldweight();
                    //if (boldWeight > 550) {  //取值在  400 ---- 700  之间
                    resultHtml.append("<span style='border: 0px; padding: 0px; font-weight: " + boldWeight + "'>");  //设置字体粗细
                    //}
                }

                if (content == null || "".equals(content.trim())) {
                    resultHtml.append("   ");
                } else {
                    resultHtml.append(content);
                }
                if (font != null) {
                    resultHtml.append("</span></td>");
                } else {
                    resultHtml.append("</td>");
                }

            }
            resultHtml.append("</tr>");
        }
        resultHtml.append("</table>");
        try {
            rwb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("预览文件：" + resultHtml);
        return resultHtml.toString();
    }

    private static String getCellValue(XSSFCell cell) {
        String result = "";
        if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
            return cell.getNumericCellValue() + "";
        }
        return result;
    }

    /**
     * 获取合并的单元格，map1为左上角单元格集合，map1为除首个单元格之外的合并单元格
     * @param sheet
     * @return
     */
    private static Map<String, String>[] getMergedMap(Sheet sheet) {
        Map<String, String> map0 = new HashMap<String, String>();
        Map<String, String> map1 = new HashMap<String, String>();
        Range[] range = sheet.getMergedCells();
        for (int i = 0; i < range.length; i++) {
            Cell topCell = range[i].getTopLeft();
            Cell bottomCell = range[i].getBottomRight();
            int topRow = topCell.getRow();
            int topCol = topCell.getColumn();
            int bottomRow = bottomCell.getRow();
            int bottomCol = bottomCell.getColumn();
            //合并单元格的左上、右下
            map0.put(topRow + "," + topCol, bottomRow + "," + bottomCol);
            int tempRow = topRow;
            while (tempRow <= bottomRow) {
                int tempCol = topCol;
                while (tempCol <= bottomCol) {
                    //合并的单元格的每个小格
                    map1.put(tempRow + "," + tempCol, "");
                    tempCol++;
                }
                tempRow++;
            }
            map1.remove(topRow + "," + topCol);
        }
        Map[] map = { map0, map1 };
        return map;
    }

    private static Map<String, String>[] getMergedMap(XSSFSheet sheet) {
        Map<String, String> map0 = new HashMap<String, String>();
        Map<String, String> map1 = new HashMap<String, String>();

        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int topRow = ca.getFirstRow();
            int topCol = ca.getFirstColumn();
            int bottomRow = ca.getLastRow();
            int bottomCol = ca.getLastColumn();
            //合并单元格的左上、右下
            map0.put(topRow + "," + topCol, bottomRow + "," + bottomCol);
            int tempRow = topRow;
            while (tempRow <= bottomRow) {
                int tempCol = topCol;
                while (tempCol <= bottomCol) {
                    //合并的单元格的每个小格
                    map1.put(tempRow + "," + tempCol, "");
                    tempCol++;
                }
                tempRow++;
            }
            map1.remove(topRow + "," + topCol);
        }
        Map[] map = { map0, map1 };
        return map;
    }

    /**
     * 水平对齐转换为html格式
     * @param alignment
     * @return
     */
    private static String convertToHtmlGrammer(Alignment alignment) {
        String align = "left";
        switch (alignment.getValue()) {
            case 1:
                align = "left";
                break;
            case 2:
                align = "center";
                break;
            case 3:
                align = "right";
                break;
            case 5:
                align = "justify";
                break;
            default:
                break;
        }
        return align;
    }

    private static String convertToHtmlGrammer(short alignment) {
        String align = "left";
        switch (alignment) {
            case 1:
                align = "left";
                break;
            case 2:
                align = "center";
                break;
            case 3:
                align = "right";
                break;
            case 5:
                align = "justify";
                break;
            default:
                break;
        }
        return align;
    }

    /**
     * 垂直对齐转换为html格式
     * @param verticalAlignment
     * @return
     */
    private static String convertToHtmlGrammer(VerticalAlignment verticalAlignment) {
        String valign = "middle";
        switch (verticalAlignment.getValue()) {
            case 1:
                valign = "middle";
                break;
            case 2:
                valign = "bottom";
                break;
            case 3:
                valign = "top";
                break;
            default:
                break;
        }
        return valign;
    }

    private static String convertToHtmlGrammerVertical(short verticalAlignment) {
        String valign = "middle";
        switch (verticalAlignment) {
            case 1:
                valign = "middle";
                break;
            case 2:
                valign = "bottom";
                break;
            case 3:
                valign = "top";
                break;
            default:
                break;
        }
        return valign;
    }

    /**
     * 返回对应的RGB参数
     * @param colour
     * @return
     */
    private static StringBuffer getRgbByColor(Colour colour) {
        StringBuffer rgb = new StringBuffer("rgb(");
        rgb.append(colour.getDefaultRGB().getRed() + ",");
        rgb.append(colour.getDefaultRGB().getGreen() + ",");
        rgb.append(colour.getDefaultRGB().getBlue() + ")");
        return rgb;
    }

    public static byte[] getBytesData(org.apache.poi.ss.usermodel.Workbook wb) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            wb.write(bos);
        } catch (IOException e) {
            System.out.println("wb读取bytes出错");
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                System.out.println("关闭stream出错");
            }
        }
        return bos.toByteArray();
    }

}
