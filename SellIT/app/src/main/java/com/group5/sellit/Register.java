package com.group5.sellit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText username_input,reg_phonenum, reg_password, reg_address, reg_age, reg_email;
    private Button registerbtn;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        username_input = (EditText) findViewById(R.id.regname);
        reg_phonenum = (EditText) findViewById(R.id.regphonenum);
        reg_password = (EditText) findViewById(R.id.regpassword);
        reg_address = (EditText) findViewById(R.id.regaddress);
        reg_age = (EditText) findViewById(R.id.regage);
        reg_email = (EditText) findViewById(R.id.regemail);

        registerbtn = (Button) findViewById(R.id.regaccountbtn);

        //ProgressDialog
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


        else{
            progressDialog.setTitle("Register Account");
            progressDialog.setMessage("Please wait, while we are checking the credentials");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            Validate_email(phonenumber,username,password,address,age,email);
        }

    }

    private void Validate_email(String phonenumber, String username, String password, String address, String age, String email) {
        final DatabaseReference dbref;
        dbref = FirebaseDatabase.getInstance().getReference();

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!(snapshot.child("Users").child(phonenumber).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("username", username);
                    userdataMap.put("phonenumber", phonenumber);
                    userdataMap.put("password", password);
                    userdataMap.put("address", address);
                    userdataMap.put("age", age);
                    userdataMap.put("email", email);
                    userdataMap.put("isUser", "1");
                    userdataMap.put("prof_img", "https://firebasestorage.googleapis.com/v0/b/sell-it-663ef.appspot.com/o/acc_icon.png?alt=media&token=a74baa6c-6d36-459d-828a-5340a6d6fe8c");
                    userdataMap.put("keyid", phonenumber);

                    dbref.child("Users").child(phonenumber).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(Register.this, "Account Registered", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                        startActivity(new Intent(Register.this, MainActivity.class));
                                    }

                                    else{
                                        Toast.makeText(Register.this, "Error Please try Again", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        return;
                                    }

                                }
                            });


                }
                else{
                    Toast.makeText(Register.this, "This Phone Number "+ phonenumber +" is already Registered" , Toast.LENGTH_SHORT).show();
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