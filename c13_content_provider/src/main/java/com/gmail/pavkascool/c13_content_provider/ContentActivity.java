package com.gmail.pavkascool.c13_content_provider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContentActivity extends AppCompatActivity {

    RecyclerView viewContactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        viewContactList = findViewById(R.id.viewContactList);
        //viewContactList.setAdapter(new ContactListAdapter(getContactList()));

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        List<Contacts> contacts = getContactList();
        showContactList(contacts);
    }

    private List<Contacts> getContactList() {
        ContentResolver contentResolver = getContentResolver();
        String[] projections = {ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor resultCursor = contentResolver.query(Uri.parse("com.pavkascool.provider/contact"), projections, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        List<Contacts> contactList = new ArrayList<>();
        if (resultCursor != null) {
            if (resultCursor.moveToFirst()) {
                int displayNameIndex = resultCursor.getColumnIndex("name");
                int displayPhoneIndex = resultCursor.getColumnIndex("phone");
                do {
                    String contactName = resultCursor.getString(displayNameIndex);
                    String contactNumber = resultCursor.getString(displayPhoneIndex);
                    contactList.add(new Contacts(contactName, contactNumber));
                }
                while (resultCursor.moveToNext());
            }
            resultCursor.close();
        }
        return contactList;
    }

    private void showContactList(List<Contacts> list) {
        ContactListAdapter adapter = new ContactListAdapter(list);
        viewContactList.setAdapter(adapter);
    }

    private class ContactListAdapter extends RecyclerView.Adapter<ContactViewHolder> {

        private List<Contacts> itemList;

        public ContactListAdapter(List<Contacts> itemList) {
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
            return new ContactViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
            Contacts contact = itemList.get(position);
            holder.BindData(contact);
        }

        @Override
        public int getItemCount() {
            if(itemList == null) return 0;
            return itemList.size();
        }
    }
    private class ContactViewHolder extends RecyclerView.ViewHolder {

        private TextView name, phone;
        public ContactViewHolder(@NonNull View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
        }

        public void BindData(Contacts contact) {
            name.setText(contact.getName());
            phone.setText(contact.getPhoneNumber());
        }
    }
}
