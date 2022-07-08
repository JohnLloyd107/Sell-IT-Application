package com.group5.sellit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.group5.sellit.prev.Prevalent;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_edit_Activity extends AppCompatActivity {

    private EditText Username, Usernum, UserAge, UserAddress, UserEmail;
    private CircleImageView profile_image;

    private static final int gallerypick = 1;
    private Uri imageUri;
    private Button save_btn;
    private String randomKey, dowloadimgURL;

    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private ProgressDialog progressDialog;

    private String username,contactno,address,age,email,userprofile_img, savecurrentdate,savecurrenttime;
    private StorageReference Profile_img_Ref;
    private DatabaseReference profRef;
    private String imgid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        getSupportActionBar().hide();

        Profile_img_Ref = FirebaseStorage.getInstance().getReference().child("Profile Images");
        profRef= FirebaseDatabase.getInstance().getReference().child("Users");

        Username = (EditText) findViewById(R.id.acc_username);
        Usernum = (EditText) findViewById(R.id.acc_phonenumber);
        UserAddress = (EditText) findViewById(R.id.acc_address);
        UserAge = (EditText) findViewById(R.id.acc_Age);
        UserEmail = (EditText) findViewById(R.id.acc_email);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        save_btn = (Button) findViewById(R.id.save_btn_edit);
        imgid = Prevalent.currentUser.getProf_img();


        Picasso.get().load(imgid).into(profile_image);

        progressDialog = new ProgressDialog(this);

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();

            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProfile();
            }
        });

    }

    private void ValidateProfile() {

        username = Username.getText().toString();
        contactno = Usernum.getText().toString();
        address = UserAddress.getText().toString();
        age = UserAge.getText().toString();
        email = UserEmail.getText().toString();

        if(imageUri == null){
            Toast.makeText(Profile_edit_Activity.this, "Image is Required", Toast.LENGTH_SHORT).show();
        }
        else if (username.isEmpty()){
            Username.setError("Name is required!");
            Username.requestFocus();
            return;
        }
        else if (contactno.isEmpty()){
            Usernum.setError("Phone Number is required!");
            Usernum.requestFocus();
            return;
        }
        else if (address.isEmpty()){
            UserAddress.setError("Address is required!");
            UserAddress.requestFocus();
            return;
        }
        else if (age.isEmpty()){
            UserAge.setError("Birthdate is required!");
            UserAge.requestFocus();
            return;
        }
        else if (email.isEmpty()){
            UserEmail.setError("Email is required!");
            UserEmail.requestFocus();
            return;
        }
        else{
            ProfileInfo();
        }



    }

    private void ProfileInfo() {
        progressDialog.setTitle("Checking Profile");
        progressDialog.setMessage("Please wait, Saving changes");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM, dd, yyyy");
        savecurrentdate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currentTime.format(calendar.getTime());

        randomKey = savecurrentdate + savecurrenttime;

        StorageReference filepath = Profile_img_Ref.child(imageUri.getLastPathSegment() + randomKey + "jpg");

        final UploadTask uploadTask = filepath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message  = e.toString();
                Toast.makeText(Profile_edit_Activity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Profile_edit_Activity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urltask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()){
                            throw task.getException();

                        }
                        dowloadimgURL = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){

                            dowloadimgURL = task.getResult().toString();

                            Toast.makeText(Profile_edit_Activity.this, "Profile Successfully Uploaded", Toast.LENGTH_SHORT).show();

                            SaveprofileInfo();


                        }
                    }
                });
            }
        });





    }

    private void SaveprofileInfo() {
        HashMap<String, Object> userdataMap = new HashMap<>();

        userdataMap.put("username", username);
        userdataMap.put("phonenumber", contactno);
        userdataMap.put("address", address);
        userdataMap.put("age", age);
        userdataMap.put("email", email);
        userdataMap.put("isUser", "1");
        userdataMap.put("prof_img", dowloadimgURL);

        profRef.child(Prevalent.currentUser.getKeyid()).updateChildren(userdataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

                            databaseReference.child(Prevalent.currentUser.getKeyid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Users users = snapshot.getValue(Users.class);

                                    Prevalent.currentUser = users;
                                    startActivity(new Intent(Profile_edit_Activity.this, UserProfile.class));
                                    progressDialog.dismiss();
                                    Toast.makeText(Profile_edit_Activity.this, "Profile Successfully Saved", Toast.LENGTH_SHORT).show();
                                    finish();



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        }
                        else {
                            progressDialog.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(Profile_edit_Activity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, gallerypick );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==gallerypick && resultCode==RESULT_OK && data!=null){
            imageUri = data.getData();
            profile_image.setImageURI(imageUri);
        }




    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(Profile_edit_Activity.this, "Tap the Profile Image to change profile picture", Toast.LENGTH_SHORT).show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        databaseReference.child(Prevalent.currentUser.getKeyid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);

                Username.setText(users.getUsername());
                Usernum.setText(users.getPhonenumber());
                UserAddress.setText(users.getAddress());
                UserAge.setText(users.getAge());
                UserEmail.setText(users.getEmail());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}