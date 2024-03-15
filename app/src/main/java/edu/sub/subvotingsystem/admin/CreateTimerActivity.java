package edu.sub.subvotingsystem.admin;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import edu.sub.subvotingsystem.R;

public class CreateTimerActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference timersRef;

    private TimePicker timePicker;
    private Button saveTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_timer);


        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        timersRef = database.getReference("Timer Users");

        timePicker = findViewById(R.id.timePicker);

        saveTimeButton = findViewById(R.id.submitButton);
        saveTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedTime();
            }
        });
    }

   private void saveSelectedTime() {
        int selectedHour = 0, selectedMinute = 0;
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           selectedHour = timePicker.getHour();
       }
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           selectedMinute = timePicker.getMinute();
       }

       long selectedTimeInMillis = calculateSelectedTimeInMillis(selectedHour, selectedMinute);

        String userId = "your_user_id"; // Provide a user ID
        DatabaseReference userTimerRef = timersRef.child(userId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("selected_time", selectedTimeInMillis);

        userTimerRef.setValue(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Timer set successfully added!", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("CreateTimerActivity", "Error adding data: " + e.getMessage());
                    // Handle error
                });
    }

    private long calculateSelectedTimeInMillis(int hourOfDay, int minute) {
        // Calculate the selected time in milliseconds
        // You can adjust this logic based on your needs
        long currentTimeInMillis = System.currentTimeMillis();
        return currentTimeInMillis + (hourOfDay * 3600000L) + (minute * 60000L);
    }
}
