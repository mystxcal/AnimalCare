package com.example.animalcare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class editprofile extends AppCompatActivity {

    EditText nameT,aboutT;
    TextView contactT;
    Button save;
    FirebaseDatabase mydatabase;
    profileData a;



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
            }
        }
    }


    //public profile(String uid)
    // {
    //this.uid=uid;
    //}

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

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

                String name=nameT.getText().toString();
                String about=aboutT.getText().toString();
                String no=contactT.getText().toString();

                profileData records=new profileData(name,about,no);

                if(!name.isEmpty() && !about.isEmpty() && !no.isEmpty())
                {
                    FirebaseUser u= FirebaseAuth.getInstance().getCurrentUser();

                    DatabaseReference dr=mydatabase.getReference("ProfileData");
                    // EditText et=(EditText) findViewById(R.id.nametxt);
                    //et.setText(a.getName());
                    //EditText at=(EditText) findViewById(R.id.nametxt);
                    //at.setText(a.getAbout());
                    // EditText ct=(EditText) findViewById(R.id.nametxt);
                    //ct.setText(a.getNo());

                    dr.child(u.getUid()).setValue(records).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(editprofile.this,"Successfully Saved",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(editprofile.this,"Unsuccessfull",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(editprofile.this,"FIll all Details",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data !=null)
        {
            Uri selectedImage=data.getData();
            try{
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                CircleImageView image=(CircleImageView) findViewById(R.id.profileImage);
                image.setImageBitmap(bitmap);
            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}

