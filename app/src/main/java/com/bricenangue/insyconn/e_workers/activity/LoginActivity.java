package com.bricenangue.insyconn.e_workers.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bricenangue.insyconn.e_workers.R;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressBar;
    private static final int APP_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        com.facebook.accountkit.AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken !=null){
            //launchAccountActivity();
        }
    }

    private void launchAccountActivity() {
        startActivity(new Intent(LoginActivity.this,HomePageActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        dismissProgressbar();
        finish();
    }

    private void showProgressbar(){
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage(getString(R.string.progress_dialog_connecting));
        progressBar.show();
    }
    private void dismissProgressbar(){
        if (progressBar!=null){
            progressBar.dismiss();
        }
    }


    private void onLogin(final LoginType loginType){
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        loginType,
                        AccountKitActivity.ResponseType.TOKEN
                );

        final AccountKitConfiguration configuration = configurationBuilder.build();

        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration);
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    public void onPhoneLogin(View view){
        onLogin(LoginType.PHONE);
    }

    public void onEmailLogin(View view){
        onLogin(LoginType.EMAIL);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showProgressbar();
       if(requestCode == APP_REQUEST_CODE){

            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null){
                Toast.makeText(getApplicationContext(),
                        loginResult.getError().getErrorType().getMessage(),Toast.LENGTH_SHORT).show();
            }else if (loginResult.getAccessToken() !=null){

                launchAccountActivity();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
