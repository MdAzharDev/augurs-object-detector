package org.tensorflow.lite.examples.objectdetection

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tutorials.objectdetection.R
import java.io.ByteArrayOutputStream


class FirebaseUtil {
    companion object{
        fun logError(msg : String){
            Log.e("FirebaseUtilLog", msg);
        }
        fun getFirebaseDatabaseInstance(): FirebaseDatabase {
            return FirebaseDatabase.getInstance()
        }
        fun getFirebaseDatabaseReference(): DatabaseReference {
            return getFirebaseDatabaseInstance().getReference()
        }

        fun getFirebaseDatabaseReferenceForCameraDataNode(): DatabaseReference {
            return getFirebaseDatabaseReference().child("camera_data")
        }

        fun encodeImage(bm: Bitmap): String? {
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }


        fun permissionDialog(context: Context?, title: String?, content: String?) {
            val builder: AlertDialog.Builder =
                AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog)
            val view: View =
                View.inflate(context, R.layout.alert_dialog_layout_with_single_button, null)
            //        builder.setView(R.layout.alert_dialog_layout_with_single_button);
            builder.setView(view)
            val alertDialog: AlertDialog = builder.create()

            //*********** make dialog outer background transparent ***********//
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow()!!.getAttributes().windowAnimations = R.style.alertDialog;

                alertDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.getWindow()!!
                    .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }

            //*********** get UI component ***********//
            val tvTitle: TextView = view.findViewById(R.id.tv_title)
            val tvMessage: TextView = view.findViewById(R.id.tv_message)
            val tvBtnOk: TextView = view.findViewById(R.id.tv_btn)

            //*********** set custom fonts *************//
//        tvTitle.setTypeface(setFontStyle(context, AppConstants.POPPINS_MEDIUM));
//        tvMessage.setTypeface(setFontStyle(context, AppConstants.POPPINS_REGULAR));
//        tvBtnOk.setTypeface(setFontStyle(context, AppConstants.POPPINS_MEDIUM));

            //*********** set content with view ***********//
            tvTitle.text = title
            tvMessage.text = content
            tvBtnOk.text = "Ok"
            tvBtnOk.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                        alertDialog.cancel()
                    openAppSettings(context)
                }
            })
            alertDialog.show()
        }

        fun openAppSettings(context: Context?) {
            val packageUri =
                Uri.fromParts("package", "com.tutorials.tflitesample", null)
            val intent =
                Intent().setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.setData(packageUri)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

//        MyApplication.getInstance().startActivity(intent);
            context?.startActivity(intent)
        }

    }


}