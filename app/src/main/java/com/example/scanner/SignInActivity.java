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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity {
    Button continueButton;
    TextView textView;
    EditText name,phonenumber;
    Button verify;
    public static final String TAG="SignInActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signin);
        name=findViewById(R.id.nameedittextgoogle);
        phonenumber=findViewById(R.id.phoneedittextgoogle);
        continueButton=findViewById(R.id.button);
        verify=findViewById(R.id.vbutton);
        textView=findViewById(R.id.verified);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                verify.setClickable(false);
                verify.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.VISIBLE);
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=name.getText().toString();
                String userNo="+91"+phonenumber.getText().toString();
                String uniqueId=userName.substring(0,3)+"."+userNo.substring(3,6)+"."+userNo.substring(10);
                Intent intent=new Intent(SignInActivity.this,PhoneVerify.class);
                intent.putExtra("UID",uniqueId);
                intent.putExtra("Phone Number",userNo);
                intent.putExtra("Name",userName);
                startActivity(intent);
                finish();
            }
        });
    }
}