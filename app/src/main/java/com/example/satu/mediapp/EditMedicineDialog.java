package com.example.satu.mediapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.example.satu.mediapp.Data.Medicine;
import com.example.satu.mediapp.Interfaces.OnDeleteMedicine;
import com.example.satu.mediapp.Interfaces.OnEditMedicine;

/**
 * Created by Piotrek on 2015-05-22.
 */

public class EditMedicineDialog extends DialogFragment {

    OnDeleteMedicine onDeleteMedicine;
    OnEditMedicine onEditMedicine;

        public EditMedicineDialog() {
        }

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.edit_medicine_dialog, null);

            final EditText dose = (EditText) view.findViewById(R.id.editText_dose);
            final Switch switch_notice = (Switch) view.findViewById(R.id.switch_notice);
            final CheckBox checkBox_morning = (CheckBox) view.findViewById(R.id.checkBox_morning);
            final CheckBox checkBox_midday = (CheckBox) view.findViewById(R.id.checkBox_midday);
            final CheckBox checkBox_evening = (CheckBox) view.findViewById(R.id.checkBox_evening);
            final Button button_delete = (Button) view.findViewById(R.id.button_delete_medi);

            final int index = getArguments().getInt(MedHistoryActivity.INDEX_EXTRA);
            final Medicine medicine = (Medicine) getArguments().getSerializable(MedHistoryActivity.MEDICINE_EXTRA);

            dose.setText(String.valueOf(medicine.getDose()));
            checkBox_evening.setChecked(medicine.isReminder_evening());
            checkBox_midday.setChecked(medicine.isReminder_midday());
            checkBox_morning.setChecked(medicine.isReminder_morning());



            button_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteMedicine =  (OnDeleteMedicine) getActivity() ;
                    onDeleteMedicine.onDeleteMedicine(index);
                    dismiss();
                }
            });

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
            builder.setMessage("Edytuj lek "+ medicine.getName())
                    .setView(view)
                    .setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int id) {

                            medicine.setDose(Double.valueOf(String.valueOf(dose.getText())));
                            if (switch_notice.isChecked()){
                                if (checkBox_morning.isChecked()) {
                                    medicine.setReminder_morning(true);
                                }
                                else medicine.setReminder_morning(false);
                                if (checkBox_midday.isChecked())
                                    medicine.setReminder_midday(true);
                                else medicine.setReminder_midday(false);
                                if (checkBox_evening.isChecked())
                                    medicine.setReminder_evening(true);
                                else medicine.setReminder_evening(false);
                            }

                            onEditMedicine =  (OnEditMedicine) getActivity() ;
                            onEditMedicine.onEditMedicine(index,medicine);
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
