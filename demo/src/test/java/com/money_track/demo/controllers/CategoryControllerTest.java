package com.money_track.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.CategoryDTO;
import com.money_track.demo.entities.enums.TypeValue;
import com.money_track.demo.repositories.UserRepository;
import com.money_track.demo.security.TokenService;
import com.money_track.demo.services.CategoryService;
import config.SecurityTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(CategoryController.class)
@Import(SecurityTestConfig.class)
public class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Category category1,category2;
    private CategoryDTO category1DTO,category2DTO;

    @BeforeEach
    void setup(){
        category1 = new Category("salary", TypeValue.REVENUE);
        category1.setId(1L);
        category2 = new Category("school", TypeValue.EXPENSE);
        category2.setId(2L);
        category1DTO = new CategoryDTO(category1);
        category2DTO = new CategoryDTO(category2);
    }

    @DisplayName("test find all categories SUCCESS")
    @Test
    void testFindAllCategoriesSuccess() throws Exception {
        when(categoryService.findAllCategories()).thenReturn(List.of(category1DTO,category2DTO));

        mockMvc.perform(get("/categories"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("salary"))
                .andExpect(jsonPath("$[1].name").value("school"));
    }

    @DisplayName("test find category byId SUCCESS")
    @Test
    void testFindCategoryByIdSuccess() throws Exception {

        when(categoryService.findCategoryById(anyLong())).thenReturn(category2DTO);

        mockMvc.perform(get("/categories/{id}",2L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("school"));
    }

    @DisplayName("test create category SUCCESS")
    @Test
    void testCreateCategorySuccess() throws Exception{

        when(categoryService.createCategory(any(Category.class))).thenReturn(category1DTO);

        mockMvc.perform(post("/categories/admin/createCategory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category1DTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("salary"))
                .andExpect(jsonPath("$.typeValue").value("REVENUE"));
    }

    @DisplayName("test update category SUCCESS")
    @Test
    void testUpdateCategorySuccess() throws Exception{

        Category category3 = new Category("Academy", TypeValue.EXPENSE);
        category3.setId(3L);
        CategoryDTO category3DTO = new CategoryDTO(category3);

        when(categoryService.updateCategory(any(Category.class),anyLong())).thenReturn(category3DTO);

        mockMvc.perform(put("/categories/admin/updateCategory/{id}",3L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category3DTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Academy"))
                .andExpect(jsonPath("$.typeValue").value("EXPENSE"));
    }

    @DisplayName("test delete category SUCCESS")
    @Test
    void testDeleteCategorySuccess() throws Exception {

        doNothing().when(categoryService).deleteCategoryById(anyLong());

        mockMvc.perform(delete("/categories/admin/deleteCategory/{id}",2L))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}
