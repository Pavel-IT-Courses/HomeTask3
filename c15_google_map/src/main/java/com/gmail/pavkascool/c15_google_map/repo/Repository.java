package com.gmail.pavkascool.c15_google_map.repo;

import com.gmail.pavkascool.c15_google_map.repo.database.PhotoEntity;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Repository {
    CompletableFuture<List<PhotoEntity>> getAllPhotos();
    CompletableFuture<PhotoEntity> getPhoto(long id);
    CompletableFuture<Void> savePhoto(File photoFile, LatLng position);
}
