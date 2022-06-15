package com.example.deliverable11;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    EditText iname, ipassword, irepassword;
    Button iregister;
    TextView ilogin;
    FirebaseAuth fAuth;
    //ProgressBar iprogressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        iname = findViewById(R.id.userName);
        ipassword = findViewById(R.id.password);
        irepassword = findViewById(R.id.reenter_password);
        iregister = findViewById(R.id.create_the_account);
        ilogin = findViewById(R.id.login);

        fAuth = FirebaseAuth.getInstance();
        //iprogressbar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        iregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = iname.getText().toString().trim();
                String password = ipassword.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    iname.setError("name is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    ipassword.setError("Password is required");
                }

                if (password.length() < 6) {
                    ipassword.setError("Password must be >= 6 Characters");
                }

                //iprogressbar.setVisibility(View.VISIBLE);

                //Register User
                fAuth.createUserWithEmailAndPassword(name, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(SignUp.this, "error occured" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        ilogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }
}