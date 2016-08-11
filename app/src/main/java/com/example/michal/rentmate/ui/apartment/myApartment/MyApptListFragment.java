package com.example.michal.rentmate.ui.apartment.myApartment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michal.rentmate.R;
import com.example.michal.rentmate.model.pojo.Apartment;
import com.example.michal.rentmate.model.pojo.User;
import com.example.michal.rentmate.model.repositories.ApartmentRepository;
import com.example.michal.rentmate.model.repositories.UserRepository;
import com.example.michal.rentmate.networking.RentMateApi;
import com.example.michal.rentmate.ui.apartment.MyApptContract;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyApptListFragment extends Fragment {

  @Bind(R.id.recyclerView_apartment_list) RecyclerView recyclerView;
  @Bind(R.id.add_first_apartment_layout) RelativeLayout addFirstAptLayout;
  @Bind(R.id.add_first_apartment_button) FloatingActionButton addFirstAptFAB;

  private AptAdapter adapter;
  private User user;
  private MyApptContract.Callbacks callbacks;

  public static MyApptListFragment newInstance() {
    return new MyApptListFragment();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    callbacks = (MyApptContract.Callbacks) context;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    callbacks = null;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    UserRepository userRepo = UserRepository.getInstance();
    user = userRepo.getUser();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_my_appt_list, container, false);
    setHasOptionsMenu(true);
    ButterKnife.bind(this, view);
    callbacks.setApartmentActionBar();
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    updateUi();
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    updateUi();
  }

  //  Listeners
  @OnClick(R.id.add_first_apartment_button)
  public void newApartment() {
    if (user.getGroupId().equals("tenant")) {
      callbacks.joinApartment();
    } else {
      callbacks.addNewApartment();
    }
  }

  private void updateUi() {
    ApartmentRepository repository = ApartmentRepository.getInstance();
    final List<Apartment> apartmentList = repository.getApartmentList();

    if (adapter == null) {
      adapter = new AptAdapter(apartmentList);
      recyclerView.setAdapter(adapter);
    } else {

      adapter.notifyDataSetChanged();
      recyclerView.setAdapter(adapter);
    }
    if (apartmentList.size() > 0) {
      addFirstAptLayout.setVisibility(View.GONE);
    } else {
      addFirstAptLayout.setVisibility(View.VISIBLE);
      addFirstAptFAB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

          callbacks.addNewApartment();
        }
      });
    }
  }

  public class ApartmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public Apartment apartment;
    @Bind(R.id.list_item_textview_apartment_name) TextView aptName;
    @Bind(R.id.list_item_textview_apartment_address) TextView aptAddress;

    public ApartmentHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);
      ButterKnife.bind(this, itemView);
    }

    public void onBind(Apartment apartment) {
      this.apartment = apartment;
      aptName.setText(apartment.getStreet());
//      aptAddress.setText(apartment.getPostalCode());
    }

    @Override
    public void onClick(View v) {
      callbacks.onApartmentSelected(apartment);
    }
  }

  private class AptAdapter extends RecyclerView.Adapter<ApartmentHolder> {

    private List<Apartment> aptList;

    public AptAdapter(List<Apartment> aptList) {
      this.aptList = aptList;
    }

    @Override
    public ApartmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(getActivity());
      View view = inflater.inflate(R.layout.apartment_view_in_recyclerview, parent, false);
      return new ApartmentHolder(view);
    }

    @Override
    public void onBindViewHolder(ApartmentHolder holder, int position) {
      Apartment apartment = aptList.get(position);
      holder.onBind(apartment);
    }

    @Override
    public int getItemCount() {
      return aptList.size();
    }
  }
}
