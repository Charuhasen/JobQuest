package com.example.jobquestV1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    //BELOW LINE
    CardView logoutBtn, profileBtn, btnadv,btnsettings, myAdv, mySocialLinks;

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View x = inflater.inflate(R.layout.fragment_profile, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");
        // STARTS FROM HERE
        logoutBtn = x.findViewById(R.id.LogoutCardView);
        profileBtn = x.findViewById(R.id.MyProfileCardView);
        btnadv = x.findViewById(R.id.CreateAdvertCardView);
        btnsettings = x.findViewById(R.id.SettingsCardView);
        myAdv = x.findViewById(R.id.MyAdvertCardView);
        mySocialLinks = x.findViewById(R.id.LinkAccountsCardView);
        //If logout button is pressed, the current user instance is signed out
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), SignUpActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        //If pressed, an intent occurs with animation
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), myprofileActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //If pressed, an intent occurs with animation
        btnadv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), createAdverts.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                //Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_SHORT).show();
            }
        });
        //If pressed, an intent occurs with animation
        myAdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyAdvertsPosted.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //If pressed, an intent occurs with animation
        mySocialLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MySocialLinks.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //If pressed, an intent occurs with animation
        btnsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Preference.class));
                //Toast.makeText(getActivity(), "Coming Soon!", Toast.LENGTH_SHORT).show();
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        return x;
    }
}
