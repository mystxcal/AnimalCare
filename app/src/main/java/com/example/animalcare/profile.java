package com.example.animalcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
TextView a,b,c;
CircleImageView image;
Button refresh;

    FirebaseStorage storage=FirebaseStorage.getInstance();
    FirebaseUser u= FirebaseAuth.getInstance().getCurrentUser();
    StorageReference sr=storage.getReference("ProfileImg").child(u.getUid()+".jpeg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);



        image=(CircleImageView) findViewById(R.id.profileImage);
        try{
            final File files =File.createTempFile("image","jpeg");

        sr.getFile(files).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap= BitmapFactory.decodeFile(files.getAbsolutePath());
                image.setImageBitmap(bitmap);
                Toast.makeText(profile.this,"image set",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile.this,"image not set",Toast.LENGTH_LONG).show();
            }
        });}
        catch(IOException e)
    {
        e.printStackTrace();
    }

        Button edit=findViewById(R.id.Editbtn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(profile.this, editprofile.class);
                startActivity(intent);
            }
        });

        a=(TextView) findViewById(R.id.namet);
        b=(TextView) findViewById(R.id.aboutt);
        c=(TextView) findViewById(R.id.contactt);
       // refresh=(Button) findViewById(R.id.Refresh);

        //refresh.setOnClickListener(new View.OnClickListener() {
           // @Override
            //public void onClick(View view) {
                //FirebaseUser u= FirebaseAuth.getInstance().getCurrentUser();
                //String x=u.toString();
                reff=FirebaseDatabase.getInstance().getReference("ProfileData").child(u.getUid());

                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        String name=dataSnapshot.child("name").getValue().toString();
                        String about=dataSnapshot.child("about").getValue().toString();
                        String no=dataSnapshot.child("no").getValue().toString();
                        FirebaseUser u= FirebaseAuth.getInstance().getCurrentUser();
                        editprofile x;

                        a.setText(name);
                        b.setText(about);
                        c.setText(no);
                        Toast.makeText(profile.this,"Refreshed",Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.i("data retrive","failed");
                    }
                });

            //}
       // });



    }
}
