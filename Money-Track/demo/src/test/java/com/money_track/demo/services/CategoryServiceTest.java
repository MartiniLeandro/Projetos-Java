package com.money_track.demo.services;

import com.money_track.demo.entities.Category;
import com.money_track.demo.entities.DTO.CategoriesDataDTO;
import com.money_track.demo.entities.DTO.CategoryDTO;
import com.money_track.demo.entities.User;
import com.money_track.demo.entities.enums.Roles;
import com.money_track.demo.entities.enums.TypeValue;
import com.money_track.demo.exceptions.AlreadyExistsException;
import com.money_track.demo.exceptions.IsNotYoursException;
import com.money_track.demo.exceptions.NotFoundException;
import com.money_track.demo.repositories.CategoryRepository;
import org.hamcrest.core.Is;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CategoryService categoryService;

    private Category category1,category2;

    private User user1,user2;

    private CategoryDTO categoryDTO;

    @BeforeEach
    void setup(){
        user1 = new User(1L,"user1","13270080922","user1@email.com","user1", Roles.ROLE_USER,new ArrayList<>());
        user2 = new User(2L,"user2","13270080923","user2@email.com","user2", Roles.ROLE_USER,new ArrayList<>());
        category1 = new Category(1L,"salary", TypeValue.REVENUE,"salary","green",user1);
        category2 = new Category(2L,"school", TypeValue.EXPENSE,"school","red",user2);

        categoryDTO = new CategoryDTO(3L,"category3",TypeValue.REVENUE,"icon","color", 1L);

        when(authentication.getPrincipal()).thenReturn(user1);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @DisplayName("test find all categories by user SUCCESS")
    @Test
    void testFindGlobalAndUserCategoriesSuccess(){
        when(categoryRepository.findGlobalAndUserCategories(anyLong())).thenReturn(List.of(category1,category2));
        List<CategoryDTO> allCategories = categoryService.findGlobalAndUserCategories();

        Assertions.assertNotNull(allCategories);
        Assertions.assertEquals("salary",allCategories.getFirst().name());
        Assertions.assertEquals("school",allCategories.get(1).name());
    }

    @DisplayName("test find all categories by user FAILED")
    @Test
    void testFindAllCategoriesFailed(){
        when(categoryRepository.findGlobalAndUserCategories(anyLong())).thenReturn(Collections.emptyList());
        List<CategoryDTO> allCategories = categoryService.findGlobalAndUserCategories();

        Assertions.assertNotNull(allCategories);
        Assertions.assertTrue(allCategories.isEmpty());
    }

    @DisplayName("test find category by id SUCCESS")
    @Test
    void testFindCategoryByIdSuccess(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category1));
        CategoryDTO category = categoryService.findCategoryById(1L);

        Assertions.assertNotNull(category);
        Assertions.assertEquals("salary", category.name());
    }

    @DisplayName("test find category by id FAILED 1")
    @Test
    void testFindCategoryByIdFailed1(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> categoryService.findCategoryById(1L));

        Assertions.assertEquals("Não existe categoria com este ID", exception.getMessage());
    }

    @DisplayName("test find category by id FAILED 2")
    @Test
    void testFindCategoryByIdFailed2(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category2));
        IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> categoryService.findCategoryById(1L));

        Assertions.assertEquals("Esta categoria não é sua",exception.getMessage());
    }

    @DisplayName("test find all categories by typeValue SUCCESS")
    @Test
    void testFindAllCategoriesByTypeValueSuccess(){
        when(categoryRepository.findAllByTypeValueAndUserId(anyString(),anyLong())).thenReturn(List.of(category1));
        List<CategoryDTO> categories = categoryService.findAllCategoriesByTypeValue(TypeValue.REVENUE);

        Assertions.assertNotNull(categories);
        Assertions.assertEquals(1,categories.size());
        Assertions.assertEquals("salary", categories.getFirst().name());
    }

    @DisplayName("test find all categories by typeValue FAILED")
    @Test
    void testFindAllCategoriesByTypeValueFailed(){
        when(categoryRepository.findAllByTypeValueAndUserId(anyString(),anyLong())).thenReturn(Collections.emptyList());
        List<CategoryDTO> categories = categoryService.findAllCategoriesByTypeValue(TypeValue.EXPENSE);

        Assertions.assertNotNull(categories);
        Assertions.assertEquals(0,categories.size());
    }

    @DisplayName("test create Category SUCCESS")
    @Test
    void testCreateCategorySuccess(){
        when(categoryRepository.existsByNameAndTypeValueAndUserId(anyString(),any(),anyLong())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));
        CategoryDTO savedCategory = categoryService.createCategory(categoryDTO);

        Assertions.assertNotNull(savedCategory);
        Assertions.assertEquals("category3", savedCategory.name());
    }

    @DisplayName("test create Category FAILED")
    @Test
    void testCreateCategoryFailed(){
        when(categoryRepository.existsByNameAndTypeValueAndUserId(anyString(),any(TypeValue.class),anyLong())).thenReturn(true);

        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> categoryService.createCategory(categoryDTO));
        Assertions.assertEquals("Já existe uma category com este nome e tipo" ,exception.getMessage());
    }


    @DisplayName("test update Category SUCCESS")
    @Test
    void testUpdateCategorySuccess(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category1));
        when(categoryRepository.existsByNameAndTypeValueAndUserId(anyString(),any(),anyLong())).thenReturn(false);
        when(categoryRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        CategoryDTO updatedCategory = categoryService.updateCategory(categoryDTO,1L);
        Assertions.assertNotNull(updatedCategory);
        Assertions.assertEquals("category3", updatedCategory.name());
    }

    @DisplayName("test update Category FAILED 1")
    @Test
    void testUpdateCategoryFailed1(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> categoryService.updateCategory(categoryDTO,1L));

        Assertions.assertEquals("Não existe categoria com este ID", exception.getMessage());
    }

    @DisplayName("test update Category FAILED 2")
    @Test
    void testUpdateCategoryFailed2() {
        category1.setUser(null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> categoryService.updateCategory(categoryDTO,1L));

        Assertions.assertEquals("Esta categoria é global ou não é sua", exception.getMessage());
    }

    @DisplayName("test update Category FAILED 3")
    @Test
    void testUpdateCategoryFailed3() {
        category1.setUser(user2);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> categoryService.updateCategory(categoryDTO,1L));

        Assertions.assertEquals("Esta categoria é global ou não é sua", exception.getMessage());
    }

   /* @DisplayName("test update Category FAILED 4") RESOLVER CASO DEPOIS
    @Test
    void testUpdateCategoryFailed4() {
        categoryDTO = new CategoryDTO(3L,"salaryFake",TypeValue.REVENUE,"icon","color", 1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class, () -> categoryService.updateCategory(categoryDTO,1L));


        Assertions.assertEquals("Já existe uma category com este nome e tipo", exception.getMessage());
    }*/


   @DisplayName("test delete Category SUCCESS")
    @Test
    void testDeleteCategorySuccess(){
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category1));
        doNothing().when(categoryRepository).delete(category1);
        categoryService.deleteCategoryById(1L);

        verify(categoryRepository).delete(category1);
    }

    @DisplayName("test delete category FAILED 1")
    @Test
    void testDeleteCategoryFailed1(){
       when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
       NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> categoryService.deleteCategoryById(1L));

       Assertions.assertEquals("Não existe categoria com este ID", exception.getMessage());
    }

    @DisplayName("test delete category FAILED 2")
    @Test
    void testDeleteCategoryFailed2(){
       category1.setUser(null);
       when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category1));
       IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> categoryService.deleteCategoryById(1L));

       Assertions.assertEquals("Esta categoria é global ou não é sua", exception.getMessage());
    }

    @DisplayName("test delete category FAILED 3")
    @Test
    void testDeleteCategoryFailed3(){
        category1.setUser(user2);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category1));
        IsNotYoursException exception = Assertions.assertThrows(IsNotYoursException.class, () -> categoryService.deleteCategoryById(1L));

        Assertions.assertEquals("Esta categoria é global ou não é sua", exception.getMessage());
    }

    @DisplayName("test get categories data SUCCESS")
    @Test
    void testGetCategoriesDataSuccess(){
        when(categoryRepository.findGlobalAndUserCategories(anyLong())).thenReturn(List.of(category1, category2));
        CategoriesDataDTO data = categoryService.getCategoryData();

        Assertions.assertNotNull(data);
        Assertions.assertEquals(2, data.TotalQuantity());
        Assertions.assertEquals(1, data.ExpenseQuantity());
        Assertions.assertEquals(1, data.RevenueQuantity());
    }
}
