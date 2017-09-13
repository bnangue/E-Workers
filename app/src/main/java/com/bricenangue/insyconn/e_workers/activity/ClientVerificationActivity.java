package com.bricenangue.insyconn.e_workers.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bricenangue.insyconn.e_workers.R;
import com.bricenangue.insyconn.e_workers.model.EmployeePosition;
import com.bricenangue.insyconn.e_workers.service.UserSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ClientVerificationActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText editText;
    private ProgressDialog progressBar;

    private UserSharedPreference userSharedPreference;
    private String  eWorkerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_verification);

        auth = FirebaseAuth.getInstance();

        userSharedPreference=new UserSharedPreference(this);
        signIn();
        editText =(EditText)findViewById(R.id.ClientVerfication_editext_enter_code);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                     eWorkerID=editText.getText().toString();
                    if (TextUtils.isEmpty(eWorkerID)){
                        editText.setError(getResources().getString(R.string.Verification_editext_enter_code_error_text));
                    }else if (eWorkerID.length()<=7){
                        editText.setError(getResources().getString(R.string.Verification_editext_enter_code_error_shortEntry));

                    }else {
                        verify(eWorkerID);
                    }

                }

                return false;
            }
        });

    }

    private void signIn(){
        auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                }else {
                    // Sign In fail
                    Toast.makeText(ClientVerificationActivity.this, "Error while connecting to server", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onSendRequestCode(View view){
         eWorkerID=editText.getText().toString();
        if (TextUtils.isEmpty(eWorkerID)){
            editText.setError(getResources().getString(R.string.Verification_editext_enter_code_error_text));
        }else if (eWorkerID.length()<=7){
            editText.setError(getResources().getString(R.string.Verification_editext_enter_code_error_shortEntry));

        }else {
            verify(eWorkerID);
        }
    }
    private void verify(String verificationCode){
        showProgressbar();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference reference =database.getReference("Verification_Codes")
                .child(verificationCode);

        reference.child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    //user exists. check if it's the first time
                    reference.child("registration_date").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                // user already registered once
                                startActivity(new Intent(ClientVerificationActivity.this,LoginActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                userSharedPreference.storeUserEWorkerID(eWorkerID);
                                dismissProgressbar();
                                finish();
                            }else {
                                //first registration save current time as registration date
                                Map<String,Object> children=new HashMap<>();
                                children.put("/status",true);
                                children.put("/registration_date", System.currentTimeMillis());
                                reference.updateChildren(children).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            startActivity(new Intent(ClientVerificationActivity.this,LoginActivity.class)
                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                            userSharedPreference.storeUserEWorkerID(eWorkerID);
                                            dismissProgressbar();
                                            finish();
                                        }else {
                                            Toast.makeText(ClientVerificationActivity.this,
                                                    "verification Failed", Toast.LENGTH_SHORT).show();

                                            dismissProgressbar();

                                        }
                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(ClientVerificationActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            dismissProgressbar();
                        }
                    });

                }else {
                    Toast.makeText(ClientVerificationActivity.this, "This e-Worker-ID Doesn't exist. Please request a verification code from your employer", Toast.LENGTH_SHORT).show();
                    dismissProgressbar();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ClientVerificationActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                dismissProgressbar();

            }
        });

        reference.child("employee").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    EmployeePosition employeePosition =dataSnapshot.getValue(EmployeePosition.class);
                    userSharedPreference.storeUserEmployeePosition(employeePosition);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ClientVerificationActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                dismissProgressbar();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        dismissProgressbar();
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
}
