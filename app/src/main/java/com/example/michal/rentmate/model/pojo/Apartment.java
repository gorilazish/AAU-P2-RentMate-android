
package com.example.michal.rentmate.model.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Apartment {

  @SerializedName("_id") private String Id;
  @SerializedName("created_at") private String createdAt;
  @SerializedName("updated_at") private String updatedAt;
  @SerializedName("apartment_id") private String apartmentId;
  @SerializedName("name") private String name;
  @SerializedName("address") private String street;
  @SerializedName("postcode") private String zip;
  @SerializedName("city") private String city;
  @SerializedName("country") private String country;
  @SerializedName("__v") private Integer V;
  @SerializedName("isOccupied") private Boolean isOccupied;
  @SerializedName("tenant") private String tenant;
  @SerializedName("claims") private List<Claim> claims = new ArrayList<Claim>();

  public String getId() {
    return Id;
  }

  public void setId(String id) {
    Id = id;
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

  public String getApartmentId() {
    return apartmentId;
  }

  public void setApartmentId(String apartmentId) {
    this.apartmentId = apartmentId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Integer getV() {
    return V;
  }

  public void setV(Integer v) {
    V = v;
  }

  public Boolean isOccupied() {
    return isOccupied;
  }

  public void setOccupied(Boolean occupied) {
    isOccupied = occupied;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  public List<Claim> getClaims() {
    return claims;
  }

  public void setClaims(List<Claim> claims) {
    this.claims = claims;
  }

  @Override
  public String toString() {
    return Apartment.this.getName();
  }
}
