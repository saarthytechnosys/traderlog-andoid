// Generated by view binder compiler. Do not edit!
package com.bibsindia.bibstraderpanel.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import com.bibsindia.bibstraderpanel.R;
import com.bibsindia.bibstraderpanel.utils.SquareImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemShadesBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final SquareImageView imgShades;

  @NonNull
  public final CardView rootLayout;

  @NonNull
  public final FrameLayout rootShades;

  @NonNull
  public final AppCompatTextView txtShadesName;

  @NonNull
  public final AppCompatTextView txtShadesNo;

  @NonNull
  public final AppCompatTextView txtTotalQty;

  private ItemShadesBinding(@NonNull LinearLayout rootView, @NonNull SquareImageView imgShades,
      @NonNull CardView rootLayout, @NonNull FrameLayout rootShades,
      @NonNull AppCompatTextView txtShadesName, @NonNull AppCompatTextView txtShadesNo,
      @NonNull AppCompatTextView txtTotalQty) {
    this.rootView = rootView;
    this.imgShades = imgShades;
    this.rootLayout = rootLayout;
    this.rootShades = rootShades;
    this.txtShadesName = txtShadesName;
    this.txtShadesNo = txtShadesNo;
    this.txtTotalQty = txtTotalQty;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemShadesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemShadesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_shades, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemShadesBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imgShades;
      SquareImageView imgShades = rootView.findViewById(id);
      if (imgShades == null) {
        break missingId;
      }

      id = R.id.rootLayout;
      CardView rootLayout = rootView.findViewById(id);
      if (rootLayout == null) {
        break missingId;
      }

      id = R.id.rootShades;
      FrameLayout rootShades = rootView.findViewById(id);
      if (rootShades == null) {
        break missingId;
      }

      id = R.id.txtShadesName;
      AppCompatTextView txtShadesName = rootView.findViewById(id);
      if (txtShadesName == null) {
        break missingId;
      }

      id = R.id.txtShadesNo;
      AppCompatTextView txtShadesNo = rootView.findViewById(id);
      if (txtShadesNo == null) {
        break missingId;
      }

      id = R.id.txtTotalQty;
      AppCompatTextView txtTotalQty = rootView.findViewById(id);
      if (txtTotalQty == null) {
        break missingId;
      }

      return new ItemShadesBinding((LinearLayout) rootView, imgShades, rootLayout, rootShades,
          txtShadesName, txtShadesNo, txtTotalQty);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}