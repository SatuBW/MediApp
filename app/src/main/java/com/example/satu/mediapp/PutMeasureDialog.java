package com.example.satu.mediapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.satu.mediapp.Data.Measure;
import com.example.satu.mediapp.Interfaces.onMeasurePut;

/**
 * Created by Piotrek on 2015-05-22.
 */

public class PutMeasureDialog extends DialogFragment {

    onMeasurePut onMeasurePut ;
     EditText systole ;
    EditText diastole ;
    EditText pulse ;
    TextView warning ;

        public PutMeasureDialog() {
        }

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {

            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.put_measure_dialog, null);

            systole = (EditText) view.findViewById(R.id.editText_systole);
            diastole = (EditText) view.findViewById(R.id.editText_diastole);
             pulse = (EditText) view.findViewById(R.id.editText_pulse);
            warning = (TextView) view.findViewById(R.id.textView_warning);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Wprowadź wynik").setView(view);
            builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.setPositiveButton("Zapisz",
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {}
                    });
            return builder.create();
        }

    @Override
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try{
                        Measure measure = new Measure(Integer.valueOf(String.valueOf(diastole.getText())),Integer.valueOf(systole.getText().toString()),Integer.valueOf(String.valueOf(pulse.getText())));
                        onMeasurePut =  (onMeasurePut) getActivity() ;
                        onMeasurePut.onMeasurePut(measure);
                        dismiss();
                    }catch (NumberFormatException e){
                        warning.setText(R.string.warning);

                    }
                }
            });
        }
    }
}
