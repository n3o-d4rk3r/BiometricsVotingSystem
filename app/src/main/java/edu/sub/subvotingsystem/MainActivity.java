package edu.sub.subvotingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import edu.sub.subvotingsystem.admin.AdminActivity;
import edu.sub.subvotingsystem.admin.AdminLoginActivity;

public class MainActivity extends AppCompatActivity {

    private Button button_login;
    private TextView textViewRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Login User
        Button AdminLoginId = findViewById(R.id.AdminLogin);
        //Result
        AdminLoginId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdminLoginActivity.class);
                startActivity(intent);
            }
        });
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        //Open Register Activity
        TextView textViewRegister = findViewById(R.id.textView_register);
        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        // Disable the back button by leaving this method empty
    }

}