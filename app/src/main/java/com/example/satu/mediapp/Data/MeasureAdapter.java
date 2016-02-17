package com.example.satu.mediapp.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.satu.mediapp.R;

import java.text.SimpleDateFormat;


/**
 * Created by Satu on 2014-09-26.
 */
public class MeasureAdapter extends ArrayAdapter<Measure> {
    private final LayoutInflater mInflater;

    public MeasureAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mInflater = LayoutInflater.from(getContext());
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.item_measure, null);
            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.textView_itemDate);
            holder.results = (TextView) convertView.findViewById(R.id.textView_itemResults);


            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final Measure measure = getItem(position);

       SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
       holder.date.setText(sdf.format(measure.getTime()));
       holder.results.setText(measure.getSystole()+"/"+measure.getDiastole()+"/"+measure.getPulse());


        return convertView;
    }



    private static class ViewHolder{
        public TextView date;
        public TextView results;

    }


}
