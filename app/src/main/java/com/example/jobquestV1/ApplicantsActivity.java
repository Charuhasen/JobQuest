package com.example.jobquestV1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class ApplicantsActivity extends AppCompatActivity {

    ImageButton viewapplicantinfo;

    private FirebaseFirestore fStore;
    private RecyclerView mFirestoreList;
    private FirestoreRecyclerAdapter adapter;
    StorageReference storageReference;

    public static final String APPLICANTID = "com.example.jobquestV1APPLICANTID";
    public static final String RANDOMID = "com.example.jobquestV1RANDOMID";

    private StorageReference mStorageReference;

    String userID;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicants);

        getSupportActionBar().setTitle("Applicants");

        fStore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.applicantviewfirestorelist);

        mFirebaseAuth = FirebaseAuth.getInstance();
        userID = mFirebaseAuth.getCurrentUser().getUid();
        //Get the data from previous intent
        Intent intent = getIntent();
        //Extract the data…
        String stuff = intent.getStringExtra(MyAdvertsPosted.RANDOMID);
        //Query the firebase firestore at this location
        Query query = fStore.collection("users").document(userID).collection("AdsPosted").document(stuff).collection("ApplicantDets");
        //RecyclerOptions
        FirestoreRecyclerOptions<MyApplicantsModel> options = new FirestoreRecyclerOptions.Builder<MyApplicantsModel>()
                .setQuery(query, MyApplicantsModel.class).build();

        adapter = new FirestoreRecyclerAdapter<MyApplicantsModel, ApplicantsActivity.ProductsViewHolder>(options) {
            @NonNull
            @Override
            public ApplicantsActivity.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applicantsview, parent, false);

                viewapplicantinfo = view.findViewById(R.id.userprofileimg);

                return new ApplicantsActivity.ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ApplicantsActivity.ProductsViewHolder holder, int position, @NonNull MyApplicantsModel model) {
                holder.fName.setText(model.getfName());
                holder.lName.setText(model.getlName());
                holder.email.setText(model.getEmail());
                holder.phNumber.setText(model.getPhNumber());

                mStorageReference = FirebaseStorage.getInstance().getReference().child("users/" + model.getUserID() + "profile.jpg");
                try {
                    final File localFile = File.createTempFile("profile", "jpg");
                    mStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            holder.prfimg.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

                viewapplicantinfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ApplicantID = model.getUserID();
                        Intent i = new Intent(ApplicantsActivity.this, ApplicantProfile.class);
                        //Add the bundle to the intent
                        i.putExtra(APPLICANTID, ApplicantID);
                        i.putExtra(RANDOMID, stuff);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });
            }
        };
        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);
        //View Holder Class
    }

    private class ProductsViewHolder extends RecyclerView.ViewHolder {
        private ImageButton prfimg;
        private TextView fName, lName, email, phNumber;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            fName = itemView.findViewById(R.id.appfname);
            lName = itemView.findViewById(R.id.applname);
            email = itemView.findViewById(R.id.appemail);
            phNumber = itemView.findViewById(R.id.appphnumber);
            prfimg = itemView.findViewById(R.id.userprofileimg);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(ApplicantsActivity.this, MyAdvertsPosted.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}