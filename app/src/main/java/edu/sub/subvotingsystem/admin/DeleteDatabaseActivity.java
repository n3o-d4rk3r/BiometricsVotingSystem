package edu.sub.subvotingsystem.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.sub.subvotingsystem.R;

public class DeleteDatabaseActivity extends AppCompatActivity {

    private CardView postDelete, candidCreate, voters, allDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_database);
        FirebaseApp.initializeApp(this);

        InitViewDelete();
    }

    public void InitViewDelete() {
        postDelete = findViewById(R.id.postDeleteId);
        postDelete.setOnClickListener(view -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Posts");

            reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(DeleteDatabaseActivity.this, "Posters Database deleted, you can' back again!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DeleteDatabaseActivity.this, "Really Sorry! Try again later!", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        });

        candidCreate = findViewById(R.id.candidCreateId);
        candidCreate.setOnClickListener(view -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("CandidatesInfo");

            reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(DeleteDatabaseActivity.this, "Posters Database deleted, you can' back again!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DeleteDatabaseActivity.this, "Really Sorry! Try again later!", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        });

        voters = findViewById(R.id.votersId);
        voters.setOnClickListener(view -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Voting");

            reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(DeleteDatabaseActivity.this, "Posters Database deleted, you can' back again!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DeleteDatabaseActivity.this, "Really Sorry! Try again later!", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        });

        allDelete = findViewById(R.id.allDeleteId);
        allDelete.setOnClickListener(view -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            // Delete data from the "Centers" path
            DatabaseReference centersReference = database.getReference("Centers");
            centersReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(DeleteDatabaseActivity.this, "Centers Database deleted, you can't go back again!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DeleteDatabaseActivity.this, "Really Sorry! Try again later!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Delete data from the "Voting" path
            DatabaseReference votingReference = database.getReference("Voting");
            votingReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(DeleteDatabaseActivity.this, "Voting Database deleted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DeleteDatabaseActivity.this, "Error deleting Voting Database!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Delete data from the "CandidatesInfo" path
            DatabaseReference candidatesReference = database.getReference("CandidatesInfo");
            candidatesReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(DeleteDatabaseActivity.this, "CandidatesInfo Database deleted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DeleteDatabaseActivity.this, "Error deleting CandidatesInfo Database!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

    }
}