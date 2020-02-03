package com.gmail.pavkascool.c15_google_map.repo;

import android.app.Application;

import com.gmail.pavkascool.c15_google_map.repo.Repository;
import com.gmail.pavkascool.c15_google_map.repo.database.PhotoDao;
import com.gmail.pavkascool.c15_google_map.repo.database.PhotoDataBase;
import com.gmail.pavkascool.c15_google_map.repo.database.PhotoEntity;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class RepositoryImpl implements Repository {

    private final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public RepositoryImpl(Application application) {
        photoDao = PhotoDataBase.getInstance(application).photoDao();
    }

    private PhotoDao photoDao;

    @Override
    public CompletableFuture<List<PhotoEntity>> getAllPhotos() {
        return CompletableFuture.supplyAsync(() -> photoDao.getAll(), EXECUTOR);
    }

    @Override
    public CompletableFuture<PhotoEntity> getPhoto(final long id) {
        return CompletableFuture.supplyAsync(() -> photoDao.getEntity(id), EXECUTOR);
    }

    @Override
    public CompletableFuture<Void> savePhoto(File photoFile, LatLng position) {
        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setPhotoFile(photoFile);
        photoEntity.setPosition(position);
        return CompletableFuture.supplyAsync(() -> {
            photoDao.insert(photoEntity);
            return null;
        }, EXECUTOR);
    }
}
