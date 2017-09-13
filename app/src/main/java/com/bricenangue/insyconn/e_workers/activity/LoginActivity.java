package com.bricenangue.insyconn.e_workers.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bricenangue.insyconn.e_workers.R;
import com.bricenangue.insyconn.e_workers.model.User;
import com.bricenangue.insyconn.e_workers.service.UserSharedPreference;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressBar;
    private static final int APP_REQUEST_CODE = 1;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private UserSharedPreference userSharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userSharedPreference=new UserSharedPreference(this);
        auth=FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                } else {
                    // User is signed out
                   auth.signInAnonymously();
                }
            }
        };
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

                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference reference =database.getReference("Users")
                                .child(account.getId())
                                .child("userPublic");

                        Map<String,Object> children=new HashMap<>();

                        final User userfb=new User();
                        userfb.setLast_logging(System.currentTimeMillis());
                        userfb.setId(account.getId());
                        String eWorkerID= userSharedPreference.getUserEWorkerID();
                        if (eWorkerID !=null && !eWorkerID.isEmpty()){
                            userfb.seteWorkerID(eWorkerID);
                        }
                        //save user in firebase realtime database

                        if (account.getEmail()!=null){
                            userfb.setLoginType(account.getEmail());

                            children.put("/account_id",userfb.getId());
                            children.put("/loginType",userfb.getLoginType());
                            children.put("/eWorkerID",userfb.geteWorkerID());
                            children.put("/last_logging",userfb.getLast_logging());

                            reference.updateChildren(children).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        launchAccountActivity();
                                        userSharedPreference.storeUserData(userfb);
                                    }
                                }
                            });
                        }else{

                            PhoneNumber phoneNumber =account.getPhoneNumber();
                            String formattedPhoneNumber=formatPhoneNumber(phoneNumber.toString());

                            userfb.setLoginType(formattedPhoneNumber);

                            children.put("/account_id",userfb.getId());
                            children.put("/loginType",userfb.getLoginType());
                            children.put("/eWorkerID",userfb.geteWorkerID());
                            children.put("/last_logging",userfb.getLast_logging());

                            reference.updateChildren(children).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        launchAccountActivity();
                                        userSharedPreference.storeUserData(userfb);


                                    }
                                }
                            });

                        }

                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Toast.makeText(getApplicationContext(), accountKitError.getErrorType().getMessage()
                        ,Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }
    }

    private String formatPhoneNumber(String phoneNumber){
        try {
            PhoneNumberUtil util = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber pn =util.parse(phoneNumber, Locale.getDefault().getCountry());
            phoneNumber = util.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);

        }catch (NumberParseException e){
            e.printStackTrace();
        }
        return phoneNumber;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
