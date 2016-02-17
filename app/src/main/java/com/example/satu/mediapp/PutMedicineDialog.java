package com.example.satu.mediapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.example.satu.mediapp.Data.Medicine;
import com.example.satu.mediapp.Interfaces.OnMedicinePut;

/**
 * Created by Piotrek on 2015-05-22.
 */

public class PutMedicineDialog extends DialogFragment {

    OnMedicinePut onMedicinePut;

        public PutMedicineDialog() {
        }

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.put_medicine_dialog, null);

            final EditText mediName = (EditText) view.findViewById(R.id.editText_mediName);
            final EditText dose = (EditText) view.findViewById(R.id.editText_dose);
            final Switch switch_notice = (Switch) view.findViewById(R.id.switch_notice);
            final CheckBox checkBox_morning = (CheckBox) view.findViewById(R.id.checkBox_morning);
            final CheckBox checkBox_midday = (CheckBox) view.findViewById(R.id.checkBox_midday);
            final CheckBox checkBox_evening = (CheckBox) view.findViewById(R.id.checkBox_evening);

            switch_notice.setChecked(false);
            switch_notice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        checkBox_evening.setClickable(true);
                        checkBox_midday.setClickable(true);
                        checkBox_morning.setClickable(true);
                    }else {
                        checkBox_evening.setClickable(false);
                        checkBox_midday.setClickable(false);
                        checkBox_morning.setClickable(false);
                    }
                }
            });
            builder.setMessage("Wprowadź lek")
                    .setView(view)
                    .setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int id) {

                            Medicine medicine = new Medicine();
                            medicine.setDose(Double.valueOf(String.valueOf(dose.getText())));
                            medicine.setName(String.valueOf(mediName.getText()));
                            if (switch_notice.isChecked()){
                                if (checkBox_morning.isChecked())
                                    medicine.setReminder_morning(true);
                                if (checkBox_midday.isChecked())
                                    medicine.setReminder_midday(true);
                                if (checkBox_evening.isChecked())
                                    medicine.setReminder_evening(true);
                            }

                            onMedicinePut =  (OnMedicinePut) getActivity() ;
                            onMedicinePut.onMedicinePut(medicine);
                        }
                    })
                    .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

            return builder.create();
        }

    //zapis Rezerwacji do bazy danych

}
