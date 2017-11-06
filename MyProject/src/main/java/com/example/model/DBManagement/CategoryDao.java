package com.example.model.DBManagement;

import com.example.model.Category;
import com.example.model.Post;
import com.example.model.exceptions.*;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Marina on 15.10.2017 ?..
 */
@Component
public class CategoryDao extends AbstractDao{

    public static ConcurrentSkipListSet<Category> cachedCategories;

    public CategoryDao() {

    }

    public static ConcurrentSkipListSet<Category> getCachedCategories() {
        return cachedCategories;
    }

    /*public static synchronized CategoryDao getInstance() {
        if(instance==null){
            instance=new CategoryDao();
        }
        return instance;
    }*/

    //tested
    public Category insertNewCategory(Category category) throws CategoryException, SQLException {
        try (PreparedStatement ps = this.getConnection().prepareStatement(
                "insert into categories(category_name) value (?);",
                Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, category.getName());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            category.setId(rs.getLong(1));
        }catch (SQLException e){
            throw new CategoryException("New Category couldn't be inserted in database.Reason: "+e.getMessage());
        }
        return category;
    }

    //tested
    public Category getCategoryById(long categoryId) throws SQLException, CategoryException {
        PreparedStatement ps = this.getConnection().prepareStatement(
                "select category_name from categories where category_id= ?  ;");
        ps.setLong(1, categoryId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        Category category=new Category(categoryId, rs.getString("category_name"));
        return category;
    }

    //tested
    public void deleteCategory(Category category) throws SQLException, CategoryException {
        PreparedStatement deleteCategory=null;
        PreparedStatement deleteFromPostsCategories=null;
        try {
            this.getConnection().setAutoCommit(false);
            deleteCategory= this.getConnection().prepareStatement(
                    "delete from categories where category_id=?;");
            deleteCategory.setLong(1, category.getId());
            deleteCategory.executeUpdate();
            deleteFromPostsCategories=this.getConnection().prepareStatement("delete from posts_categories where category_id=?;");
            deleteFromPostsCategories.setLong(1,category.getId());
            deleteFromPostsCategories.executeUpdate();
            this.getConnection().commit();
            //TODO REMOVE CATEGORY FROM ALL POSTS' COLLECTIONS
            cachedCategories.remove(category);
        } catch (SQLException e) {
            throw new CategoryException("Category could not be deleted. Reason: "+e.getMessage());
        }finally {
            this.getConnection().rollback();
            this.getConnection().setAutoCommit(true);
        }
    }

    //tested
    public HashSet<Category> getCategoriesForPost(Post post) throws SQLException, CategoryException {
        PreparedStatement ps = this.getConnection().prepareStatement("select category_id from posts_categories where post_id= ?;");
        ps.setLong(1, post.getId());
        ResultSet rs=ps.executeQuery();
        HashSet<Category> categories=new HashSet<>();
        while (rs.next()){
            categories.add(this.getCategoryById(rs.getLong("category_id")));
        }
        return categories;
    }
    
    //tested
    public void addAllCategoriesToPost(Post post,Set<Category> set) throws CategoryException {
        //TODO IF ENTRY EXISTS- THROWS EXCEPTION!!!
        try {
            for (Category category : set) {
                System.out.println("******************"+category.getId());
            }
            PreparedStatement ps = this.getConnection().prepareStatement("INSERT into posts_categories(post_id, category_id) values (?,?);");
            for (Category category : set) {
                System.out.println();
                ps.setLong(1,post.getId());
                ps.setLong(2,category.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new CategoryException("Could not add all categories to this post. Reason: "+e.getMessage());
        }

    }


    public HashMap<String,Category> getAllCategories() {
        HashMap<String,Category> categories=new HashMap<>();
        try{
            PreparedStatement ps=this.getConnection().prepareStatement("SELECT category_id, category_name " +
                    "FROM categories");
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                Category category=new Category(rs.getLong("category_id"), rs.getString("category_name"));
                categories.put(rs.getString("category_name"),category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (CategoryException e) {
            e.printStackTrace();
        }
        return categories;
    }
}
