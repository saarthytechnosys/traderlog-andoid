package com.bibsindia.bibstraderpanel.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.api.Client;
import com.bibsindia.bibstraderpanel.databinding.ActivityLoginBinding;
import com.bibsindia.bibstraderpanel.utils.NetworkUtils;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.preference.PowerPreference;

import org.json.JSONObject;

import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bibsindia.bibstraderpanel.TraderPanel.FCM_TOKEN;
import static com.bibsindia.bibstraderpanel.TraderPanel.ID;
import static com.bibsindia.bibstraderpanel.TraderPanel.IS_LOGIN;
import static com.bibsindia.bibstraderpanel.TraderPanel.MOBILE;
import static com.bibsindia.bibstraderpanel.TraderPanel.NAME;
import static com.bibsindia.bibstraderpanel.TraderPanel.PASSWORD;
import static com.bibsindia.bibstraderpanel.TraderPanel.SECRET_KEY;
import static com.bibsindia.bibstraderpanel.TraderPanel.TOKEN;
import static com.bibsindia.bibstraderpanel.utils.Utils.HideLoading;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowAlert;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowLoading;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding mBinding;
    private KProgressHUD mLoading;
    private Dialog mForgotDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        CharSequence sequence = Html.fromHtml(getString(R.string.agree_terms_privacy));
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        mBinding.checkPrivacyPolicy.setText(strBuilder);
        mBinding.checkPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("MyFirebaseMsgService", "** Fetching FCM registration token failed", task.getException());
                    } else {
                        String token = task.getResult();
                        PowerPreference.getDefaultFile().setString(FCM_TOKEN, token);
                        Log.w("MyFirebaseMsgService", "** Fetching FCM registration token : " + token);
                    }
                });

    }


    public void login(View view) {
        if (NetworkUtils.isConnected(LoginActivity.this)) {
            if (isValidate()) {
                mLoading = ShowLoading(LoginActivity.this);
                Login();
            }
        }
    }

    public void forgotPassword(View view) {
        startActivity(new Intent(this, ForgotActivity.class));
    }

    public boolean isValidate() {
        boolean valid = true;
        View focusView = null;

        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        String email = mBinding.edMobileNo.getText().toString();
        String password = mBinding.edPassword.getText().toString();

        if (email.length() != 10) {
            Toast.makeText(LoginActivity.this, getString(R.string.valid_mobile), Toast.LENGTH_SHORT).show();
            valid = false;
            focusView = mBinding.edMobileNo;
        }

        if (valid && password.isEmpty()) {
            Toast.makeText(LoginActivity.this, getString(R.string.valid_password), Toast.LENGTH_SHORT).show();
            valid = false;
            focusView = mBinding.edPassword;
        }

        if (valid && !mBinding.checkPrivacyPolicy.isChecked()) {
            Toast.makeText(LoginActivity.this, getString(R.string.valid_check), Toast.LENGTH_SHORT).show();
            valid = false;
            focusView = mBinding.checkPrivacyPolicy;
        }

        if (!valid)
            focusView.requestFocus();

        return valid;
    }

    private void Login() {
        String mobile = String.valueOf(mBinding.edMobileNo.getText());
        String password = String.valueOf(mBinding.edPassword.getText());
        String tokenFcm = PowerPreference.getDefaultFile().getString(FCM_TOKEN);
        Call<ResponseBody> call = Client.getInstance().getApi().Login(String.valueOf(1), SECRET_KEY, mobile, password, tokenFcm);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                HideLoading(mLoading);
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.e("TAG", "Login Response =>" + res);
                        PowerPreference.getDefaultFile().setBoolean(IS_LOGIN, true);
                        PowerPreference.getDefaultFile().setString(MOBILE, mobile);
                        PowerPreference.getDefaultFile().setString(PASSWORD, password);

                        JSONObject jsonObject = new JSONObject(res);
                        jsonObject = jsonObject.getJSONObject("data");
                        String mPartyName = jsonObject.getString("party_name");
                        PowerPreference.getDefaultFile().setString(NAME, mPartyName);
                        PowerPreference.getDefaultFile().setString(TOKEN, jsonObject.getString("token"));
                        PowerPreference.getDefaultFile().setInt(ID, jsonObject.getInt("id"));

                        if (mPartyName.isEmpty()) {
                            Intent i = new Intent(LoginActivity.this, UpdateProfileActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(LoginActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, HomeScreenActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } else if (response.code() == 500) {
                        ShowAlert(LoginActivity.this, null, "Internal Server Error", false);
                    } else {
                        try {
                            String str = response.errorBody().string();
                            Log.w("Error", "Error:" + str);
                            JSONObject mObj = new JSONObject(str);
                            mObj = mObj.getJSONObject("data");
                            Iterator mKeyList = mObj.keys();
                            String key = (String) mKeyList.next();
                            ShowAlert(LoginActivity.this, null, mObj.getString(key), false);
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

    private void OpenLink(String str) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(str));
       /* CustomTabColorSchemeParams darkParams = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary))
                .setNavigationBarDividerColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary))
                .setSecondaryToolbarColor(ContextCompat.getColor(LoginActivity.this, R.color.color4))
                .setNavigationBarColor(ContextCompat.getColor(LoginActivity.this, R.color.color1))
                .build();
        builder.setDefaultColorSchemeParams(darkParams);*/
    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                OpenLink(span.getURL());
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }
}