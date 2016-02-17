package com.example.satu.mediapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.example.satu.mediapp.Data.DataLoader;
import com.example.satu.mediapp.Data.Medicine;
import com.example.satu.mediapp.Interfaces.OnEditMedicine;
import com.example.satu.mediapp.Interfaces.OnMedicinePut;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MedicineActivity extends AppCompatActivity implements OnMedicinePut, OnEditMedicine {
    public static final String LIST_EXTRA = "LIST";
    public static final int REQUEST_CODE = 1;
    public static final String MY_MEDI_SCHEDULE = "my_medi_schedule";
    @Bind(R.id.button_medicines_history)
    Button button_meausure_history;

    @Bind(R.id.button_put_medicine)
    Button button_put_medicine;

    private ArrayList<Medicine> medicines;
    private DataLoader dataLoader;

    private AlarmManagerBroadcastReceiver alarmManagerBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Twoje leki");
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        alarmManagerBroadcastReceiver = new AlarmManagerBroadcastReceiver();

        dataLoader = new DataLoader(getApplicationContext());
        dataLoader.loadFile(getApplicationContext(),DataLoader.MEDI_DATA);
        medicines = dataLoader.getMedicines();
        if (medicines == null)
            medicines = new ArrayList<Medicine>();

    }

    @OnClick(R.id.button_put_medicine)
    public void onClicked_put_medicine(){
        PutMedicineDialog putMedicineDialog = new PutMedicineDialog();
        android.app.FragmentManager fm = getFragmentManager();
        putMedicineDialog.show(fm, "test");
    }
    @OnClick(R.id.button_medicines_history)
    public void onClicked_medicines_history(){
        Intent intent = new Intent(getApplicationContext(),MedHistoryActivity.class);
        intent.putExtra(LIST_EXTRA,medicines);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onMedicinePut(Medicine medicine) {
        medicine.setIDMed((medicines.size() == 0 ? 0 : medicines.get(medicines.size()-1).getIDMed()+1));
        medicines.add(medicine);
        // ustawienie powiadomienia
        if (medicine.isReminder_evening() == true  || medicine.isReminder_morning() == true || medicine.isReminder_midday() == true)
        {
            if (medicine.isReminder_morning())
            {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Long timePrefA_key = defaultSharedPreferences.getLong("timePrefA_Key", 0);
                Date date = new Date();
                date.setTime(timePrefA_key);
                alarmManagerBroadcastReceiver.setRepeatedNotification(medicine.getIDMed()+10,date.getHours(),date.getMinutes(),00,getApplicationContext(),"Weź lek: "
                        + medicine.getName() + " "+getResources().getString(R.string.dose)+ medicine.getDose());
            }
            if (medicine.isReminder_midday()){
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Long timePrefA_key = defaultSharedPreferences.getLong("timePrefB_Key", 0);
                Date date = new Date();
                date.setTime(timePrefA_key);
                alarmManagerBroadcastReceiver.setRepeatedNotification(medicine.getIDMed()+20,date.getHours(),date.getMinutes(),00,getApplicationContext(),"Weź lek: "
                        + medicine.getName() +" "+getResources().getString(R.string.dose)+ medicine.getDose());
            }
            if (medicine.isReminder_evening())
            {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Long timePrefA_key = defaultSharedPreferences.getLong("timePrefC_Key", 0);
                Date date = new Date();
                date.setTime(timePrefA_key);
                alarmManagerBroadcastReceiver.setRepeatedNotification(medicine.getIDMed()+30,date.getHours(),date.getMinutes(),00,getApplicationContext(),"Weź lek: "
                        + medicine.getName() + " "+getResources().getString(R.string.dose)+ medicine.getDose());
            }
        }

    }

    @Override
    protected void onDestroy() {
        if (medicines.size() != 0) {
            dataLoader.setMedicines(medicines);
            dataLoader.saveFile(DataLoader.FILE_NAME_MEDI);
        }
        super.onDestroy();
    }

    @Override
    public void onEditMedicine(int index, Medicine medicine) {
        medicines.set(index,medicine);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            medicines = (ArrayList<Medicine>) data.getSerializableExtra(MedHistoryActivity.RESULT_EXTRA);
        }
    }
}
