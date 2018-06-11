package org.swift.framework.util;


import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * excel文件转化为html格式的字符串，提供给前端预览
 * 提供 byte[]   InputStream   Workbook  三种方法
 * 前端使用：document.getElementById('previewDetail').innerHTML = resultHtml;
 * create by zww
 **/
public class ExcelToHtmlUtil {

    /**
     * 把 Excel文件的byte[] 转换为html格式
     * @param buffer
     * @return
     */
    public static String getExcelInfo(byte[] buffer){
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        return getExcelInfo(bais);
    }

    /**
     * 把 Excel数据流 转化为html格式
     * @param is
     * @return
     */
    public static String getExcelInfo(InputStream is) {
        try {
            return getExcelInfo(Workbook.getWorkbook(is));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把 Workbook对象 转换为html样式的String
     * @param rwb
     * @return
     */
    public static String getExcelInfo(Workbook rwb){
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
            tempSize = sheet.getColumnWidth(i) * 10;    //乘10是为了对应html的宽
            columnWidths[i] = tempSize;
            totilColumn += tempSize;
        }

        Map<String, String> map[] = getMergedMap(sheet);    //获取合并单元格的信息
        resultHtml.append("<table align='center' border='1' cellspacing='0' frame='box' bgcolor='#FAEBD7' width='" + totilColumn + "'>");//表头.界面样式被覆盖
        //resultHtml.append("<table align='center' border='1' cellspacing='0' frame='box' width='" + totilColumn + "'>");//界面样式冲突 不能设置 颜色
        for (int row = 0; row < rownumCount; row++) {
            int rowHeight = sheet.getRowHeight(row);
            resultHtml.append("<tr height='" + rowHeight/15 + "'>"); //行高
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
                if (cellFormat != null) {
                    Alignment alignment = cellFormat.getAlignment();
                    resultHtml.append("align='" + convertToHtmlGrammer(alignment) + "' ");
                    VerticalAlignment verticalAlignment = cellFormat.getVerticalAlignment();
                    resultHtml.append("valign='" + convertToHtmlGrammer(verticalAlignment) + "' ");
                    //stringBuffer.append("style='color:" + getRgbByColor(cellFormat.getFont().getColour()) + " ");  //
                    //stringBuffer.append("bgcolor='" + getRgbByColor(cellFormat.getBackgroundColour()) + "'");  //从cell获取的颜色信息是错乱的，所以不使用

                    // stringBuffer.append("border-color:" + convertToHtmlGrammer(bottomColour) + ";");  //
                    //stringBuffer.append(" ");
                }
                resultHtml.append(">");
                if (content == null || "".equals(content.trim())) {
                    resultHtml.append("   ");
                } else {
                    resultHtml.append(content);
                }
                resultHtml.append("</td>");
            }
            resultHtml.append("</tr>");
        }
        resultHtml.append("</table>");
        rwb.close();
        System.out.println("预览文件：" + resultHtml);
        return resultHtml.toString();
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

}
