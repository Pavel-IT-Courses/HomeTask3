package com.gmail.pavkascool.hometask3.contacts;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.pavkascool.hometask3.FragmentApplication;
import com.gmail.pavkascool.hometask3.FragmentDatabase;
import com.gmail.pavkascool.hometask3.Person;
import com.gmail.pavkascool.hometask3.R;
import com.gmail.pavkascool.hometask3.contacts.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.gmail.pavkascool.hometask3.contacts.utils.CameraUtils.FILENAME_PREFIX;

public class FragmentContacts extends Fragment implements View.OnClickListener {
    private Button addContact;
    private RecyclerView recyclerView;
    private PersonAdapter personAdapter;
    private List<Person> items;
    private Set<Long> ids;
    private FragmentInteractor interactor;
    private FragmentDatabase db;



    public static FragmentContacts newInstance() {
        FragmentContacts fragment = new FragmentContacts();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FragmentApplication.getInstance().getDatabase();
        initItems();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_contacts, container, false);

        addContact = v.findViewById(R.id.add);
        addContact.setOnClickListener(this);
        recyclerView = v.findViewById(R.id.rec);
        int cols = getResources().getConfiguration().orientation;
        GridLayoutManager manager = new GridLayoutManager(getContext(), cols);
        recyclerView.setLayoutManager(manager);
        personAdapter = new PersonAdapter();
        recyclerView.setAdapter(personAdapter);


        return v;
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

        if(v.getId() == R.id.add) {
            interactor.goToAdd();
        }

        else {
            int pos = recyclerView.getChildLayoutPosition(v);
            int id = (int) (items.get(pos).getId());
            interactor.goToEdit(id);
        }

    }

    private void initItems() {
        items = new ArrayList<>();
        ids = new HashSet<>();
        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {
                List<Person> persons = db.personDao().getAll();
                if(persons != null && !persons.isEmpty()) {
                    items.addAll(persons);
                    for(Person p: persons) {
                        ids.add(p.getId());
                    }
                }
            }
        });
        t.start();
    }

    private BitmapDrawable getPictureFromFile(File file){
        Bitmap bitmap = ImageUtils.decodeBitmapFromFile(file);
        return new BitmapDrawable(getResources(), bitmap);
    }


    private class PersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        @NonNull
        @Override
        public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
            v.setOnClickListener(FragmentContacts.this);
            return new PersonViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            PersonViewHolder personViewHolder = (PersonViewHolder)holder;
            Person p = items.get(position);
            personViewHolder.nameView.setText(p.getName());
            String contactType;
            int image;
            int color;

            if(p.isHasEmail()) {
                contactType = "e-mail: ";
                image = R.drawable.contact_mail;
                color = Color.RED;
            }
            else {
                contactType = "tel: ";
                image = R.drawable.contact_phone;
                color = Color.BLUE;
            }
            personViewHolder.contactView.setText(contactType + p.getContact());

            File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String imageId = FILENAME_PREFIX + p.getId() + ".jpg";
            File photo = new File(storageDir, imageId);

            if(photo.exists()) {
                Bitmap bitmap = ImageUtils.decodeBitmapFromFile(photo);
                BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
                Picasso.with(getContext()).load(photo).into(personViewHolder.imageView);

            } else {
                personViewHolder.imageView.setImageResource(image);
                personViewHolder.imageView.setColorFilter(color);
            }

        }

        @Override
        public int getItemViewType(int position) {
            return position;

        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView nameView, contactView;
        ImageView imageView;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.name);
            contactView = itemView.findViewById(R.id.contact);
            imageView = itemView.findViewById(R.id.image);
        }
    }

}
