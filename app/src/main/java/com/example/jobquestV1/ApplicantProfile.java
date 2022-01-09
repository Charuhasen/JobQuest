package com.example.jobquestV1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ApplicantProfile extends AppCompatActivity {

    private FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    //    FirebaseAuth mFirebaseAuth;
    ImageButton facebook, instagram, linkedin;
    ImageView applicantBanner, applicantProfile;
    TextView applicantname, applicantPhnumber, applicantAbout, applicantmail;
    StorageReference storageReference;
    Button rejapplicant, messageApplicant;
    EditText messageToUser;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_profile);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        getSupportActionBar().setTitle("Applicant Info");
        //Get the information from the previous intent
        Intent intent = getIntent();
        //Extract the data…
        String stuff = intent.getStringExtra(ApplicantsActivity.APPLICANTID);
        String randID = intent.getStringExtra(ApplicantsActivity.RANDOMID);
        //assign variables to the views in the .XML file
        rejapplicant = findViewById(R.id.btnapplicantreject);
        applicantProfile = findViewById(R.id.applicantprofileimg);
        applicantname = findViewById(R.id.applicantFirstname);
        applicantPhnumber = findViewById(R.id.applicantPhonenumber);
        applicantAbout = findViewById(R.id.ApplicantAboutme);
        applicantmail = findViewById(R.id.applicantemail);
        facebook = findViewById(R.id.BtnImgFacebook);
        instagram = findViewById(R.id.BtnImgInstagram);
        linkedin = findViewById(R.id.BtnImglinkedin);
        messageApplicant = findViewById(R.id.msgApplicant);
        messageToUser = findViewById(R.id.msgField);
        //Get instance of storage from Firebase
        storageReference = FirebaseStorage.getInstance().getReference();
        //Get path in the firebase storage
        StorageReference profileRef = storageReference.child("users/" + stuff + "profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            //Load the image from storage into the Image holder in XML file
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(applicantProfile);
            }
        });
        //Get path in the firebase firestore
        DocumentReference documentReference = fStore.collection("users").document(stuff);
        //Get the document within that location
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String fullname = documentSnapshot.getString("fName") + documentSnapshot.getString("lName");
                    fullname = fullname.toUpperCase();
                    //Get the data within the doc and set it to the fields in the XML
                    applicantname.setText(fullname);
                    applicantPhnumber.setText(documentSnapshot.getString("phNumber"));
                    applicantAbout.setText(documentSnapshot.getString("aboutme"));
                    applicantmail.setText(documentSnapshot.getString("email"));

                    getSupportActionBar().setSubtitle(documentSnapshot.getString("fName") + documentSnapshot.getString("lName"));
                } else {
                    //Toast.makeText(ApplicantProfile.this, "Document does not exist", Toast.LENGTH_SHORT).show;
                }
            }
        });
        //For when the user clicks on the facebook icon
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            //SVG IMAGE TAKEN FROM "https://www.flaticon.com/
                            try {
                                //get the link stored in the database
                                String fblink = documentSnapshot.getString("facebook");
                                //open link in new intent
                                Intent myLink = new Intent(Intent.ACTION_VIEW);
                                myLink.setData(Uri.parse(fblink));
                                startActivity(myLink);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(ApplicantProfile.this, "Provided Link Error", Toast.LENGTH_SHORT).show();
                            } catch (NullPointerException e) {
                                Toast.makeText(ApplicantProfile.this, "Provided Link is empty!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //Toast.makeT   ext(ApplicantProfile.this, "Document does not exist", Toast.LENGTH_SHORT).show;
                        }
                    }
                });
            }
        });
        //same thing as above
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            //SVG IMAGE TAKEN FROM "https://www.flaticon.com/
                            try {
                                String fblink = documentSnapshot.getString("instagram");
                                Intent myLink = new Intent(Intent.ACTION_VIEW);
                                myLink.setData(Uri.parse(fblink));
                                startActivity(myLink);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(ApplicantProfile.this, "Provided Link Error", Toast.LENGTH_SHORT).show();
                            } catch (NullPointerException e) {
                                Toast.makeText(ApplicantProfile.this, "Provided Link is empty!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //Toast.makeT   ext(ApplicantProfile.this, "Document does not exist", Toast.LENGTH_SHORT).show;
                        }
                    }
                });
            }
        });
        //same thing as above
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            //SVG IMAGE TAKEN FROM "https://www.flaticon.com/
                            try {
                                String fblink = documentSnapshot.getString("linkedin");
                                Intent myLink = new Intent(Intent.ACTION_VIEW);
                                myLink.setData(Uri.parse(fblink));
                                startActivity(myLink);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(ApplicantProfile.this, "Provided Link Error", Toast.LENGTH_SHORT).show();
                            } catch (NullPointerException e) {
                                Toast.makeText(ApplicantProfile.this, "Provided Link is empty!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //Toast.makeT   ext(ApplicantProfile.this, "Document does not exist", Toast.LENGTH_SHORT).show;
                        }
                    }
                });
            }
        });

        rejapplicant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = fAuth.getCurrentUser().getUid();

                try {
                    //Deletes the applicants records from the databse
                    DocumentReference documentReference = fStore.collection("users").document(userId)
                            .collection("AdsPosted").document(randID).collection("ApplicantDets").document(stuff);

                    documentReference.delete();
                } catch (RuntimeException e) {

                }
                startActivity(new Intent(ApplicantProfile.this, MyAdvertsPosted.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        messageApplicant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference db = fStore.collection("users").document(userID).collection("Chats").document(userID + stuff);
                DocumentReference db2 = fStore.collection("users").document(stuff).collection("Chats").document(userID + stuff);

                String message = messageToUser.getText().toString();
                if (message.isEmpty()) {
                    messageToUser.setError("Please enter a message.");
                    messageToUser.requestFocus();
                } else {
                    Map<String, Object> sendmessage = new HashMap<>();
                    sendmessage.put("IDONE", userID);
                    sendmessage.put("IDTWO", stuff);
//                    sendmessage.put("msg", message);
                    db.set(sendmessage);
                    db2.set(sendmessage);
                    Toast.makeText(ApplicantProfile.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //For when the back button is pressed on the mobile phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}