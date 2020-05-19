package com.example.animalcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {

FirebaseDatabase dt;
DatabaseReference reff;
TextView a,b,c;
Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

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
                FirebaseUser u= FirebaseAuth.getInstance().getCurrentUser();
                //String x=u.toString();
                reff=FirebaseDatabase.getInstance().getReference("ProfileData").child(u.getUid());

                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        String name=dataSnapshot.child("name").getValue().toString();
                        String about=dataSnapshot.child("about").getValue().toString();
                        String no=dataSnapshot.child("no").getValue().toString();
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
