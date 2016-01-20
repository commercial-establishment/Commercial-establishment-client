package kz.hts.ce.util;

import kz.hts.ce.model.dto.ProductDto;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WriteExcelFileExample {
    private static final String FILE_PATH = "/Users/Yakov/testWriteStudents.xlsx";/*TODO add path*/

    public static void writeProductsToExcel(List<ProductDto> products) {
        Workbook workbook = new XSSFWorkbook();

        Sheet studentsSheet = workbook.createSheet("Products");

        int rowIndex = 0;
        Row rowTitle = studentsSheet.createRow(rowIndex++);
        int cellIndex = 0;
        rowTitle.createCell(cellIndex++).setCellValue("Some Field");
        rowTitle.createCell(cellIndex++).setCellValue("Some Field");
        rowTitle.createCell(cellIndex++).setCellValue("Some Field");
        rowTitle.createCell(cellIndex++).setCellValue("Some Field");
        rowTitle.createCell(cellIndex++).setCellValue("Some Field");
        rowTitle.createCell(cellIndex++).setCellValue("Some Field");
        rowTitle.createCell(cellIndex++).setCellValue("Some Field");
        for (ProductDto product : products) {
            Row row = studentsSheet.createRow(rowIndex++);
            cellIndex = 0;
            row.createCell(cellIndex++).setCellValue(product.getName());
//            row.createCell(cellIndex++).setCellValue(product.getUnitName());
            row.createCell(cellIndex++).setCellValue(product.getArrival());
            row.createCell(cellIndex++).setCellValue(product.getOldAmount());
            row.createCell(cellIndex++).setCellValue(product.getResidue());
            row.createCell(cellIndex++).setCellValue(String.valueOf(product.getPrice()));
//            row.createCell(cellIndex++).setCellValue(String.valueOf(product.getTotalPrice()));
            row.createCell(cellIndex++).setCellValue(String.valueOf(product.getFinalPrice()));
            row.createCell(cellIndex++).setCellValue(product.getSoldAmount());
        }

        try {
            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            workbook.write(fos);
            fos.close();

            System.out.println(FILE_PATH + " is successfully written");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
