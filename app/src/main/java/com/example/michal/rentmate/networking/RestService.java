package com.example.michal.rentmate.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestService {
  private static String url = "http://blooming-tundra-81185.herokuapp.com";
  private static RentMateApi service;

  private RestService() {
  }

  public static RentMateApi getInstance() {
    if (service == null) {

      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(url)
          .addConverterFactory(GsonConverterFactory.create())
          .build();

      return service = retrofit.create(RentMateApi.class);
    } else {
      return service;
    }
  }
}
