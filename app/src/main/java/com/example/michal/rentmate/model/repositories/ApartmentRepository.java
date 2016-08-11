package com.example.michal.rentmate.model.repositories;

import com.example.michal.rentmate.model.pojo.Apartment;

import java.util.ArrayList;
import java.util.List;

public class ApartmentRepository {

  private static ApartmentRepository repository = null;
  private List<Apartment> apartmentList;

  private ApartmentRepository() {
    this.apartmentList = new ArrayList<>();
  }

  public static ApartmentRepository getInstance() {
    if (repository == null) {
      repository = new ApartmentRepository();
    }
    return repository;
  }

  public List<Apartment> getApartmentList() {
    return apartmentList;
  }

  public void setApartmentList(List<Apartment> apartmentList) {
    this.apartmentList = apartmentList;
  }

  public Apartment getApartment(String id) {
    for (Apartment apartment : apartmentList) {
      if (apartment.getApartmentId().equals(id)) {
        return apartment;
      }
    }
    return null;
  }
}

