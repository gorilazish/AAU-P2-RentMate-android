package com.example.michal.rentmate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.michal.rentmate.R;
import com.example.michal.rentmate.model.pojo.Apartment;
import com.example.michal.rentmate.model.pojo.Claim;
import com.example.michal.rentmate.ui.apartment.MyApptContract;
import com.example.michal.rentmate.ui.apartment.myApartment.MyApptDetailFragment;
import com.example.michal.rentmate.ui.apartment.myApartment.MyApptJoin;
import com.example.michal.rentmate.ui.apartment.myApartment.MyApptTabFragment;
import com.example.michal.rentmate.ui.apartment.newApartment.MyApptNew;
import com.example.michal.rentmate.ui.claims.ClaimContract;
import com.example.michal.rentmate.ui.claims.myClaim.ClaimListFragment;
import com.example.michal.rentmate.ui.claims.myClaim.ClaimNew;
import com.example.michal.rentmate.ui.claims.newClaimDetail.ClaimTabFragment;
import com.example.michal.rentmate.ui.homescreen.DashBoardContract;
import com.example.michal.rentmate.ui.homescreen.NoticeFragment;
import com.example.michal.rentmate.ui.profile.ProfileFragment;
import com.example.michal.rentmate.util.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RentMateActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener,
    ClaimContract.Callbacks,
    MyApptContract.Callbacks,
    DashBoardContract.Callbacks {

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.drawer_layout) DrawerLayout drawer;
  @Bind(R.id.nav_view) NavigationView navigationView;

  private ActionBarDrawerToggle toggle;

    public static Intent newIntent(Context packageContext) {
    Intent intent = new Intent(packageContext, RentMateActivity.class);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setToolbar();
    setFirstFragment();
    setDrawer();
     }

  private void setToolbar() {
    setSupportActionBar(toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }

  private void setDrawer() {
    toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();
    navigationView.setNavigationItemSelectedListener(this);
  }

  public void setFirstFragment() {
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentByTag(Constants.HOME_NOTICE_FRAG);

    if (fragment == null) {
      fragment = NoticeFragment.newInstance();
    }
    fm.beginTransaction()
        .add(R.id.fragment_container, fragment, Constants.HOME_NOTICE_FRAG)
        .addToBackStack(Constants.HOME_NOTICE_FRAG)
        .commit();
  }

  @Override
  public void onBackPressed() {
    Log.e("BACK PRESSED", "onBackPressed INVOKED");
    isDrawerEnable(true);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onNavigationItemSelected(MenuItem item) {

    int id = item.getItemId();
    Fragment fragment = null;
    String TAG = "";

    switch (id) {
      case R.id.nav_home:
        fragment = NoticeFragment.newInstance();
        TAG = Constants.HOME_NOTICE_FRAG;
        break;
      case R.id.nav_my_appartments:
        fragment = MyApptTabFragment.newInstance();
        TAG = Constants.APARTMENT_TAB_FRAG;
        break;
      case R.id.nav_claims:
        fragment = ClaimListFragment.newInstance();
        TAG = Constants.CLAIM_LIST_FRAG;
        break;
      case R.id.nav_profile:
        fragment = ProfileFragment.newInstance();
        TAG = Constants.PROFILE_FRAG;
        break;

//      TODO set log out
//      case R.id.nav_log_out:
//        fragment = Settings.newInstance();
//        TAG = FragmentUtil.SETTINGS_FRAG;
//        break;
    }
    FragmentManager fm = getSupportFragmentManager();
    fm.beginTransaction()
        .setCustomAnimations(R.anim.fragment_slide_left_enter,
            R.anim.fragment_slide_left_exit,
            R.anim.fragment_slide_right_enter,
            R.anim.fragment_slide_right_exit)
        .replace(R.id.fragment_container, fragment, TAG)
        .addToBackStack(TAG)
        .commit();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        drawer.closeDrawer(GravityCompat.START);
      }
    }, 250);
    return true;
  }

  /**
   * ClaimContract interface implementation
   */
  @Override
  public void onClaimSelected(Claim claim) {
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
    Fragment fragment = fm.findFragmentByTag(Constants.CLAIM_TAB_FRAG);
    if (fragment == null) {
      fragment = ClaimTabFragment.newInstance(claim.getClaimId());
    }
    isDrawerEnable(false);

    fm.beginTransaction()
        .setCustomAnimations(R.anim.fragment_slide_left_enter,
            R.anim.fragment_slide_left_exit,
            R.anim.fragment_slide_right_enter,
            R.anim.fragment_slide_right_exit)
        .replace(R.id.fragment_container, fragment, Constants.CLAIM_TAB_FRAG)
        .addToBackStack(Constants.CLAIM_TAB_FRAG)
        .commit();
  }

  @Override
  public void setClaimActionBar() {
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(getString(R.string.claim_action_bar));
    }
  }

  @Override
  public void addNewClaim() {
    FragmentManager fm = getSupportFragmentManager();
    Fragment targetFragment = fm.findFragmentByTag(Constants.CLAIM_LIST_FRAG);
    Fragment fragment = fm.findFragmentByTag(Constants.CLAIM_NEW_FRAG);
    if (fragment == null) {
      fragment = ClaimNew.newInstance();
      fragment.setTargetFragment(targetFragment, Constants.REQUEST_NEW_CLAIM);
    }
    isDrawerEnable(false);
    fm.beginTransaction()
        .setCustomAnimations(R.anim.fragment_slide_left_enter,
            R.anim.fragment_slide_left_exit,
            R.anim.fragment_slide_right_enter,
            R.anim.fragment_slide_right_exit)
        .replace(R.id.fragment_container, fragment, Constants.CLAIM_NEW_FRAG)
        .addToBackStack(Constants.CLAIM_NEW_FRAG)
        .commit();
  }

  /**
   * MyApptContract interface implementation
   */
  @Override
  public void onApartmentSelected(Apartment apartment) {
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentByTag(Constants.APARTMENT_DETAIL_FRAG);
    if (fragment == null) {
      fragment = MyApptDetailFragment.newInstance(apartment.getApartmentId());
    }
    isDrawerEnable(false);
    fm.beginTransaction()
        .setCustomAnimations(R.anim.fragment_slide_left_enter,
            R.anim.fragment_slide_left_exit,
            R.anim.fragment_slide_right_enter,
            R.anim.fragment_slide_right_exit)
        .replace(R.id.fragment_container, fragment, Constants.APARTMENT_DETAIL_FRAG)
        .addToBackStack(Constants.APARTMENT_DETAIL_FRAG)
        .commit();
  }

  @Override
  public void setApartmentActionBar() {
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(getString(R.string.nav_my_apartments));
    }
  }

  @Override
  public void addNewApartment() {

    FragmentManager fm = getSupportFragmentManager();
    Fragment targetFragment = fm.findFragmentByTag(Constants.APARTMENT_TAB_FRAG);
    Fragment fragment = fm.findFragmentByTag(Constants.APARTMENT_NEW_FRAG);
    if (fragment == null) {
      fragment = MyApptNew.newInstance();
      fragment.setTargetFragment(targetFragment, Constants.REQUEST_ADDRESS);
    }
    isDrawerEnable(false);
    fm.beginTransaction()
        .setCustomAnimations(R.anim.fragment_slide_left_enter,
            R.anim.fragment_slide_left_exit,
            R.anim.fragment_slide_right_enter,
            R.anim.fragment_slide_right_exit)
        .replace(R.id.fragment_container, fragment, Constants.APARTMENT_NEW_FRAG)
        .addToBackStack(Constants.APARTMENT_NEW_FRAG)
        .commit();
  }

  @Override
  public void joinApartment() {
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentByTag(Constants.APARTMENT_JOIN_FRAG);
    if (fragment == null) {
      fragment = MyApptJoin.newInstance();
    }
    isDrawerEnable(false);
    fm.beginTransaction()
        .setCustomAnimations(R.anim.fragment_slide_left_enter,
            R.anim.fragment_slide_left_exit,
            R.anim.fragment_slide_right_enter,
            R.anim.fragment_slide_right_exit)
        .replace(R.id.fragment_container, fragment, Constants.APARTMENT_JOIN_FRAG)
        .addToBackStack(Constants.APARTMENT_JOIN_FRAG)
        .commit();
  }

  /**
   * DashBoardContract interface implementation
   */
  @Override
  public void openClaimList() {
    FragmentManager manager = getSupportFragmentManager();
    Fragment fragment = manager.findFragmentByTag(Constants.CLAIM_LIST_FRAG);
    if (fragment == null) {
      fragment = ClaimListFragment.newInstance();
    }
    manager.beginTransaction()
        .setCustomAnimations(R.anim.fragment_slide_left_enter,
            R.anim.fragment_slide_left_exit,
            R.anim.fragment_slide_right_enter,
            R.anim.fragment_slide_right_exit)
        .replace(R.id.fragment_container, fragment, Constants.CLAIM_LIST_FRAG)
        .addToBackStack(Constants.CLAIM_LIST_FRAG)
        .commit();
  }

  @Override
  public void openApartmentList() {
    FragmentManager manager = getSupportFragmentManager();
    Fragment fragment = manager.findFragmentByTag(Constants.APARTMENT_TAB_FRAG);

    if (fragment == null) {
      fragment = MyApptTabFragment.newInstance();
    }

    manager.beginTransaction()
        .setCustomAnimations(R.anim.fragment_slide_left_enter,
            R.anim.fragment_slide_left_exit,
            R.anim.fragment_slide_right_enter,
            R.anim.fragment_slide_right_exit)
        .replace(R.id.fragment_container, fragment, Constants.APARTMENT_TAB_FRAG)
        .addToBackStack(Constants.APARTMENT_TAB_FRAG)
        .commit();
  }

  /**
   * Disable/enable hamburger menu, also disable/enable left slide to show menu.
   */
  public void isDrawerEnable(boolean isEnabled) {
    if (isEnabled) {
      drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
      toggle.setDrawerIndicatorEnabled(true);
      toggle.syncState();
    } else {
      drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
      toggle.setDrawerIndicatorEnabled(false);
      toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          onBackPressed();
        }
      });
      toggle.syncState();
    }
  }
}
