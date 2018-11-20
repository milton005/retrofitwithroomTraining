package mindbees.com.roomwithretrofit;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity(tableName = "posts")
public class Postmodel {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "post_id")
    private String postId;
    @ColumnInfo(name = "post_title")
    private String postTitle;
    @ColumnInfo(name = "post_description")
    private String postDescription;
    @ColumnInfo(name = "post_image")
    private String postImage;
    @ColumnInfo(name = "post_link")
    private String postLink;
    @ColumnInfo(name = "post_category")
    private String postCategory;
    @ColumnInfo(name = "posted_date")
    private String postedDate;
    public Postmodel()
    {
    }

    public Postmodel(String post_id,String post_title,String post_description,String post_image,String post_link,String post_category,String postedDate)
    {
        this.postCategory=post_category;
        this.postDescription=post_description;
        this.postedDate=postedDate;
        this.postId=post_id;
        this.postImage=post_image;
        this.postLink=post_link;
        this.postTitle=post_title;
    }
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostLink() {
        return postLink;
    }

    public void setPostLink(String postLink) {
        this.postLink = postLink;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }
}
