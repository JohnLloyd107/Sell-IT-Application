package com.group5.sellit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group5.sellit.prev.Prevalent;

import java.util.HashMap;

public class GoogleSignUp_Activity extends AppCompatActivity {

    private EditText username_input,reg_phonenum, reg_password, reg_address, reg_age, reg_email;
    private Button registerbtn;
    private ProgressDialog progressDialog;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private String id;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_up);

        getSupportActionBar().hide();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions. DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        account = GoogleSignIn.getLastSignedInAccount(this);


        username_input = (EditText) findViewById(R.id.regname);
        reg_phonenum = (EditText) findViewById(R.id.regphonenum);
        reg_address = (EditText) findViewById(R.id.regaddress);
        reg_age = (EditText) findViewById(R.id.regage);
        reg_email = (EditText) findViewById(R.id.regemail);

        registerbtn = (Button) findViewById(R.id.regaccountbtn);

        progressDialog = new ProgressDialog(this);


        if (account != null) {
            String Name = account.getDisplayName();
            String Email = account.getEmail();
            id = account.getId();

            username_input.setText(Name);
            reg_email.setText(Email);

        }


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

            Validate_email(phonenumber,username,address,age,email);
        }

    }

    private void Validate_email(String phonenumber, String username, String address, String age, String email) {
        final DatabaseReference dbref;
        dbref = FirebaseDatabase.getInstance().getReference();

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!(snapshot.child("Users").child(id).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("username", username);
                    userdataMap.put("phonenumber", phonenumber);
                    userdataMap.put("address", address);
                    userdataMap.put("age", age);
                    userdataMap.put("email", email);
                    userdataMap.put("isUser", "1");
                    userdataMap.put("prof_img", "https://firebasestorage.googleapis.com/v0/b/sell-it-663ef.appspot.com/o/acc_icon.png?alt=media&token=a74baa6c-6d36-459d-828a-5340a6d6fe8c");
                    userdataMap.put("keyid", id);

                    dbref.child("Users").child(id).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {



                                    if (task.isSuccessful()){
                                        Toast.makeText(GoogleSignUp_Activity.this, "Account Registered", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

                                        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Users users2 = snapshot.child("Users").child(id).getValue(Users.class);
                                                if (snapshot.child("Users").child(id).exists()){
                                                    progressDialog.dismiss();

                                                    Prevalent.currentUser = users2;
                                                    Intent i = new Intent(GoogleSignUp_Activity.this, Home1Activity.class);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);

                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                    else{
                                        Toast.makeText(GoogleSignUp_Activity.this, "Error Please try Again", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        return;
                                    }

                                }
                            });


                }
                else{
                    Toast.makeText(GoogleSignUp_Activity.this, "Error" , Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog.setTitle("Checking Account");
        progressDialog.setMessage("Please wait, while we are checking the your Account");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String key = account.getId();

        final DatabaseReference dbref;
        dbref = FirebaseDatabase.getInstance().getReference();

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users = snapshot.child("Users").child(key).getValue(Users.class);


                if (users != null){
                    Prevalent.currentUser = users;
                    String keyid = Prevalent.currentUser.getKeyid();
                    if (key.equals(keyid)){
                        progressDialog.dismiss();
                        Intent i = new Intent(GoogleSignUp_Activity.this, Home1Activity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(GoogleSignUp_Activity.this, "Please fill up this form", Toast.LENGTH_SHORT).show();

                    }

                }
                else{
                    progressDialog.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });






    }
}