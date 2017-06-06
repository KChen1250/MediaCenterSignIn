package com.fourfront.mediacentersignin.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fourfront.mediacentersignin.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Helper class to create a custom spinner in SelectTeacher
 * Source: https://stackoverflow.com/questions/20451499/android-spinner-set-default-text
 *
 * @author Kevin Chen
 *
 * Created May 2017, finished June 2017
 * Poolesville High School Client Project
 */
public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] objects;
    private String firstElement;
    private boolean isFirstTime;
    private int layout;

    public CustomSpinnerAdapter(Context context, int textViewResourceId, String[] objects, String defaultText, int layout) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
        this.isFirstTime = true;
        this.layout = layout;
        setDefaultText(defaultText);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (isFirstTime) {
            objects[0] = firstElement;
            isFirstTime = false;
        }
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        notifyDataSetChanged();
        return getCustomView(position, convertView, parent);
    }

    /**
     * Set default text
     *
     * @param defaultText default text
     */
    public void setDefaultText(String defaultText) {
        this.firstElement = objects[0];
        objects[0] = defaultText;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_item, parent, false);
        TextView label = (TextView) row.findViewById(R.id.spinner_text);
        label.setText(objects[position]);

        return row;
    }

}