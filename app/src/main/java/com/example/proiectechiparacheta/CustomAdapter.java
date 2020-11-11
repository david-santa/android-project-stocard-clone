package com.example.proiectechiparacheta;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<FidelityCard> arr;

    public CustomAdapter(Context context, ArrayList<FidelityCard> arr) {
        this.context = context;
        this.arr = arr;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.rowdesign,parent,false);
        TextView tvId = convertView.findViewById(R.id.idCard);
        TextView tvName = convertView.findViewById(R.id.cardName);
        TextView tvHolder = convertView.findViewById(R.id.cardHolderName);
        TextView tvBarCode = convertView.findViewById(R.id.barCode);

        tvId.setText(String.valueOf(arr.get(position).getId()));
        tvName.setText(arr.get(position).getName());
        tvHolder.setText(arr.get(position).getCardHolderName());
        tvBarCode.setText(arr.get(position).getBarCode());
        return convertView;
    }
}
