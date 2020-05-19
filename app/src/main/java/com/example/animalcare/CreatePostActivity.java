package com.example.animalcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreatePostActivity extends AppCompatActivity {

    ImageView cancelImageView,addAnimalPhoto;
    Button postBtn;
    Bitmap animalBitmap;
    EditText addDescriptionET;
    private Uri imageUri;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 2 &&  grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED ){
            getImageFromCamera();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("HEREIS","TRUE");
        Log.i("IMGURI","-> "+ imageUri.toString());

        if(requestCode == 1 && resultCode == RESULT_OK ) {

            try {
                animalBitmap = MediaStore.Images.Media.getBitmap(
                        CreatePostActivity.this.getContentResolver(), imageUri);
                addAnimalPhoto.setImageBitmap(animalBitmap);
                Log.i("IMGURI","-> "+ imageUri.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private void getImageFromCamera(){

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = CreatePostActivity.this.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Log.i("IMGURI2","-> "+ imageUri.toString());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        cancelImageView = findViewById(R.id.imageViewCancel);
        addAnimalPhoto = findViewById(R.id.ImageVIewAddPhoto);
        postBtn = findViewById(R.id.BtnPost);
        addDescriptionET = findViewById(R.id.EditTextBlogPost);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = HomeActivity.firebaseDatabase;
        firebaseStorage = FirebaseStorage.getInstance();

        addAnimalPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(CreatePostActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(CreatePostActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                }
                else{
                    getImageFromCamera();
                }
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String blogDescr = addDescriptionET.getText().toString();

                if(TextUtils.isEmpty(blogDescr)){
                    Toast.makeText(CreatePostActivity.this, "Please add some Description !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(animalBitmap == null){
                    Toast.makeText(CreatePostActivity.this, "Please add a Photo !", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = new ProgressDialog(CreatePostActivity.this);
                progressDialog.setMessage("Uploading your Post....");
                progressDialog.setCancelable(false);
                progressDialog.show();

                final FirebaseUser blogUser = firebaseAuth.getCurrentUser();
                if(blogUser!=null){
                    databaseReference = firebaseDatabase.getReference("BlogData").push();

                    final String key = databaseReference.getKey();

                     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                     animalBitmap.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);
                     byte[] myByteArray = byteArrayOutputStream.toByteArray();

                    storageReference = firebaseStorage.getReference("BlogImages");
                    storageReference.child(key).putBytes(myByteArray)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                     storageReference.child(key).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Toast.makeText(CreatePostActivity.this, "Blog Image Uploaded !", Toast.LENGTH_SHORT).show();
                                            Date c = Calendar.getInstance().getTime();
                                            System.out.println("Current time => " + c);
                                            SimpleDateFormat df = new SimpleDateFormat("h:mm a dd-MMM-yyyy ");
                                            String formattedDate = df.format(c);
                                            String imageUrl = uri.toString();
                                            BlogData myBlogData = new BlogData(blogUser.getEmail(),formattedDate,blogDescr,blogUser.getUid());
                                            myBlogData.setImageURL(imageUrl);
                                            myBlogData.setPostKey(key);

                                            addTheBlogPost(myBlogData);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.cancel();
                                            Toast.makeText(CreatePostActivity.this, "Uploading blog image Failed !", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                }
            }
        });

        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(CreatePostActivity.this,MainActivity.class);
                finish();
                //startActivity(intent);
            }
        });

    }

    public void addTheBlogPost(BlogData myBlogData){

        databaseReference.setValue(myBlogData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CreatePostActivity.this, "Post Created Successfully !", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                    //Intent intent = new Intent(CreatePostActivity.this,MainActivity.class);
                    finish();
                    //startActivity(intent);
                }else{
                    progressDialog.cancel();
                    Toast.makeText(CreatePostActivity.this, "Post Creation Failed :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

