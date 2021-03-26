// Generated by view binder compiler. Do not edit!
package com.bibsindia.bibstraderpanel.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewbinding.ViewBinding;
import com.bibsindia.bibstraderpanel.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogOrderFilterBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final AppCompatButton btnClear;

  @NonNull
  public final AppCompatButton btnFind;

  @NonNull
  public final AppCompatEditText edOrderNumber;

  @NonNull
  public final AppCompatSpinner spinnerManufacturerId;

  @NonNull
  public final AppCompatTextView txtFromData;

  @NonNull
  public final AppCompatTextView txtToData;

  private DialogOrderFilterBinding(@NonNull LinearLayout rootView,
      @NonNull AppCompatButton btnClear, @NonNull AppCompatButton btnFind,
      @NonNull AppCompatEditText edOrderNumber, @NonNull AppCompatSpinner spinnerManufacturerId,
      @NonNull AppCompatTextView txtFromData, @NonNull AppCompatTextView txtToData) {
    this.rootView = rootView;
    this.btnClear = btnClear;
    this.btnFind = btnFind;
    this.edOrderNumber = edOrderNumber;
    this.spinnerManufacturerId = spinnerManufacturerId;
    this.txtFromData = txtFromData;
    this.txtToData = txtToData;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogOrderFilterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogOrderFilterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_order_filter, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogOrderFilterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnClear;
      AppCompatButton btnClear = rootView.findViewById(id);
      if (btnClear == null) {
        break missingId;
      }

      id = R.id.btnFind;
      AppCompatButton btnFind = rootView.findViewById(id);
      if (btnFind == null) {
        break missingId;
      }

      id = R.id.edOrderNumber;
      AppCompatEditText edOrderNumber = rootView.findViewById(id);
      if (edOrderNumber == null) {
        break missingId;
      }

      id = R.id.spinnerManufacturerId;
      AppCompatSpinner spinnerManufacturerId = rootView.findViewById(id);
      if (spinnerManufacturerId == null) {
        break missingId;
      }

      id = R.id.txtFromData;
      AppCompatTextView txtFromData = rootView.findViewById(id);
      if (txtFromData == null) {
        break missingId;
      }

      id = R.id.txtToData;
      AppCompatTextView txtToData = rootView.findViewById(id);
      if (txtToData == null) {
        break missingId;
      }

      return new DialogOrderFilterBinding((LinearLayout) rootView, btnClear, btnFind, edOrderNumber,
          spinnerManufacturerId, txtFromData, txtToData);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
