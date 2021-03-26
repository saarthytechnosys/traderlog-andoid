package com.bibsindia.bibstraderpanel.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.adapter.DrawerAdapter;
import com.bibsindia.bibstraderpanel.api.Client;
import com.bibsindia.bibstraderpanel.databinding.ActivityHomeScreenBinding;
import com.bibsindia.bibstraderpanel.fragments.CatalogueFragment;
import com.bibsindia.bibstraderpanel.fragments.DashboardFragment;
import com.bibsindia.bibstraderpanel.fragments.OrderHistoryFragment;
import com.bibsindia.bibstraderpanel.model.MenuModel;
import com.bibsindia.bibstraderpanel.model.manufacterer.Data;
import com.bibsindia.bibstraderpanel.model.manufacterer.Manufacturer;
import com.bibsindia.bibstraderpanel.utils.NetworkUtils;
import com.bibsindia.bibstraderpanel.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.preference.PowerPreference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bibsindia.bibstraderpanel.TraderPanel.IS_LOGIN;
import static com.bibsindia.bibstraderpanel.TraderPanel.MOBILE;
import static com.bibsindia.bibstraderpanel.TraderPanel.NAME;
import static com.bibsindia.bibstraderpanel.TraderPanel.PASSWORD;
import static com.bibsindia.bibstraderpanel.TraderPanel.TOKEN;
import static com.bibsindia.bibstraderpanel.utils.Utils.HideLoading;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowAlert;
import static com.bibsindia.bibstraderpanel.utils.Utils.ShowLoading;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected static final String TAG = HomeScreenActivity.class.getSimpleName();
    private ActivityHomeScreenBinding mBinding;
    private boolean doubleBackToExitPressedOnce = false;
    private List<MenuModel> headerList = new ArrayList<>();
    private HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    private DrawerAdapter expandableListAdapter;
    private KProgressHUD mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setSupportActionBar(mBinding.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mBinding.drawerLayout, mBinding.toolbar, R.string.open, R.string.close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_action_name);
        toggle.syncState();

        toggle.setToolbarNavigationClickListener(view -> mBinding.drawerLayout.openDrawer(GravityCompat.START));

        mBinding.navigationView.setNavigationItemSelectedListener(this);

        View hView = mBinding.navigationView.getHeaderView(0);
        AppCompatTextView txtUserName = hView.findViewById(R.id.txtUserName);
        txtUserName.setText(String.format("%s", PowerPreference.getDefaultFile().getString(NAME)));

        mBinding.navigationView.getMenu().getItem(0).setChecked(true);
        OpenFragment(new DashboardFragment(), R.string.dashboard);
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.content_main);
            if (currentFragment instanceof DashboardFragment) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(HomeScreenActivity.this, "Please click BACK again to exit", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            } else {
//                mBinding.expandableDrawer.setSelectedGroup(0);
//                mBinding.expandableDrawer.expandGroup(0);
//                OpenFragment(new DashboardFragment(), R.string.dashboard);

                mBinding.navigationView.getMenu().getItem(0).setChecked(true);
                OpenFragment(new DashboardFragment(), R.string.dashboard);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        mBinding.navigationView.setCheckedItem(id);
        if (id == R.id.navDashboard) {
            OpenFragment(new DashboardFragment(), R.string.dashboard);
        } else if (id == R.id.navCatalogue) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isShared", false);
            CatalogueFragment mCatalogueFragment = new CatalogueFragment();
            mCatalogueFragment.setArguments(bundle);
            OpenFragment(mCatalogueFragment, R.string.catalogue);
        } else if (id == R.id.navCatalogueShare) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isShared", true);
            CatalogueFragment mCatalogueFragment = new CatalogueFragment();
            mCatalogueFragment.setArguments(bundle);
            OpenFragment(mCatalogueFragment, R.string.catalogue_share);
        } else if (id == R.id.navOrderHistory) {
            OpenFragment(new OrderHistoryFragment(), R.string.order_history);
        } else if (id == R.id.navLogout) {
            PowerPreference.getDefaultFile().remove(IS_LOGIN);
            PowerPreference.getDefaultFile().remove(NAME);
            PowerPreference.getDefaultFile().remove(MOBILE);
            PowerPreference.getDefaultFile().remove(PASSWORD);
            startActivity(new Intent(HomeScreenActivity.this, LoginActivity.class));
            finish();
        }
        mBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void OpenFragment(Fragment fragment, int strRes) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment); // give your fragment container id in first parameter
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(strRes));
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_Bucket) {
            //Toast.makeText(HomeScreenActivity.this, "Bucket", Toast.LENGTH_LONG).show();
            Intent i = new Intent(HomeScreenActivity.this, ConfirmOrderActivity.class);
            startActivityForResult(i, 596);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 596) {
            Log.w("onActivityResult", "Result Okay...");
            HomeScreenActivity.this.finish();
        } else
            Log.w("onActivityResult", "Result Cancel...");
    }

    private void getManufacturerList() {
        if (NetworkUtils.isConnected(HomeScreenActivity.this)) {
            mLoading = ShowLoading(HomeScreenActivity.this);

            Call<ResponseBody> call = Client.getInstance().getApi().ManufacturerList("Bearer " + PowerPreference.getDefaultFile().getString(TOKEN));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    HideLoading(mLoading);
                    try {
                        if (response.isSuccessful()) {
                            String res = response.body().string();
                            Log.e("TAG", "getManufacturerList Response =>" + res);

                            Gson gson = new GsonBuilder().create();
                            Manufacturer mManufacturer = gson.fromJson(res, Manufacturer.class);
                            prepareMenuDrawer(mManufacturer.getData());
//                            prepareMenuDrawer(new ArrayList<Data>());
                        } else if (response.code() == 500) {
                            ShowAlert(HomeScreenActivity.this, null, "Internal Server Error", false);
                        } else if (response.code() == 401) {
                            try {
                                String str = response.errorBody().string();
                                Log.w("Error", "Error:" + str);
                                JSONObject mObj = new JSONObject(str);
                                mObj = mObj.getJSONObject("data");
                                Iterator mKeyList = mObj.keys();
                                String key = (String) mKeyList.next();
                                Utils.ShowAlertLogout(HomeScreenActivity.this, null, mObj.getString(key));
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
                                ShowAlert(HomeScreenActivity.this, null, mObj.getString(key), false);
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

    private void prepareMenuDrawer(ArrayList<Data> mManufacturerList) {
       /* MenuModel menuModel = new MenuModel(getString(R.string.dashboard), true, false, R.drawable.ic_baseline_dashboard_24); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel(getString(R.string.catalogue), true, true, R.drawable.ic_baseline_menu_book_24); //Menu of Java Tutorials
        headerList.add(menuModel);
        List<MenuModel> childModelsList = new ArrayList<>();
        if (mManufacturerList.size() != 0) {
            for (int i = 0; i < mManufacturerList.size(); i++) {
                Data mData = mManufacturerList.get(i);
                MenuModel childModel = new MenuModel(mData.getCompany_name(), false, false);
                childModel.setManufacturerId(mData.getId());
                childModelsList.add(childModel);
            }
        } else {
            MenuModel childModel = new MenuModel("NO", false, false);
            childModelsList.add(childModel);
        }
        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


        childModelsList = new ArrayList<>();
        menuModel = new MenuModel(getString(R.string.catalogue_share), true, true, R.drawable.ic_baseline_screen_share_24); //Menu of Python Tutorials
        headerList.add(menuModel);
        if (mManufacturerList.size() != 0) {
            for (int i = 0; i < mManufacturerList.size(); i++) {
                Data mData = mManufacturerList.get(i);
                MenuModel childModel = new MenuModel(mData.getCompany_name(), false, false);
                childModel.setManufacturerId(mData.getId());
                childModelsList.add(childModel);
            }
        } else {
            MenuModel childModel = new MenuModel("NO", false, false);
            childModelsList.add(childModel);
        }
        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }
        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }


        menuModel = new MenuModel(getString(R.string.order_history), true, false, R.drawable.ic_baseline_shopping_basket_24);
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


        menuModel = new MenuModel(getString(R.string.logout), true, false, R.drawable.ic_baseline_exit_to_app_24);
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        expandableListAdapter = new DrawerAdapter(HomeScreenActivity.this, headerList, childList, new DrawerAdapter.onItemClickListener() {
            @Override
            public void onGroupClick(int groupPosition, int groupPositionOld) {
                if (mBinding.expandableDrawer.isGroupExpanded(groupPositionOld))
                    mBinding.expandableDrawer.collapseGroup(groupPositionOld);

                if (groupPosition == 0) {
                    OpenFragment(new DashboardFragment(), R.string.dashboard);
                } else if (groupPosition == 3) {
                    OpenFragment(new OrderHistoryFragment(), R.string.order_history);
                } else if (groupPosition == 4) {
                    PowerPreference.getDefaultFile().remove(IS_LOGIN);
                    PowerPreference.getDefaultFile().remove(NAME);
                    PowerPreference.getDefaultFile().remove(MOBILE);
                    PowerPreference.getDefaultFile().remove(PASSWORD);
                    startActivity(new Intent(HomeScreenActivity.this, LoginActivity.class));
                    finish();
                } else {
                    mBinding.expandableDrawer.expandGroup(groupPosition, true);
                    return;
                }
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            }

            @Override
            public void onChildClick(int groupPosition, int childPosition, View childNew, View childOld, MenuModel menuModel) {
                if (menuModel.menuName.equalsIgnoreCase("NO")) {
                    return;
                }
                if (groupPosition == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isShared", false);
                    bundle.putInt("m_id", menuModel.getManufacturerId());
                    CatalogueFragment mCatalogueFragment = new CatalogueFragment();
                    mCatalogueFragment.setArguments(bundle);
                    OpenFragment(mCatalogueFragment, R.string.catalogue);

                } else if (groupPosition == 2) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isShared", true);
                    bundle.putInt("m_id", menuModel.getManufacturerId());
                    CatalogueFragment mCatalogueFragment = new CatalogueFragment();
                    mCatalogueFragment.setArguments(bundle);
                    OpenFragment(mCatalogueFragment, R.string.catalogue_share);
                }

                if (childNew != childOld) {
                    LinearLayout rootChildNew = childNew.findViewById(R.id.rootChild);
                    rootChildNew.setBackgroundColor(ContextCompat.getColor(HomeScreenActivity.this, R.color.colorPrimaryTrans2));

                    if (childOld != null) {
                        LinearLayout rootChildOld = childOld.findViewById(R.id.rootChild);
                        rootChildOld.setBackgroundColor(ContextCompat.getColor(HomeScreenActivity.this, R.color.colorPrimaryTrans));
                    }
                }
                mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        mBinding.expandableDrawer.setAdapter(expandableListAdapter);
        mBinding.expandableDrawer.setSelectedGroup(0);
        mBinding.expandableDrawer.expandGroup(0);
        OpenFragment(new DashboardFragment(), R.string.dashboard);*/
    }
}