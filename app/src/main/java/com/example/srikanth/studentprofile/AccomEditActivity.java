package com.example.srikanth.studentprofile;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class AccomEditActivity extends Fragment {

    public static EditText accomOrgan,accomPos,accomFromyear,accomToyear;

    static RadioButton radioButtonOrganisation,radioButtonProject;
    static String radioStatus="Organisation";
    String firstEditText="";
    String secondeditText="";

    static String flagForDate="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_accom_edit,container,false);

        MainActivity.activity = "AccomEditActivity";

        accomOrgan = (EditText) v.findViewById(R.id.accom_organ_edittext);
        accomPos = (EditText) v.findViewById(R.id.accom_pos_edittext);
        accomFromyear = (EditText) v.findViewById(R.id.accom_fromyear_edittext);
        accomToyear = (EditText) v.findViewById(R.id.accom_toyear_edittext);

        // Selecting between organisation and project.
        radioButtonOrganisation = (RadioButton) v.findViewById(R.id.radio_Button_Organisation);
        radioButtonProject = (RadioButton) v.findViewById(R.id.radio_Button_Project);
        radioButtonOrganisation.setChecked(true);


        accomToyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagForDate = "ToDate";
                android.app.DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(MainActivity.fragmentManager,"datepicker");
            }
        });

        accomFromyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagForDate = "FromDate";
                android.app.DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(MainActivity.fragmentManager,"datepicker");
            }
        });

        accomOrgan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(radioStatus.equals("Organisation")){
                    if(accomOrgan.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Organisation name is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomOrgan.requestFocus();
                        return true;
                    }
                }
                else {
                    if(accomOrgan.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Project title is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomOrgan.requestFocus();
                        return true;
                    }
                }
                accomPos.requestFocus();
                return true;
            }
        });

        accomPos.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(radioStatus.equals("Organisation")){
                    if(accomPos.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Position is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomPos.requestFocus();
                        return true;
                    }
                }
                else {
                    if(accomPos.getText().toString().equals("")){
                        final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Project description is neccessary.")
                                .setPositiveButton("Ok",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        accomPos.requestFocus();
                        return true;
                    }
                }

                InputMethodManager inputManager =
                        (InputMethodManager) getActivity().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                accomPos.clearFocus();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.accom_save_action,menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.accom_save_action_button){

            final AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());
            if(radioStatus.equals("Organisation")){
                firstEditText="Organisation";
                secondeditText="Position";
            }
            else {
                firstEditText="Project Title";
                secondeditText="Project description";
            }
            //Checking validation
            if((accomOrgan.getText().toString().length()==0) && (accomPos.getText().toString().length()==0)){
                builder.setMessage(firstEditText+" and "+secondeditText+" is neccessary.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);

            }
            else if (!(accomOrgan.getText().toString().length()==0) && (accomPos.getText().toString().length()==0)){
                builder.setMessage(secondeditText+" is neccessary.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);

            }
            else if ((accomOrgan.getText().toString().length()==0) && !(accomPos.getText().toString().length()==0)){
                builder.setMessage(firstEditText+" is neccessary.")
                        .setPositiveButton("Ok",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return super.onOptionsItemSelected(item);

            }

            // Saving the data.
            //grabbing the details entered in the fields into cardview(RecyclerView's Adapter data).
            AccomDetails current=new AccomDetails();
            current.accomOrgan=AccomEditActivity.accomOrgan.getText().toString();
            current.accomPos  =AccomEditActivity.accomPos.getText().toString();
            current.accomFromyear =AccomEditActivity.accomFromyear.getText().toString();
            current.accomToyear =AccomEditActivity.accomToyear.getText().toString();
            current.radioStatus =AccomEditActivity.radioStatus;
            AccomDetailArray.makeAccomCard(current);



            Toast.makeText(getActivity(),"Saved.",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(),MainActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }


}


