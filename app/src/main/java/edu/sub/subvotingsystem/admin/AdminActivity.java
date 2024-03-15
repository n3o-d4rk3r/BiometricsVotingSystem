package edu.sub.subvotingsystem.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import edu.sub.subvotingsystem.CreateCandidateActivity;
import edu.sub.subvotingsystem.MainActivity;
import edu.sub.subvotingsystem.R;
import edu.sub.subvotingsystem.ResultActivity;

public class AdminActivity extends AppCompatActivity {

    private CardView postCreate, candidCreate, VoterResult, candidListId, cardViewLogout, textapp, deleteBtn, CenterCreate, CreateTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        CreateTimer = findViewById(R.id.CreateTimerId);
        CreateTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminActivity.this, CreateTimerActivity.class);
                startActivity(intent);
            }
        });
        CenterCreate = findViewById(R.id.CenterCreateId);
        CenterCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminActivity.this, CreateCenterActivity.class);
                startActivity(intent);
            }
        });
        deleteBtn = findViewById(R.id.deleteBtnId);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(AdminActivity.this, DeleteDatabaseActivity.class);
                startActivity(intent);
            }
        });
        //Create Post
        candidListId = findViewById(R.id.candidListId);
        candidListId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, CandidateListActivity.class);
                startActivity(intent);
            }
        });
        postCreate = findViewById(R.id.postCreateId);
        postCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, CreatePostActivity.class);
                startActivity(intent);
            }
        });
        //Create
        candidCreate = findViewById(R.id.candidCreateId);
        candidCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, CreateCandidateActivity.class);
                startActivity(intent);
            }
        });

        //Result
        VoterResult = findViewById(R.id.VoterResultId);
        VoterResult.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ResultActivity.class);
            startActivity(intent);
        });

        cardViewLogout = findViewById(R.id.cardlogoutId);
        cardViewLogout.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, MainActivity.class);
            startActivity(intent);
        });


        textapp = findViewById(R.id.LogsId);
        textapp.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, LogsActivity.class);
            startActivity(intent);
        });

    }
}