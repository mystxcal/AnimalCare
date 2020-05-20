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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class RescueActivity extends AppCompatActivity {

    Button rescueLogOut;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String a,b,c,d,e,f;
    TextView name,phone,animal,latLng,loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue);

        subscribeToTopic();

        rescueLogOut = findViewById(R.id.BtnRescueLogout);
        name = findViewById(R.id.RuserName);
        phone = findViewById(R.id.Rphone);
        latLng = findViewById(R.id.Rlatlng);
        loc = findViewById(R.id.Rlocation);
        animal = findViewById(R.id.Ranimal);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Toast.makeText(this, "Rescue Activity !", Toast.LENGTH_SHORT).show();

        rescueLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Toast.makeText(RescueActivity.this, "LogOut Successfull", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RescueActivity.this, MainActivity.class);
                finish();
                startActivity(i);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            Log.i("OKISHERE","HOLA");
            if(bundle.getString("FromWhere","NO").equals("NOTIF")) {
                a = bundle.getString("USERNAME", "UserName");
                b = bundle.getString("PHONENO", "Phone No");
                c = bundle.getString("ANIMALTYPE", "Animal Type ?");
                d = bundle.getString("LAT", "Lati");
                e = bundle.getString("LNG", "Longi");
                f = bundle.getString("Location", "Location?");
                Log.i("GETFINALHERE", a + " " + b + " " + c + " " + d + " " + e + " " + f);
                name.setText(a);
                phone.setText(b);
                loc.setText(f);
                animal.setText(c);
                latLng.setText(d + "," +e);
                Toast.makeText(this, "Rescue Details Loaded", Toast.LENGTH_SHORT).show();
            }
        }
        Log.i("GETISHERE","OKOK");
    }

    public void subscribeToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("animalhelp")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed to Topic";//getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = "Failed to subscribe";//getString(R.string.msg_subscribe_failed);
                        }
                        Log.i("TAGSEND",msg);
                        Toast.makeText(RescueActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
