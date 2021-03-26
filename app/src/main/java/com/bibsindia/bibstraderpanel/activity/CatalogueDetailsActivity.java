package com.bibsindia.bibstraderpanel.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.adapter.ShadesSelectionAdapter;
import com.bibsindia.bibstraderpanel.api.Client;
import com.bibsindia.bibstraderpanel.databinding.ActivityCatalogueDetailsBinding;
import com.bibsindia.bibstraderpanel.databinding.DialogShadeImgBinding;
import com.bibsindia.bibstraderpanel.databinding.DialogWhatsappBinding;
import com.bibsindia.bibstraderpanel.model.catalogue.Data;
import com.bibsindia.bibstraderpanel.model.catalogue.ShadeDetail;
import com.bibsindia.bibstraderpanel.utils.NetworkUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.preference.PowerPreference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bibsindia.bibstraderpanel.TraderPanel.TOKEN;
import static com.bibsindia.bibstraderpanel.utils.Utils.COPY;
import static com.bibsindia.bibstraderpanel.utils.Utils.GetShareURL;
import static com.bibsindia.bibstraderpanel.utils.Utils.HideLoading;
import static com.bibsindia.bibstraderpanel.utils.Utils.MESSENGER;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowAlert;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowLoading;
import static com.bibsindia.bibstraderpanel.utils.Utils.TELEGRAM;
import static com.bibsindia.bibstraderpanel.utils.Utils.WHATSAPP;
import static com.bibsindia.bibstraderpanel.utils.Utils.WHATSAPP_BUS;

public class CatalogueDetailsActivity extends AppCompatActivity {

    private static final String TAG = "CatalogueDetailsActivity";
    private ActivityCatalogueDetailsBinding mBinding;
    private Activity mActivity;
    private ShadesSelectionAdapter mAdapter;
    private Data mCatalogueDetails;
    private boolean isShared;
    private KProgressHUD mLoading;
    private String mShareLink = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityCatalogueDetailsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mActivity = CatalogueDetailsActivity.this;


        Bundle mIntent = getIntent().getExtras();
        if (mIntent != null) {
            isShared = mIntent.getBoolean("isShared");
            mCatalogueDetails = (Data) mIntent.getSerializable("mCatalogueDetails");
        }
        if (isShared)
            mBinding.rvShades.setLayoutManager(new GridLayoutManager(mActivity, 3));
        else
            mBinding.rvShades.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));

        mBinding.rvShades.setNestedScrollingEnabled(false);
        mBinding.txtCatalogueName.setText(mCatalogueDetails.getCatalogueName());
        mBinding.txtCatalogueNumber.setText(mCatalogueDetails.getCatelogueNo());
        mBinding.txtProductName.setText(mCatalogueDetails.getProductName());
        mBinding.txtFabricName.setText(mCatalogueDetails.getFabricName());
        mBinding.txtVariantName.setText(mCatalogueDetails.getSubFabricName());
        mBinding.txtManufacturerName.setText(mCatalogueDetails.getManufacturerName());
        mBinding.txtManufacturerName.setSelected(true);

        mBinding.txtTitle.setSelected(true);
        if (isShared) {
            mBinding.txtTitle.setText(getString(R.string.catalogue_details_share));
            mBinding.viewPrices.setVisibility(View.GONE);
            mBinding.layoutPrices.setVisibility(View.GONE);
            mBinding.layoutBucketAll.setVisibility(View.GONE);
            mBinding.btnOrderView.setVisibility(View.GONE);
            mBinding.layoutShare.setVisibility(View.VISIBLE);
//            mBinding.btnOrderView.setText(getString(R.string.share_order));
        } else {
            mBinding.txtTitle.setText(getString(R.string.catalogue_details));
            mBinding.viewPrices.setVisibility(View.VISIBLE);
            mBinding.layoutPrices.setVisibility(View.VISIBLE);
            mBinding.layoutBucketAll.setVisibility(View.VISIBLE);
            mBinding.btnOrderView.setVisibility(View.VISIBLE);
            mBinding.layoutShare.setVisibility(View.GONE);
//            mBinding.btnOrderView.setText(getString(R.string.order_view));
            mBinding.txtPrices.setText(String.format(getString(R.string.rupees), mCatalogueDetails.getSalePrice()));
        }

        mAdapter = new ShadesSelectionAdapter(mActivity, mCatalogueDetails.getShadeDetail(), isShared, mShadeDetail -> ShowPreviewShades(mShadeDetail));
        mBinding.rvShades.setAdapter(mAdapter);

        Glide.with(mActivity)
                .load(mCatalogueDetails.getCatalogueImage())
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


        mBinding.imgMinus.setOnClickListener(v -> {
            int current = Integer.parseInt(String.valueOf(mBinding.edQty.getText()));
            if (current > 0)
                current--;
            mBinding.edQty.setText(String.valueOf(current));
            mAdapter.SetQty(current);
        });

        mBinding.imgPlus.setOnClickListener(v -> {
            int current = Integer.parseInt(String.valueOf(mBinding.edQty.getText()));
            current++;
            mBinding.edQty.setText(String.valueOf(current));
            mAdapter.SetQty(current);
        });

        mBinding.edQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                Log.w("QtyChanged", "==> " + str);

                if (!str.isEmpty())
                    mAdapter.SetQty(Integer.parseInt(s.toString()));
            }
        });
    }

    public void clickBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void CheckAll(View view) {
        if (mAdapter != null)
            mAdapter.CheckAll();
    }

    public void UncheckAll(View view) {
        if (mAdapter != null)
            mAdapter.UncheckAll();
    }

    public void OrderView(View view) {
        if (mAdapter != null) {
            ArrayList<ShadeDetail> mList = new ArrayList<>();

            ArrayList<ShadeDetail> mTempList = mAdapter.GetData();
            for (int i = 0; i < mTempList.size(); i++) {
                ShadesSelectionAdapter.ViewHolder holder = (ShadesSelectionAdapter.ViewHolder) mBinding.rvShades.getChildViewHolder(mBinding.rvShades.getChildAt(i));
                String str = String.valueOf(holder.edQty.getText());
                if (!str.isEmpty() && !str.equalsIgnoreCase("0")) {
                    ShadeDetail mDtail = mTempList.get(i);
                    mDtail.setTotalQty(Integer.parseInt(str));
                    mList.add(mDtail);
                }
            }

            /*for (ShadeDetail mDetail : mAdapter.GetData()) {
                if (mDetail.getTotalQty() > 0) {
                    mList.add(mDetail);
                }
            }*/

            if (mList.size() == 0) {
                Toast.makeText(CatalogueDetailsActivity.this, "Please Add Qty", Toast.LENGTH_LONG).show();
                return;
            }

            ConfirmOrderActivity.mListData = mList;
            ConfirmOrderActivity.mCatalogueId = mCatalogueDetails.getCatalogueId();
            ConfirmOrderActivity.m_id = mCatalogueDetails.getManufacturerId();
            Intent i = new Intent(CatalogueDetailsActivity.this, ConfirmOrderActivity.class);
            startActivityForResult(i, 269);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 269) {
            Log.w("onActivityResult", "Result Okay...");
            CatalogueDetailsActivity.this.finish();
        } else
            Log.w("onActivityResult", "Result Cancel...");
    }

    private void GetShareLink(String mAction) {
        if (NetworkUtils.isConnected(mActivity)) {
            mLoading = ShowLoading(mActivity);

            Call<ResponseBody> call = Client.getInstance().getApi().ShareLink("Bearer " + PowerPreference.getDefaultFile().getString(TOKEN),
                    mCatalogueDetails.getCatalogueId(), mCatalogueDetails.getManufacturerId());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            String res = response.body().string();
                            Log.e("TAG", "PlaceOrder Response =>" + res);

                            JSONObject mObj = new JSONObject(res);
                            mObj = mObj.getJSONObject("data");
                            mShareLink = GetShareURL(mObj.getString("open_url"));
                            if (mAction.equalsIgnoreCase(WHATSAPP))
                                ShareLink("WhatsApp", WHATSAPP);
                            else if (mAction.equalsIgnoreCase(WHATSAPP_BUS))
                                ShareLink("WhatsApp Business", WHATSAPP_BUS);
                            else if (mAction.equalsIgnoreCase(TELEGRAM))
                                ShareLink("Telegram", TELEGRAM);
                            else if (mAction.equalsIgnoreCase(MESSENGER))
                                ShareLink("Facebook Messenger", MESSENGER);
                            else if (mAction.equalsIgnoreCase(COPY))
                                ShareLink(COPY, COPY);
                        } else if (response.code() == 500) {
                            ShowAlert(mActivity, null, "Internal Server Error", false);
                        } else {
                            try {
                                String str = response.errorBody().string();
                                Log.w("Error", "Error:" + str);
                                JSONObject mObj = new JSONObject(str);
                                mObj = mObj.getJSONObject("data");
                                Iterator mKeyList = mObj.keys();
                                String key = (String) mKeyList.next();
                                ShowAlert(mActivity, null, key + " : " + mObj.getString(key), false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        HideLoading(mLoading);
                        e.printStackTrace();
                    }
                    HideLoading(mLoading);
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    HideLoading(mLoading);
                }
            });
        }
    }

    private void ShareLink(String name, String packageName) {
        if (mShareLink != null) {
            if (name.equalsIgnoreCase(COPY)) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Catalogue Link", mShareLink);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(CatalogueDetailsActivity.this, "Copied!", Toast.LENGTH_LONG).show();
                return;
            }
            PackageManager pm = getPackageManager();
            boolean isInstalled = isPackageInstalled(packageName, pm);
            if (isInstalled) {
                Intent shareIntent = new Intent();
                shareIntent.setAction("android.intent.action.SEND");
                shareIntent.setType("text/plain");
                if (packageName != null) shareIntent.setPackage(packageName);
                shareIntent.putExtra("android.intent.extra.SUBJECT", getString(R.string.app_name));
                shareIntent.putExtra("android.intent.extra.TEXT", mShareLink);
                startActivity(Intent.createChooser(shareIntent, "Share Application"));
            } else
                Toast.makeText(CatalogueDetailsActivity.this, name + " is not Installed.", Toast.LENGTH_LONG).show();
        } else
            GetShareLink(packageName);
    }

    public void shareWhatsapp(View view) {
        PackageManager pm = getPackageManager();
        boolean isInstalled = isPackageInstalled(WHATSAPP_BUS, pm);
        if (isInstalled) {
            Dialog mWhatsappDialog = new Dialog(mActivity);
            mWhatsappDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            DialogWhatsappBinding mWhatsappBinding = DialogWhatsappBinding.inflate(mActivity.getLayoutInflater());
            mWhatsappDialog.setContentView(mWhatsappBinding.getRoot());
            Objects.requireNonNull(mWhatsappDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
            mWhatsappDialog.setCancelable(true);
            mWhatsappDialog.setCanceledOnTouchOutside(true);
            mWhatsappDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mWhatsappDialog.show();

            mWhatsappBinding.layoutNormal.setOnClickListener(v -> {
                mWhatsappDialog.dismiss();
                ShareLink("WhatsApp", WHATSAPP);
            });


            mWhatsappBinding.layoutBusiness.setOnClickListener(v -> {
                mWhatsappDialog.dismiss();
                ShareLink("WhatsApp Business", WHATSAPP_BUS);
            });
        } else {
            ShareLink("WhatsApp", WHATSAPP);
        }
    }

    public void shareTelegram(View view) {
        ShareLink("Telegram", TELEGRAM);
    }

    public void shareMessanger(View view) {
        ShareLink("Facebook Messenger", MESSENGER);
    }

    public void shareCopy(View view) {
        ShareLink(COPY, COPY);
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        boolean found = true;
        try {
            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            found = false;
            e.printStackTrace();
        }
        return found;
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