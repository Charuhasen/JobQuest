package com.example.jobquestV1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        mFirebaseAuth = FirebaseAuth.getInstance();
        // userID = mFirebaseAuth.getCurrentUser().getUid();
        FirebaseUser fuser = mFirebaseAuth.getCurrentUser();
        fstore = FirebaseFirestore.getInstance();

        if(fuser.isEmailVerified()){
            userID = mFirebaseAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fstore.collection("users").document(userID);
            Map<String, Object> user = new HashMap<>();
            user.put("isVerified", "1");
            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Log.d(TAG,"onSuccess: User profile is created for " + userID);
                }
            });
        }



        //When the app laaunches the first fragment to load is the HomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            //Switch case to chose between the selected fragments
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_messages:
                    selectedFragment = new MessagesFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }



    };

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //create a dialog to ask yes no question whether or not the user wants to exit
    }
}