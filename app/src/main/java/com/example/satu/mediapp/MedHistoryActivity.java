package com.example.satu.mediapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.satu.mediapp.Data.Medicine;
import com.example.satu.mediapp.Data.MedicineAdapter;
import com.example.satu.mediapp.Interfaces.OnDeleteMedicine;
import com.example.satu.mediapp.Interfaces.OnEditMedicine;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MedHistoryActivity extends AppCompatActivity implements OnDeleteMedicine, OnEditMedicine {

    public static final String INDEX_EXTRA = "INDEX_EXTRA";
    public static final String MEDICINE_EXTRA = "MEDICINE_EXTRA";
    public static final String RESULT_EXTRA = "RESULT_EXTRA";
    @Bind(R.id.listView_medicine_history)
    ListView listView;

    MedicineAdapter medicineAdapter;
    ArrayList<Medicine> medicines;
    private AlarmManagerBroadcastReceiver alarmManagerBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_history);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Lista lekow");
        setSupportActionBar(toolbar);

        alarmManagerBroadcastReceiver = new AlarmManagerBroadcastReceiver();

        Bundle extras = getIntent().getExtras();

        medicines = (ArrayList<Medicine>) extras.getSerializable(MedicineActivity.LIST_EXTRA);
        medicineAdapter= new MedicineAdapter(getApplicationContext(),R.layout.item_medicine);

        for (Medicine medicine: medicines)
            medicineAdapter.add(medicine);

        listView.setAdapter(medicineAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditMedicineDialog editMedicineDialog = new EditMedicineDialog();
                android.app.FragmentManager fm = getFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putInt(INDEX_EXTRA,position);
                bundle.putSerializable(MEDICINE_EXTRA,  medicineAdapter.getItem(position));
                editMedicineDialog.setArguments(bundle);
                editMedicineDialog.show(fm,"test");
            }
        });
    }

    @Override
    public void onDeleteMedicine(int index) {
        medicineAdapter.remove(medicines.get(index));
        medicines.remove(index);
        medicineAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditMedicine(int index, Medicine medicine) {
        medicines.set(index, medicine);
        medicineAdapter.notifyDataSetChanged();

        if (medicine.isReminder_evening() == true  || medicine.isReminder_morning() == true || medicine.isReminder_midday() == true)
        {
            if (medicine.isReminder_morning() && medicine.isPrev_reminder_morning() == false)
            {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Long timePrefA_key = defaultSharedPreferences.getLong("timePrefA_Key", 0);
                Date date = new Date();
                date.setTime(timePrefA_key);
                alarmManagerBroadcastReceiver.setRepeatedNotification(medicine.getIDMed()+10,date.getHours(),date.getMinutes(),00,getApplicationContext(),"Weź lek: "
                        + medicine.getName() + " "+getResources().getString(R.string.dose)+ medicine.getDose());
            } else if(!medicine.isReminder_morning() && medicine.isPrev_reminder_morning() == true)
                alarmManagerBroadcastReceiver.CancelOneAlarm(getApplicationContext(),medicine.getIDMed()+10);
            if (medicine.isReminder_midday() && medicine.isPrev_reminder_midday() == false){
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Long timePrefA_key = defaultSharedPreferences.getLong("timePrefB_Key", 0);
                Date date = new Date();
                date.setTime(timePrefA_key);
                alarmManagerBroadcastReceiver.setRepeatedNotification(medicine.getIDMed()+20,date.getHours(),date.getMinutes(),00,getApplicationContext(),"Weź lek: "
                        + medicine.getName() + " "+getResources().getString(R.string.dose)+ medicine.getDose());
                Log.v("PowiadomienieTEST", "Uruchomiono z edita");
            }else if(!medicine.isReminder_midday() && medicine.isPrev_reminder_midday() == true) {
                alarmManagerBroadcastReceiver.CancelOneAlarm(getApplicationContext(), medicine.getIDMed() + 20);
                Log.v("PowiadomienieTEST", "Usunieto z edita");
            }
            if (medicine.isReminder_evening() && medicine.isPrev_reminder_evening() == false)
            {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Long timePrefA_key = defaultSharedPreferences.getLong("timePrefC_Key", 0);
                Date date = new Date();
                date.setTime(timePrefA_key);
                alarmManagerBroadcastReceiver.setRepeatedNotification(medicine.getIDMed()+30,date.getHours(),date.getMinutes(),00,getApplicationContext(),"Weź lek: "
                        + medicine.getName() + " "+getResources().getString(R.string.dose)+ medicine.getDose());
            }else if(!medicine.isReminder_evening() && medicine.isPrev_reminder_evening() == true)
                alarmManagerBroadcastReceiver.CancelOneAlarm(getApplicationContext(),medicine.getIDMed()+30);
        }
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(RESULT_EXTRA,medicines);
        if (getParent() == null) {
            setResult(Activity.RESULT_OK,data);
        } else {
            getParent().setResult(Activity.RESULT_OK, data);
        }
        super.finish();
    }
}
