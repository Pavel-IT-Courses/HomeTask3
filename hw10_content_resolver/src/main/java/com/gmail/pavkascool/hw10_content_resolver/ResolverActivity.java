package com.gmail.pavkascool.hw10_content_resolver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ResolverActivity extends AppCompatActivity {

    private  RecyclerView recyclerView;
    private ContentResolver contentResolver;
    private List<Contact> contacts;
    private ContactAdapter contactAdapter;
    final Uri CONTACT_URI = Uri.parse("content://com.gmail.pavkascool.contacts/persons");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resolver);
        contentResolver = getContentResolver();
        contacts = getContacts();
        System.out.println("Now in On Create, contacts size is " + contacts.size());
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactAdapter();
        recyclerView.setAdapter(contactAdapter);
    }

    private List<Contact> getContacts() {
        List<Contact> contactList = new ArrayList<>();
        String[] projection = new String[]{"*"};
        Cursor cursor = contentResolver.query(CONTACT_URI, projection, null, null, null);
        if(cursor != null) {
            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex("name");
                int contactIndex = cursor.getColumnIndex("contact");
                int idIndex = cursor.getColumnIndex("id");

                do {
                    String contactName = cursor.getString(nameIndex);
                    String contactNumber = cursor.getString(contactIndex);
                    long id = cursor.getInt(idIndex);
                    contactList.add(new Contact(id, contactName, contactNumber));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }

        return contactList;
    }

    private class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> implements View.OnClickListener {


        @NonNull
        @Override
        public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
            v.setOnClickListener(this);
            return new ContactViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
            Contact c = contacts.get(position);
            holder.nameInfo.setText(c.getName());
            holder.contactInfo.setText(c.getContactData());
        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }

        @Override
        public void onClick(View v) {
            int index = recyclerView.getChildLayoutPosition(v);
            long id = contacts.get(index).getId();
            contentResolver.delete(Uri.parse("content://com.gmail.pavkascool.contacts/persons" + "/" + id),
                    null, null);
            contacts.remove(index);
            contactAdapter.notifyDataSetChanged();
        }
    }

    private class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameInfo, contactInfo;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameInfo = itemView.findViewById(R.id.name);
            contactInfo = itemView.findViewById(R.id.contact);
        }
    }
}
