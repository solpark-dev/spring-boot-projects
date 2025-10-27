package com.model2.mvc.service.category;

import java.util.List;
import com.model2.mvc.service.domain.Category;

public interface CategoryDao {
    void addCategory(Category category) throws Exception;
    Category getCategory(int categoryId) throws Exception;
    Category getCategoryByName(String categoryName) throws Exception;
    List<Category> getCategoryList() throws Exception;
    void updateCategory(Category category) throws Exception;
    void deleteCategory(int categoryId) throws Exception;
}
