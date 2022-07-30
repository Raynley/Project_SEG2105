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

import java.sql.Array;
import java.util.ArrayList;

/**Methods and functions for Admin to manage instructors
 * @author tannergiddings
 */
public class manage_instructors extends AppCompatActivity {
    EditText name_entry;
    ImageButton del_ins;
    TextView display;
    FirebaseDatabase database;
    DatabaseReference reference;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_instructors);
        
        name_entry = findViewById(R.id.enter_instruc_name);
        del_ins = findViewById(R.id.delete_instruc_btn);
        display = findViewById(R.id.instructor_display);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User").child("Instructor");
        ArrayList<Instructor> instructorList = new ArrayList<>();

        ValueEventListener initList = new ValueEventListener() {
            /**
             * Initialises instructorList
             * @author tannergiddings
             * @param snapshot snapshot of database
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    instructorList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        instructorList.add(ds.getValue(Instructor.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ValueEventListener postListener = new ValueEventListener() {
            /**
             * Displays the courses to the admin
             * @author tannergiddings
             * @param snapshot snapshot of database
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String textDisplay = "";
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        textDisplay = textDisplay + ds.getValue(Instructor.class).toString() + "\n";
                    }
                    display.setText(textDisplay);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        reference.addValueEventListener(postListener);
        
        del_ins.setOnClickListener(new View.OnClickListener() {
            /**
             * Removes instructor from database
             * @author tannergiddings
             * @param v
             */
            @Override
            public void onClick(View v) {
                reference.addValueEventListener(initList);
                String name = name_entry.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    name_entry.setError("Name required");
                } else {
                    int index = getIndex(name, instructorList);

                    if (index < 0) {
                        name_entry.setText("Instructor not found");
                        return;
                    } else {
                        reference.child(String.valueOf(index)).removeValue();
                        return;
                    }
                }
            }
        });
        
    }

    /**finds the index of the instructor for database
     * @author tannergiddings
     * @param instructor instructor's index to be found
     * @param instructorList list in which to be found
     * @return index of instructor or -1 if not found
     */
    public int getIndex(String instructor, ArrayList<Instructor> instructorList) {
        for (int i = 0; i < instructorList.size(); i++) {
            if (instructor.equals(instructorList.get(i).getUsername())) {
                return instructorList.get(i).getIndex();
            }
        }
         return -1;
    }
}
