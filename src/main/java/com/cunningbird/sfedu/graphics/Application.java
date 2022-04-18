package com.cunningbird.sfedu.graphics;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Application {

    public static void main(String[] args) throws URISyntaxException, IOException, CsvException {
        File csvFile = new File(Objects.requireNonNull(Application.class.getClassLoader().getResource("csv/source.csv")).getFile());
        Reader csvReader = new FileReader(csvFile);
        List<String[]> csvData = readCsv(csvReader);
        printTable(csvData);

        Path csvDestinationPath = Paths.get(ClassLoader.getSystemResource("csv/destination.csv").toURI());
        writeCsv(csvData, csvDestinationPath);

        File excelFile = new File(Objects.requireNonNull(Application.class.getClassLoader().getResource("excel/source.xlsx")).getFile());
        FileInputStream excelFileIs = new FileInputStream(excelFile);
        List<String[]> excelData = readExcel(excelFileIs);
        printTable(excelData);

        Path excelDestinationPath = Paths.get(ClassLoader.getSystemResource("excel/destination.xlsx").toURI());
        writeExcel(csvData, excelDestinationPath);
    }

    private static List<String[]> readCsv(Reader reader) throws IOException, CsvException {
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();

        List<String[]> list = csvReader.readAll();
        reader.close();
        csvReader.close();

        return list;
    }

    private static List<String[]> readExcel(FileInputStream file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file);
        Sheet datatypeSheet = workbook.getSheetAt(0);

        List<String[]> list = new ArrayList<>();

        for (Row row : datatypeSheet) {
            String[] current = new String[row.getPhysicalNumberOfCells()];
            int i = 0;
            for (Cell cell : row) {
                current[i++] = cell.getStringCellValue();
            }
            list.add(current);
        }

        return list;
    }

    private static void writeCsv(List<String[]> table, Path path) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(path.toString()));
        for (String[] array : table) {
            writer.writeNext(array);
        }

        writer.close();
    }

    private static void writeExcel(List<String[]> table, Path path) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Result");

        int rowNum = 0;
        for (Object[] datatype : table) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : datatype) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }
        }

        FileOutputStream outputStream = new FileOutputStream(String.valueOf(path));
        workbook.write(outputStream);
        workbook.close();
    }

    private static void printTable(List<String[]> table) {
        table.forEach(row -> {
            for (String cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        });
        System.out.println();
    }
}
