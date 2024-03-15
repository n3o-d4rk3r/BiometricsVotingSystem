package edu.sub.subvotingsystem.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class CreatePostActivity extends AppCompatActivity {

    private TextInputEditText edittextBoxPost;
    private Button submitBtn;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> postList;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");

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
        edittextBoxPost = findViewById(R.id.edittextBoxPost);
        submitBtn = findViewById(R.id.createPostBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postName = Objects.requireNonNull(edittextBoxPost.getText()).toString();
                if (!postName.isEmpty()) {
                    createPost(postName);
                }
            }
        });
    }

    private void createPost(String postName) {
        String postId = databaseReference.push().getKey();
        if (postId != null) {
            DatabaseReference postReference = databaseReference.child(postId);
            postReference.child("name").setValue(postName);
            edittextBoxPost.clearFocus();
            edittextBoxPost.setText("");

            Toast.makeText(this, "Post created with category: " + postName, Toast.LENGTH_SHORT).show();
        }
    }
}