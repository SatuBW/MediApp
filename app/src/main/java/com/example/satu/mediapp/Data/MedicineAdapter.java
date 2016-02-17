package com.example.satu.mediapp.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.satu.mediapp.R;


/**
 * Created by Satu on 2014-09-26.
 */
public class MedicineAdapter extends ArrayAdapter<Medicine> {
    private final LayoutInflater mInflater;

    public MedicineAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mInflater = LayoutInflater.from(getContext());
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.item_medicine, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.textView_itemName);
            holder.dose = (TextView) convertView.findViewById(R.id.textView_itemDose);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView_remind);


            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final Medicine medicine = getItem(position);


       holder.name.setText(medicine.getName());
       holder.dose.setText("Dawka: "+String.valueOf(medicine.getDose()));
       if (medicine.isReminder_evening() == true  || medicine.isReminder_morning() == true || medicine.isReminder_midday() == true)
           holder.imageView.setVisibility(View.VISIBLE);
        else holder.imageView.setVisibility(View.GONE);


        return convertView;
    }



    private static class ViewHolder{
        public TextView name;
        public TextView dose;
        public ImageView imageView;

    }


}
