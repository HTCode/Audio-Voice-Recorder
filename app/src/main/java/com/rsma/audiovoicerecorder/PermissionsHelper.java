package com.rsma.audiovoicerecorder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

public class PermissionsHelper {
    private String TAG = "PermissionsHelper";
    private Context mContext;
    private String[] mPermissions;
    public int PERMISSIONS_CODE;

    public PermissionsHelper(Context context, int CODE, String... permissions) {
        mContext = context;
        mPermissions = permissions;
        PERMISSIONS_CODE = CODE;
    }

    // ===================================================
    private boolean notHavePermissions() {
        for (String permission : mPermissions) {
            if (ActivityCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }

        return false;
    }

    // ===================================================
    private boolean shouldShowRationale() {
        for (String permission : mPermissions) {
            if ( ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permission) ) {
                return true;
            }
        }

        return false;
    }
    // ==========================================================================================
    public void requestPermissions() {
        if( notHavePermissions() ) {
            if ( shouldShowRationale() ) {
                Log.d(TAG, "shouldShowRationale");
                showRationale();
            } else {
                ActivityCompat.requestPermissions((Activity) mContext, mPermissions, PERMISSIONS_CODE);
                Log.d(TAG, "Permissions requested");
            }
        } else {
            Log.d(TAG, "All permission already granted");
        }
    }

    // ==========================================================================================
    private void showRationale() {
        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setTitle(R.string.permissions_rational_title)
                .setMessage(R.string.permissions_rational_message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        ActivityCompat.requestPermissions((Activity) mContext, mPermissions, PERMISSIONS_CODE);
                    }
                }).create().show();
    }

    // ==========================================================================================
    public void onDenied(int requestCode) {
        if (requestCode == PERMISSIONS_CODE && notHavePermissions() ) {
            if ( shouldShowRationale() ) {
                Toast.makeText(mContext, R.string.permissions_denied_message, Toast.LENGTH_LONG).show();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((Activity) mContext).finish();
                }
            }, 3500);
        }
    }
}
