package com.example.satu.mediapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;

import com.example.satu.mediapp.Data.DataLoader;
import com.example.satu.mediapp.Data.Measure;
import com.example.satu.mediapp.Interfaces.OnClearHistory;
import com.example.satu.mediapp.Interfaces.onMeasurePut;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BilansActivity extends AppCompatActivity implements onMeasurePut, OnClearHistory {

    public static final String LIST_EXTRA = "list";
    public static final String BILANS_SCHEDULE = "BILANS_SCHEDULE_SET";
    @Bind(R.id.button_moring)
    Button button_ones;

    @Bind(R.id.button_midday)
    Button button_twice;

    @Bind(R.id.button_evening)
    Button button_thrice;

    @Bind(R.id.button_put_result)
    Button button_put_result;

    @Bind(R.id.button_meausure_history)
    Button button_put_meausure_histor;

    @Bind(R.id.button_clear_history)
    Button button_clear_history;

    private ArrayList<Measure> measures;
    private DataLoader dataLoader;
    private Set<String> measureReminder ;
    private AlarmManagerBroadcastReceiver alarmManagerBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilans);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Bilans cisnienia");
        setSupportActionBar(toolbar);

        alarmManagerBroadcastReceiver = new AlarmManagerBroadcastReceiver();
        dataLoader = new DataLoader(getApplicationContext());
        dataLoader.loadFile(getApplicationContext(),DataLoader.MEASURES_DATA);
        measures = dataLoader.getMeasures();
        if (measures == null)
            measures= new ArrayList<Measure>();

        // odzyskiwanie ustawień harmonogramu bilansu
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        measureReminder = new HashSet<String>(prefs.getStringSet(BILANS_SCHEDULE, new HashSet<String>()));

        if (measureReminder.contains("1"))
            button_ones.setBackground(getResources().getDrawable(R.drawable.buttonshape1_selected));
        if (measureReminder.contains("2"))
            button_twice.setBackground((getResources().getDrawable(R.drawable.buttonshape1_selected)));
        if (measureReminder.contains("3"))
            button_thrice.setBackground((getResources().getDrawable(R.drawable.buttonshape1_selected)));
    }

    @OnClick(R.id.button_moring)
    public void onClicked_ones() {
        if (!measureReminder.contains("1")){
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Long timePrefA_key = defaultSharedPreferences.getLong("timePrefA_Key", 0);
            Date date = new Date();
            date.setTime(timePrefA_key);

            alarmManagerBroadcastReceiver.setRepeatedNotification(1,date.getHours(),date.getMinutes(),00,getApplicationContext(),null);
            measureReminder.add("1");
            button_ones.setBackground((getResources().getDrawable(R.drawable.buttonshape1_selected)));

        }else {
            button_ones.setBackground((getResources().getDrawable(R.drawable.buttonshape1)));
            measureReminder.remove("1");
            alarmManagerBroadcastReceiver.CancelOneAlarm(getApplicationContext(),1);
        }

    }
    @OnClick(R.id.button_midday)
    public void onClicked_twice(){
        if (!measureReminder.contains("2")){
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Long timePrefA_key = defaultSharedPreferences.getLong("timePrefB_Key", 0);
            Date date = new Date();
            date.setTime(timePrefA_key);

            alarmManagerBroadcastReceiver.setRepeatedNotification(2,date.getHours(),date.getMinutes(),00,getApplicationContext(),null);
            measureReminder.add("2");
            button_twice.setBackground((getResources().getDrawable(R.drawable.buttonshape1_selected)));
          }else {
            button_twice.setBackground((getResources().getDrawable(R.drawable.buttonshape1)));
            measureReminder.remove("2");
            alarmManagerBroadcastReceiver.CancelOneAlarm(getApplicationContext(),2);
        }

    }
    @OnClick(R.id.button_evening)
    public void onClicked_thrice(){
        if (!measureReminder.contains("3")){
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            Long timePrefA_key = defaultSharedPreferences.getLong("timePrefC_Key", 0);
            Date date = new Date();
            date.setTime(timePrefA_key);

            alarmManagerBroadcastReceiver.setRepeatedNotification(3,date.getHours(),date.getMinutes(),00,getApplicationContext(),null);
            measureReminder.add("3");
            button_thrice.setBackground((getResources().getDrawable(R.drawable.buttonshape1_selected)));
        }else {
            button_thrice.setBackground((getResources().getDrawable(R.drawable.buttonshape1)));
            measureReminder.remove("3");
            alarmManagerBroadcastReceiver.CancelOneAlarm(getApplicationContext(),3);
        }

    }
    @OnClick(R.id.button_put_result)
    public void onClicked_put_result(){
        PutMeasureDialog tmp = new PutMeasureDialog();
        FragmentManager fm = getFragmentManager();
        tmp.show(fm, "test");
    }
    @OnClick(R.id.button_meausure_history)
    public void onClicked_meausure_history(){
        Intent intent = new Intent(getApplicationContext(),BilansHistoryActivity.class);
        intent.putExtra(LIST_EXTRA,measures);
        startActivity(intent);
    }
    @OnClick(R.id.button_clear_history)
    public void onClicked_clear_history(){
        ClearHistoryDialog tmp = new ClearHistoryDialog();
        FragmentManager fm = getFragmentManager();
        tmp.show(fm, "test");
    }

    @Override
    public void onMeasurePut(Measure measure) {
        Calendar c = Calendar.getInstance();
        Date time = c.getTime();
        measure.setTime(time);
        measures.add(measure);
    }

    @Override
    public void onClearHistory(boolean decision) {
        if (decision)
            measures.clear();
    }

    @Override
    protected void onDestroy() {
        if (measures.size() != 0) {
            dataLoader.setMeasures(measures);
            dataLoader.saveFile(DataLoader.FILE_NAME_MEASURES);
        }
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putStringSet(BILANS_SCHEDULE,measureReminder);
        editor.apply();
        Log.v("BilansAvtivity", "PO zapisaniu do SP");
        super.onDestroy();
    }
}
