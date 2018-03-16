package excelapp.util;


import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotTableDefinition;


import java.util.List;

public class PivotTable {


    public void createPivotTable(XSSFWorkbook workbook) {

        XSSFSheet reportSheet = workbook.createSheet("reportTable");

        XSSFSheet dataSheet = workbook.getSheet("report");


        //Create some data to build the pivot table on setCellData(sheet);

        AreaReference areaReference = new AreaReference("A1:I10000", SpreadsheetVersion.EXCEL2007);
        CellReference cellReference = new CellReference("A1");


        XSSFPivotTable pivotTable = reportSheet.createPivotTable(areaReference, cellReference, dataSheet);


        //Configure the pivot table

        pivotTable.addRowLabel(0);
        pivotTable.addRowLabel(1);
        pivotTable.addRowLabel(6);
        pivotTable.addRowLabel(8);
        pivotTable.addRowLabel(2);
        pivotTable.addRowLabel(3);
        pivotTable.addRowLabel(7);
        pivotTable.addColumnLabel(DataConsolidateFunction.SUM, 4, "Zamówione");
        pivotTable.addColumnLabel(DataConsolidateFunction.SUM, 5, "Zamówione Trena");



        CTPivotTableDefinition ctPivotTableDefinition = pivotTable.getCTPivotTableDefinition();

        List<CTPivotField> ctPivotFieldList = ctPivotTableDefinition.getPivotFields().getPivotFieldList();
        pivotTable.setCTPivotTableDefinition(ctPivotTableDefinition);


        for (CTPivotField pivotField : ctPivotFieldList) {
            pivotField.setOutline(false);
            pivotField.setMeasureFilter(true);
            //pivotField.setSubtotalCaption("");

        }

    }


}
