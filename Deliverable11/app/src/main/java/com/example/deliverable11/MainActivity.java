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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText iname, ipassword;
    Button ilogin;
    TextView icreate;

    ProgressBar iprogressbar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iname = findViewById(R.id.userName);
        ipassword = findViewById(R.id.password);
        ilogin = findViewById(R.id.btn_login);
        icreate = findViewById(R.id.textView2);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        ilogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = iname.getText().toString().trim();
                String password = ipassword.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    iname.setError("name is required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    ipassword.setError("Password is required");
                    return;
                }

                if(name.length() < 6){
                    iname.setError("The name should be at least 6 characters long");
                }

                if(password.length() < 6){
                    ipassword.setError("Password must be >= 6 Characters");
                    return;
                }

                fAuth.signInWithEmailAndPassword(name,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), welcome_admin.class));
                        }else{
                            Toast.makeText(MainActivity.this, "Loggin unsuccessful" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        icreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),sign_up_page.class));
            }
        });
    }
}
