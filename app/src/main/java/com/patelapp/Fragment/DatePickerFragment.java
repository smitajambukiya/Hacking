package com.patelapp.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by AndroidDevloper on 2/7/2016.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    DatePickerFragment mInstance;

    public interface DateListener{
         void onDateSelected(int year,int month,int day);
    }
    DateListener mDateListener;
    public void setCallBack(DateListener dateListener){
        this.mDateListener = dateListener;
    }

    public void DatePickerFragment(DatePickerDialog.OnDateSetListener m){
        mInstance = this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR,-18);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        mDateListener.onDateSelected(year,month,day);
    }


}
