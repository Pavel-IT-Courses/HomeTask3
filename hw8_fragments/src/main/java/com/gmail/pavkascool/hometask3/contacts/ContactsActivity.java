package com.gmail.pavkascool.hometask3.contacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.gmail.pavkascool.hometask3.FragmentApplication;
import com.gmail.pavkascool.hometask3.FragmentDatabase;
import com.gmail.pavkascool.hometask3.Person;
import com.gmail.pavkascool.hometask3.PersonDao;
import com.gmail.pavkascool.hometask3.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private FragmentDatabase db = FragmentApplication.getInstance().getDatabase();
    private List<Person> persons;
    private Fragment fragment;
    private FragmentTransaction fragmentTransaction;
    private String telOrMail;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("telOrMail", telOrMail);
        outState.putInt("index", index);
    }

    @Nullable
    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return persons;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        persons = (List<Person>)getLastCustomNonConfigurationInstance();
        if (persons == null) {
            persons = new ArrayList<>();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Person> p = db.personDao().getAll();
                    if (p != null && !p.isEmpty()) {
                        persons.addAll(p);
                    }
                }
            });
            t.start();
        }
        if(savedInstanceState == null) {
            fragment = new FragmentContacts();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frame_layout, fragment, fragment.getClass().getName());
            fragmentTransaction.commit();
        }
        else {
            telOrMail = savedInstanceState.getString("telOrMail");
        }

    }

    public void savePerson(final Person person) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                db.personDao().insert(person);
                persons.clear();
                persons.addAll(db.personDao().getAll());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        goToContacts();
                    }
                });
            }
        });
        t.start();

    }

    public void removePerson(final int index) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                PersonDao dao = db.personDao();
                Person person = dao.getById(index);
                dao.delete(person);
                persons.clear();
                persons.addAll(dao.getAll());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        goToContacts();
                    }
                });
            }
        });
        t.start();
    }

    public void goToEdit(int index) {
        this.index = index;
        fragment = new FragmentEdit();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, fragment.getClass().getName());
        fragmentTransaction.commit();

    }

    public void goToAdd() {
        fragment = new FragmentAdd();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    public void setToM(String s) {
        telOrMail = s;
    }

    public String getToM() {
        return telOrMail;
    }

    public void goToContacts() {
        fragment = new FragmentContacts();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    public void updateContacts() {
        persons.clear();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                PersonDao dao = db.personDao();
                persons.addAll(dao.getAll());
            }
        });
        t.start();
    }

}
