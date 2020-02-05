package com.gmail.pavkascool.hometask3.weather;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Button;

import com.gmail.pavkascool.hometask3.R;

import androidx.annotation.Nullable;



public class FragmentScale extends PreferenceFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

    }
}
