package com.example.michal.rentmate.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.michal.rentmate.R;
import com.example.michal.rentmate.util.Constants;
import com.example.michal.rentmate.util.FontUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @Bind(R.id.splash_welcome_text) TextView welcomeText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    ButterKnife.bind(this);
    FontUtil.setFont(getApplicationContext(), getString(R.string.font_roboto_thin), welcomeText);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        Intent intent = new Intent(SplashActivity.this, LogInActivity.class);
        startActivity(intent);
        finish();
      }
    }, Constants.SPLASH_SCREEN_DELAY);
  }
}
