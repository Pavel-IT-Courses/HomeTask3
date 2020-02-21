package com.gmail.pavkascool.hometask3.contacts;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.KeyEvent;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.pavkascool.hometask3.FragmentApplication;
import com.gmail.pavkascool.hometask3.FragmentDatabase;
import com.gmail.pavkascool.hometask3.Person;
import com.gmail.pavkascool.hometask3.R;
import com.gmail.pavkascool.hometask3.contacts.utils.CameraUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class FragmentAdd extends Fragment implements View.OnClickListener {
    private EditText editName, editContact;
    private RadioButton tel, mail;
    private Button save, back;
    private Button takePhoto;
    private TextView telOrMail;
    private FragmentInteractor interactor;
    private FragmentDatabase db;
    private String phoneOrEmail;
    private Bitmap picture;

    private static final int CAMERA_REQUEST = 0;
    private static final int SAVE_PICTURE_REQUEST = 1;



    public static FragmentAdd newInstance() {
        FragmentAdd fragment = new FragmentAdd();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

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
        takePhoto = v.findViewById(R.id.take);
        takePhoto.setOnClickListener(this);
        if(!CameraUtils.hasCameraHardware(getContext())) takePhoto.setEnabled(false);
        if (picture != null) {
            BitmapDrawable bd = new BitmapDrawable(getResources(), picture);
            takePhoto.setBackground(bd);
        }
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
                takePhoto.getBackground();
                if(mail.isChecked()) person.setHasEmail(true);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long index = db.personDao().insert(person);
                        if (picture != null) {
                            FileOutputStream fos = null;
                            try {
                                File photoFile = CameraUtils.preparePhotoFile(getContext(), index);
                                fos = new FileOutputStream(photoFile);
                                picture.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Photo Not Saved", Toast.LENGTH_SHORT).show();
                            }
                            finally {
                                if(fos != null) {
                                    try {
                                        fos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                });
                t.start();
                try {
                    Thread.currentThread().sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                interactor.goToContacts();
                break;
            case R.id.back:
                interactor.goToContacts();
                break;
            case R.id.take:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            //Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            picture = (Bitmap) data.getExtras().get("data");

            BitmapDrawable bd = new BitmapDrawable(getResources(), picture);
            takePhoto.setBackground(bd);

        }
    }
}
