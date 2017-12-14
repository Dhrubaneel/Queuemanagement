package com.das.dhrubaneel.queuemanagement.utilities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Date;

/**
 * Created by Admin on 28/04/15.
 */
public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener ondateSet;

    public DatePickerFragment() {
    }

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    private int year, month, day;
    private long minDate;

    @SuppressLint("NewApi")
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
        minDate = args.getLong("minDate");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

       /* DatePickerDialog d = new DatePickerDialog(getActivity(), ondateSet, year, month, day);
        DatePicker dp = d.getDatePicker();
        dp.setMinDate(minDate);
        return d;*/

        DatePickerDialog dialog=new DatePickerDialog(getActivity(), ondateSet, year, month, day);
        dialog.getDatePicker().setMinDate(new Date().getTime());
        return dialog;
        
    }
}
