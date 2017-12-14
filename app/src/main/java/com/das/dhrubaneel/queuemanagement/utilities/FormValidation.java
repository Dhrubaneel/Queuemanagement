package com.das.dhrubaneel.queuemanagement.utilities;

/**
 * Created by Admin on 24/04/15.
 */

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.das.dhrubaneel.queuemanagement.R;

public class FormValidation {
   // private Activity activity;

    // Error Messages
    private static final String REQUIRED_MSG = "Field is mandatory";

 // call this method when you need to check email validation
    public static boolean isEmailAddress(EditText editText, boolean required) {

        boolean check;
        Pattern p;
        Matcher m;
    String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
         p = Pattern.compile(EMAIL_STRING);
         m = p.matcher(editText.getText().toString());
        check = m.matches();
     if(!check){
            editText.setError("Please enter a valid Email address");
            return false;
     }
        else{
          return true;
        }
    }

    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {
         boolean check;
        Character ch=editText.getText().toString().charAt(0);
         if(editText.getText().length() < 10 || editText.getText().length() > 10)
        {
            check = false;
           editText.setError("Please enter a valid 10 digit phone number");
        }
         else if(ch.toString().equals("0")){
             Log.e("Argha hait", "Invilid mobile No");
             editText.setError("Please Enter a valid mobile no.");
             check=false;
         }
        else
        {
            check = true;

        }
        return check;
    }
    public static boolean isPinCode(EditText editText, boolean required) {
        boolean check;
        if(editText.getText().length() < 6 || editText.getText().length() > 6)
        {
            check = false;
            editText.setError("Please enter a valid 6 digit pincode");
        }
        else
        {
            check = true;

        }
        return check;
    }


    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }
    public static boolean isEmpty(String text) {
        if(text.equals("")){
            return true;
        }
        else {
            return false;
        }
    }
    public static boolean validateAlphabetTextField(EditText editText){
        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
         if (!text.toString().matches("[a-zA-Z ]+")) {
            editText.setError("Enter alphabets only.");
            return false;
        }
         return true;
     }
    public static boolean validateNumbersTextField(EditText editText){
        String number = editText.getText().toString().trim();
        if(!number.toString().matches("^[0-9:]+$")){
            editText.setError("Enter number only");
            return false;
        }
        else
        {
            return true;
        }

    }
    public static boolean validateAlphaNumeric(EditText editText){
        String alphaNum = editText.getText().toString().trim();
        if(alphaNum.toString().matches("^[a-zA-Z0-9]*$")){
             return true;
        }
        else {
            editText.setError("Enter alpha numeric only");
            return false;
        }
    }

    // validation code for  create user
     public static boolean validateNameField(EditText nameText, Context context){
        if (hasText(nameText)){
            boolean validateChar = validateAlphabetTextField(nameText);
            if(validateChar == true){
                return true;
            }
            else {
                return false;
            }
       }
        else{
            nameText.setError(context.getResources().getString(R.string.formvalidator_empty));
            return false;
        }
    }
    public static boolean validateEmailField(EditText emailTextBox, Context context){
        if (hasText(emailTextBox)){
            boolean validEmail = isEmailAddress(emailTextBox , true);
            if(validEmail == true){
                return true;
          }
            else{
                return false;
            }
         }
        else{
            emailTextBox.setError(context.getResources().getString(R.string.formvalidator_empty));
            return false;
        }
    }
    public static boolean validateDobField(EditText Dob, Context context){
        if (hasText(Dob)){
            return true;
        }
        else{
            Dob.setError(context.getResources().getString(R.string.formvalidator_empty));
            return false;
        }
    }
    public static boolean validateContactField(EditText contactTextBox, Context context){
        if (hasText(contactTextBox)){
            boolean validatePhone = isPhoneNumber(contactTextBox, true);
           if(validatePhone == true){
               return true;
           }
            else{
               return false;
           }

        }
        else{
            contactTextBox.setError(context.getResources().getString(R.string.formvalidator_empty));
            return false;
        }
    }
    public static boolean validateSpeciality(EditText drSpeciality , Context context){
        if (hasText(drSpeciality)){
            boolean validateChar = validateAlphabetTextField(drSpeciality);
            if(validateChar == true){
                return true;
            }
            else {
                return false;
            }

        }
        else{
            drSpeciality.setError(context.getResources().getString(R.string.formvalidator_empty));
            return false;
        }

    }
    public  static boolean validateAddress(EditText addressTextBox, Context context){
        if(hasText(addressTextBox)){
            return true;
        }
        else{
           return false;
        }

    }

    // validation code for Service Master

    public static boolean validateSpinner(Spinner spinner, Context context){
        if(spinner.getSelectedItem().toString().trim().equalsIgnoreCase("Select"))
        {
            TextView errorText = (TextView)spinner.getSelectedView();
            //errorText.setError("anything here, just to add the icon");
            errorText.setTextColor(Color.RED);
            errorText.setText(R.string.spinnerValue_error);
        }

        boolean serCat = spinner.getSelectedItem().toString().trim().equalsIgnoreCase("Select");
        return !serCat;
     }




}
