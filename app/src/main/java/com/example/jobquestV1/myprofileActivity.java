package com.example.jobquestV1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class myprofileActivity extends AppCompatActivity {

    TextView fname, lname, email, phonenumber, verEmail;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    //Button btnprofpicchng;
    ImageButton profilepicture;
    //ImageView profimg;
    StorageReference storageReference;
    Button abtme, verifyBtn;
    String userID;
    FirebaseFirestore fstore;
    EditText description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        getSupportActionBar().setTitle("My Profile");


        VerificationDialog VerificationDialog = new VerificationDialog(myprofileActivity.this);

        phonenumber = findViewById(R.id.myprofilePhonenumber);
        fname = findViewById(R.id.myprofileFirstname);
        profilepicture = findViewById(R.id.userprofileimg);

        abtme = findViewById(R.id.saveabtme);
        description = findViewById(R.id.myprofileAboutme);

        verEmail = findViewById(R.id.verifyMessage);
        verifyBtn = findViewById(R.id.VerificationButton);

        fAuth = FirebaseAuth.getInstance();
        FirebaseUser fuser = fAuth.getCurrentUser();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userId = fAuth.getCurrentUser().getUid();

        if (!(fuser.isEmailVerified())) {
            verifyBtn.setVisibility(View.VISIBLE);

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VerificationDialog.startLoadingDialogue();
                    //Send verification link
                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(myprofileActivity.this, "Verification Mail Sent!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.d(TAG, "onFailure: Verification Mail not sent!" + e.getMessage());
                        }
                    });
                }
            });
        } else {
            verEmail.setVisibility(View.GONE);
        }
        //Downloads the Image of the user that he/she uploaded to the storage
        StorageReference profileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "profile.jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilepicture);
            }
        });

        //Set location to retrieve details
        DocumentReference documentReference = fStore.collection("users").document(userId);
        //Create a docsnapshot to retrieve the specified details
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    String fullname = documentSnapshot.getString("fName") + documentSnapshot.getString("lName");

                    fullname = fullname.toUpperCase();

                    fname.setText(fullname);
                    //lname.setText(documentSnapshot.getString("lName"));
                    phonenumber.setText(documentSnapshot.getString("phNumber"));
                    //email.setText(documentSnapshot.getString("email"));
                    description.setText(documentSnapshot.getString("aboutme"));

                    getSupportActionBar().setSubtitle(documentSnapshot.getString("fName") + documentSnapshot.getString("lName"));
                } else {
                    Toast.makeText(myprofileActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //If user clicks on their profile picture, it intents to the users gallery to allow them to upload an image
        profilepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open specific type of intent('ACTION_PICK'). Pick the data from media storage and get the URI of the image selected
                //After, start the activity and provide a req code
                Intent openGalaryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalaryIntent, 1000);
            }
        });
        //If user presses this, the information entered in the about me section is uploaded to the DB
        abtme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aboutme = description.getText().toString();
                userID = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fstore.collection("users").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("aboutme", aboutme);
                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG,"onSuccess: User profile is created for " + userID);
                    }
                });
            }
        });
    }

    //To identify which intent is invoking the activity result we use the specified req code
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                //profimg.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //uploads image to Firebase Storage
        StorageReference fileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilepicture);
                    }
                });
                Toast.makeText(myprofileActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(myprofileActivity.this, "Image Upload Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(myprofileActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}