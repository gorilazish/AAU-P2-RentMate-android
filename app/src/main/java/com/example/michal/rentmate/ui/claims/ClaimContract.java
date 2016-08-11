package com.example.michal.rentmate.ui.claims;

import com.example.michal.rentmate.model.pojo.Claim;


public interface ClaimContract {

  interface Callbacks {
    void onClaimSelected(Claim claim);
    void setClaimActionBar();
    void addNewClaim();
  }
}
