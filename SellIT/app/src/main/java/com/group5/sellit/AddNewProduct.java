package com.group5.sellit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.group5.sellit.Viewholder.CartViewHolder;
import com.group5.sellit.Viewholder.CategoryViewHolder;
import com.group5.sellit.Viewholder.ColorViewHolder;
import com.group5.sellit.Viewholder.UploadListAdapter;
import com.group5.sellit.model.CartModel;
import com.group5.sellit.model.CategoryModel;
import com.group5.sellit.model.ColorModel;
import com.group5.sellit.prev.Prevalent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AddNewProduct extends AppCompatActivity {
    private String category, Description, Price, ProductName, savecurrentdate,savecurrenttime,tags1,tags2, tags3;

    private Button addproductbtn, upload_img;
    private Spinner spinner;
    private TextView tag1, tag2, tag3;
    private AlertDialog.Builder dialogbuilder, dialogbuilder2;
    private AlertDialog dialog, dialog2;
    private RecyclerView img_recycle;

    private List<String> fileNameList;
    private List<String> fileDoneList;
    private UploadListAdapter uploadListAdapter;
    private String filename, idkey;

    private EditText product_name, product_description, product_price,  clothes_size;

    private ImageView product_img, product_img2, product_img3, product_img4, product_img5;

    private static final int gallerypick = 1, gallerypick2 = 2, gallerypick3 = 3, gallerypick4 = 4, gallerypick5 = 5;
    private Uri imageUri, imageUri2, imageUri3, imageUri4, imageUri5, fileUri;

    private String randomKey, dowloadimgURL, dowloadimgURL2, dowloadimgURL3, dowloadimgURL4, dowloadimgURL5,categories_list,inputtag;
    private ProgressDialog progressDialog;

    private StorageReference Product_img_Ref;
    private DatabaseReference prodRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        getSupportActionBar().hide();

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList);
        img_recycle = (RecyclerView) findViewById(R.id.img_recycle);

        img_recycle.setLayoutManager(new LinearLayoutManager(this));
        img_recycle.setHasFixedSize(true);
        img_recycle.setAdapter(uploadListAdapter);



        FirebaseMessaging.getInstance().subscribeToTopic("all");

        dialogbuilder = new AlertDialog.Builder(this);
        dialogbuilder2 = new AlertDialog.Builder(this);


        Product_img_Ref = FirebaseStorage.getInstance().getReference().child("Product Images");
        prodRef= FirebaseDatabase.getInstance().getReference().child("Products");

        product_name = (EditText) findViewById(R.id.product_name);
        product_description = (EditText) findViewById(R.id.product_description);
        product_price = (EditText) findViewById(R.id.product_price);
        clothes_size = (EditText) findViewById(R.id.clothes_size);
        tag1 = (TextView) findViewById(R.id.tag1_input);
        tag2 = (TextView) findViewById(R.id.tag2_input);
        tag3 = (TextView) findViewById(R.id.tag3_input);
        spinner = (Spinner) findViewById(R.id.product_category_list);

//        product_img = (ImageView) findViewById(R.id.selectedproduct_img);
//        product_img2 = (ImageView) findViewById(R.id.selectedproduct_img2);
//        product_img3 = (ImageView) findViewById(R.id.selectedproduct_img3);
//        product_img4 = (ImageView) findViewById(R.id.selectedproduct_img4);
//        product_img5 = (ImageView) findViewById(R.id.selectedproduct_img5);

        addproductbtn = (Button) findViewById(R.id.add_new_productbtn);
        upload_img = (Button) findViewById(R.id.upload_btn);


        progressDialog = new ProgressDialog(this);

        tag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputtag = "tag1";
                tagselect();
            }
        });

        tag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputtag = "tag2";
                tagselect();
            }
        });

        tag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputtag = "tag3";
                tagselect();
            }
        });

        List<String> ID = new ArrayList<>();
        ID.add(0,"...");
        ID.add("Clothes");
        ID.add("Computers");
        ID.add("Sports");
        ID.add("Toys");
        ID.add("Groceries");

        ArrayAdapter<String> myAdapter;
        myAdapter = new ArrayAdapter<>(AddNewProduct.this, android.R.layout.simple_spinner_item,ID);

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(myAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select Category")){

                }
                else {
                    categories_list = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });







        upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

//        product_img2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                OpenGallery2();
//            }
//        });
//
//        product_img3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                OpenGallery3();
//            }
//        });
//
//        product_img4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                OpenGallery4();
//            }
//        });
//
//        product_img5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                OpenGallery5();
//            }
//        });

        addproductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProduct_data();

            }
        });


    }


    private void tagselect() {


        final View popup = getLayoutInflater().inflate(R.layout.tag_selection, null);
        Button none = (Button) popup.findViewById(R.id.none_btn);
        Button shirt = (Button) popup.findViewById(R.id.shirt_btn);
        Button shorts = (Button) popup.findViewById(R.id.short_btn);
        Button jean = (Button) popup.findViewById(R.id.jeans_btn);
        Button digital = (Button) popup.findViewById(R.id.digital_btn);
        Button game = (Button) popup.findViewById(R.id.game_btn);
        Button shoes = (Button) popup.findViewById(R.id.shoes_btn);
        Button jacket = (Button) popup.findViewById(R.id.jacket_btn);
        Button toy = (Button) popup.findViewById(R.id.toy_btn);
        Button computer = (Button) popup.findViewById(R.id.computer_btn);
        Button groceries = (Button) popup.findViewById(R.id.grocries_btn);
        Button sport = (Button) popup.findViewById(R.id.sport_btn);
        EditText customtag =(EditText) popup.findViewById(R.id.input_tag);

        Button save = (Button) popup.findViewById(R.id.customtag_btn);


        dialogbuilder.setView(popup);
        dialog = dialogbuilder.create();
        dialog.show();

        if (inputtag.equals("tag1")){

            none.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag1.setText(" ");
                    dialog.dismiss();
                }
            });

            shirt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag1.setText("shirt");
                    dialog.dismiss();
                }
            });

            shorts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag1.setText("short");
                    dialog.dismiss();
                }
            });

            jean.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag1.setText("jeans");
                    dialog.dismiss();
                }
            });

            digital.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag1.setText("digital");
                    dialog.dismiss();
                }
            });

            game.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag1.setText("game");
                    dialog.dismiss();
                }
            });

            shoes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag1.setText("shoes");
                    dialog.dismiss();
                }
            });

            jacket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag1.setText("jacket");
                    dialog.dismiss();
                }
            });

            toy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag1.setText("toy");
                    dialog.dismiss();
                }
            });

            computer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag1.setText("computer");
                    dialog.dismiss();
                }
            });

            groceries.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag1.setText("groceries");
                    dialog.dismiss();
                }
            });

            sport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag1.setText("sport");
                    dialog.dismiss();
                }
            });

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String custom = customtag.getText().toString();
                    tag1.setText(custom);
                    dialog.dismiss();
                }
            });


        }

        else if (inputtag.equals("tag2")){

            none.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag2.setText(" ");
                    dialog.dismiss();
                }
            });

            shirt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag2.setText("shirt");
                    dialog.dismiss();
                }
            });

            shorts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag2.setText("short");
                    dialog.dismiss();
                }
            });

            jean.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag2.setText("jeans");
                    dialog.dismiss();
                }
            });

            digital.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag2.setText("digital");
                    dialog.dismiss();
                }
            });

            game.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag2.setText("game");
                    dialog.dismiss();
                }
            });

            shoes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag2.setText("shoes");
                    dialog.dismiss();
                }
            });

            jacket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag2.setText("jacket");
                    dialog.dismiss();
                }
            });

            toy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag2.setText("toy");
                    dialog.dismiss();
                }
            });

            computer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag2.setText("computer");
                    dialog.dismiss();
                }
            });

            groceries.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag2.setText("groceries");
                    dialog.dismiss();
                }
            });

            sport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag2.setText("sport");
                    dialog.dismiss();
                }
            });

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String custom = customtag.getText().toString();
                    tag2.setText(custom);
                    dialog.dismiss();
                }
            });


        }

        else if (inputtag.equals("tag3")){

            none.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag3.setText(" ");
                    dialog.dismiss();
                }
            });

            shirt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag3.setText("shirt");
                    dialog.dismiss();
                }
            });

            shorts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag3.setText("short");
                    dialog.dismiss();
                }
            });

            jean.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag3.setText("jeans");
                    dialog.dismiss();
                }
            });

            digital.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag3.setText("digital");
                    dialog.dismiss();
                }
            });

            game.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag3.setText("game");
                    dialog.dismiss();
                }
            });

            shoes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag3.setText("shoes");
                    dialog.dismiss();
                }
            });

            jacket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag3.setText("jacket");
                    dialog.dismiss();
                }
            });

            toy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag3.setText("toy");
                    dialog.dismiss();
                }
            });

            computer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag3.setText("computer");
                    dialog.dismiss();
                }
            });

            groceries.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag3.setText("groceries");
                    dialog.dismiss();
                }
            });

            sport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag3.setText("sport");
                    dialog.dismiss();
                }
            });

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String custom = customtag.getText().toString();
                    tag3.setText(custom);
                    dialog.dismiss();
                }
            });


        }



    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"), gallerypick);
    }

//    private void OpenGallery2() {
//        Intent galleryIntent2 = new Intent();
//        galleryIntent2.setAction(Intent.ACTION_GET_CONTENT);
//        galleryIntent2.setType("image/*");
//        startActivityForResult(galleryIntent2, gallerypick2 );
//    }
//
//    private void OpenGallery3() {
//        Intent galleryIntent3 = new Intent();
//        galleryIntent3.setAction(Intent.ACTION_GET_CONTENT);
//        galleryIntent3.setType("image/*");
//        startActivityForResult(galleryIntent3, gallerypick3 );
//    }
//
//    private void OpenGallery4() {
//        Intent galleryIntent4 = new Intent();
//        galleryIntent4.setAction(Intent.ACTION_GET_CONTENT);
//        galleryIntent4.setType("image/*");
//        startActivityForResult(galleryIntent4, gallerypick4 );
//    }
//
//    private void OpenGallery5() {
//        Intent galleryIntent5 = new Intent();
//        galleryIntent5.setAction(Intent.ACTION_GET_CONTENT);
//        galleryIntent5.setType("image/*");
//        startActivityForResult(galleryIntent5, gallerypick5 );
//    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==gallerypick && resultCode==RESULT_OK && data!=null){

            if (data.getClipData() != null){
                int totalItemSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemSelected; i++){

                    fileUri = data.getClipData().getItemAt(i).getUri();
                    filename = getFileName(fileUri);
                    fileNameList.add(filename);
                    uploadListAdapter.notifyDataSetChanged();

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM, dd, yyyy");
                    savecurrentdate = currentDate.format(calendar.getTime());

                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                    savecurrenttime = currentTime.format(calendar.getTime());

                    randomKey = savecurrentdate + savecurrenttime;

                    StorageReference filepath = Product_img_Ref.child(randomKey).child(filename + randomKey + "jpg");
                    idkey = randomKey;

                    String imageURL = filepath.getDownloadUrl().toString();


//        StorageReference filepath2 = Product_img_Ref.child(imageUri2.getLastPathSegment() + randomKey + "jpg");
//        StorageReference filepath3 = Product_img_Ref.child(imageUri3.getLastPathSegment() + randomKey + "jpg");
//        StorageReference filepath4 = Product_img_Ref.child(imageUri4.getLastPathSegment() + randomKey + "jpg");
//        StorageReference filepath5 = Product_img_Ref.child(imageUri5.getLastPathSegment() + randomKey + "jpg");

                    final UploadTask uploadTask = filepath.putFile(fileUri);
//        final UploadTask uploadTask2 = filepath2.putFile(imageUri2);
//        final UploadTask uploadTask3 = filepath3.putFile(imageUri3);
//        final UploadTask uploadTask4 = filepath4.putFile(imageUri4);
//        final UploadTask uploadTask5 = filepath5.putFile(imageUri5);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String message  = e.toString();
                            Toast.makeText(AddNewProduct.this, "Error: " + message, Toast.LENGTH_SHORT).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AddNewProduct.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

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
                                        prodRef.child(randomKey).child("images").push().child("images").setValue(dowloadimgURL)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){

                                                            Toast.makeText(AddNewProduct.this, "Image Added", Toast.LENGTH_SHORT).show();

                                                        }
                                                        else {
                                                            progressDialog.dismiss();
                                                            String message = task.getException().toString();
                                                            Toast.makeText(AddNewProduct.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });

                                        Toast.makeText(AddNewProduct.this, "Product Image uploaded to Database Successfully", Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });
                        }
                    });

                }

            }

//            imageUri = data.getData();
//            product_img.setImageURI(imageUri);
        }
//        else if(requestCode==gallerypick2 && resultCode==RESULT_OK && data!=null){
//            imageUri2 = data.getData();
//            product_img2.setImageURI(imageUri2);
//        }
//
//        else if(requestCode==gallerypick3 && resultCode==RESULT_OK && data!=null){
//            imageUri3 = data.getData();
//            product_img3.setImageURI(imageUri3);
//        }
//
//
//        else if(requestCode==gallerypick4 && resultCode==RESULT_OK && data!=null){
//            imageUri4 = data.getData();
//            product_img4.setImageURI(imageUri4);
//        }
//
//
//        else if(requestCode==gallerypick5 && resultCode==RESULT_OK && data!=null){
//            imageUri5 = data.getData();
//            product_img5.setImageURI(imageUri5);
//        }

    }

    public String getFileName(Uri uri){
        String result  = null;
        if (uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1){
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void ValidateProduct_data(){
        Description = product_description.getText().toString();
        ProductName = product_name.getText().toString();
        Price = product_price.getText().toString();
        tags1 = tag1.getText().toString();
        tags2 = tag2.getText().toString();
        tags3 = tag3.getText().toString();

        if(fileUri == null){
            Toast.makeText(AddNewProduct.this, "Image is Required", Toast.LENGTH_SHORT).show();
        }
        else if (Description.isEmpty()){
            product_description.setError("Description is required!");
            product_description.requestFocus();
            return;
        }
        else if (ProductName.isEmpty()){
            product_name.setError("Product Name is required!");
            product_name.requestFocus();
            return;
        }
        else if (Price.isEmpty()){
            product_price.setError("Description is required!");
            product_price.requestFocus();
            return;
        }
        else if (tags1.isEmpty()){
            tag1.setError("Tag is required!");
            tag1.requestFocus();
            return;
        }
        else if (tags2.isEmpty()){
            tag2.setError("Tag is required!");
            tag2.requestFocus();
            return;
        }
        else if (tags3.isEmpty()){
            tag3.setError("Tag is required!");
            tag3.requestFocus();
            return;
        }

        else{
            StoreProductInfo();
        }
    }

    private void StoreProductInfo() {



//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("MMM, dd, yyyy");
//        savecurrentdate = currentDate.format(calendar.getTime());
//
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
//        savecurrenttime = currentTime.format(calendar.getTime());
//
//        randomKey = savecurrentdate + savecurrenttime;
//
//        StorageReference filepath = Product_img_Ref.child(filename + randomKey + "jpg");
////        StorageReference filepath2 = Product_img_Ref.child(imageUri2.getLastPathSegment() + randomKey + "jpg");
////        StorageReference filepath3 = Product_img_Ref.child(imageUri3.getLastPathSegment() + randomKey + "jpg");
////        StorageReference filepath4 = Product_img_Ref.child(imageUri4.getLastPathSegment() + randomKey + "jpg");
////        StorageReference filepath5 = Product_img_Ref.child(imageUri5.getLastPathSegment() + randomKey + "jpg");
//
//        final UploadTask uploadTask = filepath.putFile(fileUri);
////        final UploadTask uploadTask2 = filepath2.putFile(imageUri2);
////        final UploadTask uploadTask3 = filepath3.putFile(imageUri3);
////        final UploadTask uploadTask4 = filepath4.putFile(imageUri4);
////        final UploadTask uploadTask5 = filepath5.putFile(imageUri5);
//
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                String message  = e.toString();
//                Toast.makeText(AddNewProduct.this, "Error: " + message, Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(AddNewProduct.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
//
//                Task<Uri> urltask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//
//                        if (!task.isSuccessful()){
//                            throw task.getException();
//
//                        }
//                        dowloadimgURL = filepath.getDownloadUrl().toString();
//                        return filepath.getDownloadUrl();
//
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        if (task.isSuccessful()){
//
//                            dowloadimgURL = task.getResult().toString();
//
//                            Toast.makeText(AddNewProduct.this, "Product Image uploaded to Database Successfully", Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    }
//                });
//            }
//        });




        //2
//        uploadTask2.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                String message  = e.toString();
//                Toast.makeText(AddNewProduct.this, "Error: " + message, Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(AddNewProduct.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
//
//                Task<Uri> urltask2 = uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//
//                        if (!task.isSuccessful()){
//                            throw task.getException();
//
//                        }
//                        dowloadimgURL2 = filepath2.getDownloadUrl().toString();
//                        return filepath2.getDownloadUrl();
//
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        if (task.isSuccessful()){
//
//                            dowloadimgURL2 = task.getResult().toString();
//
//                            Toast.makeText(AddNewProduct.this, "Product Image uploaded to Database Successfully", Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    }
//                });
//            }
//        });
//
//
//        //3
//        uploadTask3.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                String message  = e.toString();
//                Toast.makeText(AddNewProduct.this, "Error: " + message, Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(AddNewProduct.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
//
//                Task<Uri> urltask3 = uploadTask3.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//
//                        if (!task.isSuccessful()){
//                            throw task.getException();
//
//                        }
//                        dowloadimgURL3 = filepath3.getDownloadUrl().toString();
//                        return filepath3.getDownloadUrl();
//
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        if (task.isSuccessful()){
//
//                            dowloadimgURL3 = task.getResult().toString();
//
//                            Toast.makeText(AddNewProduct.this, "Product Image uploaded to Database Successfully", Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    }
//                });
//            }
//        });
//
//
//
//
//        //4
//        uploadTask4.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                String message  = e.toString();
//                Toast.makeText(AddNewProduct.this, "Error: " + message, Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(AddNewProduct.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
//
//                Task<Uri> urltask4 = uploadTask4.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//
//                        if (!task.isSuccessful()){
//                            throw task.getException();
//
//                        }
//                        dowloadimgURL4 = filepath4.getDownloadUrl().toString();
//                        return filepath4.getDownloadUrl();
//
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        if (task.isSuccessful()){
//
//                            dowloadimgURL4 = task.getResult().toString();
//
//                            Toast.makeText(AddNewProduct.this, "Product Image uploaded to Database Successfully", Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    }
//                });
//            }
//        });
//
//
//        //5
//        uploadTask5.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                String message  = e.toString();
//                Toast.makeText(AddNewProduct.this, "Error: " + message, Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(AddNewProduct.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
//
//                Task<Uri> urltask5 = uploadTask5.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                    @Override
//                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//
//                        if (!task.isSuccessful()){
//                            throw task.getException();
//
//                        }
//                        dowloadimgURL5 = filepath5.getDownloadUrl().toString();
//                        return filepath5.getDownloadUrl();
//
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Uri> task) {
//                        if (task.isSuccessful()){
//
//                            dowloadimgURL5 = task.getResult().toString();
//
//                            Toast.makeText(AddNewProduct.this, "Product Image uploaded to Database Successfully", Toast.LENGTH_SHORT).show();
//
//                            SaveproductInfo();
//                        }
//                    }
//                });
//            }
//        });

        SaveproductInfo();




    }

    private void SaveproductInfo() {
        progressDialog.setTitle("Add New Product");
        progressDialog.setMessage("Please wait, Adding new Product");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", randomKey);
        productMap.put("date", savecurrentdate);
        productMap.put("time", savecurrenttime);
        productMap.put("description", Description);
        productMap.put("image", dowloadimgURL);
//        productMap.put("image2", dowloadimgURL2);
//        productMap.put("image3", dowloadimgURL3);
//        productMap.put("image4", dowloadimgURL4);
//        productMap.put("image5", dowloadimgURL5);
        productMap.put("category", categories_list);
        productMap.put("price", Price);
        productMap.put("name", ProductName);
        productMap.put("tag1", tags1);
        productMap.put("tag2", tags2);
        productMap.put("tag3", tags3);
        productMap.put("ratemaxusernum", "0");
        productMap.put("star5_0", "0");
        productMap.put("star4_5", "0");
        productMap.put("star4_0", "0");
        productMap.put("star3_5", "0");
        productMap.put("star3_0", "0");
        productMap.put("star2_5", "0");
        productMap.put("star2_0", "0");
        productMap.put("star1_5", "0");
        productMap.put("star1_0", "0");

        prodRef.child(randomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();

                            NotifcationActivity();

//                            String title = "New Products";
//                            String message = "Hello we have added a new products for sale!!!";
//
//                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topic/all", title,message,getApplicationContext(),AddNewProduct.this);
//                            notificationsSender.SendNotifications();

                            if (categories_list.equals("Clothes")){
                                final View popup2 = getLayoutInflater().inflate(R.layout.clothes_info_popup, null);
                                Button addcolorbtn = (Button) popup2.findViewById(R.id.color_btn);
                                Button sizeadd_btn = (Button) popup2.findViewById(R.id.size_btn);
                                Button saveinfobtn = (Button) popup2.findViewById(R.id.save_btn);
                                EditText clothes_color = (EditText) popup2.findViewById(R.id.clothes_color);
                                EditText clothes_size = (EditText) popup2.findViewById(R.id.clothes_size);

                                dialogbuilder2.setView(popup2);
                                dialog2 = dialogbuilder2.create();
                                dialog2.setCanceledOnTouchOutside(false);
                                dialog2.show();

                                addcolorbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String color = clothes_color.getText().toString();

                                        if (color.isEmpty()){
                                            clothes_color.setError("Color is required!");
                                            clothes_color.requestFocus();
                                            return;
                                        }

                                        else{

                                            prodRef.child(randomKey).child("color").push().child("color").setValue(color)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){

                                                                Toast.makeText(AddNewProduct.this, "Color Added", Toast.LENGTH_SHORT).show();

                                                            }
                                                            else {
                                                                progressDialog.dismiss();
                                                                String message = task.getException().toString();
                                                                Toast.makeText(AddNewProduct.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    });

                                        }
                                    }
                                });

                                sizeadd_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String size = clothes_size.getText().toString();

                                        if (size.isEmpty()){
                                            clothes_size.setError("Size is required!");
                                            clothes_size.requestFocus();
                                            return;
                                        }

                                        else{

                                            prodRef.child(randomKey).child("size").push().child("size").setValue(size)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){

                                                                Toast.makeText(AddNewProduct.this, "Size Added", Toast.LENGTH_SHORT).show();

                                                            }
                                                            else {
                                                                progressDialog.dismiss();
                                                                String message = task.getException().toString();
                                                                Toast.makeText(AddNewProduct.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    });

                                        }
                                    }
                                });



                                saveinfobtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(AddNewProduct.this, AddProduct_Activity.class));
                                        progressDialog.dismiss();
                                        Toast.makeText(AddNewProduct.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }

                            else {
                                startActivity(new Intent(AddNewProduct.this, AddProduct_Activity.class));
                                progressDialog.dismiss();
                                Toast.makeText(AddNewProduct.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {
                            progressDialog.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AddNewProduct.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void NotifcationActivity() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(AddNewProduct.this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("New Product Added")
                .setContentText("Hello we have added a new products for sale!!!")
                .setAutoCancel(true);

        Intent intent =  new Intent(AddNewProduct.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(AddNewProduct.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE
        );

        notificationManager.notify(0,builder.build());
    }

//    private void ColorView() {
//        final View popup2 = getLayoutInflater().inflate(R.layout.clothes_info_popup, null);
//
//        RecyclerView color_recycle = (RecyclerView) popup2.findViewById(R.id.color_recycle);
//        color_recycle.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//
//        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");
//
//        FirebaseRecyclerOptions<ColorModel> option = new FirebaseRecyclerOptions.Builder<ColorModel>()
//                .setQuery(reference.child(randomKey).child("color"),ColorModel.class).build();
//
//        FirebaseRecyclerAdapter<ColorModel,ColorViewHolder> adapter = new FirebaseRecyclerAdapter<ColorModel, ColorViewHolder>(option) {
//            @Override
//            protected void onBindViewHolder(@NonNull ColorViewHolder holder, int position, @NonNull ColorModel model) {
//
//                holder.c_color.setText(model.getColor());
//
//            }
//
//            @NonNull
//            @Override
//            public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_list, parent, false);
//                ColorViewHolder colorViewHolder = new ColorViewHolder(view);
//                return colorViewHolder;
//            }
//        };
//
//        color_recycle.setAdapter(adapter);
//        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 1 ,GridLayoutManager.HORIZONTAL,false);
//        color_recycle.setLayoutManager(gridLayoutManager2);
//        adapter.startListening();
//
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        ColorView();
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (randomKey == null){
            Toast.makeText(AddNewProduct.this, "Add Product Canceled", Toast.LENGTH_SHORT).show();
            randomKey = "";
        }
        else{
            prodRef.child(randomKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Product_img_Ref.child(randomKey).delete();

                    }
                }
            });
        }


    }
}