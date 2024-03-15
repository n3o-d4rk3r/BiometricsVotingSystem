package edu.sub.subvotingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import edu.sub.subvotingsystem.admin.adapter.VoteLogsAdapter;
import edu.sub.subvotingsystem.admin.model.VoteLogInfo;

public class ResultActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ListView listView;
    private ListView listView2;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter1;
    private PieChart pieChart;

    private TextView textViewTotalVotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        pieChart = findViewById(R.id.piechart);
        loadVoteLogsWithPieChart();
        topVoters();
        topVoters2();
    }

    private void loadVoteLogsWithPieChart() {
        DatabaseReference votingReference = FirebaseDatabase.getInstance().getReference("Voting");

        votingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear previous data
                pieChart.clearChart();

                for (DataSnapshot candidateSnapshot : dataSnapshot.getChildren()) {
                    String candidateName = candidateSnapshot.getKey();
                    long voteCount = 0; // Initialize voteCount variable

                    if (candidateSnapshot.child("voteCount").exists()) {
                        // If the voteCount node exists, get its value
                        Long voteCountLong = candidateSnapshot.child("voteCount").getValue(Long.class);
                        if (voteCountLong != null) {
                            voteCount = voteCountLong; // Assign the value to voteCount variable
                        }
                    }
                    // Add pie slice using the voteCount value
                    pieChart.addPieSlice(new PieModel(candidateName, voteCount, Color.parseColor(getRandomColor())));
                }

                pieChart.startAnimation(); // Start animation for the pie chart
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private String getRandomColor() {
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return "#" + Integer.toHexString(color).substring(2);
    }

    static class UserVote {
        private String userName;
        private long voteCount;

        public UserVote(String userName, long voteCount) {
            this.userName = userName;
            this.voteCount = voteCount;
        }

        public String getUserName() {
            return userName;
        }

        public long getVoteCount() {
            return voteCount;
        }
    }

    public void topVoters() {
        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Voting");

        // Retrieve data from Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserVote> userVotes = new ArrayList<>();
                long totalVotes = 0; // Initialize the total vote count

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userName = userSnapshot.getKey();
                    Map<String, Object> userData = (Map<String, Object>) userSnapshot.getValue();

                    if (userData != null && userData.containsKey("voteCount")) {
                        long voteCount = (long) userData.get("voteCount");
                        userVotes.add(new UserVote(userName, voteCount));
                        totalVotes += voteCount; // Increment the total vote count
                    }
                }

                // Sort the userVotes list by voteCount in descending order
                Collections.sort(userVotes, new Comparator<UserVote>() {
                    @Override
                    public int compare(UserVote user1, UserVote user2) {
                        return Long.compare(user2.getVoteCount(), user1.getVoteCount());
                    }
                });

                // Create a list of strings containing the top 4 voter candidate names and vote counts
                List<String> topVoterInfo = new ArrayList<>();
                int count = 0;
                for (UserVote userVote : userVotes) {
                    if (count >= 4) {
                        break;
                    }
                    String userName = userVote.getUserName();
                    long voteCount = userVote.getVoteCount();
                    topVoterInfo.add(userName + ": " + voteCount);
                    count++;
                }

                // Set up the ListView adapter
                ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(
                        ResultActivity.this,
                        android.R.layout.simple_list_item_1,
                        topVoterInfo
                );
                ListView listView = findViewById(R.id.listViewTopVoters);
                listView.setAdapter(listViewAdapter);

                // Update the TextView with the total vote count
                TextView textViewTotalVotes = findViewById(R.id.textViewTotalVotes);
                textViewTotalVotes.setText("Total Votes: " + totalVotes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    public void topVoters2() {
        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Voting");

        // Retrieve data from Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserVote> userVotes = new ArrayList<>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userName = userSnapshot.getKey();
                    Map<String, Object> userData = (Map<String, Object>) userSnapshot.getValue();

                    if (userData != null && userData.containsKey("voteCount")) {
                        long voteCount = (long) userData.get("voteCount");
                        userVotes.add(new UserVote(userName, voteCount));
                    }
                }

                // Sort the userVotes list by voteCount in descending order
                Collections.sort(userVotes, new Comparator<UserVote>() {
                    @Override
                    public int compare(UserVote user1, UserVote user2) {
                        return Long.compare(user2.getVoteCount(), user1.getVoteCount());
                    }
                });

                // Create a list of strings containing all voter candidate names and vote counts
                List<String> voterInfo = new ArrayList<>();
                for (UserVote userVote : userVotes) {
                    String userName = userVote.getUserName();
                    long voteCount = userVote.getVoteCount();
                    voterInfo.add(userName + ": " + voteCount);
                }

                // Set up the ListView adapter
                ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(
                        ResultActivity.this,
                        android.R.layout.simple_list_item_1,
                        voterInfo
                );
                ListView listView2 = findViewById(R.id.recyclerViewTotalVoters);
                listView2.setAdapter(listViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

}
