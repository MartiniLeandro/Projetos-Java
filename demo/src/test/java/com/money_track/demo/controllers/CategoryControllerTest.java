package com.money_track.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.CategoryDTO;
import com.money_track.demo.entities.enums.TypeValue;
import com.money_track.demo.exceptions.AlreadyExistsException;
import com.money_track.demo.exceptions.NotFoundException;
import com.money_track.demo.repositories.UserRepository;
import com.money_track.demo.security.TokenService;
import com.money_track.demo.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
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

    @DisplayName("test find all categories")
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

    @DisplayName("test find category byId FAILED")
    @Test
    void testFindCategoryByIdFailed() throws Exception {
        Long id = 1L;
        when(categoryService.findCategoryById(anyLong())).thenThrow(new NotFoundException("Não existe categoria com este ID"));

        mockMvc.perform(get("/categories/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
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

    @DisplayName("test create category FAILED")
    @Test
    void testCreateCategoryFailed() throws Exception {
        when(categoryService.createCategory(any(Category.class))).thenThrow(new AlreadyExistsException("Já existe uma category com este nome e tipo"));

        mockMvc.perform(post("/categories/admin/createCategory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category1)))
                .andDo(print())
                .andExpect(status().isBadRequest());
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

    @DisplayName("test update category FAILED case1")
    @Test
    void testUpdateCategoryFailed1() throws Exception {
        Long id = 5L;
        when(categoryService.updateCategory(any(Category.class),eq(id))).thenThrow(new NotFoundException("Não existe categoria com este ID"));

        mockMvc.perform(put("/categories/admin/updateCategory/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category1DTO)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("test update category FAILED case2")
    @Test
    void testUpdateCategoryFailed2() throws Exception {
        Long id = 5L;
        when(categoryService.updateCategory(any(Category.class),eq(id))).thenThrow(new AlreadyExistsException("Já existe uma category com este nome e tipo"));

        mockMvc.perform(put("/categories/admin/updateCategory/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category1DTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @DisplayName("test delete category SUCCESS")
    @Test
    void testDeleteCategorySuccess() throws Exception {

        doNothing().when(categoryService).deleteCategoryById(anyLong());

        mockMvc.perform(delete("/categories/admin/deleteCategory/{id}",2L))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("test delete category FAILED")
    @Test
    void testDeleteCategoryFailed() throws Exception {
        Long id = 5L;
        doThrow(new NotFoundException("Não existe categoria com este ID")).when(categoryService).deleteCategoryById(eq(id));

        mockMvc.perform(delete("/categories/admin/deleteCategory/{id}",id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
