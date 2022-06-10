package com.example.deliverable11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class sign_up_page<userName> extends AppCompatActivity {
    EditText userName, password, re_password;
    Button create;
    FirebaseAuth fAuth;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        re_password = findViewById(R.id.reenter_password);
        create = findViewById(R.id.create_the_account);

        fAuth = FirebaseAuth.getInstance();
        bar = findViewById(R.id.progressBar);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String r_username = userName.getText().toString().trim();
                String r_password = password.getText().toString().trim();
                String password_2 = re_password.getText().toString().trim();

                if(TextUtils.isEmpty(r_username)){
                    userName.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(r_password)) {
                    password.setError("Password is required");
                    return;
                }

                if(!password_2.equals(r_password)){
                    re_password.setError("Password doesn't match");
                    return;
                }

                bar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(r_username,r_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(sign_up_page.this, "User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else {
                            Toast.makeText(sign_up_page.this, "Error ! " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

}