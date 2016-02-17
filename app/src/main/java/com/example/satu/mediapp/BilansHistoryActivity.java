package com.example.satu.mediapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.satu.mediapp.Data.Measure;
import com.example.satu.mediapp.Data.MeasureAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BilansHistoryActivity extends AppCompatActivity {

    @Bind(R.id.listView_bilans_hitory)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilans_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Historia wynikow");
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();

        ArrayList<Measure> measures;
        measures = (ArrayList<Measure>) extras.getSerializable(BilansActivity.LIST_EXTRA);
        MeasureAdapter measureAdapter = new MeasureAdapter(getApplicationContext(),R.layout.item_measure);

        for (Measure measure: measures)
            measureAdapter.add(measure);

        listView.setAdapter(measureAdapter);

    }

}
