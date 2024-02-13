package com.tutorials.objectdetection;

import android.Manifest;;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import androidx.core.app.ActivityCompat;

import org.tensorflow.lite.examples.objectdetection.FirebaseUtil;


public class MyGpsTracker implements LocationListener {
    Context context;

    public MyGpsTracker(Context c) {
        context = c;
    }

    public Location getLocation() {

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnabled) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0.1f, this, Looper.getMainLooper());
            Location l = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            return l;
        } else {
            /*new IOSDialog.Builder(context)
                    .title("GPS is disabled")              // String or String Resource ID
                    .message("Show location settings?")  // String or String Resource ID
                    .positiveButtonText("Ok")  // String or String Resource ID
                    .positiveClickListener(new IOSDialog.Listener() {
                        @Override
                        public void onClick(IOSDialog iosDialog) {
                            iosDialog.dismiss();
                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .build()
                    .show();*/
            /*new iOSDialogBuilder(context)
                    .setTitle("GPS is disabled")
                    .setSubtitle("Show location settings?")
                    .setFont(setFontStyle(context, AppConstants.POPPINS_REGULAR))
                    .setBoldPositiveLabel(true)
                    .setCancelable(false)
                    .setPositiveListener("Ok", new iOSDialogClickListener() {
                        @Override
                        public void onClick(iOSDialog dialog) {

                            dialog.dismiss();
                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .build().show();*/

            FirebaseUtil.Companion.permissionDialog(
                    context,
            "GPS disabled",
                    "Open Settings"
                        );
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(context, "LocationUpdate :" + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
//        updateLocationForLiveLocationSharing(context, location);

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}