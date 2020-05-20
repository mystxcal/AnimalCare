package com.example.animalcare;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPass extends AppCompatActivity {

    FirebaseAuth fb;
    TextView email;
    Button reset;
    public void resetpass (View view)
    {
        String stremail= email.getText().toString();

        if(stremail.isEmpty())
        {
            email.setError("Please enter your email id");
            email.requestFocus();
        }
        else {
            fb.sendPasswordResetEmail(stremail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPass.this, "Reset Link Sent to Email", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpass);
        fb=FirebaseAuth.getInstance();

        email=findViewById(R.id.frgtemail);
        reset=findViewById(R.id.resetbutton);




    }
}