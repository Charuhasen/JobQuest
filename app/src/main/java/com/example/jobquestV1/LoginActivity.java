package com.example.jobquestV1;
//PNG IMAGES TAKEN FROM https://www.flaticon.com/authors/pixel-perfect https://www.flaticon.com/authors/icongeek26 https://www.freepik.com/
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText emailId, password;
    TextView btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    String userID;
    FirebaseFirestore fstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Log-In");

        LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
        mFirebaseAuth = FirebaseAuth.getInstance();
       // userID = mFirebaseAuth.getCurrentUser().getUid();
        FirebaseUser fuser = mFirebaseAuth.getCurrentUser();
        fstore = FirebaseFirestore.getInstance();

        //Create variables for the XML ID's
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.SignInEmail);
        password = findViewById(R.id.SignInPassword);
        btnSignIn = findViewById(R.id.SignInbutton);
        tvSignUp = findViewById(R.id.DontHaveAccTextView);
        //Checks to see if there is already an existing account in place else asks to login
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    Toast.makeText(LoginActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };
        //If login is pressed, there is some validations that are in place before User is allowed to Log-In
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialogue();
                String email = emailId.getText().toString();
                String pass = password.getText().toString();
                if (email.isEmpty()) {
                    emailId.setError("Please enter Email Id");
                    emailId.requestFocus();
                } else if (pass.isEmpty()) {
                    password.setError("Please enter your Password");
                    password.requestFocus();
                } else if (email.isEmpty() && pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter Email & Password!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pass.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                loadingDialog.dismissDialog();
                                Toast.makeText(LoginActivity.this, "Login error! Please try again.", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intToHome = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //If user presses the dont have account text, it intents to the sign up page
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intSignUp);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}