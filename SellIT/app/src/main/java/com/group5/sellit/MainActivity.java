package com.group5.sellit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group5.sellit.model.AdminModel;
import com.group5.sellit.prev.Prevalent;

import java.util.HashMap;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private EditText loginnum, login_password;
    private EditText username_input,reg_phonenum, reg_password, reg_address, reg_age, reg_email;
    private TextView isAdmin, isUser, remember;
    private Button Registerbtn, Loginbtn,fbregisterbtn;
    private ImageView googlebtnsignin, fblogin;
    private ProgressDialog progressDialog;
    private String pdbname = "Users", uniqueid, ImageURL;
    private CheckBox rcheckbox;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private LoginButton login;
    private AlertDialog dialog, dialog2;
    private long pressedTime;
    public CallbackManager callbackManager;
    private AlertDialog.Builder dialogbuilder,dialogbuilder2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions. DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);


        //TextView
        isAdmin = (TextView) findViewById(R.id.isadmin);
        isUser = (TextView) findViewById(R.id.isuser);
        remember = (TextView) findViewById(R.id.remember);



        isAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUser.setVisibility(View.VISIBLE);
                isAdmin.setVisibility(View.INVISIBLE);
                rcheckbox.setVisibility(View.INVISIBLE);
                remember.setVisibility(View.INVISIBLE);
                Loginbtn.setText("Admin Login");
                pdbname = "Admins";
            }
        });

        isUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUser.setVisibility(View.INVISIBLE);
                isAdmin.setVisibility(View.VISIBLE);
                rcheckbox.setVisibility(View.VISIBLE);
                remember.setVisibility(View.VISIBLE);
                Loginbtn.setText("Login");
                pdbname = "Users";
            }
        });

        //EditText
        loginnum = (EditText) findViewById(R.id.usernumber);
        login_password = (EditText) findViewById(R.id.userpassword);

        //Button
        Registerbtn = (Button) findViewById(R.id.regbtn);
        Loginbtn = (Button) findViewById(R.id.loginbtn);
        googlebtnsignin = (ImageView) findViewById(R.id.google_btn);
        fblogin = (ImageView) findViewById(R.id.btn_facebook);

        callbackManager = CallbackManager.Factory.create();


        //CheckBox
        rcheckbox = (CheckBox)findViewById(R.id.boxremember);
        Paper.init(this);

        //ProgressDialog
        progressDialog = new ProgressDialog(this);


        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginAccount();
            }
        });

        googlebtnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSign_In();
            }
        });

        fblogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FbLogin();


            }
        });



        Registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pdbname == "Admins"){
                    startActivity(new Intent(MainActivity.this, Admin_reg.class));

                }

                else{
                    startActivity(new Intent(MainActivity.this, Register.class));

                }


            }
        });
    }

    private void FbLogin() {
        final View popup = getLayoutInflater().inflate(R.layout.loginfb, null);
        login = (LoginButton) popup.findViewById(R.id.login_button);

        dialogbuilder = new AlertDialog.Builder(this);
        dialogbuilder.setView(popup);
        dialog = dialogbuilder.create();
        dialog.show();

        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String keyid = loginResult.getAccessToken().getUserId();
                uniqueid = keyid;
                ImageURL = "https://firebasestorage.googleapis.com/v0/b/sell-it-663ef.appspot.com/o/acc_icon.png?alt=media&token=a74baa6c-6d36-459d-828a-5340a6d6fe8c";


                progressDialog.setTitle("Checking Account");
                progressDialog.setMessage("Please wait, while we are checking the your Account");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                final DatabaseReference dbref;
                dbref = FirebaseDatabase.getInstance().getReference();

                dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Users users = snapshot.child("Users").child(keyid).getValue(Users.class);
                        if (users != null){
                            Prevalent.currentUser = users;
                            String userid = Prevalent.currentUser.getKeyid();
                            if (keyid.equals(userid)) {
                                progressDialog.dismiss();
                                Intent i = new Intent(MainActivity.this, Home1Activity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        }
                        else{
                            dialog.dismiss();
                            final View popup2 = getLayoutInflater().inflate(R.layout.fbsignup, null);
                            dialogbuilder.setView(popup2);
                            dialog2 = dialogbuilder.create();
                            dialog2.show();

                            username_input = (EditText) popup2.findViewById(R.id.regname);
                            reg_phonenum = (EditText) popup2.findViewById(R.id.regphonenum);
                            reg_address = (EditText) popup2.findViewById(R.id.regaddress);
                            reg_age = (EditText) popup2.findViewById(R.id.regage);
                            reg_email = (EditText) popup2.findViewById(R.id.regemail);

                            fbregisterbtn = (Button) popup2.findViewById(R.id.regaccountbtn);

                            if (keyid != null) {
                                String uniqueid = keyid;

                            }

                            fbregisterbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

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
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(@NonNull FacebookException e) {

            }
        });

    }

    private void Validate_email(String phonenumber, String username, String address, String age, String email) {

        final DatabaseReference dbref;
        dbref = FirebaseDatabase.getInstance().getReference();

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!(snapshot.child("Users").child(uniqueid).exists())) {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("username", username);
                    userdataMap.put("phonenumber", phonenumber);
                    userdataMap.put("address", address);
                    userdataMap.put("age", age);
                    userdataMap.put("email", email);
                    userdataMap.put("isUser", "1");
                    userdataMap.put("prof_img", ImageURL);
                    userdataMap.put("keyid", uniqueid);

                    dbref.child("Users").child(uniqueid).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {



                                    if (task.isSuccessful()){
                                        Toast.makeText(MainActivity.this, "Account Registered", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

                                        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Users users2 = snapshot.child("Users").child(uniqueid).getValue(Users.class);
                                                if (snapshot.child("Users").child(uniqueid).exists()){
                                                    progressDialog.dismiss();

                                                    Prevalent.currentUser = users2;
                                                    Intent i = new Intent(MainActivity.this, Home1Activity.class);
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
                                        Toast.makeText(MainActivity.this, "Error Please try Again", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        return;
                                    }

                                }
                            });


                }
                else{
                    Toast.makeText(MainActivity.this, "Error" , Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void GoogleSign_In() {

        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 100);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                finish();
                startActivity(new Intent(MainActivity.this, GoogleSignUp_Activity.class));
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void LoginAccount() {

        String phonenumber = loginnum.getText().toString();
        String password = login_password.getText().toString();

        if (phonenumber.isEmpty()){
            loginnum.setError("Phone Number is required!");
            loginnum.requestFocus();
            return;
        }

        if (password.isEmpty()){
            login_password.setError("Password is required!");
            login_password.requestFocus();
            return;
        }

        else{
            progressDialog.setTitle("Login Account");
            progressDialog.setMessage("Please wait, while we are checking the credentials");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            LoggingAccount(phonenumber,password);
        }


    }

    private void LoggingAccount(String phonenumber, String password) {

        if (rcheckbox.isChecked()){
            Paper.book().write(Prevalent.UserPhone, phonenumber);
            Paper.book().write(Prevalent.UserPassword, password);
        }

        final DatabaseReference dbref;
        dbref = FirebaseDatabase.getInstance().getReference();

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(pdbname).child(phonenumber).exists()){

                    Users users = snapshot.child(pdbname).child(phonenumber).getValue(Users.class);


                    if (users.getPhonenumber().equals(phonenumber)){
                        if (users.getPassword().equals(password)){

                            if (pdbname.equals("Admins")){
                                Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent = new Intent(MainActivity.this, AdminDasboard.class);
                                intent.putExtra("uid", phonenumber);
                                startActivity(intent);
                            }

                            if (pdbname.equals("Users")){
                                Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Prevalent.currentUser = users;
                                startActivity(new Intent(MainActivity.this, Home1Activity.class));
                            }

                        }
                        else{
                            Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Error Please try again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "This Account is not Registered", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });


    }

    @Override
    protected void onStart(){
        super.onStart();

        LoginManager.getInstance().logOut();

        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


            }
        });


        String UserPhoneKey = Paper.book().read(Prevalent.UserPhone);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPassword);

        if(UserPhoneKey != "" && UserPasswordKey != ""){
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess(UserPhoneKey, UserPasswordKey);

                progressDialog.setTitle("Login Account");
                progressDialog.setMessage("Please wait, while we are checking the credentials");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        }
    }

    private void AllowAccess(final String phonenumber, final String password) {
        final DatabaseReference dbref;
        dbref = FirebaseDatabase.getInstance().getReference();

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("Users").child(phonenumber).exists()){

                    Users users = snapshot.child("Users").child(phonenumber).getValue(Users.class);

                    if (users.getPhonenumber().equals(phonenumber)){
                        if (users.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Prevalent.currentUser = users;
                            startActivity(new Intent(MainActivity.this, Home1Activity.class));
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Error Please try again", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        return;
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "This Account is not Registered", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();


    }

}