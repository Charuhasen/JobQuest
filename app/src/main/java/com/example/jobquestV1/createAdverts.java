package com.example.jobquestV1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class createAdverts extends AppCompatActivity {

    private Spinner spinner;
    private Button postad;
    private EditText titlename, indcmname, description, price;

    FirebaseAuth mFirebaseAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_adverts);

        final Vibrator vibe = (Vibrator) createAdverts.this.getSystemService(Context.VIBRATOR_SERVICE);

        getSupportActionBar().setTitle("Create Advert");

        mFirebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        spinner = findViewById(R.id.dropdown);
        postad = findViewById(R.id.btnPost);
        titlename = findViewById(R.id.titleofad);
        indcmname = findViewById(R.id.nameofindcmp);
        description = findViewById(R.id.descriptionofad);
        price = findViewById(R.id.priceofad);
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //Items to be selected from the spinner tool
        List<String> services = new ArrayList<>();
        services.add(0, "Select-Category");
        services.add(1, "Graphic Design");
        services.add(2, "Video & Animation");
        services.add(3, "Digital Marketing");
        services.add(4, "Programming and Tech");

        //Style and Populate the Spinner
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, services);

        //Dropdown layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        userID = mFirebaseAuth.getCurrentUser().getUid();
        DocumentReference db = fstore.collection("users").document(userID);

        postad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference applicant = fstore.collection("users").document(userID);
                applicant.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String isVerified = documentSnapshot.getString("isVerified");

                            if (isVerified.compareTo("0") == 0) {
                                Toast.makeText(createAdverts.this, "Please Verify Mail!", Toast.LENGTH_SHORT).show();
                            } else {
                                //for the dropdown menu
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (parent.getItemAtPosition(position).equals("Select-Category")) {

                                        } else if (parent.getItemAtPosition(position).equals("Graphic Design")) {

                                            Toast.makeText(createAdverts.this, "Selected: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                                        } else if (parent.getItemAtPosition(position).equals("Video & Animation")) {

                                            Toast.makeText(createAdverts.this, "Selected: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                                        } else if (parent.getItemAtPosition(position).equals("Digital Marketing")) {

                                            Toast.makeText(createAdverts.this, "Selected: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                                        } else if (parent.getItemAtPosition(position).equals("Programming and Tech")) {

                                            Toast.makeText(createAdverts.this, "Selected: " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        //Enter code here
                                    }
                                });
                                String title = titlename.getText().toString();
                                String username = indcmname.getText().toString();
                                String desc = description.getText().toString();
                                String cost = price.getText().toString();
                                //Checks if all the field have been filled in
                                if (title.isEmpty()) {
                                    titlename.setError("Please enter Title");
                                    titlename.requestFocus();
                                } else if (username.isEmpty()) {
                                    indcmname.setError("Please enter Name");
                                    indcmname.requestFocus();
                                } else if (desc.isEmpty()) {
                                    description.setError("Please enter Description");
                                    description.requestFocus();
                                } else if (cost.isEmpty()) {
                                    price.setError("Please enter Cost");
                                    price.requestFocus();
                                } else if (spinner.getSelectedItemPosition() == 0) {
                                    Toast.makeText(createAdverts.this, "Please select a category", Toast.LENGTH_SHORT).show();
                                } else {
                                    userID = mFirebaseAuth.getCurrentUser().getUid();
                                    if (spinner.getSelectedItemPosition() == 1) {
                                        Random randNum = new Random();
                                        int randInt = randNum.nextInt();
                                        String rID = userID + Integer.toString(randInt);
                                        //Below Hash Map is for the Individual categories saved and then fed to the Recycler view
                                        DocumentReference service = fstore.collection("GraphicDesign").document(rID);
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("AdID", userID); //this is the ID of the user posting the Ad
                                        user.put("Title", title); //Add value to Title in the DB
                                        user.put("name", username);
                                        user.put("Description", desc);
                                        user.put("Cost", cost);
                                        user.put("randomID", rID); //This is the ID of the ad for tracking purpose
                                        service.set(user);
                                        //Below Hash Map is for the Users posted Ads saved and retrieved in the 'My Adverts; Section of the app
                                        DocumentReference userdb = fstore.collection("users").document(userID).collection("AdsPosted").document(rID);
                                        Map<String, Object> adid = new HashMap<>();
                                        adid.put("Title", title);
                                        adid.put("Description", desc);
                                        adid.put("randomID", rID); //This is the ID of the ad for tracking purpose
                                        adid.put("AdID", userID); //this is the ID of the user posting the Ad
                                        userdb.set(adid);

                                    } else if (spinner.getSelectedItemPosition() == 2) {
                                        Random randNum = new Random();
                                        int randInt = randNum.nextInt();
                                        String rID = userID + randInt;
                                        DocumentReference service = fstore.collection("VideoandAnimation").document(rID);
                                        //Below Hash Map is for the Individual categories saved and then fed to the Recycler view
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("AdID", userID); //this is the ID of the user posting the Ad
                                        user.put("Title", title);
                                        user.put("name", username);
                                        user.put("Description", desc);
                                        user.put("Cost", cost);
                                        user.put("randomID", rID); //This is the ID of the ad for tracking purpose
                                        service.set(user);
                                        //Below Hash Map is for the Users posted Ads saved and retrieved in the 'My Adverts; Section of the app
                                        DocumentReference userdb = fstore.collection("users").document(userID).collection("AdsPosted").document(rID);
                                        Map<String, Object> adid = new HashMap<>();
                                        adid.put("Title", title);
                                        adid.put("Description", desc);
                                        adid.put("randomID", rID); //This is the ID of the ad for tracking purpose
                                        adid.put("AdID", userID); //this is the ID of the user posting the Ad
                                        userdb.set(adid);

                                    } else if (spinner.getSelectedItemPosition() == 3) {
                                        Random randNum = new Random();
                                        int randInt = randNum.nextInt();
                                        String rID = userID + Integer.toString(randInt);
                                        DocumentReference service = fstore.collection("DigitalMarketing").document(rID);
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("AdID", userID); //this is the ID of the user posting the Ad
                                        user.put("Title", title);
                                        user.put("name", username);
                                        user.put("Description", desc);
                                        user.put("Cost", cost);
                                        user.put("randomID", rID); //This is the ID of the ad for tracking purpose
                                        service.set(user);

                                        DocumentReference userdb = fstore.collection("users").document(userID).collection("AdsPosted").document(rID);
                                        Map<String, Object> adid = new HashMap<>();
                                        adid.put("Title", title);
                                        adid.put("Description", desc);
                                        adid.put("randomID", rID); //This is the ID of the ad for tracking purpose
                                        adid.put("AdID", userID); //this is the ID of the user posting the Ad
                                        userdb.set(adid);

                                    } else {
                                        Random randNum = new Random();
                                        int randInt = randNum.nextInt();
                                        String rID = userID + Integer.toString(randInt);
                                        DocumentReference service = fstore.collection("ProgrammingandTech").document(rID);
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("AdID", userID); //this is the ID of the user posting the Ad
                                        user.put("Title", title);
                                        user.put("name", username);
                                        user.put("Description", desc);
                                        user.put("Cost", cost);
                                        user.put("randomID", rID); //This is the ID of the ad for tracking purpose
                                        service.set(user);

                                        DocumentReference userdb = fstore.collection("users").document(userID).collection("AdsPosted").document(rID);
                                        Map<String, Object> adid = new HashMap<>();
                                        adid.put("Title", title);
                                        adid.put("Description", desc);
                                        adid.put("randomID", rID); //This is the ID of the ad for tracking purpose
                                        adid.put("AdID", userID); //this is the ID of the user posting the Ad
                                        userdb.set(adid);

                                    }
                                    vibe.vibrate(80);
                                    Toast.makeText(createAdverts.this, "Succesfuly Posted", Toast.LENGTH_SHORT).show();
                                    //After the advert has been pushed to the database, the fields are then cleared
                                    titlename.setText("");
                                    indcmname.setText("");
                                    description.setText("");
                                    price.setText("");
                                    spinner.setSelection(0);
//                    Intent i = new Intent(createAdverts.this, ProfileFragment.class);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                                }
                            }
                        } else {
                            //Add Code Here
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(createAdverts.this, HomeActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}