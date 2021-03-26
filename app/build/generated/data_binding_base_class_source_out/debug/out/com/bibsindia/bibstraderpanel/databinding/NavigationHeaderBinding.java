// Generated by view binder compiler. Do not edit!
package com.bibsindia.bibstraderpanel.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewbinding.ViewBinding;
import com.bibsindia.bibstraderpanel.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class NavigationHeaderBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final AppCompatImageView imgIcon;

  @NonNull
  public final AppCompatTextView txtUserName;

  private NavigationHeaderBinding(@NonNull LinearLayout rootView,
      @NonNull AppCompatImageView imgIcon, @NonNull AppCompatTextView txtUserName) {
    this.rootView = rootView;
    this.imgIcon = imgIcon;
    this.txtUserName = txtUserName;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static NavigationHeaderBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static NavigationHeaderBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.navigation_header, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static NavigationHeaderBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imgIcon;
      AppCompatImageView imgIcon = rootView.findViewById(id);
      if (imgIcon == null) {
        break missingId;
      }

      id = R.id.txtUserName;
      AppCompatTextView txtUserName = rootView.findViewById(id);
      if (txtUserName == null) {
        break missingId;
      }

      return new NavigationHeaderBinding((LinearLayout) rootView, imgIcon, txtUserName);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
