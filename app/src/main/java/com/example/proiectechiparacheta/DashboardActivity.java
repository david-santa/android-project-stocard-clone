package com.example.proiectechiparacheta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ListView listView = (ListView) findViewById(R.id.customListView);

        ArrayList<FidelityCard> arrayList = new ArrayList<FidelityCard>();
        arrayList.add(new FidelityCard(1,"IKEA","David Santa","123123123"));
        arrayList.add(new FidelityCard(2,"MORTIMEI","David Santa","32132131231"));
        arrayList.add(new FidelityCard(3,"TEIMORTI","David Santa","69696969"));

        CustomAdapter adapter = new CustomAdapter(this, arrayList);
        listView.setAdapter(adapter);
    }
}