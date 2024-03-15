package edu.sub.subvotingsystem;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateCandidateActivity extends AppCompatActivity {

    private EditText editTextCandidateName, edittextCandidateGender;
    private Button buttonRegisterCandidate;
    private ProgressBar progressBar;

    private Spinner postsSpinner;
    private Spinner centersSpinner;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_candidate);

        postsSpinner = findViewById(R.id.posts_spinnerId);
        centersSpinner = findViewById(R.id.centers_spinnerId);


        progressBar = findViewById(R.id.progressBar);
        editTextCandidateName = findViewById(R.id.editText_candidate_name);
        edittextCandidateGender = findViewById(R.id.editText_candidate_gender);

        firebaseDatabase = FirebaseDatabase.getInstance();

        fetchPostsData();
        fetchCentersData();
        iniParty();

    }

    private void fetchPostsData() {
        FirebaseDatabase.getInstance().getReference("Posts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> postsList = new ArrayList<>();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            // Assuming the "Posts" table has a "title" field
                            String postTitle = postSnapshot.child("name").getValue(String.class);
                            postsList.add(postTitle);
                        }

                        ArrayAdapter<String> postsAdapter = new ArrayAdapter<>(
                                CreateCandidateActivity.this,
                                android.R.layout.simple_spinner_item,
                                postsList
                        );
                        postsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        postsSpinner.setAdapter(postsAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }

    private void fetchCentersData() {
        FirebaseDatabase.getInstance().getReference("Centers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> centersList = new ArrayList<>();
                        for (DataSnapshot centerSnapshot : dataSnapshot.getChildren()) {
                            // Assuming the "Centers" table has a "name" field
                            String centerName = centerSnapshot.child("name").getValue(String.class);
                            centersList.add(centerName);
                        }

                        ArrayAdapter<String> centersAdapter = new ArrayAdapter<>(
                                CreateCandidateActivity.this,
                                android.R.layout.simple_spinner_item,
                                centersList
                        );
                        centersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        centersSpinner.setAdapter(centersAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }

    public void iniParty() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        int voteCount = 0;
        buttonRegisterCandidate = findViewById(R.id.button_registerCandidate);
        buttonRegisterCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String candidatePartyName = postsSpinner.getSelectedItem().toString();
                String candidateCenterName = centersSpinner.getSelectedItem().toString();
                String candidateName = editTextCandidateName.getText().toString();
                String candidateGender = edittextCandidateGender.getText().toString();

                if (TextUtils.isEmpty(candidateName)) {
                    Toast.makeText(CreateCandidateActivity.this, "Field can't be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    addDatatoFirebase(candidateName, candidateCenterName, candidatePartyName, candidateGender, voteCount);
                }

                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addDatatoFirebase(String candidateName, String candidateCenterName, String candidatePartyName, String candidateGender, int voteCount) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        // Create a new node in the database with a unique key
        DatabaseReference candidatesReference = databaseReference.child("CandidatesInfo").push();
        CandidateInfo candidate = new CandidateInfo(candidateName, candidateCenterName, candidatePartyName, candidateGender, voteCount);
        candidatesReference.setValue(candidate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data successfully written to the database
                        Toast.makeText(CreateCandidateActivity.this, "Succefully added candidate!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        editTextCandidateName.setText("");
                        edittextCandidateGender.setText("");


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
                        Toast.makeText(CreateCandidateActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}