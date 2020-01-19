package com.gmail.pavkascool.hometask3.contacts;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gmail.pavkascool.hometask3.Person;
import com.gmail.pavkascool.hometask3.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentAdd extends Fragment implements View.OnClickListener {
    private EditText editName, editContact;
    private RadioButton tel, mail;
    private Button save, back;
    private TextView telOrMail;
    private ContactsActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add, null);
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
        String s = activity.getToM();
        if(s == null || s.isEmpty()) s = getString(R.string.phone);
        telOrMail.setText(s);
        if(s == getString(R.string.email)) mail.setChecked(true);
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (ContactsActivity)context;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.tel:
                telOrMail.setText(R.string.phone);
                activity.setToM(getString(R.string.phone));
                break;
            case R.id.mail:
                telOrMail.setText(R.string.email);
                activity.setToM(getString(R.string.email));
                break;
            case R.id.save:
                Person person = new Person();
                person.setName(editName.getText().toString());
                person.setContact(editContact.getText().toString());
                if(mail.isChecked()) person.setHasEmail(true);
                activity.savePerson(person);
                break;
            case R.id.back:
                activity.goToContacts();
        }

    }
}
