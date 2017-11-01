package com.example.model.DBManagement;

import com.example.model.Post;
import com.example.model.Tag;
import com.mysql.jdbc.Statement;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marina on 30.10.2017 г..
 */
@Component
public class TagDao extends AbstractDao{
    public TagDao() {
    }

    public HashSet<String> getAllTags() {
        HashSet<String> tags=new HashSet<>();
        try{
            PreparedStatement ps=this.getConnection().prepareStatement("SELECT tag_name FROM tags");
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                String tagName=rs.getString("tag_name");
                tags.add(tagName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    public HashSet<Tag> getTagsForPost(Post post){
        HashSet<Tag> tags=new HashSet<>();
        try{
            PreparedStatement ps=this.getConnection().prepareStatement("SELECT tags.tag_id, tags.tag_name " +
                    "from tags join posts_tags " +
                    "on tags.tag_id=posts_tags.tag_id " +
                    "WHERE posts_tags.post_id=?;");
            ps.setLong(1, post.getId());
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                Tag tag=new Tag(rs.getLong("tag_id"), rs.getString("tag_name"));
                tags.add(tag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }


    public Tag getTagByTagName(String tagName) {
        Tag tag=null;
        try{
            PreparedStatement ps=this.getConnection().prepareStatement("SELECT tag_id FROM tags WHERE tag_name LIKE ?");
            ps.setString(1, tagName);
            ResultSet rs=ps.executeQuery();
            rs.next();
            tag=new Tag(rs.getLong("tag_id"),tagName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tag;
    }

    public Tag insertTag(String tagName) {
        Tag tag=null;
        try{
            PreparedStatement ps=this.getConnection().prepareStatement("INSERT INTO tags(tag_name) " +
                    "VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, tagName);
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            rs.next();
            tag=new Tag(rs.getLong(1),tagName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tag;
    }

    public void addTagsToPost(Post post, Set<Tag> tags) {
        try{
            PreparedStatement ps =this.getConnection().prepareStatement(
                    "insert into posts_tags(post_id, tag_id) values(?,?);");
            for (Tag tag : tags) {
                ps.setLong(1,post.getId());
                ps.setLong(2,tag.getTag_id());
                ps.addBatch();
            }
            ps.executeBatch();
        }catch (SQLException e){
            e.printStackTrace();
        }
}
}
