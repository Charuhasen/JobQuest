package com.example.jobquestV1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class Preference extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        //website
        final android.preference.Preference website = findPreference("website");
        website.setOnPreferenceClickListener(preference ->  {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.google.com/"));
            startActivity(i);
            return false;
        });
        //about-us
        final android.preference.Preference aboutus = findPreference("about-us");
        aboutus.setOnPreferenceClickListener(preference ->  {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.google.com/"));
            startActivity(i);
            return false;
        });
        //how to use JobQuest
        final android.preference.Preference howto = findPreference("how_to");
        howto.setOnPreferenceClickListener(preference ->  {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.youtube.com/"));
            startActivity(i);
            return false;
        });
        //like the app
        final android.preference.Preference likeapp = findPreference("Rate");
        likeapp.setOnPreferenceClickListener(preference ->  {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.google.com/"));
            startActivity(i);
            return false;
        });
        //Share App
        findPreference("Share").setOnPreferenceClickListener(preference -> {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "JOB QUEST");
                intent.putExtra(Intent.EXTRA_TEXT, "JobQuest.com");
                startActivity(Intent.createChooser(intent,"ShareApp"));
            }
            catch (Exception e )
            {
                e.printStackTrace();
            }
            return false;
        });
        //report a bug
        final android.preference.Preference report = findPreference("Report");
        report.setOnPreferenceClickListener(preference ->  {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.google.com/"));
            startActivity(i);
            return false;
        });
        //Privacy Policy
        final android.preference.Preference privacyPolicy = findPreference("privacy");
        privacyPolicy.setOnPreferenceClickListener(preference ->  {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.google.com/"));
            startActivity(i);
            return false;
        });
        //FAQ
        final android.preference.Preference faq = findPreference("faq");
        faq.setOnPreferenceClickListener(preference -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.google.com/"));
            startActivity(i);
            return false;
        });
        //Contact Me
        final android.preference.Preference contactMe = findPreference("contact");
        contactMe.setOnPreferenceClickListener(preference ->  {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.google.com/"));
            startActivity(i);
            return false;
        });
        //App Version
        final android.preference.Preference appVersionPreference = findPreference("app_version");
        appVersionPreference.setTitle("Copyright 2021");
        appVersionPreference.setOnPreferenceClickListener(preference -> {
            Toast.makeText(this, "V 0.994", Toast.LENGTH_SHORT).show();
            return false;
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
