package com.example.michal.rentmate.ui.apartment;

import com.example.michal.rentmate.model.pojo.Apartment;

/**
 * Created by Michal on 17/04/2016.
 */
public interface MyApptContract {

  interface Callbacks {
    void onApartmentSelected(Apartment apartment);
    void setApartmentActionBar();
    void addNewApartment();
    void joinApartment();
  }
}
