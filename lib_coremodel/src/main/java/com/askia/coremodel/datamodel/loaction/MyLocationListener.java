package com.askia.common.ui;

import android.location.Location;
import android.location.LocationListener;


public interface MyLocationListener extends LocationListener {

    void onLocationChanged(Location location);

}
