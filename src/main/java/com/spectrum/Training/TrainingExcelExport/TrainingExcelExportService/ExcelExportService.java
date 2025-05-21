package com.spectrum.Training.TrainingExcelExport.TrainingExcelExportService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ExcelExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ByteArrayInputStream exportToExcel(List<Map<String, Object>> reportData, String[] headers, String[] keys) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Report");
            createHeaderRow(workbook, sheet, headers);
            populateDataRows(workbook, sheet, reportData, keys);
            autoSizeColumns(sheet, headers.length);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private void createHeaderRow(Workbook workbook, Sheet sheet, String[] headers) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(workbook);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private void populateDataRows(Workbook workbook, Sheet sheet, List<Map<String, Object>> reportData, String[] keys) {
        CellStyle dateStyle = createDateStyle(workbook);
        int rowIdx = 1;

        for (Map<String, Object> data : reportData) {
            Row row = sheet.createRow(rowIdx++);
            for (int colIdx = 0; colIdx < keys.length; colIdx++) {
                Object value = data.getOrDefault(keys[colIdx], "");
                createCell(workbook, row, colIdx, value, dateStyle);
            }
        }
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
        return dateStyle;
    }

    private void createCell(Workbook workbook, Row row, int colIdx, Object value, CellStyle dateStyle) {
        Cell cell = row.createCell(colIdx);

        if (value instanceof Date) {
            cell.setCellValue((Date) value);
            cell.setCellStyle(dateStyle);
        } else if (value instanceof java.time.LocalDate) {
            cell.setCellValue(((java.time.LocalDate) value).format(DATE_FORMATTER));
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, Math.min(sheet.getColumnWidth(i) + 1024, 256 * 256));
        }
    }
}
