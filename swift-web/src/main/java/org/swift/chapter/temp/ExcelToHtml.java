package org.swift.chapter.temp;


import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.CellFormat;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * create by ww
 **/
public class ExcelToHtml {

    public static String getExcelInfo(File sourcefile) throws Exception {
        StringBuffer sb = new StringBuffer();
        InputStream is = new FileInputStream(sourcefile);
        Workbook rwb = Workbook.getWorkbook(is);
        Sheet sheet = rwb.getSheet(0);
        int colnum = sheet.getColumns();
        int rownum = sheet.getRows();
        Map<String, String> map[] = getRowSpanColSpanMap(sheet);
        sb.append("<table border='1' cellspacing='0' frame='box'>");
        for (int row = 0; row < rownum; row++) {
            sb.append("<tr>");
            for (int col = 0; col < colnum; col++) {
                Cell cell = sheet.getCell(col, row);
                String content = cell.getContents();
                CellFormat cellFormat = cell.getCellFormat();
                //判断是否属于合并单元格
                if (map[0].containsKey(row + "," + col)) {
                    //获得右下角单元格
                    String pointString = map[0].get(row + "," + col);
                    map[0].remove(row + "," + col);
                    int bottomeRow = Integer.valueOf(pointString.split(",")[0]);
                    int bottomeCol = Integer.valueOf(pointString.split(",")[1]);
                    //行距
                    int rowSpan = bottomeRow - row + 1;
                    //列距
                    int colSpan = bottomeCol - col + 1;
                    sb.append("<td rowspan= '" + rowSpan + "' colspan= '" + colSpan + "' ");
                } else if (map[1].containsKey(row + "," + col)) {   //左上角之外的合并单元格
                    map[1].remove(row + "," + col);
                    continue;
                } else {
                    sb.append("<td ");
                }
                if (cellFormat != null) {
                    Alignment alignment = cellFormat.getAlignment();
                    sb.append("align='" + convertToHtmlGrammer(alignment) + "' ");
                    VerticalAlignment verticalAlignment = cellFormat.getVerticalAlignment();
                    sb.append("valign='" + convertToHtmlGrammer(verticalAlignment) + "' ");
                    //sb.append("style='color:" + getRgbByColor(cellFormat.getFont().getColour()) + " ");  //
                    sb.append("bgcolor:" + getRgbByColor(cellFormat.getBackgroundColour()) + ";");  //

                    // sb.append("border-color:" + convertToHtmlGrammer(bottomColour) + ";");  //
                    sb.append("' ");
                }
                sb.append(">");
                if (content == null || "".equals(content.trim())) {
                    sb.append("   ");
                } else {
                    sb.append(content);
                }
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        rwb.close();
        is.close();
        return sb.toString();
    }

    private static StringBuffer getRgbByColor(Colour colour) {
        StringBuffer rgb = new StringBuffer("rgb(");
        rgb.append(colour.getDefaultRGB().getRed() + ",");
        rgb.append(colour.getDefaultRGB().getGreen() + ",");
        rgb.append(colour.getDefaultRGB().getBlue() + ")");
        return rgb;
    }

    private static Map<String, String>[] getRowSpanColSpanMap(Sheet sheet) {
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

    private static String fillWithZero(String str) {
        if (str != null && str.length() < 2) {
            return "0" + str;
        }
        return str;
    }


}
