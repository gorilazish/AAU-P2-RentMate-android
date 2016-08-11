package com.example.michal.rentmate.ui.claims.newClaimDetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.michal.rentmate.R;
import com.example.michal.rentmate.model.pojo.Claim;
import com.example.michal.rentmate.model.pojo.Message;
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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClaimMessageFragment extends Fragment {

  @Bind(R.id.add_message_button) FloatingActionButton addMsgButton;
  @Bind(R.id.recyclerView_message_list) RecyclerView recyclerView;

  private MsgAdapter adapter;
  private RentMateApi service;
  private User user;
  private List<Message> msgList;
  private String claimId;

  public static ClaimMessageFragment newInstance(String claimID) {
    Bundle arg = new Bundle();
    arg.putSerializable(Constants.ARG_CLAIM_MESSAGE_ID, claimID);
    ClaimMessageFragment messageFragment = new ClaimMessageFragment();
    messageFragment.setArguments(arg);
    return messageFragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    claimId = (String) getArguments().getSerializable(Constants.ARG_CLAIM_MESSAGE_ID);
    Claim claim = ClaimRepository.getInstance().getClaim(claimId);
    msgList = claim.getMessages();
    user = UserRepository.getInstance().getUser();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.frag_claim_messsage, container, false);
    ButterKnife.bind(this, view);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    updateUI(msgList);
    return view;
  }

  //  Listeners
  @OnClick(R.id.add_message_button)
  public void newMessage() {
    newMessageDialog();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case Constants.REQUEST_MESSAGE:
          reloadMessages();
      }
    }
  }

  private void updateUI(List<Message> msgList) {
    adapter = new MsgAdapter(msgList);
    recyclerView.setAdapter(adapter);
  }

  private void newMessageDialog() {
    FragmentManager manager = getFragmentManager();
    ClaimMessageDialog msgDialog = ClaimMessageDialog.newInstance(claimId);
    msgDialog.setTargetFragment(ClaimMessageFragment.this, Constants.REQUEST_MESSAGE);
    msgDialog.show(manager, Constants.DIALOG_MESSAGE);
  }

  private void reloadMessages() {
    service = RestService.getInstance();
    Call<Claim> call = service.getClaim(Helper.getHeader(user), claimId);
    call.enqueue(new Callback<Claim>() {
      @Override
      public void onResponse(Call<Claim> call, Response<Claim> response) {
        if (response.isSuccessful()) {
          msgList = response.body().getMessages();
          updateUI(msgList);
        }
      }

      @Override
      public void onFailure(Call<Claim> call, Throwable t) {

      }
    });
  }

  public class MsgHolder extends RecyclerView.ViewHolder {
    public Message message;
    @Bind(R.id.message_text_view) TextView messageTextView;
    @Bind(R.id.card_view_tenant) CardView cardView;

    public MsgHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void onBind(Message message) {
      this.message = message;
      CardView.LayoutParams params = new CardView.LayoutParams(
          CardView.LayoutParams.MATCH_PARENT,
          CardView.LayoutParams.WRAP_CONTENT
      );
      params.setMargins(0, 10, 70, 0);
      messageTextView.setText(message.getMessage());
      if (message.getPostedBy().getGroupId().equals("tenant")) {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.material_green_300));
        cardView.setLayoutParams(params);
        messageTextView.setTextColor(getResources().getColor(R.color.colorWhite));
      } else {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.colorCard));
      }
    }
  }

  public class MsgAdapter extends RecyclerView.Adapter<MsgHolder> {
    private List<Message> messageList;

    public MsgAdapter(List<Message> messageList) {
      this.messageList = messageList;
    }

    @Override
    public MsgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(getActivity());
      View view = inflater.inflate(R.layout.message_view_in_recyclerview, parent, false);
      return new MsgHolder(view);
    }

    @Override
    public void onBindViewHolder(MsgHolder holder, int position) {
      Message msg = messageList.get(position);
      holder.onBind(msg);
    }

    @Override
    public int getItemCount() {
      return messageList.size();
    }
  }
}
