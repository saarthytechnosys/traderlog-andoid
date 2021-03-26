package com.bibsindia.bibstraderpanel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bibsindia.bibstraderpanel.api.Client;
import com.bibsindia.bibstraderpanel.databinding.ActivitySplashScreenBinding;
import com.bibsindia.bibstraderpanel.utils.NetworkUtils;
import com.bibsindia.bibstraderpanel.utils.Utils;
import com.google.firebase.messaging.FirebaseMessaging;
import com.preference.PowerPreference;

import org.json.JSONObject;

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

public class SplashScreenActivity extends AppCompatActivity {
    private ActivitySplashScreenBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        boolean isLogin = PowerPreference.getDefaultFile().getBoolean(IS_LOGIN);
        if (isLogin) {
            if (NetworkUtils.isConnected(SplashScreenActivity.this))
                Login();
        } else
            OpenActivity(LoginActivity.class, false);

        Log.w("ShareData", "==>>" + Utils.GetShareURL("https://endpoint.bibsindia.com/api/v1/openurl/ODTST1-TRD705923-pMk3PzTu"));

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

    private void OpenActivity(Class<?> cls, boolean isFromLogin) {
        long time = 3000;
        if (isFromLogin) time = 1000;
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreenActivity.this, cls));
            SplashScreenActivity.this.finish();
        }, time);
    }

    private void Login() {
        String mobile = PowerPreference.getDefaultFile().getString(MOBILE);
        String password = PowerPreference.getDefaultFile().getString(PASSWORD);
        String tokenFcm = PowerPreference.getDefaultFile().getString(FCM_TOKEN);
        Call<ResponseBody> call = Client.getInstance().getApi().Login(String.valueOf(1), SECRET_KEY, mobile, password, tokenFcm);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.e("TAG", "Login Response =>" + res);

                        JSONObject jsonObject = new JSONObject(res);
                        jsonObject = jsonObject.getJSONObject("data");
                        PowerPreference.getDefaultFile().setString(TOKEN, jsonObject.getString("token"));
                        PowerPreference.getDefaultFile().setString(NAME, jsonObject.getString("party_name"));
                        PowerPreference.getDefaultFile().setInt(ID, jsonObject.getInt("id"));

                        OpenActivity(HomeScreenActivity.class, true);
                    } else {
                        PowerPreference.getDefaultFile().remove(IS_LOGIN);
                        PowerPreference.getDefaultFile().remove(NAME);
                        PowerPreference.getDefaultFile().remove(MOBILE);
                        PowerPreference.getDefaultFile().remove(PASSWORD);
                        OpenActivity(LoginActivity.class, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                PowerPreference.getDefaultFile().remove(IS_LOGIN);
                PowerPreference.getDefaultFile().remove(NAME);
                PowerPreference.getDefaultFile().remove(MOBILE);
                PowerPreference.getDefaultFile().remove(PASSWORD);
                OpenActivity(LoginActivity.class, true);
            }
        });
    }
}