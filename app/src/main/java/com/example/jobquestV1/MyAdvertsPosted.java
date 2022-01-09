package com.example.jobquestV1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class MyAdvertsPosted extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private RecyclerView mFirestoreList;
    private FirestoreRecyclerAdapter adapter;

    public static final String RANDOMID = "com.example.jobquestV1RANDOMID";

    Button viewapplicants;
    String userID;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adverts_posted);

        getSupportActionBar().setTitle("My Adverts");

        fStore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.mapfirestorelist);

        mFirebaseAuth = FirebaseAuth.getInstance();
        userID = mFirebaseAuth.getCurrentUser().getUid();

        //Query the specific DB location to check for User posted ads
        Query query = fStore.collection("users").document(userID).collection("AdsPosted");
        //RecyclerOptions
        FirestoreRecyclerOptions<MyAdvertsModel> options = new FirestoreRecyclerOptions.Builder<MyAdvertsModel>()
                .setQuery(query, MyAdvertsModel.class).build();

        adapter = new FirestoreRecyclerAdapter<MyAdvertsModel, MyAdvertsPosted.ProductsViewHolder>(options) {
            @NonNull
            @Override
            public MyAdvertsPosted.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myadvertsposted, parent, false);

                //assign XML ID to variable for button
                viewapplicants = view.findViewById(R.id.btnviewapplicants);

                return new MyAdvertsPosted.ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyAdvertsPosted.ProductsViewHolder holder, int position, @NonNull MyAdvertsModel model) {
                //Get the Title and the Description from specified location in DB to display in the Recycler view
                holder.title.setText(model.getTitle());
                holder.desc.setText(model.getDescription());
                //If the view applicant button is pressed, the ID of the AD is passed to the next intent to retrieve the details of the Applicants who applied
                viewapplicants.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String randomadID = model.getRandomID();
                        Intent i = new Intent(MyAdvertsPosted.this, ApplicantsActivity.class);
                        //Add the bundle to the intent
                        i.putExtra(RANDOMID, randomadID);
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

        private TextView title, desc;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.gatitle);
            desc = itemView.findViewById(R.id.gadesc);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(MyAdvertsPosted.this, HomeActivity.class));
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