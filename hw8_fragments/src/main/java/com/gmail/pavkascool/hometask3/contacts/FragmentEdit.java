package com.gmail.pavkascool.hometask3.contacts;

import android.content.Context;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentEdit extends Fragment implements View.OnClickListener {
    private FragmentDatabase db = FragmentApplication.getInstance().getDatabase();
    private TextView telOrMail;
    private EditText name, contact;
    private Button edit, remove;
    private int index;
    private ContactsActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (ContactsActivity)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit, null);
        telOrMail = v.findViewById(R.id.contact);
        name = v.findViewById(R.id.edit_name);
        contact = v.findViewById(R.id.edit_contact);
        edit = v.findViewById(R.id.edit);
        edit.setOnClickListener(this);
        remove = v.findViewById(R.id.remove);
        remove.setOnClickListener(this);
        if(savedInstanceState != null) {
            index = savedInstanceState.getInt("index");
        }
        else {
            index = activity.getIndex();
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Person person = db.personDao().getById(index);
                final String n = person.getName();
                final String c = person.getContact();
                final boolean hasEmail = person.isHasEmail();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        name.setText(n);
                        contact.setText(c);
                        if(hasEmail) telOrMail.setText("Email");
                        else telOrMail.setText("Phone");
                    }
                });
            }
        });
        t.start();

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index", index);
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
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.updateContacts();
                                activity.goToContacts();
                            }
                        });
                    }
                });
                t.start();
                break;
            case R.id.remove:
                activity.removePerson(index);
                break;
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
