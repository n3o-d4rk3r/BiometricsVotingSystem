package edu.sub.subvotingsystem.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.sub.subvotingsystem.R;

public class CreateCenterActivity extends AppCompatActivity {
    private TextInputEditText edittextBoxCenter;
    private Button submitBtn;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> postList;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_center);
        databaseReference = FirebaseDatabase.getInstance().getReference("Centers");

        listView = findViewById(R.id.listitems);
        postList = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                postList
        );
        listView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String postName = postSnapshot.child("name").getValue(String.class);
                    if (postName != null) {
                        postList.add(postName);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        edittextBoxCenter = findViewById(R.id.edittextBoxCenterId);
        submitBtn = findViewById(R.id.createCenterBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postName = Objects.requireNonNull(edittextBoxCenter.getText()).toString();
                if (!postName.isEmpty()) {
                    createPost(postName);
                }
            }
        });
    }

    private void createPost(String centerName) {
        String postId = databaseReference.push().getKey();
        if (postId != null) {
            DatabaseReference postReference = databaseReference.child(postId);
            postReference.child("name").setValue(centerName);
            edittextBoxCenter.clearFocus();
            Toast.makeText(this, "Center created with " + centerName, Toast.LENGTH_SHORT).show();
        }
    }
}