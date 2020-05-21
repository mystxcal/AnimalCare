package com.example.animalcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class editprofile extends AppCompatActivity {

    EditText nameT,aboutT;
    TextView contactT;
    Button save;
    FirebaseDatabase mydatabase;
    StorageReference store;
    ProgressBar mprogressbar;
    profileData a;
    Uri selectedImage;
    CircleImageView image;
    DatabaseReference dr;
    profileData records;
    ProgressDialog progressDialog;
    Bitmap bitmap;
    private  int isHome=0;

    FirebaseUser u= FirebaseAuth.getInstance().getCurrentUser();


    public void getPhoto(){
        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent,1);
    }
    CircleImageView imagedef;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getPhoto();
                //upload();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        isHome = getIntent().getIntExtra("home",0);
        Log.i("ISHOME","->"+isHome);
        imagedef=(CircleImageView) findViewById(R.id.profileImage);
        imagedef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }
                else
                {
                    getPhoto();
                    //upload();
                }
            }
        });

        nameT =findViewById(R.id.nametxt);
        aboutT=findViewById(R.id.abouttxt);
        contactT=findViewById(R.id.contactno);

        save=findViewById(R.id.Savebtn);
        mydatabase=FirebaseDatabase.getInstance();



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(editprofile.this);
                progressDialog.setMessage("Upadting your profile, Please Wait :)");
                progressDialog.setCancelable(false);
                progressDialog.show();

                String name = nameT.getText().toString();
                String about = aboutT.getText().toString();
                String no = contactT.getText().toString();
                //url();

                store = FirebaseStorage.getInstance().getReference("ProfileImg");
                //upload();
                records = new profileData(name, about, no);

                if (!name.isEmpty() && !about.isEmpty() && !no.isEmpty() && bitmap != null) {
//                    if(bitmap==null){
//                        Toast.makeText(editprofile.this,"Please Select a Image",Toast.LENGTH_LONG).show();
//                        progressDialog.cancel();
//                        return;
//                    }else {
                    dr = mydatabase.getReference("ProfileData");

                    dr.child(u.getUid()).setValue(records).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(editprofile.this, "Successfully Saved", Toast.LENGTH_LONG).show();
                                upload();
                            } else {
                                progressDialog.cancel();
                                Toast.makeText(editprofile.this, "Unsuccessfull,Please Try Again !", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(editprofile.this, "Fill all Details", Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                }

            }


        });


    }
  /* public  void url()
    {
        StorageReference ref=store.child(u.getUid()+"."+fileExtension(selectedImage));
        ref.child(u.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageurl=uri.toString();
                profileData c=new profileData(imageurl);
                c.setImageUrl(imageurl);


            }
        });
        //return 0;
    }*/
public String fileExtension(Uri uri)
{
    ContentResolver cr=getContentResolver();
    MimeTypeMap mtp=MimeTypeMap.getSingleton();
    return MimeTypeMap.getFileExtensionFromUrl(cr.getType(uri));
}
 private void upload()
 {

     StorageReference ref=store.child(u.getUid());//+"."+fileExtension(selectedImage));
     Log.i("IMGURI","->"+selectedImage);
     ref.putFile(selectedImage)
             .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     // Get a URL to the uploaded content
                    // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                     Toast.makeText(editprofile.this,"Photo uploaded",Toast.LENGTH_LONG).show();
                     progressDialog.cancel();
                     Log.i("yessss", "onSuccess");
                     if(isHome ==0) {
                         //Intent i = new Intent(editprofile.this , HomeActivity.class);
                         finish();
                         //startActivity(i);
                       }else  if(isHome==1){
                         Intent i = new Intent(editprofile.this , HomeActivity.class);
                         finish();
                         startActivity(i);
                    }

                 }
             })
             .addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception exception) {
                     // Handle unsuccessful uploads
                     // ...
                     Toast.makeText(editprofile.this,"Error while Uploading Photo,Please Try Again",Toast.LENGTH_LONG).show();
                     progressDialog.cancel();
                     Log.i("NOOOOO", "onFailure: ");
                 }
 });
 }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data !=null)
        {
            selectedImage=data.getData();

            try{
                bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                image=(CircleImageView) findViewById(R.id.profileImage);
                image.setImageBitmap(bitmap);
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}

