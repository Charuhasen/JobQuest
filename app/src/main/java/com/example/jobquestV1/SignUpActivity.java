package com.example.jobquestV1;
//PNG IMAGES TAKEN FROM https://www.flaticon.com/authors/pixel-perfect

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText emailId, password, fname, lname, pnumber;
    TextView btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().setTitle("Sign-Up");

        LoadingDialog loadingDialog = new LoadingDialog(SignUpActivity.this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.SignUpEmail);
        password = findViewById(R.id.SignUpPassword);
        btnSignUp = findViewById(R.id.SignUpbutton);
        tvSignIn = findViewById(R.id.AlreadyHaveAccTextView);
        fname = findViewById(R.id.firstname);
        lname = findViewById(R.id.lastname);
        pnumber = findViewById(R.id.phonenumber);
        fstore = FirebaseFirestore.getInstance();


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Text from the EditText are retrieved and stored in the variables
                String email = emailId.getText().toString();
                String pass = password.getText().toString();
                String firstName = fname.getText().toString();
                String lastName = lname.getText().toString();
                String phoneNumber = pnumber.getText().toString();
                ArrayList applicant = new ArrayList<>();

                loadingDialog.startLoadingDialogue();

                //Form validation occurs below
                if (email.isEmpty()) {
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                } else if (pass.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                } else if (email.isEmpty() && pass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please enter Email & Password!", Toast.LENGTH_SHORT).show();
                } else if (firstName.isEmpty()) {
                    fname.setError("Please Enter your First Name!");
                    fname.requestFocus();
                } else if (lastName.isEmpty()) {
                    lname.setError("Please enter your Last Name!");
                    lname.requestFocus();
                } else if (phoneNumber.isEmpty()) {
                    pnumber.setError("Please enter a phone number!");
                    pnumber.requestFocus();
                } else if (!(email.isEmpty() && pass.isEmpty() && firstName.isEmpty() && lastName.isEmpty() && phoneNumber.isEmpty())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                loadingDialog.dismissDialog();
                                Toast.makeText(SignUpActivity.this, "Sign Up Unsuccessful!, Please try again.", Toast.LENGTH_SHORT).show();
                            } else {
                                //Send verification link
                                FirebaseUser fuser = mFirebaseAuth.getCurrentUser();
                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignUpActivity.this, "Verification Mail Sent!", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Log.d(TAG, "onFailure: Verification Mail not sent!" + e.getMessage());
                                    }
                                });

                                userID = mFirebaseAuth.getCurrentUser().getUid();
                                //Location for where the data is stores is created below
                                DocumentReference documentReference = fstore.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("fName", firstName);
                                user.put("lName", lastName);
                                user.put("email", email);
                                user.put("phNumber", phoneNumber);
                                user.put("aboutme", null);
                                user.put("facebook", null);
                                user.put("instagram", null);
                                user.put("linkedin", null);
                                user.put("isVerified", "0");
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Log.d(TAG,"onSuccess: User profile is created for " + userID);
                                    }
                                });
                                startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Text to be clicked if user already has an account with the app
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}