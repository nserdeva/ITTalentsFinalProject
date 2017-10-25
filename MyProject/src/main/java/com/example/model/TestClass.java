package com.example.model;
import com.example.model.exceptions.*;
import java.sql.SQLException;

/**
 * Created by Marina on 18.10.2017 г..
 */
public class TestClass {
    public static void main(String[] args) throws UserException, SQLException, CategoryException, LocationException, PostException, MultimediaException, VisitedLocationException {
        //User user=new User("pencho","pencho","pencho@pe.ncho");
        //if(UserDao.getInstance().existsUser(user.getUsername(),user.getPassword())){
        //    System.out.println("User exist");
        //}else{
        //    System.out.println("User does not exist");
        //}
        //UserDao.getInstance().insertUser(user);
        //User user1=UserDao.getInstance().getUserById(1);
        //System.out.println(user1.getUsername());
        //System.out.println(user1.getEmail());
        //System.out.println(user1.getPassword());
        //Category category=new Category("trial category");
        //Category category1=CategoryDao.getInstance().getCategoryById(3);
        //int aff=CategoryDao.getInstance().deleteCategory(category);
        //System.out.println(aff);
        //System.out.println(category.getId());
        //Location location=new Location("lat", "long","desc","trial location");
        //System.out.println(location.getId());
        //Location updated=LocationDao.getInstance().insertLocation(location);
       // System.out.println(updated.getId());
       // Location loc=LocationDao.getInstance().getLocationById(2);
        //System.out.println(loc.getLocationName());
        //System.out.println(loc.getDescription());
       //Multimedia multimedia=new Multimedia("dfgsdf", false);
       //MultimediaDao.getInstance().insertMultimedia(post, multimedia);
        //HashSet<Category> categories=new HashSet<>();
        //categories.add(category1);
        //HashSet<User> taggedPeople=new HashSet<>();
        //taggedPeople.add(user1);
        //Post post=new Post(user1,categories,taggedPeople);
       // PostDao.getInstance().insertNewPost(post);
        //User user=UserDao.getInstance().getUserById(2);
        //TODO if the id does not exist in database, line 51 throws exception: empty resultset
        //Post post2=PostDao.getInstance().getPostById(8);
        //PostDao.getInstance().tagUser(post2,user);
        //PostDao.getInstance().addCategoryToPost(post2, category1);
        //PostDao.getInstance().deletePost(post2);
        //PostDao.getInstance().updateLocation(post2,loc );
        //PostDao.getInstance().decrementDislikes(post2);
        //PostDao.getInstance().updateDescription(post2,"This is a new description");
        //HashSet<Post> posts=PostDao.getInstance().getPostsForUser(user1);
        //for (Post post:posts) {
        //    System.out.println(post.getId());
        //    System.out.println(post.getDescription());
        //    System.out.println(post.getDateTime());
        //}
        //HashSet<Category> categories=new HashSet<>();
        //categories.add(CategoryDao.getInstance().getCategoryById(7));
        //categories.add(CategoryDao.getInstance().getCategoryById(6));
        //CategoryDao.getInstance().addAllCategoriesToPost(post2, categories);
        //Location location=LocationDao.getInstance().getLocationByPost(post2);
        //System.out.println(location.getId());
        //System.out.println(location.getLocationName());
        //System.out.println(location.getDescription());
        //Multimedia multimedia=MultimediaDao.getInstance().insertMultimedia(post2,new Multimedia("fake_url",false));
        //MultimediaDao.getInstance().deleteMultimedia(multimedia);
        User user=new User("petkancho","trialPassword","email@emai.com");
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        System.out.println(user.getEmail());



    }
}
