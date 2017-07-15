package com.example.srikanth.studentprofile;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.example.srikanth.studentprofile.AccomEditActivity.accomPos;

public class MainActivity extends AppCompatActivity {

    // This is a reference to catch Profile picture imagebutton view.
    CircleImageView profilePicImage;
    private final static  int SELECTED_PICTURE_FOR_GALLERY=1;
    private final static  int CAPTURED_PICTURE=0;
    String mCurrentPhotoPath;



    EditText email,phoneno,nickName,aboutThePerson;
    static String emailString="",phonenoString="",nickNameString="",aboutThePersonString="";
    CheckBox roomNoCheckbox;
    static Boolean roomNoChecked=true;

    public static android.app.FragmentManager  fragmentManager;

    RecyclerView accomRV;
    public static AccomAdapter accomadapter;

    private File imageFile;

    static String activity="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = "MainActivity";
        email = (EditText) findViewById(R.id.contactEmail);
        phoneno = (EditText) findViewById(R.id.contactPhoneno);
        nickName = (EditText) findViewById(R.id.nick_name) ;
        aboutThePerson = (EditText) findViewById(R.id.about_the_person);
        roomNoCheckbox = (CheckBox) findViewById(R.id.room_no_checkbox);
        roomNoCheckbox.setChecked(true);


        fragmentManager = getFragmentManager();

        profilePicImage = (CircleImageView) findViewById(R.id.profile_pic);
        profilePicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(MainActivity.this,profilePicImage);
                popupMenu.getMenuInflater().inflate(R.menu.image_dropdown_menu,popupMenu.getMenu());


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                         if(item.getItemId()==R.id.remove_image_item){
                             item.setVisible(false);

                            AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Do you want to remove your Proile Pic?")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            profilePicImage.setImageResource(R.drawable.dummypropic);
                                        }
                                    })
                                    .setNegativeButton("Cancel",null);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                        if(item.getItemId()==R.id.default_image_item){
                            item.setVisible(false);
                            setDefaultProfilePic();
                        }
                        
                        /*else if(item.getItemId()==R.id.upload_image_item){
                            onUploadButtonClicked();
                        }
                        
                       else if(item.getItemId()==R.id.capture_image_item){

                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // Ensure that there's a camera activity to handle the intent
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                // Create the File where the photo should go
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                } catch (IOException ex) {
                                    // Error occurred while creating the File

                                }
                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    Uri photoURI = FileProvider.getUriForFile(MainActivity.this,
                                            "com.example.android.fileprovider",
                                            photoFile);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(takePictureIntent, CAPTURED_PICTURE);
                                }
                            }


                        }*/

                        return true;


                    }
                });

                popupMenu.show();
            }
        });




        accomRV=(RecyclerView) findViewById(R.id.accom_rv);
        accomadapter=new AccomAdapter(this, AccomDetailArray.getAccomData());
        LinearLayoutManager layoutmanger=new LinearLayoutManager(this);
        accomRV.setLayoutManager(layoutmanger);
        accomRV.setAdapter(accomadapter);

        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(!isEmailValid(email.getText().toString())){
                    final AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Email is invalid.")
                            .setPositiveButton("Ok",null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    return true;
                }

                return false;
            }
        });

        phoneno.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(!isPhonenoValid(phoneno.getText().toString())){
                    final AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Phone no. is invalid.")
                            .setPositiveButton("Ok",null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    return true;
                }
                InputMethodManager inputManager =
                        (InputMethodManager) MainActivity.this.
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        MainActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                phoneno.clearFocus();
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        nickNameString = nickName.getText().toString();
        aboutThePersonString = aboutThePerson.getText().toString();
        phonenoString = phoneno.getText().toString();
        emailString = email.getText().toString();
        roomNoChecked = roomNoCheckbox.isChecked();
    }

    @Override
    protected void onResume() {
        super.onResume();
        nickName.setText(nickNameString);
        email.setText(emailString);
        phoneno.setText(phonenoString);
        aboutThePerson.setText(aboutThePersonString);
        if(!roomNoChecked){
            roomNoCheckbox.setChecked(false);
            ((TextView) findViewById(R.id.room_no)).setTextColor(Color.GRAY);
        }
    }

    private void setDefaultProfilePic() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.accom_save_action,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.accom_save_action_button) {
            if (activity.equals("MainActivity")) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                //Checking validation
                if (!isEmailValid(email.getText().toString()) && !isPhonenoValid(phoneno.getText().toString())) {
                    builder.setMessage("Invalid Email and phone no.")
                            .setPositiveButton("Ok", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return super.onOptionsItemSelected(item);
                } else if (!isEmailValid(email.getText().toString()) && isPhonenoValid(phoneno.getText().toString())) {
                    builder.setMessage("Invalid Email.")
                            .setPositiveButton("Ok", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return super.onOptionsItemSelected(item);
                } else if (isEmailValid(email.getText().toString()) && !isPhonenoValid(phoneno.getText().toString())) {
                    builder.setMessage("Invalid Phone no.")
                            .setPositiveButton("Ok", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    return super.onOptionsItemSelected(item);
                }

                //You can replace this when you have MySingleton class
                final RequestQueue queue = Volley.newRequestQueue(this);

                builder.setMessage("Do you want to save changes?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Upload the data into the server.


                                /**Changes made**/

                                //getPostParams method is declared dbelow
                                final String requestBody = getPostParams().toString();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                       Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        String message = null;
                                        if (error instanceof NetworkError) {
                                            message = "Cannot connect to Internet. Please check your connection!!";
                                        } else if (error instanceof ServerError) {
                                            message = "Server down. Please try again after some time!!";
                                        } else if (error instanceof AuthFailureError) {
                                            message = "Authentication error!!";
                                        } else if (error instanceof ParseError) {
                                            message = "Parsing error! Please try again after some time!!";
                                        } else if (error instanceof TimeoutError) {
                                            message = "Connection TimeOut! Please check your internet connection.";
                                        }
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    public String getBodyContentType() {
                                        return String.format("application/json; charset=utf-8");
                                    }

                                    @Override
                                    public byte[] getBody() throws AuthFailureError {
                                        try {
                                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                                        } catch (UnsupportedEncodingException uee) {
                                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                                    requestBody, "utf-8");
                                            return null;
                                        }
                                    }
                                };
                              queue.add(stringRequest);

                                //Changes end here

                                Toast.makeText(MainActivity.this, "Uploading", Toast.LENGTH_SHORT).show();

                                /**********************************/
                            }
                        })
                        .setNegativeButton("Cancel", null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        }
        return super.onOptionsItemSelected(item);
    }



    // This method invokes when the upload imagebutton is clicked.

    public void onUploadButtonClicked(){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,SELECTED_PICTURE_FOR_GALLERY);

    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    private boolean isEmailValid(String email) {
        if( email.length()==0)     // email field can be vacant.
            return true;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private boolean isPhonenoValid(String s) {

        if(s.length()==10 || s.length()==0)
            return true;
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == SELECTED_PICTURE_FOR_GALLERY){
                Uri image_uri = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(image_uri);

                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    profilePicImage.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this,"Unable to locate image",Toast.LENGTH_LONG).show();
                }
            }



            else if(requestCode == CAPTURED_PICTURE){
                Toast.makeText(MainActivity.this,"image saved.",Toast.LENGTH_LONG).show();
                // Get the dimensions of the View
                int targetW = profilePicImage.getWidth();
                int targetH = profilePicImage.getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                profilePicImage.setImageBitmap(bitmap);

            }
        }
    }
    /*****************************************************************/


    // This method is invoked when accom_plus_image is clicked.
    public void onAccomPlusClicked(View view) {

        // Create new fragment and transaction
        android.app.Fragment newFragment = new AccomEditActivity();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.mainActivityRelativeLayout, newFragment);
        transaction.addToBackStack(null);

// Commit the transaction
        transaction.commit();
    }


    // Required for Capture image ,this method creates a file for saving the captured image.
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_Button_Organisation:
                if (checked)
                    AccomEditActivity.radioButtonProject.setChecked(false);
                    AccomEditActivity.radioStatus="Organisation";
                    AccomEditActivity.accomOrgan.setHint("Organisation");
                    accomPos.setHint("Position in Organisation");
                break;
            case R.id.radio_Button_Project:
                if (checked)
                    AccomEditActivity.radioButtonOrganisation.setChecked(false);
                    AccomEditActivity.radioStatus="Project";
                    AccomEditActivity.accomOrgan.setHint("Project Title");
                    accomPos.setHint("Project description");
                break;
        }
    }

    public void onRoomNoChecked(View view){
        if(roomNoCheckbox.isChecked()){
            roomNoCheckbox.setChecked(true);
            ((TextView) findViewById(R.id.room_no)).setTextColor(Color.BLACK);
        }
        else{
            roomNoCheckbox.setChecked(false);
            ((TextView) findViewById(R.id.room_no)).setTextColor(Color.GRAY);
        }
    }


    //This is the method to get JsonObject from user input
    public JSONObject getPostParams(){
        int size = accomRV.getAdapter().getItemCount();
        JSONArray accomplishmentsArray = new JSONArray();
            for(int i = 0; i < size; i++){
                JSONObject accomplishmentsObj = new JSONObject();
                AccomDetails currDetails = ((AccomAdapter)accomRV.getAdapter()).getItem(i);
                try {
                    accomplishmentsObj.put("title",currDetails.accomOrgan );
                    accomplishmentsObj.put("desc", currDetails.accomPos);
                    accomplishmentsObj.put("from", currDetails.accomFromyear);
                    accomplishmentsObj.put("to", currDetails.accomToyear);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                accomplishmentsArray.put(accomplishmentsObj);
            }
        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("Accomplishments", accomplishmentsArray);
            jsonPost.put("email",emailString );
            jsonPost.put("Phone", phonenoString);
            jsonPost.put("about", aboutThePersonString);
            jsonPost.put("reveal_photo", 1);
            jsonPost.put("reveal_place", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    return jsonPost;

    }


}
