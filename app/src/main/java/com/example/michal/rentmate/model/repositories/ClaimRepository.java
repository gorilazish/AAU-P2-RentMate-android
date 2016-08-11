package com.example.michal.rentmate.model.repositories;

import com.example.michal.rentmate.model.pojo.Claim;
import com.example.michal.rentmate.model.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class ClaimRepository {

  private static ClaimRepository claimSingleton = null;
  private List<Claim> claimList;

  private ClaimRepository() {
    this.claimList = new ArrayList<>();
  }

  public static ClaimRepository getInstance() {
    if (claimSingleton == null) {
      claimSingleton = new ClaimRepository();
    }
    return claimSingleton;
  }

  public List<Claim> getClaimList() {
    return claimList;
  }

  public void setClaimList(List<Claim> claimList) {
    this.claimList = claimList;
  }

  public Claim getClaim(String id) {
    for (Claim claim : claimList) {
      if (claim.getClaimId().equals(id)) {
        return claim;
      }
    }
    return null;
  }



}

