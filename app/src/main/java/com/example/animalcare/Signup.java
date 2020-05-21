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

public class Signup extends AppCompatActivity {

    FirebaseAuth fb;
    TextView emailid, pass ;
    Button signupBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

         emailid = (TextView) findViewById(R.id.emailText);
         pass = (TextView) findViewById(R.id.LoginPassword);
         signupBtn =findViewById(R.id.Signupbtn2);



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
                            if (task.isSuccessful()) {
                            Toast.makeText(Signup.this,"SignUp Successfull",Toast.LENGTH_LONG).show();
                                sendemailverification();



                            } else {
                                Toast.makeText(Signup.this, "SignUp Unsuccessfull!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }
        });

    }
    private void sendemailverification(){
        FirebaseUser user = fb.getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Signup.this, "Registration Successful! Verification Mail Sent", Toast.LENGTH_LONG).show();
                        fb.signOut();
                        finish();
                        Intent i = new Intent(Signup.this , MainActivity.class);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(Signup.this, "Verification Mail Not sent", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}

