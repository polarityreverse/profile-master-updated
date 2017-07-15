package com.example.srikanth.studentprofile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends android.app.DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    Calendar myCalendar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        
        myCalendar = Calendar.getInstance();
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light, this, year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getDatePicker().findViewById(Resources.getSystem().getIdentifier("day","id","android")).setVisibility(View.GONE);

        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Do something with the date chosen by the user
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        UpdateLabel();
    }

    private void UpdateLabel() {

        String myFormat = "MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        switch (AccomEditActivity.flagForDate) {
            case "FromDate" :  AccomEditActivity.accomFromyear.setText(sdf.format(myCalendar.getTime())); AccomEditActivity.flagForDate="";break;
            case  "ToDate":AccomEditActivity.accomToyear.setText(sdf.format(myCalendar.getTime())); AccomEditActivity.flagForDate="";break;
            default:  ;
        }

        switch (AccomPencilEdit.flagForDate) {
            case "FromDate" :  AccomPencilEdit.accomPencilFromyear.setText(sdf.format(myCalendar.getTime())); AccomPencilEdit.flagForDate="";break;
            case  "ToDate":AccomPencilEdit.accomPencilToyear.setText(sdf.format(myCalendar.getTime())); AccomPencilEdit.flagForDate="";break;
            default:;
        }

    }
}