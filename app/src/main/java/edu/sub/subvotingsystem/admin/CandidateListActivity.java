package edu.sub.subvotingsystem.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.sub.subvotingsystem.CandidateInfo;
import edu.sub.subvotingsystem.R;
import edu.sub.subvotingsystem.admin.adapter.CandidateAdapter;

public class CandidateListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CandidateAdapter adapter;
    private List<CandidateInfo> candidateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_list);
        loadAllCandidates();
    }

    public void loadAllCandidates() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CandidateAdapter(candidateList);
        recyclerView.setAdapter(adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CandidatesInfo");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                candidateList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CandidateInfo candidate = snapshot.getValue(CandidateInfo.class);
                    candidateList.add(candidate);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        // Set a click listener for each candidate item
        adapter.setOnItemClickListener(new CandidateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click here, without the voting logic
            }
        });
    }

}