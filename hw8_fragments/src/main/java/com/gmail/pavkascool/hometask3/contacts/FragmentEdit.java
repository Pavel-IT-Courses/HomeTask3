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
import android.widget.TextView;

import com.gmail.pavkascool.hometask3.FragmentApplication;
import com.gmail.pavkascool.hometask3.FragmentDatabase;
import com.gmail.pavkascool.hometask3.Person;
import com.gmail.pavkascool.hometask3.PersonDao;
import com.gmail.pavkascool.hometask3.R;

public class FragmentEdit extends Fragment implements View.OnClickListener {
    private FragmentDatabase db = FragmentApplication.getInstance().getDatabase();
    private TextView telOrMail;
    private EditText name, contact;
    private Button edit, remove;
    private int index;
    private FragmentInteractor interactor;

    public static FragmentEdit newInstance(int index) {
        FragmentEdit fragment = new FragmentEdit();
        fragment.index = index;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        interactor = (FragmentInteractor)context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        interactor = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        telOrMail = v.findViewById(R.id.contact);
        name = v.findViewById(R.id.edit_name);
        contact = v.findViewById(R.id.edit_contact);
        edit = v.findViewById(R.id.edit);
        edit.setOnClickListener(this);
        remove = v.findViewById(R.id.remove);
        remove.setOnClickListener(this);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Person person = db.personDao().getById(index);
                final String n = person.getName();
                final String c = person.getContact();
                final boolean hasEmail = person.isHasEmail();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        name.setText(n);
                        contact.setText(c);
                        if (hasEmail) telOrMail.setText("Email");
                        else telOrMail.setText("Phone");
                    }
                });
            }
        });
        t.start();
        return v;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.edit:
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PersonDao dao = db.personDao();
                        Person person = dao.getById(index);
                        boolean hasMail = person.isHasEmail();
                        Person newPerson = new Person();
                        newPerson.setId(index);
                        newPerson.setHasEmail(hasMail);
                        newPerson.setName(name.getText().toString());
                        newPerson.setContact(contact.getText().toString());
                        dao.update(newPerson);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                interactor.goToContacts();
                            }
                        });
                    }
                });
                t.start();
                break;
            case R.id.remove:
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PersonDao dao = db.personDao();
                        Person person = dao.getById(index);
                        dao.delete(person);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                interactor.goToContacts();
                            }
                        });
                    }
                });
                thread.start();
                break;
        }
    }
}
