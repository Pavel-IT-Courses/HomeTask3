package com.gmail.pavkascool.c13_content_provider2;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContactProvider extends ContentProvider {

    private DBOpenHelper dbOpenHelper;
    private static UriMatcher uriMatcher;
    private final static String AUTHORITIES = "com.pavkascool.provider";
    private static String CONTACT_MIME_TYPE = "object/contact";
    private SQLiteDatabase database;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITIES, "contact", 1);
    }
    @Override
    public boolean onCreate() {
        dbOpenHelper = new DBOpenHelper(getContext());
        database = dbOpenHelper.getReadableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        if(uriMatcher.match(uri) == 1) {
            cursor = database.query("contacts", projection, selection, selectionArgs,
                    null, null, sortOrder);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        if(uriMatcher.match(uri) == 1) return CONTACT_MIME_TYPE;
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if(uriMatcher.match(uri) == 1) {
            long id = database.insert("contacts", null, values);
            if(id != -1) {
                return Uri.withAppendedPath(uri, String.valueOf(id));
            }
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if(uriMatcher.match(uri) == 1) {
            return database.delete("contacts", selection, selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if(uriMatcher.match(uri) == 1) {
            return database.update("contacts", values, selection, selectionArgs);
        }
        return 0;
    }
}
