package com.model2.mvc.service.category;

import java.util.List;
import com.model2.mvc.service.domain.Category;

public interface CategoryService {
    void addCategory(Category category) throws Exception;
    Category getCategory(int categoryId) throws Exception;
    List<Category> getCategoryList() throws Exception;
    void updateCategory(Category category) throws Exception;
    void deleteCategory(int categoryId) throws Exception;
    boolean checkDuplication(String categoryName) throws Exception;
}
