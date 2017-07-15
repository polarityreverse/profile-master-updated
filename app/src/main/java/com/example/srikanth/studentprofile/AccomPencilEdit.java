package com.example.srikanth.studentprofile;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import static com.example.srikanth.studentprofile.AccomEditActivity.accomPos;

public class AccomPencilEdit extends Fragment {
    static EditText accomPencilOrgan,accomPencilPos,accomPencilFromyear,accomPencilToyear;
    RadioGroup radioGroup;
    public static String flagForDate="",radioStatus="" ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_accom_edit,container,false);

        MainActivity.activity = "AccomPencilEdit";

        accomPencilOrgan = (EditText) v.findViewById(R.id.accom_organ_edittext);
        accomPencilPos= (EditText) v.findViewById(R.id.accom_pos_edittext);
        accomPencilFromyear= (EditText) v.findViewById(R.id.accom_fromyear_edittext);
        accomPencilToyear = (EditText) v.findViewById(R.id.accom_toyear_edittext);

        accomPencilOrgan.setText(AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomOrgan);
        accomPencilPos.setText(AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomPos);
        accomPencilFromyear.setText(AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomFromyear);
        accomPencilToyear.setText(AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomToyear);
        radioStatus = AccomAdapter.adapterData.get(AccomAdapter.postionValue).radioStatus;
        if(radioStatus.equals("Organisation")){
            accomPencilOrgan.setHint("Organisation");
            accomPencilPos.setHint("Position in Organisation");
        }else{
            accomPencilOrgan.setHint("Project Title");
            accomPencilPos.setHint("Project description");
        }

        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroupAccom);
        radioGroup.setVisibility(View.GONE);

        accomPencilToyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagForDate = "ToDate";
                android.app.DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(MainActivity.fragmentManager,"datepicker");
            }
        });

        accomPencilFromyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagForDate = "FromDate";
                android.app.DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(MainActivity.fragmentManager,"datepicker");
            }
        });

        accomPencilOrgan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(radioStatus.equals("Organisation")){
                    if(accomPencilOrgan.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Organisation name is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomPencilOrgan.requestFocus();
                        return true;
                    }
                }
                else {
                    if(accomPencilOrgan.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Project title is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomPencilOrgan.requestFocus();
                        return true;
                    }
                }
                accomPencilPos.requestFocus();
                return true;
            }
        });

        accomPencilPos.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(radioStatus.equals("Organisation")){
                    if(accomPencilPos.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Position is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomPencilPos.requestFocus();
                        return true;
                    }
                }
                else {
                    if(accomPencilPos.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Project description is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomPencilPos.requestFocus();
                        return true;
                    }
                }

                InputMethodManager inputManager =
                        (InputMethodManager) getActivity().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                accomPencilPos.clearFocus();
                return false;
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.accom_save_action,menu);
        return super.onCreateOptionsMenu(menu);
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.accom_save_action_button){
            //Checking for validation.
            final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());

            //Checking validation
            if((accomPencilOrgan.getText().toString().length()==0) && (accomPencilPos.getText().toString().length()==0)){
                builder.setMessage("Enter Organisation name and position.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);
            }
            else if (!(accomPencilOrgan.getText().toString().length()==0) && (accomPencilPos.getText().toString().length()==0)){
                builder.setMessage("Position name is neccessary.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);
            }
            else if ((accomPencilOrgan.getText().toString().length()==0) && !(accomPencilPos.getText().toString().length()==0)){
                builder.setMessage("Enter Organisation name.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);
            }


            // Saving the data.
            builder.setMessage("Do you want to save changes?")
                   .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomOrgan    = accomPencilOrgan   .getText().toString();
                           AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomPos      = accomPencilPos     .getText().toString();
                           AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomFromyear = accomPencilFromyear.getText().toString();
                           AccomAdapter.adapterData.get(AccomAdapter.postionValue).accomToyear   = accomPencilToyear  .getText().toString();
                           startActivity(new Intent(getActivity(),MainActivity.class));
                       }
                   })
                   .setNegativeButton("Cancel",null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();



        }
        return super.onOptionsItemSelected(item);
    }
}

