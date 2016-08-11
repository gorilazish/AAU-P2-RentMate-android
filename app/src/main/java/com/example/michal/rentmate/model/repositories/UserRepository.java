package com.example.michal.rentmate.model.repositories;

import com.example.michal.rentmate.model.pojo.Claim;
import com.example.michal.rentmate.model.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

  private static UserRepository repository = null;
  private User user;


  private UserRepository() {
    this.user = new User();

  }

  public static UserRepository getInstance() {
    if (repository == null) {
      repository = new UserRepository();
    }
    return repository;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}

