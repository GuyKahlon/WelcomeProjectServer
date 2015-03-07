package com.citi.innovaciti.welcome.utils;

import com.citi.innovaciti.welcome.domain.Host;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Liron
 * Date: 05/03/15
 * Time: 20:19
 * To change this template use File | Settings | File Templates.
 */
public class ExcelFileHostsExtractor {

    private final static Logger log = LoggerFactory.getLogger(ExcelFileHostsExtractor.class);

    private static final int FIRST_NAME_COLUMN_INDEX = 0;
    private static final int LAST_NAME_COLUMN_INDEX = 1;
    private static final int EMAIL_COLUMN_INDEX = 2;
    private static final int PHONE_NUMBER_COLUMN_INDEX = 3;
    public static final String XLS = "xls";
    public static final String XLSX = "xlsx";


    public static List<Host> getHostsFromExcelFileInputStream(InputStream inputStream, String fileExtension) throws IOException {

        List<Host> hosts = new ArrayList<Host>();

        if (!(fileExtension.equals(XLS) || fileExtension.equals(XLSX))) {
            throw new IllegalArgumentException("File type is neither " + XLS + " nor " + XLSX);
        }

        Workbook workbook = null;

        try {

            if (fileExtension.equals(XLSX)) {

                workbook = new XSSFWorkbook(inputStream);    //try to create a workbook for "xlsx"
            } else {
                workbook = new HSSFWorkbook(inputStream); //try to create a workbook for "xls"
            }

        } catch (IOException ex) {
            log.error("Failed creating a workbook", ex);
            throw ex;
        } finally {
            inputStream.close();
        }


        if (workbook == null) {
            log.error("workbook is null");
            throw new IllegalStateException("workbook is null");
        }

        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) {
            log.error("Sheet is null");
            throw new IllegalStateException("Sheet is null");
        }

        Iterator<Row> rows = sheet.rowIterator();

        while (rows.hasNext()) {
            Row currRow = rows.next();
            Host currHost = getHostFromRow(currRow);
            hosts.add(currHost);
        }

        return hosts;
    }

    private static Host getHostFromRow(Row row) {

        String firstName = (String) getCellValue(row.getCell(FIRST_NAME_COLUMN_INDEX));
        String lastName = (String) getCellValue(row.getCell(LAST_NAME_COLUMN_INDEX));
        String email = (String) getCellValue(row.getCell(EMAIL_COLUMN_INDEX));
        String phoneNumber = (String) getCellValue(row.getCell(PHONE_NUMBER_COLUMN_INDEX));

        Host host = new Host();
        host.setFirstName(firstName);
        host.setLastName(lastName);
        host.setEmail(email);
        host.setPhoneNumber(phoneNumber);
        return host;

    }

    private static String getCellValue(Cell cell) {

        if (cell == null) {
            return null;
        }

        String cellValue = null;

        try {

            cellValue = cell.getStringCellValue();

        } catch (IllegalStateException e) {

            if (cell.getColumnIndex() == PHONE_NUMBER_COLUMN_INDEX) {  //this column might contain a numeric vale, if so convert it to string
                cellValue = String.valueOf((int) cell.getNumericCellValue());
            }
        }

        return cellValue;

    }
}
