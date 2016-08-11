
package com.example.michal.rentmate.model.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class User {

  @SerializedName("_id") private String Id;
  @SerializedName("created_at") private String createdAt;
  @SerializedName("updated_at") private String updatedAt;
  @SerializedName("user_id") private String userId;
  @SerializedName("group_id") private String groupId;
  @SerializedName("email") private String email;
  @SerializedName("active") private Boolean active;
  @SerializedName("first_name") private String firstName;
  @SerializedName("last_name") private String lastName;
  @SerializedName("phone") private String phone;
  @SerializedName("__v") private Integer V;
  @SerializedName("apartments") private List<Apartment> apartments = new ArrayList<Apartment>();

  private String password;
  private String token;
  private List<Claim>userClaims = new ArrayList<>();

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getId() {
    return Id;
  }

  public void setId(String Id) {
    this.Id = Id;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Integer getV() {
    return V;
  }

  public void setV(Integer V) {
    this.V = V;
  }

  public List<Apartment> getApartments() {
    return apartments;
  }

  public void setApartments(List<Apartment> apartments) {
    this.apartments = apartments;
  }

  public List<Claim> getUserClaims() {
    return userClaims;
  }

  public void setUserClaims(List<Claim> userClaims) {
    this.userClaims = userClaims;
  }
}
