package com.gmail.pavkascool.hometask3.contacts;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gmail.pavkascool.hometask3.FragmentApplication;
import com.gmail.pavkascool.hometask3.FragmentDatabase;
import com.gmail.pavkascool.hometask3.Person;
import com.gmail.pavkascool.hometask3.R;


public class FragmentAdd extends Fragment implements View.OnClickListener {
    private EditText editName, editContact;
    private RadioButton tel, mail;
    private Button save, back;
    private TextView telOrMail;
    private FragmentInteractor interactor;
    private FragmentDatabase db;
    private String phoneOrEmail;



    public static FragmentAdd newInstance() {
        FragmentAdd fragment = new FragmentAdd();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add, container, false);
        db = FragmentApplication.getInstance().getDatabase();
        editContact = v.findViewById(R.id.edit_contact);
        editName = v.findViewById(R.id.edit_name);
        save = v.findViewById(R.id.save);
        save.setOnClickListener(this);
        back = v.findViewById(R.id.back);
        back.setOnClickListener(this);
        tel = v.findViewById(R.id.tel);
        tel.setOnClickListener(this);
        mail = v.findViewById(R.id.mail);
        mail.setOnClickListener(this);
        telOrMail = v.findViewById(R.id.tel_mail);
        if(savedInstanceState != null) {
            phoneOrEmail = savedInstanceState.getString("telOrMail");
        }
        else phoneOrEmail = getString(R.string.phone);
        telOrMail.setText(phoneOrEmail);
        if(phoneOrEmail == getString(R.string.email)) {
            mail.setChecked(true);
        }

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("telOrMail", phoneOrEmail);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        interactor = (FragmentInteractor) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactor = null;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.tel:
                telOrMail.setText(R.string.phone);
                phoneOrEmail = getString(R.string.phone);
                break;
            case R.id.mail:
                telOrMail.setText(R.string.email);
                phoneOrEmail = getString(R.string.email);
                break;
            case R.id.save:
                final Person person = new Person();
                person.setName(editName.getText().toString());
                person.setContact(editContact.getText().toString());
                if(mail.isChecked()) person.setHasEmail(true);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db.personDao().insert(person);
                    }
                });
                t.start();
                interactor.goToContacts();
                break;
            case R.id.back:
                interactor.goToContacts();
        }
    }
}
