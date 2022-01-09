package com.example.jobquestV1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class VideoanimActivity extends AppCompatActivity {
    //See DigitalMarketing Activity for comments
    private FirebaseFirestore fStore;
    private RecyclerView mFirestoreList;
    private FirestoreRecyclerAdapter adapter;
    //THREE LINES BELOW
    Button apply;
    String userID;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoanim);

        getSupportActionBar().setTitle("Video and Animation");

        fStore = FirebaseFirestore.getInstance();
        mFirestoreList = findViewById(R.id.vafirestorelist);
        //THESE TWO LINES BELOW
        mFirebaseAuth = FirebaseAuth.getInstance();
        userID = mFirebaseAuth.getCurrentUser().getUid();

        //Query
        Query query = fStore.collection("VideoandAnimation");
        //RecyclerOptions
        FirestoreRecyclerOptions<ProductsModel> options = new FirestoreRecyclerOptions.Builder<ProductsModel>()
                .setQuery(query, ProductsModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ProductsModel, VideoanimActivity.ProductsViewHolder>(options) {
            @NonNull
            @Override
            public VideoanimActivity.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);

                //line below
                apply = view.findViewById(R.id.btnapply);

                return new VideoanimActivity.ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VideoanimActivity.ProductsViewHolder holder, int position, @NonNull ProductsModel model) {

                holder.title.setText(model.getTitle());
                holder.name.setText(model.getName());
                holder.desc.setText(model.getDescription());
                holder.price.setText("GHC " + model.getCost());
                //LINE 74 - 89
                //Get the random ID of the ad created using the getter. Obtains from the ProductsModel.java file
                String randomId = model.getRandomID();
                String userpostedID = model.getAdID();
                //Set the path in the Collection

                //CollectionReference db = fStore.collection("users").document(userpostedID).collection("AdsPosted").document(randomId).collection(userID);
                DocumentReference db = fStore.collection("users").document(userpostedID).collection("AdsPosted").document(randomId).collection("ApplicantDets").document(userID);
//-----------------------------------------------------------------------------------------------------------------
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
                                        Toast.makeText(VideoanimActivity.this, "Please Verify Mail!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Map<String, Object> addApplicant = new HashMap<>();
                                        //add the UserId of the current Instance(Person applying) to the array list(Applicant Id) of the Ad
                                        addApplicant.put("fName", applname);
                                        addApplicant.put("lName", appllsname);
                                        addApplicant.put("phNumber", applnumber);
                                        addApplicant.put("email", applemail);
                                        addApplicant.put("UserID", userID);
                                        db.set(addApplicant);
                                        Toast.makeText(VideoanimActivity.this, "Successful Apply!", Toast.LENGTH_SHORT).show();
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
        //View Holder Class
    }

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