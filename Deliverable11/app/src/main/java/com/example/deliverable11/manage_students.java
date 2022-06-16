package com.example.deliverable11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class manage_students extends AppCompatActivity {
    EditText name_entry;
    ImageButton add_btn, del_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

        name_entry = findViewById(R.id.enter_stud_name_editText);
        del_btn = findViewById(R.id.delete_stud_btn);
        add_btn = findViewById(R.id.add_stud_button);

        del_btn.setOnClickListener(new View.OnClickListener() {
            String name = name_entry.getText().toString().trim();
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name)) {
                    name_entry.setError("Name is required");
                } else {
                    //REMOVE FROM DATABASE
                }
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            String name = name_entry.getText().toString().trim();
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name)) {
                    name_entry.setError("Name is required");
                } else {
                    //ADD TO DATABASE
                }
            }
        });
    }
}
