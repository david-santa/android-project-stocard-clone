package com.example.proiectechiparacheta;
import com.example.proiectechiparacheta.R;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;

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
        ImageView imageClick = convertView.findViewById(R.id.imageClick);

        tvId.setText(String.valueOf(arr.get(position).getId()));
        tvName.setText(arr.get(position).getName());
        tvHolder.setText(arr.get(position).getCardHolderName());
        tvBarCode.setText(arr.get(position).getBarCode());

        imageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.edit:
                                Toast.makeText(context,"EDIT", Toast.LENGTH_LONG).show();

                                break;
                            case R.id.delete:
                                Toast.makeText(context,"DELETE", Toast.LENGTH_LONG).show();
                                break;

                            default:
                                break;
                        }
                        return true;
                    }
                });
            }
        });

        return convertView;
    }
}
