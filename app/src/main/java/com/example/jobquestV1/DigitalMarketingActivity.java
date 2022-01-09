package com.example.jobquestV1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class DigitalMarketingActivity extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private RecyclerView mFirestoreList;
    private FirestoreRecyclerAdapter adapter;
    //THREE LINES BELOW
    Button apply;
    String userID;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_marketing);

        getSupportActionBar().setTitle("Digital Marketing");

        fStore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.dmfirestorelist);
        //THESE TWO LINES BELOW
        mFirebaseAuth = FirebaseAuth.getInstance();
        userID = mFirebaseAuth.getCurrentUser().getUid();

        //Query the database for whats going to be fed into the recycler
        Query query = fStore.collection("DigitalMarketing");
        //RecyclerOptions
        FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>()
                .setQuery(query, ProductsModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ProductsModel, DigitalMarketingActivity.ProductsViewHolder>(options) {
            @NonNull
            @Override
            public DigitalMarketingActivity.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);

                //button to pass the data of the user applying for specific ad
                apply = view.findViewById(R.id.btnapply);

                return new DigitalMarketingActivity.ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DigitalMarketingActivity.ProductsViewHolder holder, int position, @NonNull ProductsModel model) {
                //Retrieves the data from using the Model class and set in the appropriate holders
                holder.title.setText(model.getTitle());
                holder.name.setText(model.getName());
                holder.desc.setText(model.getDescription());
                holder.price.setText("GHC " + model.getCost());

                //Get the random ID of the ad created using the getter. Obtains from the ProductsModel.java file
                String randomId = model.getRandomID();
                String userpostedID = model.getAdID();
                //Set the path in the Collection
                DocumentReference db = fStore.collection("users").document(userpostedID).collection("AdsPosted").document(randomId).collection("ApplicantDets").document(userID);
//-----------------------------------------------------------------------------------------------------------------
                //If apply button is pressed, the current user ID is taken and then stored with the appropriate ads
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference applicant = fStore.collection("users").document(userID);
                        applicant.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String applname = documentSnapshot.getString("fName");
                                    String appllsname = documentSnapshot.getString("lName");
                                    String applnumber = documentSnapshot.getString("phNumber");
                                    String applemail = documentSnapshot.getString("email");
                                    String isVerified = documentSnapshot.getString("isVerified");

                                    if (isVerified.compareTo("0") == 0){
                                        Toast.makeText(DigitalMarketingActivity.this, "Please Verify Mail!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Map<String, Object> addApplicant = new HashMap<>();
                                        //add the UserId of the current Instance(Person applying) to the array list(Applicant Id) of the Ad
                                        addApplicant.put("fName", applname);
                                        addApplicant.put("lName", appllsname);
                                        addApplicant.put("phNumber", applnumber);
                                        addApplicant.put("email", applemail);
                                        addApplicant.put("UserID", userID);
                                        db.set(addApplicant);
                                        Toast.makeText(DigitalMarketingActivity.this, "Successful Apply!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    //Add Code Here
                                }
                            }
                        });
                    }
                });
            }
        };

        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(this));
        mFirestoreList.setAdapter(adapter);

    }

    //Model class below
    private class ProductsViewHolder extends RecyclerView.ViewHolder {

        private TextView title, name, desc, price;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.gdtitle);
            name = itemView.findViewById(R.id.gdname);
            desc = itemView.findViewById(R.id.gddesc);
            price = itemView.findViewById(R.id.gdprice);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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