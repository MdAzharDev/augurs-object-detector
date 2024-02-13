package com.tutorials.objectdetection

import android.os.Build
import android.os.Bundle
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tutorials.objectdetection.databinding.ActivityMainBinding
import org.tensorflow.lite.examples.objectdetection.FirebaseUtil
import org.tensorflow.lite.examples.objectdetection.FirebaseUtil.Companion.getFirebaseDatabaseReferenceForCameraDataNode

/**
 * Main entry point into our app. This app follows the single-activity pattern, and all
 * functionality is implemented in the form of fragments.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        getLocation()
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            // Workaround for Android Q memory leak issue in IRequestFinishCallback$Stub.
            // (https://issuetracker.google.com/issues/139738913)
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }


    private fun getLocation() {
        FirebaseUtil.logError("getLocation called")
        Dexter.withContext(this@MainActivity)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object :MultiplePermissionsListener{
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport?) {
                    if (multiplePermissionsReport != null) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            val myGpsTracker = MyGpsTracker(this@MainActivity)
                            val location = myGpsTracker.location
                            if (location != null) {
                                val lat = location.latitude
                                val lon = location.longitude
                                val lat_lon = "$lat,$lon"
                                getFirebaseDatabaseReferenceForCameraDataNode()
                            }
                        } else if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {

                            FirebaseUtil.permissionDialog(
                                this@MainActivity,
                                "Permission Required",
                                "Location permission is required"
                            )

                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    permissionToken: PermissionToken?
                ) {
                    permissionToken?.continuePermissionRequest()
                }

            }).onSameThread().check()
    }

}
