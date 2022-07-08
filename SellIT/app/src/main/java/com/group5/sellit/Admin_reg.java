package com.group5.sellit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Admin_reg extends AppCompatActivity {

    private EditText username_input,reg_phonenum, reg_password, reg_address, reg_age, reg_email,reg_pin;
    private EditText pin1, pin2, pin3, pin4, pin5, pin6;
    private String login_pin, securitypin;
    private Button registerbtn, pinsubmitbtn;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private StringBuilder stringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reg);
        getSupportActionBar().hide();

        username_input = (EditText) findViewById(R.id.regadminname);
        reg_phonenum = (EditText) findViewById(R.id.regadminphonenum);
        reg_password = (EditText) findViewById(R.id.regadminpassword);
        reg_address = (EditText) findViewById(R.id.regadminaddress);
        reg_age = (EditText) findViewById(R.id.regadminage);
        reg_email = (EditText) findViewById(R.id.regadminemail);
        reg_pin = (EditText) findViewById(R.id.regadminpin);



        registerbtn = (Button) findViewById(R.id.regaccountbtn);

        progressDialog = new ProgressDialog(this);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateAccount();
            }
        });
    }

    private void CreateAccount() {

        String username = username_input.getText().toString();
        String phonenumber = reg_phonenum.getText().toString();
        String password = reg_password.getText().toString();
        String address = reg_address.getText().toString();
        String age = reg_age.getText().toString();
        String email = reg_email.getText().toString();
        String pin = reg_pin.getText().toString();

        securitypin = pin;

        if (username.isEmpty()){
            username_input.setError("Username is required!");
            username_input.requestFocus();
            return;
        }

        if (phonenumber.isEmpty()){
            reg_phonenum.setError("Phone Number is required!");
            reg_phonenum.requestFocus();
            return;
        }

        if (password.isEmpty()){
            reg_password.setError("Password is required!");
            reg_password.requestFocus();
            return;
        }

        if (address.isEmpty()){
            reg_address.setError("Address is required!");
            reg_address.requestFocus();
            return;
        }

        if (age.isEmpty()){
            reg_age.setError("Age is required!");
            reg_age.requestFocus();
            return;
        }

        if (email.isEmpty()){
            reg_email.setError("Password is required!");
            reg_email.requestFocus();
            return;
        }

        if (pin.isEmpty()){
            reg_pin.setError("Security Pin is required!");
            reg_pin.requestFocus();
            return;
        }

        else{


            dialogbuilder = new AlertDialog.Builder(this);
            final View popup = getLayoutInflater().inflate(R.layout.security_login_popup, null);
            pin1 = (EditText) popup.findViewById(R.id.pin1);
            pin2 = (EditText) popup.findViewById(R.id.pin2);
            pin3 = (EditText) popup.findViewById(R.id.pin3);
            pin4 = (EditText) popup.findViewById(R.id.pin4);
            pin5 = (EditText) popup.findViewById(R.id.pin5);
            pin6 = (EditText) popup.findViewById(R.id.pin6);
            EditText edit = (EditText) popup.findViewById(R.id.edittext);
            pinsubmitbtn = (Button) popup.findViewById(R.id.pinsubmitbtn);

            dialogbuilder.setView(popup);
            dialog = dialogbuilder.create();
            dialog.show();



            pinsubmitbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String p1 = pin1.getText().toString();
                    String p2 = pin2.getText().toString();
                    String p3 = pin3.getText().toString();
                    String p4 = pin4.getText().toString();
                    String p5 = pin5.getText().toString();
                    String p6 = pin6.getText().toString();



                    if (p1.isEmpty()){
                        pin6.setError("Pin is required!");
                        pin1.requestFocus();
                        return;
                    }

                    if (p2.isEmpty()){
                        pin6.setError("Pin is required!");
                        pin2.requestFocus();
                        return;
                    }

                    if (p3.isEmpty()){
                        pin6.setError("Pin is required!");
                        pin3.requestFocus();
                        return;
                    }

                    if (p4.isEmpty()){
                        pin6.setError("Pin is required!");
                        pin4.requestFocus();
                        return;
                    }

                    if (p5.isEmpty()){
                        pin6.setError("Pin is required!");
                        pin5.requestFocus();
                        return;
                    }

                    if (p6.isEmpty()){
                        pin6.setError("Pin is required!");
                        pin6.requestFocus();
                        return;
                    }

                    else {


                        edit.setText(p1+p2+p3+p4+p5+p6);
                        login_pin = edit.getText().toString();

                        if (login_pin.equals(securitypin)){
                            Toast.makeText(Admin_reg.this, "Security Pin Matched", Toast.LENGTH_SHORT).show();

                            progressDialog.setTitle("Register Account");
                            progressDialog.setMessage("Please wait, while we are checking the credentials");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            Validate_email(phonenumber,username,password,address,age,email,pin);




                        }
                        else {
                            Toast.makeText(Admin_reg.this, "Invalid Pin", Toast.LENGTH_SHORT).show();



                        }

                    }





                }
            });




        }


    }


    private void Validate_email(String phonenumber, String username, String password, String address, String age, String email, String pin) {

        final DatabaseReference dbref;
        dbref = FirebaseDatabase.getInstance().getReference();

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!(snapshot.child("Admins").child(phonenumber).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("username", username);
                    userdataMap.put("phonenumber", phonenumber);
                    userdataMap.put("password", password);
                    userdataMap.put("address", address);
                    userdataMap.put("age", age);
                    userdataMap.put("email", email);
                    userdataMap.put("pin", pin);
                    userdataMap.put("isAdmin", "1");
                    userdataMap.put("prof_img", "img");

                    dbref.child("Admins").child(phonenumber).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(Admin_reg.this, "Account Registered", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                        startActivity(new Intent(Admin_reg.this, MainActivity.class));
                                    }

                                    else{
                                        Toast.makeText(Admin_reg.this, "Error Please try Again", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        return;
                                    }

                                }
                            });


                }
                else{
                    Toast.makeText(Admin_reg.this, "This Phone Number "+ phonenumber +" is already Registered" , Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}