package com.money_track.demo.services;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.LaunchesFilterDTO;
import com.money_track.demo.entities.Launch;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import com.money_track.demo.entities.enums.TypeValue;
import com.money_track.demo.repositories.LaunchRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExportLaunchesServiceTest {

    @Mock
    private LaunchRepository launchRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ExportLaunchesService exportLaunchesService;

    private User user1;
    private LaunchesFilterDTO launchesFilter;
    private Launch launch1;
    private Category category1;

    @BeforeEach
    void setup(){
        user1 = new User(1L,"user","702.413.770-30","user@email.com","user123", Roles.ROLE_USER, new ArrayList<>());
        launchesFilter = new LaunchesFilterDTO(null,null,null,null,null);
        category1 = new Category(1L,"category1", TypeValue.REVENUE,"icon","color",user1);
        launch1 = new Launch(1L,"salary",category1,1500.0, LocalDate.of(2025,6,1),user1);


        when(authentication.getPrincipal()).thenReturn(user1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @DisplayName("Test create Excel file SUCCESS")
    @Test
    void testCreateExcelFileSuccess() throws Exception{
        when(launchRepository.getLaunchesWithFilters(eq(user1.getId()), any(), any(), any(), any(), any())).thenReturn(List.of(launch1));
        byte[] excelFile = exportLaunchesService.CreateExcelFile(launchesFilter);

        Assertions.assertNotNull(excelFile);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(bais)) {

            Sheet sheet = workbook.getSheetAt(0);
            Assertions.assertEquals("Launches", sheet.getSheetName());

            Row headerRow = sheet.getRow(0);
            Assertions.assertNotNull(headerRow);
            Assertions.assertEquals("Date", headerRow.getCell(0).getStringCellValue());
            Assertions.assertEquals("Description", headerRow.getCell(1).getStringCellValue());
            Assertions.assertEquals("Category", headerRow.getCell(2).getStringCellValue());
            Assertions.assertEquals("Type", headerRow.getCell(3).getStringCellValue());
            Assertions.assertEquals("Value", headerRow.getCell(4).getStringCellValue());

            Row dataRow = sheet.getRow(1);
            Assertions.assertNotNull(dataRow);

            Assertions.assertEquals("2025-06-01", dataRow.getCell(0).getStringCellValue());
            Assertions.assertEquals("salary", dataRow.getCell(1).getStringCellValue());
            Assertions.assertEquals("category1", dataRow.getCell(2).getStringCellValue());
            Assertions.assertEquals("REVENUE", dataRow.getCell(3).getStringCellValue());
            Assertions.assertEquals(1500.0, dataRow.getCell(4).getNumericCellValue(), 0.001);
        }
    }

    @DisplayName("Test create Excel file EMPTY LIST")
    @Test
    void testCreateExcelFileEmptyList() throws Exception {
        when(launchRepository.getLaunchesWithFilters(eq(user1.getId()), any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());
        byte[] excelFile = exportLaunchesService.CreateExcelFile(launchesFilter);

        Assertions.assertNotNull(excelFile);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(bais)) {

            Sheet sheet = workbook.getSheetAt(0);

            Assertions.assertEquals(0, sheet.getLastRowNum());

            Row headerRow = sheet.getRow(0);
            Assertions.assertNotNull(headerRow);
            Assertions.assertEquals("Date", headerRow.getCell(0).getStringCellValue());

            Row dataRow = sheet.getRow(1);
            Assertions.assertNull(dataRow);
        }
    }
}
