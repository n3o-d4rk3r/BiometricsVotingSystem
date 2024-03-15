package edu.sub.subvotingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFullName, editTextRegisterEmail, editTextRegisterDoB, editTextRegisterMobile, editTextRegisterPwD, editTextRegisterConfirmPwD;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;
    private DatePickerDialog picker;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar = findViewById(R.id.progressBar);
        editTextRegisterFullName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDoB = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPwD = findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPwD = findViewById(R.id.editText_register_conf_password);

        //RadioButton for Gender
        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();

        //Setting up DatePicker on EditText
        editTextRegisterDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date Picker Dialog
                picker = new DatePickerDialog(RegisterActivity.this, (view, year1, month1, dayOfMonth) -> editTextRegisterDoB.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1), year, month, day);
                picker.show();
            }
        });

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected = findViewById(selectedGenderId);

                // Obtain the entered data
                String textFullName = editTextRegisterFullName.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textDoB = editTextRegisterDoB.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textPwD = editTextRegisterPwD.getText().toString();
                String textConfirmPwD = editTextRegisterConfirmPwD.getText().toString();
                String textGender;

                if (TextUtils.isEmpty(textFullName)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    editTextRegisterFullName.setError("Full Name is required");
                    editTextRegisterFullName.requestFocus();
                } else if (!textFullName.matches("[a-zA-Z ]+")) {
                    Toast.makeText(RegisterActivity.this, "Special characters are not allowed in full name", Toast.LENGTH_LONG).show();
                    editTextRegisterFullName.setError("Only text characters are allowed");
                    editTextRegisterFullName.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();
                } else if (!textEmail.endsWith("@gmail.com") &&
                        !textEmail.endsWith("@hotmail.com") &&
                        !textEmail.endsWith("@cloud.com") &&
                        !textEmail.endsWith("@yahoo.com")) {
                    Toast.makeText(RegisterActivity.this, "Only specific email domains are allowed", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Invalid email format");
                    editTextRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(textDoB)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your date of birth", Toast.LENGTH_LONG).show();
                    editTextRegisterDoB.setError("Date of Birth is required");
                    editTextRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your mobile no.", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile No. is required");
                    editTextRegisterMobile.requestFocus();
                } else if (textMobile.length() != 11) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your mobile no.", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile No. should be 10 digits");
                    editTextRegisterMobile.requestFocus();
                } else if (textMobile.matches("(\\d)\\1{2,}")) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter a valid mobile no.", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Repeating numbers like 000, 111, etc. are not allowed");
                    editTextRegisterMobile.requestFocus();
                } else if (TextUtils.isEmpty(textPwD)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    editTextRegisterPwD.setError("Password is required");
                    editTextRegisterPwD.requestFocus();
                } else if (textPwD.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 digits", Toast.LENGTH_LONG).show();
                    editTextRegisterPwD.setError("Password too weak");
                    editTextRegisterPwD.requestFocus();
                } else if (TextUtils.isEmpty(textConfirmPwD)) {
                    Toast.makeText(RegisterActivity.this, "Please confirm your password", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwD.setError("Password Confirmation is required");
                    editTextRegisterConfirmPwD.requestFocus();
                } else if (!textPwD.equals(textConfirmPwD)) {
                    Toast.makeText(RegisterActivity.this, "Please same password", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwD.setError("Password Confirmation is required");
                    editTextRegisterConfirmPwD.requestFocus();
                    //Clear the entered passwords
                    editTextRegisterPwD.clearComposingText();
                    editTextRegisterConfirmPwD.clearComposingText();
                } else {
                    textGender = radioButtonRegisterGenderSelected.getText().toString();

                    // Adding the age validation here
                    Calendar cal = Calendar.getInstance();
                    int currentYear = cal.get(Calendar.YEAR);
                    int currentMonth = cal.get(Calendar.MONTH) + 1; // Months are 0-based
                    int currentDay = cal.get(Calendar.DATE);

                    String[] dobParts = textDoB.split("/"); // Assuming date of birth is in "dd/mm/yyyy" format

                    int inputDay = Integer.parseInt(dobParts[0]);
                    int inputMonth = Integer.parseInt(dobParts[1]);
                    int inputYear = Integer.parseInt(dobParts[2]);

                    cal.set(inputYear, inputMonth - 1, inputDay);


                    int age = currentYear - inputYear;
                    if (inputMonth > currentMonth || (inputMonth == currentMonth && inputDay > currentDay)) {
                        age--;
                    }

                    if (age < 18) {
                        Toast.makeText(RegisterActivity.this, "You must be at least 18 years old to register", Toast.LENGTH_LONG).show();
                        editTextRegisterDoB.setError("Invalid date of birth");
                        editTextRegisterDoB.requestFocus();
                        return; // Exit the click event handler
                    }

                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName, textEmail, textDoB, textGender, textMobile, textPwD);
                }
            }
        });
    }

    //Register User using the credentials given
    private void registerUser(String textFullName, String textEmail, String textDoB, String textGender, String textMobile, String textPwD) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Create User Profile
        auth.createUserWithEmailAndPassword(textEmail, textPwD).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser firebaseuser = auth.getCurrentUser();

                    //Update Display Name of User
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                    assert firebaseuser != null;
                    firebaseuser.updateProfile(profileChangeRequest);

                    //Enter User Data into the Firebase Realtime Database.
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textDoB, textGender, textMobile);

                    //Extracting User reference from Database for "Registered Users"
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");

                    referenceProfile.child(firebaseuser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Send Verification Email
                                firebaseuser.sendEmailVerification();

                                Toast.makeText(RegisterActivity.this, "User registered successfully. Please verify your email", Toast.LENGTH_LONG).show();

                                //Open User Profile after successful registeration
                                Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
                                //To prevent user from returning back to Register Activity on pressing back button after registeration
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();   //To close Register Activity
                            } else {
                                Toast.makeText(RegisterActivity.this, "User registered failed. Please try again", Toast.LENGTH_LONG).show();
                            }
                            //Hide ProgressBar whether User creation is successful or failed
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                } else {
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (FirebaseAuthWeakPasswordException e) {
                        editTextRegisterPwD.setError("Your password is weak. Kindly use a mix of alphabets, numbers and special characters");
                        editTextRegisterPwD.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        editTextRegisterPwD.setError("Your email is invalid or already in use. Kindly re-enter");
                        editTextRegisterPwD.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        editTextRegisterPwD.setError("User is already registered with this email. Kindly use other email");
                        editTextRegisterPwD.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void isValidEmal() {
        String ptrn = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    }
}