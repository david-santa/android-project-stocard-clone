package com.example.proiectechiparacheta.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.proiectechiparacheta.R;

public class Popup_Barcode extends DialogFragment {
    public Bitmap bitmap;
    public Popup_Barcode(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_barcode,container,false);
        ImageView ivBarcode = view.findViewById(R.id.ivBarcode);
        ivBarcode.setImageBitmap(bitmap);
        return view;
    }
}
