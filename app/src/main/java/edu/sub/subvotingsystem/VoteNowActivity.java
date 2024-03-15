package edu.sub.subvotingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.sub.subvotingsystem.admin.adapter.CandidateAdapter;

public class VoteNowActivity extends AppCompatActivity {

    private LinearLayout presidentGone, vicePresidentGone, jointSecretaryGone, generalSecretaryGone, other;

    private Button VoteDone;

    private RecyclerView recyclerViewPresident;
    private RecyclerView recyclerViewVicePresident;
    private RecyclerView recyclerViewGeneralSecretary;
    private RecyclerView recyclerViewJointSecretary;
    private RecyclerView recyclerViewOthers;

    private CandidateAdapter adapterPresident;
    private CandidateAdapter adapterVicePresident;
    private CandidateAdapter adapterGeneralSecretary;
    private CandidateAdapter adapterJointSecretary;
    private CandidateAdapter adapterOthers;

    private List<CandidateInfo> presidentList = new ArrayList<>();
    private List<CandidateInfo> vicePresidentList = new ArrayList<>();
    private List<CandidateInfo> generalSecretaryList = new ArrayList<>();
    private List<CandidateInfo> jointSecretaryList = new ArrayList<>();
    private List<CandidateInfo> OthersList = new ArrayList<>();

    private boolean isVotingInProgress = false;
    private DatabaseReference votingReference; // Firebase reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_now);

        initRecyclerViews();
        votingReference = FirebaseDatabase.getInstance().getReference("Voting");
        loadCandidates();
    }

    private void initRecyclerViews() {
        presidentGone = findViewById(R.id.layoutPresident);
        jointSecretaryGone = findViewById(R.id.jointSecretaryGoneId);
        vicePresidentGone = findViewById(R.id.vicePresidentGoneId);
        generalSecretaryGone = findViewById(R.id.generalSecretaryGoneId);
        other = findViewById(R.id.otherId);

        VoteDone = findViewById(R.id.VoteDoneId);
        VoteDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickBtn(); // Call the method when the button is clicked
            }
        });


        recyclerViewPresident = findViewById(R.id.recyclerView);
        recyclerViewVicePresident = findViewById(R.id.recyclerView1);
        recyclerViewJointSecretary = findViewById(R.id.recyclerView2);
        recyclerViewGeneralSecretary = findViewById(R.id.recyclerView3);
        recyclerViewOthers = findViewById(R.id.recyclerView4);


        recyclerViewPresident.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVicePresident.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGeneralSecretary.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewJointSecretary.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOthers.setLayoutManager(new LinearLayoutManager(this));

        adapterPresident = new CandidateAdapter(presidentList);
        adapterVicePresident = new CandidateAdapter(vicePresidentList);
        adapterGeneralSecretary = new CandidateAdapter(generalSecretaryList);
        adapterJointSecretary = new CandidateAdapter(jointSecretaryList);
        adapterOthers = new CandidateAdapter(OthersList);

        recyclerViewPresident.setAdapter(adapterPresident);
        recyclerViewVicePresident.setAdapter(adapterVicePresident);
        recyclerViewGeneralSecretary.setAdapter(adapterGeneralSecretary);
        recyclerViewJointSecretary.setAdapter(adapterJointSecretary);
        recyclerViewOthers.setAdapter(adapterOthers);

        adapterPresident.setOnItemClickListener(position -> voteForCandidate(position, presidentList));
        adapterVicePresident.setOnItemClickListener(position -> voteForCandidate(position, vicePresidentList));
        adapterJointSecretary.setOnItemClickListener(position -> voteForCandidate(position, jointSecretaryList));
        adapterGeneralSecretary.setOnItemClickListener(position -> voteForCandidate(position, generalSecretaryList));
        adapterOthers.setOnItemClickListener(position -> voteForCandidate(position, OthersList));

    }

    private void loadCandidates() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("CandidatesInfo");
        Query query = databaseReference.orderByChild("candidatePartyName");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                presidentList.clear();
                vicePresidentList.clear();
                generalSecretaryList.clear();
                OthersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CandidateInfo candidate = snapshot.getValue(CandidateInfo.class);
                    assert candidate != null;

                    String partyName = candidate.getCandidatePartyName();

                    if (partyName.equals("President")) {
                        presidentGone.setVisibility(View.VISIBLE);
                        presidentList.add(candidate);
                    } else if (partyName.equals("Vice-president")) {
                        vicePresidentGone.setVisibility(View.VISIBLE);
                        vicePresidentList.add(candidate);
                    } else if (partyName.equals("General-secretary")) {
                        generalSecretaryGone.setVisibility(View.VISIBLE);
                        generalSecretaryList.add(candidate);
                    } else if (partyName.equals("Joint-secretary")) {
                        jointSecretaryGone.setVisibility(View.VISIBLE);
                        jointSecretaryList.add(candidate);
                    } else {
                        other.setVisibility(View.VISIBLE);
                        OthersList.add(candidate);
                    }
                }

                adapterPresident.notifyDataSetChanged();
                adapterVicePresident.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void voteForCandidate(final int position, List<CandidateInfo> candidateList) {
        if (!isVotingInProgress) {
            isVotingInProgress = true;
            final CandidateInfo selectedCandidate = candidateList.get(position);
            final String candidateId = selectedCandidate.getCandidateName();
            final String userId = getUserId();

            assert userId != null;
            votingReference.child(candidateId).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Toast.makeText(VoteNowActivity.this, "You have already voted for this candidate!", Toast.LENGTH_SHORT).show();
                    } else {
                        performVote(candidateId, userId, position, candidateList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }
    }

    private void performVote(final String candidateId, final String userId, final int position, List<CandidateInfo> candidateList) {
        votingReference.child(candidateId).child(userId).setValue(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DatabaseReference candidateReference = votingReference.child(candidateId);
                        candidateReference.runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                Integer currentVoteCount = currentData.child("voteCount").getValue(Integer.class);
                                if (currentVoteCount == null) {
                                    currentData.child("voteCount").setValue(1);
                                } else {
                                    currentData.child("voteCount").setValue(currentVoteCount + 1);
                                }
                                return Transaction.success(currentData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, boolean committed, @Nullable DataSnapshot currentData) {
                                if (committed) {
                                    candidateList.get(position).setVoteCount(candidateList.get(position).getVoteCount() + 1);
                                    if (adapterPresident != null) {
                                        adapterPresident.notifyDataSetChanged();
                                    }
                                    if (adapterVicePresident != null) {
                                        adapterVicePresident.notifyDataSetChanged();
                                    }
                                    //Toast.makeText(VoteNowActivity.this, "Select!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(VoteNowActivity.this, "Vote failed!", Toast.LENGTH_SHORT).show();
                                    if (databaseError != null) {
                                        Log.e("VoteNowActivity", "Vote error: " + databaseError.getMessage());
                                    }
                                }
                                isVotingInProgress = false; // Reset voting flag
                            }
                        });
                    } else {
                        Toast.makeText(VoteNowActivity.this, "Vote failed!", Toast.LENGTH_SHORT).show();
                        isVotingInProgress = false; // Reset voting flag
                    }
                });
    }

    public void ClickBtn() {
        Toast.makeText(VoteNowActivity.this, "Vote successful!", Toast.LENGTH_SHORT).show();
        // Create an Intent to navigate to the ProfileActivity
        Intent intent = new Intent(VoteNowActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();

    }

    private String getUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser != null ? currentUser.getUid() : null;
    }
}
