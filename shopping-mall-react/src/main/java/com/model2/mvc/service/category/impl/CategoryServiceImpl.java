package com.model2.mvc.service.category.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.service.category.CategoryDao;
import com.model2.mvc.service.category.CategoryService;
import com.model2.mvc.service.domain.Category;

/**
 * 카테고리 Service 구현체
 * 카테고리 관련 비즈니스 로직 처리
 */
@Service("categoryServiceImpl")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    @Qualifier("categoryDaoImpl")
    private CategoryDao categoryDao;
    
    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public CategoryServiceImpl() {
        System.out.println(this.getClass());
    }
    
    /**
     * 카테고리 추가
     */
    @Override
    public void addCategory(Category category) throws Exception {
        System.out.println("==> CategoryServiceImpl.addCategory() 시작");
        
        // 1. 입력값 검증
        if (category == null) {
            throw new IllegalArgumentException("Category 객체가 null입니다.");
        }
        
        if (category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("카테고리명은 필수입니다.");
        }
        
        // 2. 중복 체크
        Category existingCategory = categoryDao.getCategoryByName(category.getCategoryName());
        if (existingCategory != null) {
            throw new IllegalArgumentException("이미 존재하는 카테고리명입니다: " + category.getCategoryName());
        }
        
        // 3. 카테고리 등록
        categoryDao.addCategory(category);
        System.out.println("==> 카테고리 등록 완료: " + category.getCategoryName());
    }
    
    /**
     * 카테고리 조회 (단건)
     */
    @Override
    public Category getCategory(int categoryId) throws Exception {
        System.out.println("==> CategoryServiceImpl.getCategory() 시작, categoryId: " + categoryId);
        
        if (categoryId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 카테고리 ID입니다: " + categoryId);
        }
        
        Category category = categoryDao.getCategory(categoryId);
        
        if (category == null) {
            System.out.println("==> 카테고리를 찾을 수 없습니다: " + categoryId);
        }
        
        return category;
    }
    
    /**
     * 카테고리 전체 목록 조회
     */
    @Override
    public List<Category> getCategoryList() throws Exception {
        System.out.println("==> CategoryServiceImpl.getCategoryList() 시작");
        
        List<Category> categoryList = categoryDao.getCategoryList();
        
        System.out.println("==> 조회 완료, 카테고리 개수: " + 
                         (categoryList != null ? categoryList.size() : 0));
        return categoryList;
    }
    
    /**
     * 카테고리 수정
     */
    @Override
    public void updateCategory(Category category) throws Exception {
        System.out.println("==> CategoryServiceImpl.updateCategory() 시작");
        
        // 1. 입력값 검증
        if (category == null) {
            throw new IllegalArgumentException("Category 객체가 null입니다.");
        }
        
        if (category.getCategoryId() <= 0) {
            throw new IllegalArgumentException("유효하지 않은 카테고리 ID입니다: " + category.getCategoryId());
        }
        
        if (category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
            throw new IllegalArgumentException("카테고리명은 필수입니다.");
        }
        
        // 2. 카테고리 존재 여부 확인
        Category existingCategory = categoryDao.getCategory(category.getCategoryId());
        if (existingCategory == null) {
            throw new Exception("해당 카테고리를 찾을 수 없습니다. categoryId: " + category.getCategoryId());
        }
        
        // 3. 카테고리명 중복 체크 (다른 카테고리와 중복되는지)
        Category duplicateCheck = categoryDao.getCategoryByName(category.getCategoryName());
        if (duplicateCheck != null && duplicateCheck.getCategoryId() != category.getCategoryId()) {
            throw new IllegalArgumentException("이미 존재하는 카테고리명입니다: " + category.getCategoryName());
        }
        
        // 4. 카테고리 수정
        categoryDao.updateCategory(category);
        System.out.println("==> 카테고리 수정 완료: " + category.getCategoryName());
    }
    
    /**
     * 카테고리 삭제
     */
    @Override
    public void deleteCategory(int categoryId) throws Exception {
        System.out.println("==> CategoryServiceImpl.deleteCategory() 시작, categoryId: " + categoryId);
        
        if (categoryId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 카테고리 ID입니다: " + categoryId);
        }
        
        // 카테고리 존재 여부 확인
        Category category = categoryDao.getCategory(categoryId);
        if (category == null) {
            throw new Exception("해당 카테고리를 찾을 수 없습니다. categoryId: " + categoryId);
        }
        
        // TODO: 해당 카테고리를 사용하는 상품이 있는지 확인 (필요시)
        // 현재는 바로 삭제
        
        categoryDao.deleteCategory(categoryId);
        System.out.println("==> 카테고리 삭제 완료");
    }
    
    /**
     * 카테고리명 중복 체크
     */
    @Override
    public boolean checkDuplication(String categoryName) throws Exception {
        System.out.println("==> CategoryServiceImpl.checkDuplication() 시작");
        
        boolean result = false;
        Category category = categoryDao.getCategoryByName(categoryName);
        
        if (category != null) {
            result = true;
            System.out.println("==> 중복된 카테고리명입니다: " + categoryName);
        } else {
            System.out.println("==> 사용 가능한 카테고리명입니다: " + categoryName);
        }
        
        return result;
    }
}