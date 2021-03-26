package com.bibsindia.bibstraderpanel.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bibsindia.bibstraderpanel.BuildConfig;
import com.bibsindia.bibstraderpanel.adapter.ShadesAdapter;
import com.bibsindia.bibstraderpanel.api.Client;
import com.bibsindia.bibstraderpanel.databinding.ActivityOrderDetailsBinding;
import com.bibsindia.bibstraderpanel.databinding.DialogShadeImgBinding;
import com.bibsindia.bibstraderpanel.model.orderDetails.Data;
import com.bibsindia.bibstraderpanel.model.orderDetails.OrderDetails;
import com.bibsindia.bibstraderpanel.model.orderDetails.ShadeDetail;
import com.bibsindia.bibstraderpanel.utils.NetworkUtils;
import com.bibsindia.bibstraderpanel.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.preference.PowerPreference;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.bibsindia.bibstraderpanel.TraderPanel.TOKEN;
import static com.bibsindia.bibstraderpanel.utils.Utils.HideLoading;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowAlert;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowLoading;

public class OrderDetailsActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 236;
    private static final String TAG = "OrderDetailsActivity";
    private ActivityOrderDetailsBinding mBinding;
    private Activity mActivity;
    private String mOrderNo;
    private int m_id;
    private KProgressHUD mLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mActivity = OrderDetailsActivity.this;
        mOrderNo = getIntent().getStringExtra("mOrderNo");

        mBinding.txtTitle.setSelected(true);

        //mBinding.rvShades.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        mBinding.rvShades.setLayoutManager(new GridLayoutManager(this, 3));
        mBinding.rvShades.setNestedScrollingEnabled(false);

        if (NetworkUtils.isConnected(mActivity)) {
            mLoading = ShowLoading(mActivity);
            OrderDetails(mOrderNo);
        }
    }

    public void clickBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void exportPDF(View view) {
        if (checkPermission()) {
            if (NetworkUtils.isConnected(mActivity)) {
                mLoading = ShowLoading(mActivity);
                ExportPDf(mOrderNo);
            }
        } else
            requestPermission();
    }

    private void OrderDetails(String orderNo) {
        Call<ResponseBody> call = Client.getInstance().getApi()
                .OrderDetails("Bearer " + PowerPreference.getDefaultFile().getString(TOKEN), orderNo);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                HideLoading(mLoading);
                try {
                    if (response.code() == 200) {
                        String res = response.body().string();
                        Log.e("TAG", "Dashboards Response =>" + res);

                        Gson gson = new GsonBuilder().create();
                        OrderDetails mOrderDetails = gson.fromJson(res, OrderDetails.class);

                        Data mData = mOrderDetails.getData();

                        m_id = mData.getManufacturer_id();
                        mBinding.txtOrderNumber.setText(mData.getOrderNo());
                        mBinding.txtCatalogueName.setText(mData.getCatalogueName());
                        mBinding.txtProductName.setText(mData.getProductName());
                        mBinding.txtFabricName.setText(mData.getFabricName());
                        mBinding.txtVariantName.setText(mData.getSubfabricName());
                        mBinding.txtManufacturerName.setText(mData.getManufacturer_name());

                        ShadesAdapter mAdapter = new ShadesAdapter(mActivity, mData.getShadeDetail(), mShadeDetail -> {
                            ShowPreviewShades(mShadeDetail);
                        });
                        mBinding.rvShades.setAdapter(mAdapter);


                        Glide.with(mActivity)
                                .load(mData.getCatalogueImage())
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        mBinding.loadingView.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        mBinding.loadingView.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(mBinding.imgCatalogueImage);

                    } else if (response.code() == 500) {
                        ShowAlert(mActivity, null, "Internal Server Error", false);
                    } else if (response.code() == 401) {
                        try {
                            String str = response.errorBody().string();
                            Log.w("Error", "Error:" + str);
                            JSONObject mObj = new JSONObject(str);
                            mObj = mObj.getJSONObject("data");
                            Iterator mKeyList = mObj.keys();
                            String key = (String) mKeyList.next();
                            Utils.ShowAlertLogout(OrderDetailsActivity.this, null, mObj.getString(key));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            String str = response.errorBody().string();
                            Log.w("Error", "Error:" + str);
                            JSONObject mObj = new JSONObject(str);
                            mObj = mObj.getJSONObject("data");
                            Iterator mKeyList = mObj.keys();
                            String key = (String) mKeyList.next();
                            ShowAlert(mActivity, null, mObj.getString(key), false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    HideLoading(mLoading);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                HideLoading(mLoading);
            }
        });
    }

    private void ExportPDf(String orderNo) {
        Call<ResponseBody> call = Client.getInstance().getApi()
                .SavePDF("Bearer " + PowerPreference.getDefaultFile().getString(TOKEN), orderNo, m_id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                HideLoading(mLoading);
                try {
                    if (response.code() == 200) {
                        //String res = response.body().string();
                        //Log.e("TAG", "Dashboards Response =>" + res);
                        SavePDf(response.body());

                    } else if (response.code() == 500) {
                        ShowAlert(mActivity, null, "Internal Server Error", false);
                    } else if (response.code() == 401) {
                        try {
                            String str = response.errorBody().string();
                            Log.w("Error", "Error:" + str);
                            JSONObject mObj = new JSONObject(str);
                            mObj = mObj.getJSONObject("data");
                            Iterator mKeyList = mObj.keys();
                            String key = (String) mKeyList.next();
                            Utils.ShowAlertLogout(OrderDetailsActivity.this, null, mObj.getString(key));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            String str = response.errorBody().string();
                            Log.w("Error", "Error:" + str);
                            JSONObject mObj = new JSONObject(str);
                            mObj = mObj.getJSONObject("data");
                            Iterator mKeyList = mObj.keys();
                            String key = (String) mKeyList.next();
                            ShowAlert(mActivity, null, mObj.getString(key), false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    HideLoading(mLoading);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                HideLoading(mLoading);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void SavePDf(ResponseBody body) {
        new AsyncTask<Void, Void, Void>() {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String mFileName = mOrderNo + "_" + timeStamp + ".pdf";
            String mFileSavedPath = null;

            @Override
            protected Void doInBackground(Void... voids) {
                mFileSavedPath = Utils.writeResponseBodyToDisk(OrderDetailsActivity.this, body, mFileName);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (mFileSavedPath != null) {

                    Dialog mAlertDialog = new Dialog(OrderDetailsActivity.this);
                    mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    com.bibsindia.bibstraderpanel.databinding.DialogAlertBinding mAlertBinding = com.bibsindia.bibstraderpanel.databinding.DialogAlertBinding.inflate(getLayoutInflater());
                    mAlertDialog.setContentView(mAlertBinding.getRoot());
                    Objects.requireNonNull(mAlertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
                    mAlertDialog.setCancelable(false);
                    mAlertDialog.setCanceledOnTouchOutside(false);
                    mAlertDialog.show();

                    mAlertBinding.tvTitle.setText("Save Location");
                    mAlertBinding.tvMsg.setText("Location : " + mFileSavedPath);
                    mAlertBinding.btnNo.setVisibility(View.VISIBLE);
                    mAlertBinding.btnNo.setText("Open");

                    mAlertBinding.btnOkay.setOnClickListener((View.OnClickListener) v -> {
                        mAlertDialog.dismiss();
                    });
                    mAlertBinding.btnNo.setOnClickListener((View.OnClickListener) v -> {
                        mAlertDialog.dismiss();
                        OpenPDF(new File(mFileSavedPath));
                    });
                }
            }
        }.execute();
    }

    private void OpenPDF(File mFile) {
        Uri bmpUri;
        if (Build.VERSION.SDK_INT < 24) {
            bmpUri = Uri.fromFile(mFile);
            Log.d(TAG, "1bmpUri : " + bmpUri);
        } else {
            bmpUri = FileProvider.getUriForFile(OrderDetailsActivity.this, BuildConfig.APPLICATION_ID + ".provider", mFile);
            Log.d(TAG, "2bmpUri : " + bmpUri);
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(bmpUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "ActivityNotFoundException : ", e);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    boolean writeD = shouldShowRequestPermissionRationale(permissions[0]);
                    boolean readD = shouldShowRequestPermissionRationale(permissions[1]);
                    if (write && read) {
                        Log.w("Permission", "Permission Granted");
                        if (NetworkUtils.isConnected(mActivity)) {
                            mLoading = ShowLoading(mActivity);
                            ExportPDf(mOrderNo);
                        }
                    } else if (!writeD && !readD) {
                        new AlertDialog.Builder(OrderDetailsActivity.this)
                                .setTitle("Permission Denied")
                                .setMessage("You need to allow access to the permissions. Without this permission you can't access your storage. Are you sure deny this permission?")
                                .setPositiveButton("Give Permission", (dialog, which) -> {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                })
                                .setNegativeButton("Deny Permission", null)
                                .create()
                                .show();
                        Log.w("Permission", "Permission Deny Dialog");
                    } else {
                        Log.w("Permission", "Permission Denied");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE) ||
                                    shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                                new AlertDialog.Builder(OrderDetailsActivity.this)
                                        .setMessage("You need to allow access to the permissions")
                                        .setPositiveButton("OK", (dialog, which) -> requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                                                PERMISSION_REQUEST_CODE))
                                        .setNegativeButton("Cancel", null)
                                        .create()
                                        .show();
                                return;
                            }
                        }

                    }
                }
                break;
        }
    }

    private void ShowPreviewShades(ShadeDetail mShadeDetail) {
        Dialog mPreviewDialog = new Dialog(mActivity);
        mPreviewDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogShadeImgBinding mPreviewBinding = DialogShadeImgBinding.inflate(mActivity.getLayoutInflater());
        mPreviewDialog.setContentView(mPreviewBinding.getRoot());
        Objects.requireNonNull(mPreviewDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        mPreviewDialog.setCancelable(true);
        mPreviewDialog.setCanceledOnTouchOutside(false);
        mPreviewDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPreviewDialog.show();

        Glide.with(mActivity).load(mShadeDetail.getShadeThumbImage()).into(mPreviewBinding.photoView);

        mPreviewBinding.imgClose.setOnClickListener(v -> {
            mPreviewDialog.dismiss();
        });
    }
}