package edu.sub.subvotingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import edu.sub.subvotingsystem.admin.CandidateListActivity;

public class VoteActivity extends AppCompatActivity {

    CardView cardViewVoteNow, cardViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote2);

        cardViewResult = findViewById(R.id.candidateId);
        cardViewVoteNow = findViewById(R.id.card_voteNow);

        cardViewVoteNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VoteActivity.this, "Welcome! You can proceed.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(VoteActivity.this, VoteFingerprintActivity.class);
                startActivity(intent);
                finish();

                //boolean isFirstTime = getSharedPreferences("PREF", MODE_PRIVATE).getBoolean("isFirstTime", true);

//                if (isFirstTime) {
//                    getSharedPreferences("PREF", MODE_PRIVATE).edit().putBoolean("isFirstTime", false).apply();
//
//
//                } else {
//                    Toast.makeText(VoteActivity.this, "Don't try to Fake vote!", Toast.LENGTH_SHORT).show();
//                }
            }
        });


        //Result
        cardViewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VoteActivity.this, CandidateListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Handle the back button press to navigate to UpdateProfile.class
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the activity stack
        startActivity(intent);
        finish(); // Finish the current activity
    }
}