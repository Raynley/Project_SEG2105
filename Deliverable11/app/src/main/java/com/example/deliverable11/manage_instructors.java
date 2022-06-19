package com.example.deliverable11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class manage_instructors extends AppCompatActivity {
    EditText name_entry;
    ImageButton del_ins, add_ins;
    TextView display;
    FirebaseDatabase database;
    DatabaseReference reference;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_instructors);
        
        name_entry = findViewById(R.id.course_code_to_edit);
        del_ins = findViewById(R.id.delete_course_btn);
        add_ins = findViewById(R.id.delete_instruc_btn);
        display = findViewById(R.id.instructor_display);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Instructor");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Instructor instructor = snapshot.getValue(Instructor.class);
                    String textDisplay = "";
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        textDisplay = textDisplay + ds.getValue(Instructor.class).getUsername() + "\n";
                    }
                    display.setText(textDisplay);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        reference.addValueEventListener(postListener);
        
        add_ins.setOnClickListener(new View.OnClickListener() {
            String name = name_entry.getText().toString().trim();
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name)) {
                    name_entry.setError("Name required");
                } else {
                    //ADD TO DATABASE
                }
            }
        });
        
        del_ins.setOnClickListener(new View.OnClickListener() {
            String name = name_entry.getText().toString().trim();
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name)) {
                    name_entry.setError("Name required");
                } else {
                    //DELETE FROM DATABASE
                }
            }
        });
        
    }
}
