package com.example.scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneVerify extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button button;
    EditText editText;
    String verificationCode,name,phoneNumber,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_phone_verify);
        firebaseAuth=FirebaseAuth.getInstance();
        button=findViewById(R.id.verifyButton);
        editText=findViewById(R.id.otpEditText);
        Intent in=getIntent();
        name=in.getStringExtra("Name");
        phoneNumber=in.getStringExtra("Phone Number");
        uid=in.getStringExtra("UID");
        intitiateOtp();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationCode,editText.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });
    }

    private void intitiateOtp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                5,
                TimeUnit.MILLISECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(PhoneVerify.this, "Error"+e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        verificationCode=s;
                    }
                }
        );
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent=new Intent(PhoneVerify.this,MainActivity.class);
                            intent.putExtra("UID",uid);
                            intent.putExtra("Phone Number",phoneNumber);
                            intent.putExtra("Name",name);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(PhoneVerify.this, "Sign in failed", Toast.LENGTH_SHORT).show();;
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(PhoneVerify.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}