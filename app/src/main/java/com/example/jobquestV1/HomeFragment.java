package com.example.jobquestV1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    ImageButton grphdsgn, vdeoanim, dgtmrk, prgmntech;
    CardView graphicDesign, videoAnimation, digitalMarketing, programmingTech;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View x = inflater.inflate(R.layout.fragment_home, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");
        //Assign variables to all XML ID's
        grphdsgn = x.findViewById(R.id.Btngraphicdesign);
        vdeoanim = x.findViewById(R.id.BtnVideoandanimation);
        dgtmrk = x.findViewById(R.id.Btndigitalmarketing);
        prgmntech = x.findViewById(R.id.Btnprogrammingandtech);
        graphicDesign = x.findViewById(R.id.GraphicDesignCardView);
        videoAnimation = x.findViewById(R.id.VideoandAnimationCardView);
        digitalMarketing = x.findViewById(R.id.DigitalMarketingCardView);
        programmingTech = x.findViewById(R.id.ProgrammingandTechCardView);


        //If user click on the card then Intent
        graphicDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GraphicDesignActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        //If user click on the card then Intent
        videoAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VideoanimActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        //If user click on the card then Intent
        digitalMarketing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DigitalMarketingActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        //If user click on the card then Intent
        programmingTech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProgrammingAndTechActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        //If graphic design is pressed it, intents to the Graphic Design Activity
        grphdsgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GraphicDesignActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //Same as above
        vdeoanim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VideoanimActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //Same as above
        dgtmrk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DigitalMarketingActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        //Same as above
        prgmntech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProgrammingAndTechActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        return x;
    }
}
