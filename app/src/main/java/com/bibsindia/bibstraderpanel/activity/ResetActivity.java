package com.bibsindia.bibstraderpanel.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.api.Client;
import com.bibsindia.bibstraderpanel.databinding.ActivityResetBinding;
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

public class ResetActivity extends AppCompatActivity {
    private ActivityResetBinding mBinding;
    private KProgressHUD mLoading;
    private String mobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityResetBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mobileNo = getIntent().getStringExtra("mobileNO");
    }

    public boolean isValidate() {
        boolean valid = true;
        View focusView = null;

        String newPass = mBinding.edNewPassword.getText().toString();
        String confirmPass = mBinding.edConfirmPassword.getText().toString();

        if (newPass.isEmpty()) {
            Toast.makeText(ResetActivity.this, getString(R.string.valid_new_password), Toast.LENGTH_SHORT).show();
            valid = false;
            focusView = mBinding.edNewPassword;
        }

        if (valid && confirmPass.isEmpty()) {
            Toast.makeText(ResetActivity.this, getString(R.string.valid_confirm_password), Toast.LENGTH_SHORT).show();
            valid = false;
            focusView = mBinding.edConfirmPassword;
        }

        if (valid && !confirmPass.equals(newPass)) {
            Toast.makeText(ResetActivity.this, getString(R.string.valid_mismatch_password), Toast.LENGTH_SHORT).show();
            valid = false;
            focusView = mBinding.edConfirmPassword;
        }

        if (!valid)
            focusView.requestFocus();

        return valid;
    }

    private void ResetPassword() {
        String newPass = String.valueOf(mBinding.edNewPassword.getText());
        String confirm = String.valueOf(mBinding.edConfirmPassword.getText());
        Call<ResponseBody> call = Client.getInstance().getApi().Reset(newPass, confirm, mobileNo);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                HideLoading(mLoading);
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.e("TAG", "SendOtp Response =>" + res);
                        Toast.makeText(ResetActivity.this, "Successfully Password Reset", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (response.code() == 500) {
                        ShowAlert(ResetActivity.this, null,"Internal Server Error",  false);
                    } else if (response.code() == 401) {
                        try {
                            String str = response.errorBody().string();
                            Log.w("Error", "Error:" + str);
                            JSONObject mObj = new JSONObject(str);
                            mObj = mObj.getJSONObject("data");
                            Iterator mKeyList = mObj.keys();
                            String key = (String) mKeyList.next();
                            Utils.ShowAlertLogout(ResetActivity.this, null, mObj.getString(key));
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
                            ShowAlert(ResetActivity.this, null, mObj.getString(key), false);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void resetPassword(View view) {
        if (NetworkUtils.isConnected(ResetActivity.this)) {
            if (isValidate()) {
                mLoading = ShowLoading(ResetActivity.this);
                ResetPassword();
            }
        }
    }
}