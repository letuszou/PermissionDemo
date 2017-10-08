package com.example.permissiondemo;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * 权限管理类
 */

public class PermissionUtils {
    private Activity activity;
    private AlertDialog alertDialog;

    public PermissionUtils(Activity activity) {
        this.activity = activity;
    }

    //申请权限
    public void setPermissionsNeed(String[] permissions, int permissionCode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (permissions.length > 0) {
            for (String permission : permissions) {
                switch (permission) {
                    case Manifest.permission.CAMERA:
                        if (hasPermission(Manifest.permission.CAMERA)) {
                            ActivityCompat.requestPermissions(activity,permissions, permissionCode);
                        }
                        break;
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            ActivityCompat.requestPermissions(activity, permissions, permissionCode);
                        }
                        break;
                    case Manifest.permission.ACCESS_COARSE_LOCATION:
                        if (hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            ActivityCompat.requestPermissions(activity, permissions, permissionCode);
                        }
                        break;
                }

            }
        }
    }

    //设置权限回调 grantResults 0 yes -1 no
    public void setPermissionBack(String[] permissions) {
        if (permissions.length > 0) {
            for (String permission : permissions) {
                switch (permission) {
                    case Manifest.permission.CAMERA:
                        if (hasPermission(Manifest.permission.CAMERA)) {
                            setPermissionDialog("相机权限");
                        }
                        break;
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                        if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            setPermissionDialog("存储权限");
                        }
                        break;
                    case Manifest.permission.ACCESS_COARSE_LOCATION:
                        if (hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            setPermissionDialog("定位权限");
                        }
                        break;
                }
            }
        } else {
            dialogPermissionDismiss();
        }
    }


    private void setPermissionDialog(String permission) {
        alertDialog = new AlertDialog.Builder(activity).setTitle("提示")
                .setMessage("当前应用需要打开" + permission + "，否则无法正常使用，是否去设置权限")
                .setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("立即设置",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //跳转GPS设置界面
                                dialog.dismiss();
                                startAppSettings();
                            }
                        })

                .setCancelable(false)
                .show();
    }

    public void dialogPermissionDismiss() {
        try {
            alertDialog.dismiss();
        } catch (NullPointerException e) {
            Log.e("error", "");
        }

    }

    //去设置中 设置允许权限
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }

    private boolean hasPermission(String permission){
        return ContextCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED;
    }


}
