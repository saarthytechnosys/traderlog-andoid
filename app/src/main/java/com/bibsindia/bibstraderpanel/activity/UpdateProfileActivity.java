package com.bibsindia.bibstraderpanel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.adapter.StateAdapter;
import com.bibsindia.bibstraderpanel.api.Client;
import com.bibsindia.bibstraderpanel.databinding.ActivityUpdateProfileBinding;
import com.bibsindia.bibstraderpanel.model.profile.Profile;
import com.bibsindia.bibstraderpanel.model.state.list.Data;
import com.bibsindia.bibstraderpanel.model.state.list.State;
import com.bibsindia.bibstraderpanel.utils.NetworkUtils;
import com.bibsindia.bibstraderpanel.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.preference.PowerPreference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bibsindia.bibstraderpanel.TraderPanel.ID;
import static com.bibsindia.bibstraderpanel.TraderPanel.NAME;
import static com.bibsindia.bibstraderpanel.TraderPanel.TOKEN;
import static com.bibsindia.bibstraderpanel.utils.Utils.HideLoading;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowAlert;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowLoading;

public class UpdateProfileActivity extends AppCompatActivity {
    private ActivityUpdateProfileBinding mBinding;
    private KProgressHUD mLoading;
    private ArrayList<com.bibsindia.bibstraderpanel.model.state.list.Data> mStateList = new ArrayList<>();
    private int mStateId = -1;
    private int mStatePos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        if (NetworkUtils.isConnected(UpdateProfileActivity.this)) {
            mLoading = ShowLoading(UpdateProfileActivity.this);
            getProfile();
        }
    }

    public boolean isValidate() {
        boolean valid = true;
        View focusView = null;

        String agentName = String.valueOf(mBinding.edAgentName.getText()).trim();
        String partyName = String.valueOf(mBinding.edPartyName.getText()).trim();
        String GSTno = String.valueOf(mBinding.edGSTNo.getText()).trim();
        String mobileNo = String.valueOf(mBinding.edMobileNo.getText()).trim();

        if (agentName.isEmpty()) {
            Toast.makeText(UpdateProfileActivity.this, getString(R.string.valid_agent), Toast.LENGTH_SHORT).show();
            valid = false;
            focusView = mBinding.edAgentName;
        }

        if (valid && partyName.isEmpty()) {
            Toast.makeText(UpdateProfileActivity.this, getString(R.string.valid_party), Toast.LENGTH_SHORT).show();
            valid = false;
            focusView = mBinding.edPartyName;
        }

        if (valid && GSTno.isEmpty()) {
            Toast.makeText(UpdateProfileActivity.this, getString(R.string.valid_gst), Toast.LENGTH_SHORT).show();
            valid = false;
            focusView = mBinding.edGSTNo;
        }

        if (valid && GSTno.length() != 15) {
            Toast.makeText(UpdateProfileActivity.this, getString(R.string.valid_gst_15), Toast.LENGTH_SHORT).show();
            valid = false;
            focusView = mBinding.edGSTNo;
        }

        if (valid && mobileNo.length() != 10) {
            Toast.makeText(UpdateProfileActivity.this, getString(R.string.valid_mobile), Toast.LENGTH_SHORT).show();
            valid = false;
            focusView = mBinding.edMobileNo;
        }

        if (!valid)
            focusView.requestFocus();
        return valid;
    }

    private void getProfile() {
        String token = "Bearer " + PowerPreference.getDefaultFile().getString(TOKEN);
        Call<ResponseBody> call = Client.getInstance().getApi().GetProfileDetails(token, PowerPreference.getDefaultFile().getInt(ID));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                HideLoading(mLoading);
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Gson gson = new GsonBuilder().create();
                        Profile mProfile = gson.fromJson(res, Profile.class);
                        if (mProfile.getSuccess()) {

                            mBinding.edAgentName.setText(mProfile.getData().getAgentName());
                            mBinding.edPartyName.setText(mProfile.getData().getPartyName());
                            mBinding.edGSTNo.setText(mProfile.getData().getGstNo());
                            mBinding.edMobileNo.setText(mProfile.getData().getMobileNo());
                            mBinding.edEmail.setText(mProfile.getData().getEmail());
                            mBinding.edCity.setText(mProfile.getData().getCity());
                            mStateId = Integer.parseInt(mProfile.getData().getState());
                            GetStateData();
                        }
                    } else if (response.code() == 500) {
                        ShowAlert(UpdateProfileActivity.this, null, "Internal Server Error", false);
                    } else {
                        try {
                            String str = response.errorBody().string();
                            Log.w("Error", "Error:" + str);
                            JSONObject mObj = new JSONObject(str);
                            mObj = mObj.getJSONObject("data");
                            Iterator mKeyList = mObj.keys();
                            String key = (String) mKeyList.next();
                            ShowAlert(UpdateProfileActivity.this, null, mObj.getString(key), false);
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

    private void GetStateData() {
        Call<ResponseBody> call = Client.getInstance().getApi().GetState();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
                        Log.e("TAG", "GetProductData Response =>" + res);

                        Gson gson = new GsonBuilder().create();
                        State mState = gson.fromJson(res, State.class);

                        mStateList.clear();
                        com.bibsindia.bibstraderpanel.model.state.list.Data state = new com.bibsindia.bibstraderpanel.model.state.list.Data();
                        state.setId(-1);
                        state.setState_name("Select State");
                        state.setState_code("Select State");
                        mStateList.add(state);

                        if (mState.getSuccess()) {
                            mStateList.addAll(mState.getData());
                            FillCountry();
                        }
                    } else if (response.code() == 500) {
                        ShowAlert(UpdateProfileActivity.this, null, "Internal Server Error", false);
                    } else if (response.code() == 401) {
                        try {
                            String str = response.errorBody().string();
                            Log.w("Error", "Error:" + str);
                            JSONObject mObj = new JSONObject(str);
                            mObj = mObj.getJSONObject("data");
                            Iterator mKeyList = mObj.keys();
                            String key = (String) mKeyList.next();
                            Utils.ShowAlertLogout(UpdateProfileActivity.this, null, mObj.getString(key));
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
                            ShowAlert(UpdateProfileActivity.this, null, mObj.getString(key), false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
            }
        });
    }

    public void clickBack(View view) {
        onBackPressed();
    }

    private void getCountry(int id) {
        for (int i = 0; i < mStateList.size(); i++) {
            Data mData = mStateList.get(i);
            if (mData.getId() == id) {
                mStatePos = i;
                break;
            }
        }
    }

    private void FillCountry() {
        getCountry(mStateId);
        StateAdapter mStateAdapter = new StateAdapter(UpdateProfileActivity.this, mStateList);
        mBinding.spinnerState.setAdapter(mStateAdapter);
        mBinding.spinnerState.setSelection(mStatePos);
        mBinding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.w("DataList", "State Position: " + position + "\t\t" + mStateList.get(position).getState_name() + "\t\tid : " + id);
                mStatePos = position;
                if (position == 0)
                    mStateId = -1;
                else
                    mStateId = mStateList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void updateProfile() {
        if (isValidate()) {
            if (NetworkUtils.isConnected(UpdateProfileActivity.this)) {
                mLoading = ShowLoading(UpdateProfileActivity.this);

                String token = "Bearer " + PowerPreference.getDefaultFile().getString(TOKEN);
                Call<ResponseBody> call = Client.getInstance().getApi().UpdateProfileDetails(
                        token,
                        PowerPreference.getDefaultFile().getInt(ID),
                        String.valueOf(mBinding.edAgentName.getText()).trim(),
                        String.valueOf(mBinding.edPartyName.getText()).trim(),
                        String.valueOf(mBinding.edGSTNo.getText()).trim(),
                        String.valueOf(mBinding.edMobileNo.getText()).trim(),
                        String.valueOf(mBinding.edEmail.getText()).trim(),
                        mStateId,
                        String.valueOf(mBinding.edCity.getText()).trim(),
                        "PATCH"
                );

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        HideLoading(mLoading);
                        try {
                            if (response.isSuccessful()) {
                                String res = response.body().string();
                                JSONObject mObj = new JSONObject(res);
                                if (mObj.getBoolean("success")) {
                                    Toast.makeText(UpdateProfileActivity.this, "Successfully Updated..", Toast.LENGTH_SHORT).show();

                                    PowerPreference.getDefaultFile().setString(NAME, String.valueOf(mBinding.edPartyName.getText()).trim());

                                    Intent i = new Intent(UpdateProfileActivity.this, HomeScreenActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                }

                            } else if (response.code() == 500) {
                                ShowAlert(UpdateProfileActivity.this, null, "Internal Server Error", false);
                            } else {
                                try {
                                    String str = response.errorBody().string();
                                    Log.w("Error", "Error:" + str);
                                    JSONObject mObj = new JSONObject(str);
                                    mObj = mObj.getJSONObject("data");
                                    Iterator mKeyList = mObj.keys();
                                    String key = (String) mKeyList.next();
                                    ShowAlert(UpdateProfileActivity.this, null, mObj.getString(key), false);
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
        }
    }

    public void clickUpdate(View view) {
        updateProfile();
    }
}