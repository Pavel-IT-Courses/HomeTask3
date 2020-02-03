package com.gmail.pavkascool.hometask3.contacts;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.gmail.pavkascool.hometask3.FragmentApplication;
import com.gmail.pavkascool.hometask3.FragmentDatabase;
import com.gmail.pavkascool.hometask3.Person;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public class ContactProvider extends ContentProvider {

    static final String AUTHORITY = "com.gmail.pavkascool.contacts";
    static final String PATH = "persons";
    public static final Uri CONTACT_CONTENT_URI = Uri.parse("context://" + AUTHORITY + "/" + PATH);

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH, 1);
        uriMatcher.addURI(AUTHORITY, PATH + "/#", 2);
    }

    private FragmentDatabase db;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if(uriMatcher.match(uri) == 2) {
            String idString = uri.getLastPathSegment();
            final long id = Long.parseLong(idString);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Person p = db.personDao().getById(id);
                    db.personDao().delete(p);
                }
            });
            thread.start();
            return 1;
        }
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.pavkascool.myType";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public boolean onCreate() {

        db = Room.databaseBuilder(getContext(), FragmentDatabase.class, "fragment_database")
                .build();
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;
        if(uriMatcher.match(uri) == 1) {
            FutureTask<Cursor> future = new FutureTask<Cursor>(new Callable<Cursor>() {
                @Override
                public Cursor call() throws Exception {

                    return db.personDao().getAllRecords();
                }
            });
            new Thread(future).start();
            try {
                cursor = future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }
}
