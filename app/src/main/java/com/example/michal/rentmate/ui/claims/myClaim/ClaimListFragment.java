package com.example.michal.rentmate.ui.claims.myClaim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michal.rentmate.R;
import com.example.michal.rentmate.model.pojo.Claim;
import com.example.michal.rentmate.model.pojo.User;
import com.example.michal.rentmate.model.repositories.ClaimRepository;
import com.example.michal.rentmate.model.repositories.UserRepository;
import com.example.michal.rentmate.networking.RentMateApi;
import com.example.michal.rentmate.networking.RestService;
import com.example.michal.rentmate.ui.claims.ClaimContract;
import com.example.michal.rentmate.util.Constants;
import com.example.michal.rentmate.util.Helper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClaimListFragment extends Fragment {

  @Bind(R.id.add_first_crime_layout) RelativeLayout addFirstClaimLayout;
  @Bind(R.id.add_first_crime_button) FloatingActionButton addClaimButtonFAB;
  @Bind(R.id.recyclerView_claim_list) RecyclerView claimRecyclerView;

  private ClaimAdapter adapter;
  private ClaimContract.Callbacks callbacks;
  private List<Claim> claimList;
  private User user;
  private RentMateApi service;
  private boolean isClaimCreated;

  public static ClaimListFragment newInstance() {
    return new ClaimListFragment();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    callbacks = (ClaimContract.Callbacks) context;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    callbacks = null;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    user = UserRepository.getInstance().getUser();
    ClaimRepository repository = ClaimRepository.getInstance();
    claimList = repository.getClaimList();
//    new FetchClaims().execute();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_claim_list, container, false);
    setHasOptionsMenu(true);
    ButterKnife.bind(this, view);
    callbacks.setClaimActionBar();
    claimRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    updateUi();

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    if (isClaimCreated) {
      reloadClaimList();
      Log.e(Constants.TAG_ON_CREATED, "CLAIM IS CREATED");
    } else {
      Log.e(Constants.TAG_ON_FAILURE, "CLAIM IS NOT CREATED");
    }
  }

  //  Listeners
  @OnClick(R.id.add_first_crime_button)
  public void addNewClaim() {
    callbacks.addNewClaim();
  }

  public void updateUi() {
    if (isAdded()) {
      claimRecyclerView.setAdapter(new ClaimAdapter(claimList));
    }
    if (adapter == null) {
      adapter = new ClaimAdapter(claimList);
      claimRecyclerView.setAdapter(adapter);
    } else {
      adapter.setClaimList(claimList);
      adapter.notifyDataSetChanged();
      claimRecyclerView.setAdapter(adapter);
    }
    if (claimList.size() > 0) {
      addFirstClaimLayout.setVisibility(View.GONE);
    } else {
      addFirstClaimLayout.setVisibility(View.VISIBLE);
      addClaimButtonFAB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          callbacks.addNewClaim();
        }
      });
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case Constants.REQUEST_NEW_CLAIM:
          isClaimCreated = (boolean) data.getSerializableExtra(Constants.EXTRA_NEW_CLAIM);
      }
    }
  }

  private void reloadClaimList() {
    service = RestService.getInstance();
    Call<User> call = service.getUser(Helper.getHeader(user));
    call.enqueue(new Callback<User>() {
      @Override
      public void onResponse(Call<User> call, Response<User> response) {
        if (response.isSuccessful()) {
          Log.e(Constants.TAG_USER, "LOADING USER'S CLAIMS");
          user = response.body();
          updateClaimRepository(setUserClaims(user));
          updateUi();
        }
      }

      @Override
      public void onFailure(Call<User> call, Throwable t) {
      }
    });
  }

  public List<Claim> setUserClaims(User user) {
    List<Claim> userClaims = user.getUserClaims();
    for (int i = 0; i < user.getApartments().size(); i++) {
      for (int j = 0; j < user.getApartments().get(i).getClaims().size(); j++) {
        userClaims.add(user.getApartments().get(i).getClaims().get(j));
      }
    }
    claimList = userClaims;
    return userClaims;
  }

  public static void updateClaimRepository(List<Claim> claims) {
    ClaimRepository claimRepository = ClaimRepository.getInstance();
    claimRepository.getClaimList().clear();
    claimRepository.setClaimList(claims);
  }

  public class ClaimHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @Bind(R.id.list_item_textview_claim_name) TextView titleTextView;
    @Bind(R.id.list_item_claim_date) TextView dateTextView;
    @Bind(R.id.state_claim_button) Button statusButton;
    public Claim claim;

    public ClaimHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      callbacks.onClaimSelected(claim);
    }

    public void bindClaim(Claim claim) {
      this.claim = claim;

      int color = setButtonColor(claim.getStatus());
      titleTextView.setText(claim.getTitle());
      dateTextView.setText(claim.getCreatedAt().toString());
      statusButton.getBackground().setColorFilter(getResources().getColor(color), PorterDuff.Mode.ADD);
      statusButton.setText(claim.getStatus());
    }

    public int setButtonColor(String status) {
      int color;
//      TODO set statuses!!!!!!!
      switch (status) {
        case "active":
          color = R.color.colorProblem;
          break;
        case "pending":
          color = R.color.colorOpen;
          break;
        case "resolved":
          color = R.color.colorOk;
          break;
        case "closed":
          color = R.color.colorPrimary;
          break;
        default:
          color = R.color.colorOk;
          break;
      }
      return color;
    }
  }

  private class ClaimAdapter extends RecyclerView.Adapter<ClaimHolder> {

    private List<Claim> claimList;

    public ClaimAdapter(List<Claim> claimList) {
      this.claimList = claimList;
    }

    @Override
    public ClaimHolder onCreateViewHolder(ViewGroup parent, int viewType) {

      LayoutInflater inflater = LayoutInflater.from(getActivity());
      View view = inflater.inflate(R.layout.claim_view_in_recyclerview, parent, false);
      return new ClaimHolder(view);
    }

    @Override
    public void onBindViewHolder(ClaimHolder holder, int position) {
      Claim claim = claimList.get(position);
      holder.bindClaim(claim);
    }

    @Override
    public int getItemCount() {
      return claimList.size();
    }

    public void setClaimList(List<Claim> claimList) {
      this.claimList = claimList;
    }
  }
}
