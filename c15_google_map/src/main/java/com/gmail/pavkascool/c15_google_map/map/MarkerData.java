package com.gmail.pavkascool.c15_google_map.map;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MarkerData {
    private long id;
    private MarkerOptions markerOptions;

    public MarkerData(long id, MarkerOptions markerOptions) {
        this.id = id;
        this.markerOptions = markerOptions;
    }

    public long getId() {
        return id;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }
}
