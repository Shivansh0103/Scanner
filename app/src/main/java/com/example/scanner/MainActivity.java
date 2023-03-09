package com.example.scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference ref=firebaseDatabase.getReference("Users");
    TextView answer;
    String name,phone,uid;
    private static final int requestCode = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        answer=findViewById(R.id.textCode);
        getSupportActionBar().hide();
        Intent in=getIntent();
        name=in.getStringExtra("Name");
        phone=in.getStringExtra("Phone Number");
        uid=in.getStringExtra("UID");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, requestCode);
            CodeScannerView scannerView = findViewById(R.id.scannerView);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String ans=result.getText();
                        String status;
                        if(ans.equalsIgnoreCase(uid))
                            status="true";
                        else
                            status="false";
                        String text=ans+" "+uid;
                        //answer.setText(text);
                        //Toast.makeText(MainActivity.this, ans, Toast.LENGTH_SHORT).show();
                        DatabaseReference databaseReference = ref.child(phone);
                        Map<String, Object> map = new HashMap<>();
                        map.put("Status", status);
                        //map.put("Name",name);
                        //map.put("Number",phone);
                        databaseReference.setValue(map);
                        finish();
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}