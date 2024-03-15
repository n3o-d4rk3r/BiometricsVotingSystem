package edu.sub.subvotingsystem;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference timersRef;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;

    private ImageView imageView;
    private TextView textViewFullName;
    private String fullName;
    public static FirebaseAuth authProfile;
    private LinearLayout VoteNowId, profileId, settingid, passChangeId, deleteAcoount, logoutBoxId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        intId();

        database = FirebaseDatabase.getInstance();
        timersRef = database.getReference("Timer Users");

        timerTextView = findViewById(R.id.textViewRemainingTime); // Replace with your TextView ID
        String userId = "your_user_id"; // Replace with the actual user ID

        timersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long selectedTimeInMillis = dataSnapshot.child("selected_time").getValue(Long.class);
                    if (selectedTimeInMillis != null) {
                        startCountdown(selectedTimeInMillis);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ProfileActivity", "Error fetching data: " + databaseError.getMessage());
                // Handle error
            }
        });
    }

    private void startCountdown(long selectedTimeInMillis) {
        long currentTimeInMillis = System.currentTimeMillis();
        long remainingTimeInMillis = selectedTimeInMillis - currentTimeInMillis;

        countDownTimer = new CountDownTimer(remainingTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                displayTimer(millisUntilFinished);
                initTimeOnVoteBtn();
            }

            @Override
            public void onFinish() {
                // Handle countdown
                initTimeEndVoteBtn();
            }
        };

        countDownTimer.start();
    }

    private void displayTimer(long millisUntilFinished) {
        int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
        int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
        int seconds = (int) (millisUntilFinished / 1000) % 60;

        String timerText = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerTextView.setText(timerText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void initTimeOnVoteBtn() {
        VoteNowId = findViewById(R.id.voteNowId);
        VoteNowId.setOnClickListener(view -> {
            //Intent intent = new Intent(ProfileActivity.this, PhoneVerificationActivity.class);
            Toast.makeText(this, "You can't vote right now! please wait for count down!", Toast.LENGTH_LONG).show();

        });
    }

    public void initTimeEndVoteBtn() {
        VoteNowId = findViewById(R.id.voteNowId);
        VoteNowId.setOnClickListener(view -> {
            //Intent intent = new Intent(ProfileActivity.this, PhoneVerificationActivity.class);
            Intent intent = new Intent(ProfileActivity.this, PhoneVerificationActivity.class);
            startActivity(intent);
        });
    }

    public void intId() {

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(this, "User Not Found! Please Registration First!", Toast.LENGTH_LONG).show();
        } else {
            showUserProfile(firebaseUser);
        }
        deleteAcoount = findViewById(R.id.deleteAcoountId);
        deleteAcoount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a custom dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.confirmation_dialog, null);
                builder.setView(dialogView);

                TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);
                Button confirmButton = dialogView.findViewById(R.id.confirm_button);
                Button cancelButton = dialogView.findViewById(R.id.cancel_button);

                final AlertDialog dialog = builder.create();
                dialog.show();

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // User clicked "Yes," delete the Firebase user account
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                if (firebaseUser != null) {
                                                    FirebaseAuth.getInstance().signOut();
                                                    Toast.makeText(ProfileActivity.this, firebaseUser.getEmail() + " Sign out!", Toast.LENGTH_SHORT).show();

                                                    // Clear the activity stack and start the LoginActivity
                                                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                }
                                                Toast.makeText(getApplicationContext(), "Account deleted", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Handle account deletion failure
                                                Toast.makeText(getApplicationContext(), "Account deletion failed", Toast.LENGTH_SHORT).show();
                                            }
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // User clicked "No," close the dialog
                        dialog.dismiss();
                    }
                });
            }
        });

        logoutBoxId = findViewById(R.id.logoutIdd);
        logoutBoxId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseUser != null) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(ProfileActivity.this, firebaseUser.getEmail() + " Sign out!", Toast.LENGTH_SHORT).show();

                    // Clear the activity stack and start the LoginActivity
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, "You aren't logged in yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewFullName = findViewById(R.id.textViewName);

        imageView = findViewById(R.id.imageView_profile_dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UploadProfilePicActivity.class);
                startActivity(intent);
            }
        });

        profileId = findViewById(R.id.myProfileId);
        settingid = findViewById(R.id.mySettingId);
        passChangeId = findViewById(R.id.passChangeid);

        profileId.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
        });
        settingid.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
        });

        passChangeId.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        });
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extracting User Reference from Database for "Registered Users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {
                    fullName = firebaseUser.getDisplayName();

                    textViewFullName.setText(fullName);

                    //Set User DP (After user has uploaded)
                    Uri uri = firebaseUser.getPhotoUrl();

                    //ImageViewer setImagerUri() should not be used with regular URIs. So we are using Picasso
                    Picasso.with(ProfileActivity.this).load(uri).into(imageView);
                } else {
                    Toast.makeText(ProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Disable the back button by leaving this method empty
    }
}