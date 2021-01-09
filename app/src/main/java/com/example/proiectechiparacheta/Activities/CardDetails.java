package com.example.proiectechiparacheta.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.proiectechiparacheta.R;
import com.example.proiectechiparacheta.models.FidelityCard;

import org.w3c.dom.Text;

public class CardDetails  extends DialogFragment {
    public Bitmap bitmap;
    public FidelityCard card;
    public CardDetails(Bitmap bitmap, FidelityCard card) {
        this.bitmap = bitmap;
        this.card = card;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_details,container,false);
        ImageView ivBarcode = view.findViewById(R.id.ivBarcodeDetails);
        ivBarcode.setImageBitmap(bitmap);
        TextView tvCard = view.findViewById(R.id.tvCardName);
        TextView tvChn = view.findViewById(R.id.tvCardHolderName);
        TextView tvBcv = view.findViewById(R.id.tvBarcodeValue);
        tvCard.setText(card.getName());
        tvChn.setText(getString(R.string.card_holder) +" " +card.getCardHolderName());
        tvBcv.setText(card.getBarCode());
        return view;
    }
}
