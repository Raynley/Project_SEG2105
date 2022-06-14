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

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    EditText username_entry, password_entry;
    Button verify_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        username_entry = findViewById(R.id.userName);
        password_entry = findViewById(R.id.password);
        verify_btn = findViewById(R.id.btn_login);
        User_Database userDatabase;

        userDatabase = new User_Database(this);

        verify_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Find product", Toast.LENGTH_SHORT).show();
                String username = username_entry.getText().toString().trim();
                String password = password_entry.getText().toString().trim();

                if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
                    username_entry.setError("Username is required");
                    password_entry.setError("Password is required");
                    return;
                } else if (TextUtils.isEmpty(username)) {
                    username_entry.setError("Username is required");
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    password_entry.setError("Password is required");
                    return;
                } else {

                }
            }
        });
    }

}
