package com.example.deliverable11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class edit_courses extends AppCompatActivity {
    EditText old_code_entry, old_name_entry, new_code_entry, new_name_entry;
    ImageButton edit_btn, del_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_courses);

        old_code_entry = findViewById(R.id.course_code_to_edit);
        old_name_entry = findViewById(R.id.course_name_to_edit);
        new_code_entry = findViewById(R.id.new_course_code);
        new_name_entry = findViewById(R.id.new_course_name);
        edit_btn = findViewById(R.id.edit_course_btn);
        del_btn = findViewById(R.id.delete_course_btn);

        edit_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String old_code = old_code_entry.getText().toString().trim();
                String old_name = old_name_entry.getText().toString().trim();
                String new_code = new_code_entry.getText().toString().trim();
                String new_name = new_name_entry.getText().toString().trim();

                if (TextUtils.isEmpty(old_code) && TextUtils.isEmpty(old_name) && TextUtils.isEmpty(new_code) && TextUtils.isEmpty(new_name)) {
                    old_code_entry.setError("Course code required");
                    old_name_entry.setError("Course name required");
                    new_code_entry.setError("New course details required");
                    new_name_entry.setError("New course details required");
                    return;
                } else if (TextUtils.isEmpty(old_code) && TextUtils.isEmpty(old_name)) {
                    old_code_entry.setError("Course code required");
                    old_name_entry.setError("Course name required");
                    return;
                } else if (TextUtils.isEmpty(old_code)) {
                    old_code_entry.setError("Course code required");
                    return;
                } else if (TextUtils.isEmpty(old_name)) {
                    old_name_entry.setError("Course name required");
                    return;
                } else if (TextUtils.isEmpty(new_code) && TextUtils.isEmpty(new_name)) {
                    new_code_entry.setError("New course details required");
                    new_name_entry.setError("New course details required");
                    return;
                } else if (TextUtils.isEmpty(new_code)) {
                    new_code = old_code;
                    //ENTER INTO DATABASE
                } else if (TextUtils.isEmpty(new_name)) {
                    new_name = old_name;
                    //ENTER INTO DATABASE
                } else {
                    //ENTER INTO DATABASE
                }
            }
        });
        
        del_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String old_code = old_code_entry.getText().toString().trim();
                String old_name = old_name_entry.getText().toString().trim();
                String new_code = new_code_entry.getText().toString().trim();
                String new_name = new_name_entry.getText().toString().trim();
                
                if (TextUtils.isEmpty(old_code) && TextUtils.isEmpty(old_name)) {
                    old_code_entry.setError("Course code required");
                    old_name_entry.setError("Course name required");
                    return;
                } else if (TextUtils.isEmpty(old_code)) {
                    old_code_entry.setError("Course code required");
                    return;
                } else if (TextUtils.isEmpty(old_name)) {
                    old_name_entry.setError("Course name required");
                    return;
                } else {
                    //REMOVE FROM DATABASE
                }
            }
        });

    }
}
