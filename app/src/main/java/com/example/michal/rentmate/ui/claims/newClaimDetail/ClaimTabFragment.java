package com.example.michal.rentmate.ui.claims.newClaimDetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.michal.rentmate.R;
import com.example.michal.rentmate.model.pojo.Claim;
import com.example.michal.rentmate.model.pojo.User;
import com.example.michal.rentmate.model.repositories.ClaimRepository;
import com.example.michal.rentmate.model.repositories.UserRepository;
import com.example.michal.rentmate.networking.RentMateApi;
import com.example.michal.rentmate.networking.RestService;
import com.example.michal.rentmate.util.Constants;
import com.example.michal.rentmate.util.Helper;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ClaimTabFragment extends Fragment {

  @Bind(R.id.claim_detail_view_pager) ViewPager pager;
  @Bind(R.id.claim_detail_tab_layout) TabLayout tabLayout;

  private Claim claim;
  private FragmentManager manager;
  private User user;
  private UserRepository userRepo;
  private RentMateApi service;

  public static ClaimTabFragment newInstance(String claimID) {
    Bundle arg = new Bundle();
    arg.putSerializable(Constants.ARG_CLAIM_DETAIL_ID, claimID);
    ClaimTabFragment tabFragment = new ClaimTabFragment();
    tabFragment.setArguments(arg);
    return tabFragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String claimID = (String) getArguments().getSerializable(Constants.ARG_CLAIM_DETAIL_ID);
    claim = ClaimRepository.getInstance().getClaim(claimID);
    userRepo = UserRepository.getInstance();
    user = userRepo.getUser();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_tab_claim, container, false);
    ButterKnife.bind(this, view);
    initLayout();
    return view;
  }

  private void initLayout() {
    FragmentManager manager = getFragmentManager();
    ClaimPagerAdapter adapter = new ClaimPagerAdapter(manager);
    adapter.notifyDataSetChanged();
    pager.setAdapter(adapter);
    tabLayout.setupWithViewPager(pager);
    pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
  }

  private class ClaimPagerAdapter extends FragmentStatePagerAdapter {

    public ClaimPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {

      return position == 0 ? ClaimDetailFragment.newInstance(claim.getClaimId()) : ClaimMessageFragment.newInstance(claim.getClaimId());
    }

    @Override
    public int getCount() {
      return Constants.NUMBER_OF_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {

      return position == 0 ? getString(R.string.claim_tab_detail) : getString(R.string.claim_tab_message);
    }
  }
}
