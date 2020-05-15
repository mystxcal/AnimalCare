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

public class Signup extends AppCompatActivity {

    TextView emailid = (TextView) findViewById(R.id.emailText);
    TextView pass = (TextView) findViewById(R.id.password);
    Button signupBtn =findViewById(R.id.signupbtn);
    FirebaseAuth fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= emailid.getText().toString();
                String passwd=pass.getText().toString();
                fb = fb.getInstance();

                if(email.isEmpty())
                {
                    emailid.setError("Please enter your email id");
                    emailid.requestFocus();
                }
                else if (passwd.isEmpty())
                {
                    pass.setError("Enter a password");
                    pass.requestFocus();
                }
                else if(!email.isEmpty() && !passwd.isEmpty())
                {
                    fb.createUserWithEmailAndPassword(email,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(Signup.this,"SignUp Failed",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                startActivity(new Intent(Signup.this,HomeActivity.class));
                            }
                        }
                    });
                }
            }
        });




    }
}

