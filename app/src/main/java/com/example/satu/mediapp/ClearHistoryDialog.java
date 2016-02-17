package com.example.satu.mediapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.satu.mediapp.Interfaces.OnClearHistory;

/**
 * Created by Piotrek on 2015-05-22.
 */

public class ClearHistoryDialog extends DialogFragment {

    OnClearHistory onClearHistory ;

        public ClearHistoryDialog() {
        }

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.abc_dialog_title_material, null);

            final EditText systole = (EditText) view.findViewById(R.id.editText_systole);
            final EditText diastole = (EditText) view.findViewById(R.id.editText_diastole);
            final EditText pulse = (EditText) view.findViewById(R.id.editText_pulse);

            builder.setMessage("Czy napewno chcesz wyczyścić bilans ?")
                    .setView(view)
                    .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int id) {
                           onClearHistory =  (OnClearHistory) getActivity() ;
                            onClearHistory.onClearHistory(true);
                        }
                    })
                    .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

            return builder.create();
        }

    //zapis Rezerwacji do bazy danych

}
