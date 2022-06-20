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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText iname, ipassword;
    Button ilogin;
    TextView icreate;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iname = findViewById(R.id.userName);
        ipassword = findViewById(R.id.password);
        ilogin = findViewById(R.id.btn_login);
        icreate = findViewById(R.id.textView2);
        database = FirebaseDatabase.getInstance();

        ilogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = iname.getText().toString();
                String password = ipassword.getText().toString();
                String type;

                if(TextUtils.isEmpty(name)){
                    iname.setError("name is required");
                    return;
                } else if(TextUtils.isEmpty(password)){
                    ipassword.setError("Password is required");
                    return;
                } else {
                    if (name.equals("admin") && password.equals("admin123")) {
                        startActivity(new Intent(getApplicationContext(),welcome_admin.class));
                    }
                    /*reference = database.getReference("User");
                    ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String data_password = ;
                                if (data_password.equals(password)) {
                                    startActivity(new Intent(MainActivity.this, welcome_admin.class));
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    };

                    reference.addValueEventListener(postListener);*/
                }
            }
        });

        icreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
            }
        });
    }
}
