package com.gmail.pavkascool.hometask3.contacts;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.pavkascool.hometask3.Person;
import com.gmail.pavkascool.hometask3.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentContacts extends Fragment implements View.OnClickListener{
    private Button addContact;
    private RecyclerView recyclerView;
    private PersonAdapter personAdapter;
    private List<Person> items;
    private ContactsActivity activity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, null);
        items = activity.getPersons();
        addContact = v.findViewById(R.id.add);
        addContact.setOnClickListener(this);
        recyclerView = v.findViewById(R.id.rec);
        int cols = getResources().getConfiguration().orientation;
        GridLayoutManager manager = new GridLayoutManager(activity, cols);
        recyclerView.setLayoutManager(manager);
        personAdapter = new PersonAdapter();
        recyclerView.setAdapter(personAdapter);

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (ContactsActivity)context;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.add) {
            activity.goToAdd();
        }

        else {
            int pos = recyclerView.getChildLayoutPosition(v);
            int id = (int) (items.get(pos).getId());
            activity.goToEdit(id);
        }

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
            personViewHolder.imageView.setImageResource(image);
            personViewHolder.imageView.setColorFilter(color);

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
