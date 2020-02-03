package com.gmail.pavkascool.c15_google_map.map;

import android.graphics.Bitmap;

import com.gmail.pavkascool.c15_google_map.repo.database.PhotoEntity;
import com.gmail.pavkascool.c15_google_map.utils.ImageUtils;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PhotoEntityToMarkerMapper implements Function<List<PhotoEntity>, List<MarkerData>> {
    @Override
    public List<MarkerData> apply(List<PhotoEntity> data) {
        List<MarkerData> mapMarkerList = Collections.emptyList();
        if (data != null && !data.isEmpty()) {
            mapMarkerList = data.stream()
                    .map(this::getMarker)
                    .collect(Collectors.toList());
        }
        return mapMarkerList;
    }

    private MarkerData getMarker(PhotoEntity markerViewModelState) {
        DateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy", Locale.getDefault());
        MarkerOptions markerOptions = new MarkerOptions().position(markerViewModelState.getPosition())

                .draggable(false)
                .icon(getBitmapIcon(markerViewModelState.getPhotoFile()));
        return new MarkerData(markerViewModelState.getId(), markerOptions);
    }

    private BitmapDescriptor getBitmapIcon(File photoFile) {
        Bitmap imageBitmap = ImageUtils.decodeBitmapFromFile(photoFile);
        Bitmap scaled = Bitmap.createScaledBitmap(imageBitmap, 100, 100, true);
        return BitmapDescriptorFactory.fromBitmap(scaled);
    }
}

