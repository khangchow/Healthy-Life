package com.myapplication.healthylife.fragments.homefragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.myapplication.healthylife.R;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUs extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about_us, container, false);
        Element adsElement= new Element();
        View aboutPage = new AboutPage(getActivity())
                .isRTL(false)
                .setDescription("Healthy Life - Stay strong and fit!")
                .addItem(new Element().setTitle("Version 2.0"))
                .addGroup("CONNECT WITH US!")
                .addEmail("khangchow@gmail.com")
                .addWebsite("https://www.facebook.com/profile.php?id=100032225955405")
                .addYoutube("")
                .addItem(createCopyright())
                .create();
        v=aboutPage;
        return v;
    }
    private Element createCopyright()
    {
        Element copyright = new Element();
        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Copyright %d by Chow Minh Khang", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        // copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),copyrightString,Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }
}