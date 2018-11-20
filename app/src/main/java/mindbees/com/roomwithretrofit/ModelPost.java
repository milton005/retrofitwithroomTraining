package mindbees.com.roomwithretrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelPost {

@SerializedName("post_id")
@Expose
private String postId;
@SerializedName("post_title")
@Expose
private String postTitle;
@SerializedName("post_description")
@Expose
private String postDescription;
@SerializedName("post_image")
@Expose
private String postImage;
@SerializedName("post_link")
@Expose
private String postLink;
@SerializedName("post_category")
@Expose
private String postCategory;
@SerializedName("posted_date")
@Expose
private String postedDate;

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