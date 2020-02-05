package com.gmail.pavkascool.hometask3.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.gmail.pavkascool.hometask3.R;

public class ContactsActivity extends AppCompatActivity implements FragmentInteractor{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        if(savedInstanceState == null) goToContacts();

    }


    @Override
    public void goToContacts() {

        replaceFragment(FragmentContactsNew.newInstance());
    }
    @Override
    public void goToAdd() {

        replaceFragment(FragmentAddNew.newInstance());
    }


    @Override
    public void goToEdit(int index) {

        replaceFragment(FragmentEditNew.newInstance(index));
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, fragment.getClass().getName());
        fragmentTransaction.commit();
    }
}
