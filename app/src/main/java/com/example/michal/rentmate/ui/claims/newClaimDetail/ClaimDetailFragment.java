package com.example.michal.rentmate.ui.claims.newClaimDetail;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.michal.rentmate.R;
import com.example.michal.rentmate.model.pojo.Claim;
import com.example.michal.rentmate.model.repositories.ClaimRepository;
import com.example.michal.rentmate.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ClaimDetailFragment extends Fragment {

  @Bind(R.id.state_claim_button) Button stateButton;
  @Bind(R.id.claim_detail_description)TextView detailTextView;
  @Bind(R.id.claim_title_textview)TextView titleTextView;
  @Bind(R.id.claim_date_textview)TextView dateTextView;

  private Claim claim;

  public static ClaimDetailFragment newInstance() {
    return new ClaimDetailFragment();
  }

  public static ClaimDetailFragment newInstance(String crimeID) {
    Bundle arg = new Bundle();
    arg.putSerializable(Constants.ARG_CLAIM_DETAIL_ID, crimeID);
    ClaimDetailFragment claimDetailFragment = new ClaimDetailFragment();
    claimDetailFragment.setArguments(arg);
    return claimDetailFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      String claimID = (String) getArguments().getSerializable(Constants.ARG_CLAIM_DETAIL_ID);
      claim = ClaimRepository.getInstance().getClaim(claimID);
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_claim_detail, container, false);
    ButterKnife.bind(this, view);

    setUI();
    return view;
  }

  private void setUI(){
    titleTextView.setText(claim.getTitle());
    dateTextView.setText(claim.getCreatedAt().toString());
    detailTextView.setText(claim.getDescription());
    int color = setButtonColor(claim.getStatus());
    stateButton.getBackground().setColorFilter(getResources().getColor(color), PorterDuff.Mode.ADD);
  }

  private int setButtonColor(String status) {
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


