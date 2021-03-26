// Generated by view binder compiler. Do not edit!
package com.bibsindia.bibstraderpanel.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.bibsindia.bibstraderpanel.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentOrderHistoryBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final FloatingActionButton floatingFilter;

  @NonNull
  public final RecyclerView rvOrderHistory;

  @NonNull
  public final AppCompatTextView txtNoData;

  private FragmentOrderHistoryBinding(@NonNull LinearLayout rootView,
      @NonNull FloatingActionButton floatingFilter, @NonNull RecyclerView rvOrderHistory,
      @NonNull AppCompatTextView txtNoData) {
    this.rootView = rootView;
    this.floatingFilter = floatingFilter;
    this.rvOrderHistory = rvOrderHistory;
    this.txtNoData = txtNoData;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentOrderHistoryBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentOrderHistoryBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_order_history, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentOrderHistoryBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.floatingFilter;
      FloatingActionButton floatingFilter = rootView.findViewById(id);
      if (floatingFilter == null) {
        break missingId;
      }

      id = R.id.rvOrderHistory;
      RecyclerView rvOrderHistory = rootView.findViewById(id);
      if (rvOrderHistory == null) {
        break missingId;
      }

      id = R.id.txtNoData;
      AppCompatTextView txtNoData = rootView.findViewById(id);
      if (txtNoData == null) {
        break missingId;
      }

      return new FragmentOrderHistoryBinding((LinearLayout) rootView, floatingFilter,
          rvOrderHistory, txtNoData);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
