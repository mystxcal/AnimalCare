package com.example.animalcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends AppCompatActivity {

FirebaseDatabase dt;
DatabaseReference reff;
static TextView a,b,c;
CircleImageView image;
Button refresh;
private ProgressDialog progressDialog;

    FirebaseStorage storage=FirebaseStorage.getInstance();
    FirebaseUser u= FirebaseAuth.getInstance().getCurrentUser();
    StorageReference sr=storage.getReference("ProfileImg").child(u.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        progressDialog = new ProgressDialog(profile.this);
        progressDialog.setMessage("Loading your Profile !");
        progressDialog.setCancelable(false);
        progressDialog.show();

        image=findViewById(R.id.profileI);

        Button edit=findViewById(R.id.Editbtn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(profile.this, editprofile.class);
                finish();
                startActivity(intent);
            }
        });

        a=(TextView) findViewById(R.id.namet);
        b=(TextView) findViewById(R.id.aboutt);
        c=(TextView) findViewById(R.id.contactt);
//        if(a.getText().toString().equals("")){
//            Toast.makeText(profile.this, "Please update your profile", Toast.LENGTH_LONG).show();
//            Intent intent =new Intent(profile.this, editprofile.class);
//            finish();
//            startActivity(intent);
//        }

                reff=FirebaseDatabase.getInstance().getReference("ProfileData").child(u.getUid());

                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child("name").getValue()!=null){
                            Log.i("ISNULL","TRUE");
//                            Toast.makeText(profile.this, "Please update your profile", Toast.LENGTH_LONG).show();
//                            Intent intent =new Intent(profile.this, editprofile.class);
//                            finish();
//                            startActivity(intent);
                        //}else {
                            String name = dataSnapshot.child("name").getValue().toString();
                            String about = dataSnapshot.child("about").getValue().toString();
                            String no = dataSnapshot.child("no").getValue().toString();
                            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
                            editprofile x;

                            a.setText(name);
                            b.setText(about);
                            c.setText(no);
                            loadImage();
                            //Toast.makeText(profile.this, "Refreshed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("data retrive","failed");
                    }
                });

            //}
       // });



    }

    public void loadImage(){
        try{
            final File files =File.createTempFile("image","jpeg");

            sr.getFile(files).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap= BitmapFactory.decodeFile(files.getAbsolutePath());
                    if(bitmap!=null) {
                        image.setImageBitmap(bitmap);
                        Toast.makeText(profile.this, "Profile Loaded !", Toast.LENGTH_LONG).show();
                        progressDialog.cancel();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(profile.this,"Error while loading...",Toast.LENGTH_LONG).show();
                    progressDialog.cancel();
                }
            });}
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
