package edu.sub.subvotingsystem.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.sub.subvotingsystem.R;
import edu.sub.subvotingsystem.admin.adapter.VoteLogsAdapter;
import edu.sub.subvotingsystem.admin.model.VoteLogInfo;

public class LogsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VoteLogsAdapter adapter;
    private final List<VoteLogInfo> voteLogsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        recyclerView = findViewById(R.id.recyclerViewLogs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VoteLogsAdapter(voteLogsList);
        recyclerView.setAdapter(adapter);

        loadVoteLogs();
    }

    private void loadVoteLogs() {
        DatabaseReference votingReference = FirebaseDatabase.getInstance().getReference("Voting");

        votingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                voteLogsList.clear();

                for (DataSnapshot candidateSnapshot : dataSnapshot.getChildren()) {
                    String candidateName = candidateSnapshot.getKey(); // Candidate's name
                    long voteCount = candidateSnapshot.child("voteCount").getValue(Long.class);

                    List<String> voters = new ArrayList<>();
                    for (DataSnapshot voterSnapshot : candidateSnapshot.getChildren()) {
                        if (!Objects.equals(voterSnapshot.getKey(), "voteCount")) {
                            voters.add(voterSnapshot.getKey());
                        }
                    }

                    VoteLogInfo voteLogInfo = new VoteLogInfo(candidateName, voteCount, voters);
                    voteLogsList.add(voteLogInfo);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}