package com.example.proiectechiparacheta;
import com.example.proiectechiparacheta.R;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<FidelityCard> arr;
    Animation scaleUp;

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
//        ImageView iv1 = convertView.findViewById(R.id.iv1);
//        ImageView iv2 = convertView.findViewById(R.id.iv2);
        ImageView ivFav = convertView.findViewById(R.id.ivFav);

//        ObjectAnimator animation = ObjectAnimator.ofFloat(iv1, "rotationY", 0.0f, 360f);
//        animation.setDuration(10000);
//        animation.setRepeatCount(ObjectAnimator.INFINITE);
//        animation.setInterpolator(new LinearInterpolator());
//
//
//        ObjectAnimator animation2 = ObjectAnimator.ofFloat(iv2, "rotationY", 0.0f, 360f);
//        animation2.setDuration(10000);
//        animation2.setRepeatCount(ObjectAnimator.INFINITE);
//        animation2.setInterpolator(new LinearInterpolator());
//
//        animation.start();
//        animation2.start();

        tvId.setText(String.valueOf(arr.get(position).getId()));
        tvName.setText(arr.get(position).getName());
        tvHolder.setText(arr.get(position).getCardHolderName());
        tvBarCode.setText(arr.get(position).getBarCode());
        if(arr.get(position).isFav){
            ivFav.setImageResource(android.R.drawable.btn_star_big_on);
        }
        else{
            ivFav.setImageResource(android.R.drawable.btn_star_big_off);
        }



        return convertView;
    }
}
