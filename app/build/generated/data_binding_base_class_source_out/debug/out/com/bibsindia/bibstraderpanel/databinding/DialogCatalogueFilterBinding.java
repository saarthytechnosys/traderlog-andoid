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
import androidx.viewbinding.ViewBinding;
import com.bibsindia.bibstraderpanel.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class DialogCatalogueFilterBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final AppCompatButton btnClear;

  @NonNull
  public final AppCompatButton btnFind;

  @NonNull
  public final AppCompatEditText edCatalogueName;

  @NonNull
  public final AppCompatEditText edCatalogueNumber;

  @NonNull
  public final AppCompatSpinner spinnerFabricId;

  @NonNull
  public final AppCompatSpinner spinnerManufacturerId;

  @NonNull
  public final AppCompatSpinner spinnerProductId;

  @NonNull
  public final AppCompatSpinner spinnerSubFabricId;

  private DialogCatalogueFilterBinding(@NonNull LinearLayout rootView,
      @NonNull AppCompatButton btnClear, @NonNull AppCompatButton btnFind,
      @NonNull AppCompatEditText edCatalogueName, @NonNull AppCompatEditText edCatalogueNumber,
      @NonNull AppCompatSpinner spinnerFabricId, @NonNull AppCompatSpinner spinnerManufacturerId,
      @NonNull AppCompatSpinner spinnerProductId, @NonNull AppCompatSpinner spinnerSubFabricId) {
    this.rootView = rootView;
    this.btnClear = btnClear;
    this.btnFind = btnFind;
    this.edCatalogueName = edCatalogueName;
    this.edCatalogueNumber = edCatalogueNumber;
    this.spinnerFabricId = spinnerFabricId;
    this.spinnerManufacturerId = spinnerManufacturerId;
    this.spinnerProductId = spinnerProductId;
    this.spinnerSubFabricId = spinnerSubFabricId;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static DialogCatalogueFilterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static DialogCatalogueFilterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.dialog_catalogue_filter, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static DialogCatalogueFilterBinding bind(@NonNull View rootView) {
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

      id = R.id.edCatalogueName;
      AppCompatEditText edCatalogueName = rootView.findViewById(id);
      if (edCatalogueName == null) {
        break missingId;
      }

      id = R.id.edCatalogueNumber;
      AppCompatEditText edCatalogueNumber = rootView.findViewById(id);
      if (edCatalogueNumber == null) {
        break missingId;
      }

      id = R.id.spinnerFabricId;
      AppCompatSpinner spinnerFabricId = rootView.findViewById(id);
      if (spinnerFabricId == null) {
        break missingId;
      }

      id = R.id.spinnerManufacturerId;
      AppCompatSpinner spinnerManufacturerId = rootView.findViewById(id);
      if (spinnerManufacturerId == null) {
        break missingId;
      }

      id = R.id.spinnerProductId;
      AppCompatSpinner spinnerProductId = rootView.findViewById(id);
      if (spinnerProductId == null) {
        break missingId;
      }

      id = R.id.spinnerSubFabricId;
      AppCompatSpinner spinnerSubFabricId = rootView.findViewById(id);
      if (spinnerSubFabricId == null) {
        break missingId;
      }

      return new DialogCatalogueFilterBinding((LinearLayout) rootView, btnClear, btnFind,
          edCatalogueName, edCatalogueNumber, spinnerFabricId, spinnerManufacturerId,
          spinnerProductId, spinnerSubFabricId);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}