package com.example.jobquestV1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.perfmark.Link;

public class MySocialLinks extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    Button addSocialMedia;
    EditText facebookLink, instagramLink, linkedinLink;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_social_links);

        fAuth = FirebaseAuth.getInstance();

        addSocialMedia = findViewById(R.id.btnAddSocialMedia);
        facebookLink = findViewById(R.id.linkfacebook);
        instagramLink = findViewById(R.id.linkinstagram);
        linkedinLink = findViewById(R.id.linklinkedin);

        userID = fAuth.getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();
        //Set location that I want to query in DB
        DocumentReference documentReference = fStore.collection("users").document(userID);
        //Use doc snapshot to retrieve the data and place in the holders
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    facebookLink.setText(documentSnapshot.getString("facebook"));
                    instagramLink.setText(documentSnapshot.getString("instagram"));
                    linkedinLink.setText(documentSnapshot.getString("linkedin"));
                    getSupportActionBar().setSubtitle(documentSnapshot.getString("fName") + documentSnapshot.getString("lName"));
                } else {
                    Toast.makeText(MySocialLinks.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Data is retrieved from the EditText views and then pushed to the database
        addSocialMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] Links = new String[3];
                Links[0] = facebookLink.getText().toString();
                Links[1] = instagramLink.getText().toString();
                Links[2] = linkedinLink.getText().toString();

                fStore = FirebaseFirestore.getInstance();

                userID = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("users").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("facebook", Links[0]);
                user.put("instagram", Links[1]);
                user.put("linkedin", Links[2]);
                documentReference.update(user);
                Toast.makeText(MySocialLinks.this, "Successfully Added Links", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed () {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}




