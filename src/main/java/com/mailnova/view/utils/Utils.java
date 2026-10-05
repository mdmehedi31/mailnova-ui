package com.mailnova.view.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.util.List;

public class Utils {

    public static List<String> colors =List.of(
            "#4F46E5",
            "#0EA5E9",
            "#22C55E",
            "#F59E0B",
            "#A855F7",
            "#6366F1",
            "#06B6D4",
            "#F97316",
            "#EC4899",
            "#EF4444",
            "#14B8A6",
            "#EAB308",
            "#3B82F6"
    );

    private static final String[] HEADERS = { "Name", "Email" };


    public static Instant buildEmailScheduleTime(LocalDate date,LocalTime time, String timeZone){
        ZoneId zone = ZoneId.of(timeZone);
        LocalDateTime dateTime = LocalDateTime.of(
               date, time
        );
        Instant instant = dateTime
                .atZone(zone)
                .toInstant();

        return instant;
    }

    public static InputStream generateEmailCampaignFile() {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Template");

            // Header style (optional, but makes it look nicer)
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 20 * 256); // reasonable column width
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel template", e);
        }
    }
}
