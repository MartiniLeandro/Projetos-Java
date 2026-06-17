package com.money_track.demo.entities.DTO;

import java.util.List;

public record CategoriesDataDTO(List<CategoryDTO> categories, int TotalQuantity, int ExpenseQuantity, int RevenueQuantity) {
}
