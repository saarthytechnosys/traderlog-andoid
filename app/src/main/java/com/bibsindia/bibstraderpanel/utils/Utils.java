package com.bibsindia.bibstraderpanel.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;

import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.activity.LoginActivity;
import com.bibsindia.bibstraderpanel.databinding.DialogAlertBinding;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.preference.PowerPreference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import okhttp3.ResponseBody;

import static com.bibsindia.bibstraderpanel.TraderPanel.IS_LOGIN;
import static com.bibsindia.bibstraderpanel.TraderPanel.MOBILE;
import static com.bibsindia.bibstraderpanel.TraderPanel.NAME;
import static com.bibsindia.bibstraderpanel.TraderPanel.PASSWORD;

public class Utils {
    public static final String WHATSAPP = "com.whatsapp";
    public static final String WHATSAPP_BUS = "com.whatsapp.w4b";
    public static final String TELEGRAM = "org.telegram.messenger";
    public static final String MESSENGER = "com.facebook.orca";
    public static final String COPY = "COPY";

    public static KProgressHUD ShowLoading(Activity activity) {
        return KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                /*.setLabel("Please wait")*/
                .setCancellable(false)
                .show();
    }

    public static void HideLoading(KProgressHUD d) {
        if (d != null && d.isShowing()) {
            d.dismiss();
        }
    }

    public static Dialog mAlertDialog = null;

    public static void ShowAlert(final Activity activity, String title, String msg, boolean isFinish) {
        if (mAlertDialog != null && mAlertDialog.isShowing())
            mAlertDialog.dismiss();

        mAlertDialog = new Dialog(activity);
        mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogAlertBinding mAlertBinding = DialogAlertBinding.inflate(activity.getLayoutInflater());
        mAlertDialog.setContentView(mAlertBinding.getRoot());
        Objects.requireNonNull(mAlertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        mAlertDialog.setCancelable(false);
        mAlertDialog.setCanceledOnTouchOutside(false);

        if (title == null)
            mAlertBinding.tvTitle.setText(activity.getString(R.string.alert_title));
        else
            mAlertBinding.tvTitle.setText(title);
        mAlertBinding.tvMsg.setText(msg);
        mAlertBinding.btnOkay.setOnClickListener(v -> {
            mAlertDialog.dismiss();
            if (isFinish) {
                activity.finish();
            }
        });
        mAlertDialog.show();
    }

    public static void ShowAlertLogout(final Activity activity, String title, String msg) {
        if (mAlertDialog != null && mAlertDialog.isShowing())
            mAlertDialog.dismiss();

        mAlertDialog = new Dialog(activity);
        mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogAlertBinding mAlertBinding = DialogAlertBinding.inflate(activity.getLayoutInflater());
        mAlertDialog.setContentView(mAlertBinding.getRoot());
        Objects.requireNonNull(mAlertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        mAlertDialog.setCancelable(false);
        mAlertDialog.setCanceledOnTouchOutside(false);

        if (title == null)
            mAlertBinding.tvTitle.setText(activity.getString(R.string.alert_title));
        else
            mAlertBinding.tvTitle.setText(title);
        mAlertBinding.tvMsg.setText(msg);
        mAlertBinding.btnOkay.setOnClickListener(v -> {
            mAlertDialog.dismiss();
            PowerPreference.getDefaultFile().remove(IS_LOGIN);
            PowerPreference.getDefaultFile().remove(NAME);
            PowerPreference.getDefaultFile().remove(MOBILE);
            PowerPreference.getDefaultFile().remove(PASSWORD);
            Intent i = new Intent(activity, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(i);
            activity.finish();
        });
        mAlertDialog.show();
    }

    public static String writeResponseBodyToDisk(Activity activity, ResponseBody body, String mFileName) {
        try {
            // todo change the file location/name according to your needs
            File mDirectory = new File(activity.getExternalFilesDir(null) + File.separator + "Bibs Trader Panel");
            if (!mDirectory.exists())
                Log.d("writeResponse", "Created Directory! " + mDirectory.mkdirs());
            File mFilePath = new File(mDirectory, mFileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(mFilePath);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("writeResponse", mFilePath + " file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return mFilePath.getAbsolutePath();
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

    public static String GetShareURL(String url) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        Log.w("GetShareURL", "ID : " + fileName);
//        fileName = "https://bibsindia.com/sharedcatlog/" + fileName;
        fileName = "https://trader.thewowl.com/sharedcatlog/" + fileName;
        return fileName;
    }
}
