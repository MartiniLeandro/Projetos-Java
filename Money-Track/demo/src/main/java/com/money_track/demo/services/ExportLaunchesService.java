package com.money_track.demo.services;

import com.money_track.demo.entities.DTO.LaunchesFilterDTO;
import com.money_track.demo.entities.Launch;
import com.money_track.demo.entities.User;
import com.money_track.demo.repositories.LaunchRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExportLaunchesService {

    private final LaunchRepository launchRepository;

    public ExportLaunchesService(LaunchRepository launchRepository) {
        this.launchRepository = launchRepository;
    }

    public byte[] CreateExcelFile(LaunchesFilterDTO data){
        User user =  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LocalDate initialDate = data.initialDate() != null ? data.initialDate() : LocalDate.now().withDayOfMonth(1);
        LocalDate  finalDate = data.finalDate() != null ? data.finalDate() : LocalDate.now();
        List<Launch> launches = launchRepository.getLaunchesWithFilters(user.getId(),data.typeValue() != null ? data.typeValue().name() : null, data.categoryId(), initialDate,finalDate, data.description());
        try(Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Launches");

            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row headerRow = sheet.createRow(0);
            String[] columns = {"Date","Description","Category","Type","Value"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Launch launch : launches) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(launch.getDate().toString());
                row.createCell(1).setCellValue(launch.getDescription());
                row.createCell(2).setCellValue(launch.getCategory().getName());
                row.createCell(3).setCellValue(launch.getCategory().getTypeValue().name());
                row.createCell(4).setCellValue(launch.getValue());
            }

            for(int i=0;i<columns.length;i++){
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }catch (IOException e){
           throw new RuntimeException("Falha interna ao gerar o arquivo excel: " + e.getMessage());
        }
    }
}
