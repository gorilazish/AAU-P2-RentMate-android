package com.example.michal.rentmate.ui.apartment.myApartment;

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
import com.example.michal.rentmate.model.pojo.Apartment;
import com.example.michal.rentmate.model.pojo.User;
import com.example.michal.rentmate.model.repositories.ApartmentRepository;
import com.example.michal.rentmate.model.repositories.UserRepository;
import com.example.michal.rentmate.networking.RentMateApi;
import com.example.michal.rentmate.networking.RestService;
import com.example.michal.rentmate.util.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApptTabFragment extends Fragment {

  @Bind(R.id.apartment_tab_layout) TabLayout tabLayout;
  @Bind(R.id.apartment_tab_view_pager) ViewPager pager;

  private FragmentManager manager;
  private User user;
  private UserRepository userRepo;
  private RentMateApi service;
  private boolean isApartmentAdded;

  public static MyApptTabFragment newInstance() {
    return new MyApptTabFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    userRepo = UserRepository.getInstance();
    user = userRepo.getUser();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_tab_apartment, container, false);
    ButterKnife.bind(this, view);
    initLayout();
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    if (isApartmentAdded) {
      reloadUserApartments();
      Log.e(Constants.TAG_ON_CREATED, "APARTMENT IS CREATED");
    } else {
      Log.e(Constants.TAG_ON_FAILURE, "APARTMENT IS NOT CREATED");
    }
    initLayout();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case Constants.REQUEST_NEW_APARTMENT:
          isApartmentAdded = (boolean) data.getSerializableExtra(Constants.EXTRA_NEW_APARTMENT);
      }
    }
  }

  private void initLayout() {
    manager = getFragmentManager();
    ApartmentPagerAdapter adapter = new ApartmentPagerAdapter(manager);
    adapter.notifyDataSetChanged();
    pager.setAdapter(adapter);
    tabLayout.setupWithViewPager(pager);
    pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
  }

  private void reloadUserApartments() {
    service = RestService.getInstance();

    Call<User> call = service.getUser(Constants.AUTHENTICATION + user.getToken());
    call.enqueue(new Callback<User>() {
      @Override
      public void onResponse(Call<User> call, Response<User> response) {
        if (response.isSuccessful()) {
          isApartmentAdded = false;
          Log.e(Constants.TAG_USER, "LOADING USER'S APARTMENTS");
          user = response.body();
          updateApartmentRepository(user.getApartments());
        }
      }

      @Override
      public void onFailure(Call<User> call, Throwable t) {
      }
    });
  }

  public static void updateApartmentRepository(List<Apartment> apartments) {
    ApartmentRepository apartmentRepository = ApartmentRepository.getInstance();
    apartmentRepository.getApartmentList().clear();
    apartmentRepository.setApartmentList(apartments);
  }

  public class ApartmentPagerAdapter extends FragmentStatePagerAdapter {
    public ApartmentPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      return position == 0 ? MyApptListFragment.newInstance() : MyApptMapFragment.newInstance();
    }

    @Override
    public int getCount() {
      return Constants.NUMBER_OF_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {

      return position == 0 ? getString(R.string.apartment_tab_my_apt) : getString(R.string.apartment_tab_location);
    }
  }
}
