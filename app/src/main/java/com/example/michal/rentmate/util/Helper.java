package com.example.michal.rentmate.util;


import com.example.michal.rentmate.model.pojo.User;

public class Helper {

  public static String getHeader(User user) {
    return Constants.AUTHENTICATION + user.getToken();
  }

}
