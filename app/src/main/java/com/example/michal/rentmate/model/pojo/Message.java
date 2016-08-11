
package com.example.michal.rentmate.model.pojo;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Message {

  @SerializedName("msg") private String message;
  @SerializedName("posted_at") private String postedAt;
  @SerializedName("posted_by") private PostedBy postedBy;
  @SerializedName("_id") private String Id;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getPostedAt() {
    return postedAt;
  }

  public void setPostedAt(String postedAt) {
    this.postedAt = postedAt;
  }

  public PostedBy getPostedBy() {
    return postedBy;
  }

  public void setPostedBy(PostedBy postedBy) {
    this.postedBy = postedBy;
  }

  public String getId() {
    return Id;
  }

  public void setId(String Id) {
    this.Id = Id;
  }
}
