package excelapp.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class WorkbookHeader {

    public WorkbookHeader() {
    }

    public Row createHeaderRow(Workbook workbook, Sheet sheet) {

        Row headerRow = sheet.createRow(0);

        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontHeight(12);
        font.setColor(Font.COLOR_RED);
        font.setBold(true);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillBackgroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        headerRow.createCell(0).setCellValue("Symbol");
        headerRow.getCell(0).setCellStyle(style);

        headerRow.createCell(1).setCellValue("Nazwa Produktu");
        headerRow.getCell(1).setCellStyle(style);
        headerRow.createCell(2).setCellValue("Cena");
        headerRow.getCell(2).setCellStyle(style);
        headerRow.createCell(3).setCellValue("Ilość FV dostawcy");
        headerRow.getCell(3).setCellStyle(style);
        headerRow.createCell(4).setCellValue("Ilość zamówiona");
        headerRow.getCell(4).setCellStyle(style);


        headerRow.createCell(5).setCellValue("Dostawca");
        headerRow.getCell(5).setCellStyle(style);
        headerRow.createCell(6).setCellValue("Trena");
        headerRow.getCell(6).setCellStyle(style);
        headerRow.createCell(7).setCellValue("STAN MAGAZYNOWY");
        headerRow.getCell(7).setCellStyle(style);

        headerRow.createCell(8).setCellValue("Nr ZD");
        headerRow.getCell(8).setCellStyle(style);

        headerRow.createCell(9).setCellValue("Grupa");
        headerRow.getCell(9).setCellStyle(style);
        headerRow.createCell(10).setCellValue("Waluta");
        headerRow.getCell(10).setCellStyle(style);
        headerRow.createCell(11).setCellValue("nrZD");
        headerRow.getCell(11).setCellStyle(style);
        headerRow.createCell(12).setCellValue("Data Dostawy");
        headerRow.getCell(12).setCellStyle(style);


        return headerRow;

    }


    public List<String> getUpdateList(List<String> listOfOrderNameWithDateOfModifiedBefore, List<String> listOfOrderNameWithDateOfModifiedActual) {
        List<String> updateList = new LinkedList<>();

        List<String> sumList = new LinkedList<>();
        List<String> transferList = new LinkedList<>();
        List<String> formatList = new LinkedList<>();

        sumList.addAll(listOfOrderNameWithDateOfModifiedActual);
        sumList.addAll(listOfOrderNameWithDateOfModifiedBefore);

        Collections.sort(sumList);


        for (String orderNow : sumList) {
            if (!transferList.contains(orderNow)){
                transferList.add(orderNow);
            } else {
                transferList.remove(orderNow);
            }
        }


        for (String orderNow : transferList) {
            String zdNumber = orderNow.substring(0,orderNow.indexOf(" "));
            formatList.add(zdNumber);
        }

        // We want to delete old version of order, then add new one
        for (String orderNow : formatList) {
            if (!updateList.contains(orderNow)){
                updateList.add(orderNow);
            }
        }

        System.out.println("----------------------");
        for (String orderNow : updateList) {
            System.out.println(orderNow);

        }
        return updateList;
    }


}
