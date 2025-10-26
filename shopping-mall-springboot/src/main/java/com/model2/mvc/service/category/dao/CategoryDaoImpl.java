package com.model2.mvc.service.category.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.service.category.CategoryDao;
import com.model2.mvc.service.domain.Category;

/**
 * 카테고리 DAO 구현체
 * MyBatis SqlSession을 이용한 데이터베이스 접근
 */
@Repository("categoryDaoImpl")
public class CategoryDaoImpl implements CategoryDao {

    @Autowired
    @Qualifier("sqlSessionTemplate")
    private SqlSession sqlSession;
    
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public CategoryDaoImpl() {
        System.out.println(this.getClass() + " 생성자 호출");
    }
    
    /**
     * 카테고리 추가
     */
    @Override
    public void addCategory(Category category) throws Exception {
        System.out.println("==> CategoryDaoImpl.addCategory() 시작");
        
        if (category == null) {
            throw new IllegalArgumentException("Category 객체가 null입니다.");
        }
        
        sqlSession.insert("CategoryMapper.addCategory", category);
        System.out.println("==> 카테고리 추가 완료, categoryId: " + category.getCategoryId());
    }
    
    /**
     * 카테고리 조회 (단건)
     */
    @Override
    public Category getCategory(int categoryId) throws Exception {
        System.out.println("==> CategoryDaoImpl.getCategory() 시작, categoryId: " + categoryId);
        
        Category category = sqlSession.selectOne("CategoryMapper.getCategory", categoryId);
        
        System.out.println("==> 조회 결과: " + (category != null ? "존재" : "없음"));
        return category;
    }
    
    /**
     * 카테고리 전체 목록 조회
     */
    @Override
    public List<Category> getCategoryList() throws Exception {
        System.out.println("==> CategoryDaoImpl.getCategoryList() 시작");
        
        List<Category> categoryList = sqlSession.selectList("CategoryMapper.getCategoryList");
        
        System.out.println("==> 조회된 카테고리 개수: " + (categoryList != null ? categoryList.size() : 0));
        return categoryList;
    }
    
    /**
     * 카테고리 수정
     */
    @Override
    public void updateCategory(Category category) throws Exception {
        System.out.println("==> CategoryDaoImpl.updateCategory() 시작");
        System.out.println("    categoryId: " + category.getCategoryId() + 
                         ", categoryName: " + category.getCategoryName());
        
        sqlSession.update("CategoryMapper.updateCategory", category);
        System.out.println("==> 카테고리 수정 완료");
    }
    
    /**
     * 카테고리 삭제
     */
    @Override
    public void deleteCategory(int categoryId) throws Exception {
        System.out.println("==> CategoryDaoImpl.deleteCategory() 시작, categoryId: " + categoryId);
        
        sqlSession.delete("CategoryMapper.deleteCategory", categoryId);
        System.out.println("==> 카테고리 삭제 완료");
    }
    
    /**
     * 카테고리명으로 조회 (중복 체크용)
     */
    @Override
    public Category getCategoryByName(String categoryName) throws Exception {
        System.out.println("==> CategoryDaoImpl.getCategoryByName() 시작, name: " + categoryName);
        
        Category category = sqlSession.selectOne("CategoryMapper.getCategoryByName", categoryName);
        
        System.out.println("==> 중복 체크 결과: " + (category != null ? "이미 존재" : "사용 가능"));
        return category;
    }
}