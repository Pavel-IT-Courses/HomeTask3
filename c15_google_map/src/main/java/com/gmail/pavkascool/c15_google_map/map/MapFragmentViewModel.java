package com.gmail.pavkascool.c15_google_map.map;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gmail.pavkascool.c15_google_map.repo.Repository;
import com.gmail.pavkascool.c15_google_map.repo.RepositoryImpl;
import com.gmail.pavkascool.c15_google_map.repo.database.PhotoEntity;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.TaskExecutors;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MapFragmentViewModel extends AndroidViewModel {
    private Repository repository;
    private MutableLiveData<List<MarkerData>> liveData = new MutableLiveData<>();

    public MapFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new RepositoryImpl(application);
    }

    public void fetchMarkers() {
        repository.getAllPhotos().thenApplyAsync(new Function<List<PhotoEntity>, List<MarkerData>>() {
            @Override
            public List<MarkerData> apply(List<PhotoEntity> photoEntities) {
                ArrayList<MarkerData> markerDataArrayList = new ArrayList<>();
                if(!photoEntities.isEmpty()) {

                    for(PhotoEntity entity: photoEntities) {
                        File file = entity.getPhotoFile();
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                        MarkerOptions markerOptions = new MarkerOptions()
                                .draggable(false)
                                .position(entity.getPosition())
                                .icon(descriptor);


                        MarkerData markerData = new MarkerData(entity.getId(), markerOptions);
                        markerDataArrayList.add(markerData);
                    }

                }
                return markerDataArrayList;
            }
        }).thenAcceptAsync(markerData -> liveData.setValue(markerData), TaskExecutors.MAIN_THREAD);

    }

    public LiveData<List<MarkerData>> getLiveData() {
        return liveData;
    }
}
