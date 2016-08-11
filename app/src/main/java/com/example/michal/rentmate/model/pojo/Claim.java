
package com.example.michal.rentmate.model.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Claim {

  @SerializedName("_id") private String Id;
  @SerializedName("created_at") private Date createdAt;
  @SerializedName("updated_at") private String updatedAt;
  @SerializedName("claim_id") private String claimId;
  @SerializedName("title") private String title;
  @SerializedName("created_by")private String createdBy;
  @SerializedName("description") private String description;
  @SerializedName("__v") private int V;
  @SerializedName("messages") private List<Message> messages = new ArrayList<Message>();
  @SerializedName("status") private String status;

  @SerializedName("apartment_id")private String relatedApt;

  public String getId() {
    return Id;
  }

  public void setId(String Id) {
    this.Id = Id;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getClaimId() {
    return claimId;
  }

  public void setClaimId(String claimId) {
    this.claimId = claimId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getV() {
    return V;
  }

  public void setV(int V) {
    this.V = V;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getRelatedApt() {
    return relatedApt;
  }

  public void setRelatedApt(String relatedApt) {
    this.relatedApt = relatedApt;
  }
}
