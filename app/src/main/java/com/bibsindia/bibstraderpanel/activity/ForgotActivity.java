package com.bibsindia.bibstraderpanel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.api.Client;
import com.bibsindia.bibstraderpanel.databinding.ActivityForgotBinding;
import com.bibsindia.bibstraderpanel.utils.NetworkUtils;
import com.bibsindia.bibstraderpanel.utils.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bibsindia.bibstraderpanel.utils.Utils.HideLoading;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowAlert;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowLoading;

public class ForgotActivity extends AppCompatActivity {
    private ActivityForgotBinding mBinding;
    private KProgressHUD mLoading;
    private boolean isSendOtp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityForgotBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

    }

    public boolean isValidate() {
        boolean valid = true;
        View focusView = null;

        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        String email = mBinding.edMobileNo.getText().toString();

        if (email.length() != 10) {
            Toast.makeText(ForgotActivity.this, getString(R.string.valid_mobile), Toast.LENGTH_SHORT).show();
            valid = false;
            focusView = mBinding.edMobileNo;
        }

        if (!valid)
            focusView.requestFocus();

        return valid;
    }

    private void SendOtp() {
        String mobile = String.valueOf(mBinding.edMobileNo.getText());
        Call<ResponseBody> call = Client.getInstance().getApi().SendOtp(mobile);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                HideLoading(mLoading);
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.e("TAG", "SendOtp Response =>" + res);
                        isSendOtp = false;
                        mBinding.layoutOtp.setVisibility(View.VISIBLE);
                        mBinding.btnSendOtp.setText("Verify");
                        mBinding.edMobileNo.setEnabled(false);
                    } else if (response.code() == 500) {
                        ShowAlert(ForgotActivity.this,null ,"Internal Server Error", false);
                    } else if (response.code() == 401) {
                        try {
                            String str = response.errorBody().string();
                            Log.w("Error", "Error:" + str);
                            JSONObject mObj = new JSONObject(str);
                            mObj = mObj.getJSONObject("data");
                            Iterator mKeyList = mObj.keys();
                            String key = (String) mKeyList.next();
                            Utils.ShowAlertLogout(ForgotActivity.this, null, mObj.getString(key));
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
                            ShowAlert(ForgotActivity.this, null,  mObj.getString(key), false);
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

    private void VerifyOtp() {
        String mobile = String.valueOf(mBinding.edMobileNo.getText());
        String otp = String.valueOf(mBinding.edOTP.getText());
        Call<ResponseBody> call = Client.getInstance().getApi().VerifyOtp(mobile, otp);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                HideLoading(mLoading);
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.e("TAG", "SendOtp Response =>" + res);

                        Intent i = new Intent(ForgotActivity.this, ResetActivity.class);
                        i.putExtra("mobileNO", mobile);
                        startActivity(i);
                        finish();

                    } else if (response.code() == 500) {
                        ShowAlert(ForgotActivity.this,null ,"Internal Server Error",  false);
                    } else if (response.code() == 401) {
                        try {
                            String str = response.errorBody().string();
                            Log.w("Error", "Error:" + str);
                            JSONObject mObj = new JSONObject(str);
                            mObj = mObj.getJSONObject("data");
                            Iterator mKeyList = mObj.keys();
                            String key = (String) mKeyList.next();
                            Utils.ShowAlertLogout(ForgotActivity.this, null, mObj.getString(key));
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
                            ShowAlert(ForgotActivity.this, null,  mObj.getString(key), false);
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

    public void clickBack(View view) {
        onBackPressed();
    }

    public void sendOtp(View view) {
        if (isSendOtp) {
            if (NetworkUtils.isConnected(ForgotActivity.this)) {
                if (isValidate()) {
                    mLoading = ShowLoading(ForgotActivity.this);
                    SendOtp();
                }
            }
        } else {
            if (NetworkUtils.isConnected(ForgotActivity.this)) {
                String otp = mBinding.edOTP.getText().toString();
                if (otp.isEmpty()) {
                    Toast.makeText(ForgotActivity.this, getString(R.string.valid_otp), Toast.LENGTH_SHORT).show();
                    return;
                }
                mLoading = ShowLoading(ForgotActivity.this);
                VerifyOtp();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}