package edu.sub.subvotingsystem.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.sub.subvotingsystem.ProfileActivity;
import edu.sub.subvotingsystem.R;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText editTextLoginEmail, editTextLoginPwD;
    private Button AdminLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        editTextLoginEmail = findViewById(R.id.email);
        editTextLoginPwD = findViewById(R.id.pwd);
        AdminLoginBtn = findViewById(R.id.AdminBtn);

        InitLogin();
    }

    public void InitLogin() {
        AdminLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve input values
                String email = editTextLoginEmail.getText().toString();
                String password = editTextLoginPwD.getText().toString();

                // Check if the input matches the predefined admin credentials
                if (email.equals("admin@gmail.com") && password.equals("123456789")) {
                    Intent intent = new Intent(AdminLoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(AdminLoginActivity.this, "Admin Login successful!", Toast.LENGTH_SHORT).show();
                    // Example: startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
                } else {
                    // Invalid credentials, show an error message
                    Toast.makeText(AdminLoginActivity.this, "Invalid Admin Password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
