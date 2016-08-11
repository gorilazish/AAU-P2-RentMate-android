package com.example.michal.rentmate.ui.claims.newClaimDetail;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.michal.rentmate.R;
import com.example.michal.rentmate.model.pojo.Claim;
import com.example.michal.rentmate.model.pojo.Message;
import com.example.michal.rentmate.model.pojo.User;
import com.example.michal.rentmate.model.repositories.UserRepository;
import com.example.michal.rentmate.networking.RentMateApi;
import com.example.michal.rentmate.networking.RestService;
import com.example.michal.rentmate.util.Constants;
import com.example.michal.rentmate.util.Helper;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClaimMessageDialog extends DialogFragment {

  @Bind(R.id.new_dialog_message_edit_text) EditText messageEditText;

  private RentMateApi service;
  private User user;
  private UserRepository userRepo;
  private String claimID;
  private boolean isMessageCreated = true;

  public static ClaimMessageDialog newInstance(String claimID) {
    Bundle arg = new Bundle();
    arg.putSerializable(Constants.ARG_CLAIM_NEW_MESSAGE, claimID);

    ClaimMessageDialog dialog = new ClaimMessageDialog();
    dialog.setArguments(arg);
    return dialog;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    userRepo = UserRepository.getInstance();
    user = userRepo.getUser();
    claimID = (String) getArguments().getSerializable(Constants.ARG_CLAIM_NEW_MESSAGE);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    AlertDialog.Builder msgDialog = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.frag_message_dilaog, null);
    ButterKnife.bind(this, view);
    msgDialog.setView(view)
        .setTitle(R.string.new_message)
        .setPositiveButton(R.string.create_message, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            createMessage(claimID);
          }
        })
        .setNegativeButton(R.string.delete_message, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });

    return msgDialog.create();
  }

  private void createMessage(String claimID) {
    service = RestService.getInstance();
    Call<Claim> call = service.createMessage(Helper.getHeader(user), claimID, getMessage());
    call.enqueue(new Callback<Claim>() {
      @Override
      public void onResponse(Call<Claim> call, Response<Claim> response) {
        if (response.isSuccessful()) {
          sendResult(Activity.RESULT_OK, isMessageCreated);
        } else {
          sendResult(Activity.RESULT_OK, !isMessageCreated);
        }
      }

      @Override
      public void onFailure(Call<Claim> call, Throwable t) {
        Log.e("MSG RESPONCE", "MSG FAILURE");
      }
    });
  }

  private Message getMessage() {
    Message message = new Message();
    String msg = messageEditText.getText().toString();
    message.setMessage(msg);
    return message;
  }

  private void sendResult(int resultCode, Boolean isMessageCreated) {
    if (getTargetFragment() == null) {
      return;
    }
    Intent intent = new Intent();
    intent.putExtra(Constants.EXTRA_MESSAGE, isMessageCreated);
    getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
  }
}
