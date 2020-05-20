package com.example.animalcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth fb;
    Button signUp, loginBtn,frgtpass;
    EditText userName, userPassword;
    String email,pwd;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    TextView animalRescue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        animalRescue = findViewById(R.id.TextViewAnimalRescue);
        
        animalRescue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Animal Rescue", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,AnimalRescueLoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

        fb= FirebaseAuth.getInstance();
        FirebaseUser fbUser = fb.getCurrentUser();
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.i("TAGGY", "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//
//                        // Log and toast
//                        String msg = token;//getString(R.string.msg_token_fmt, token);
//                        //Log.d(TAG, msg);
//                        Log.i("TOKKY",msg);
//                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });


        if(fbUser!=null && fbUser.isEmailVerified()){
            Intent i = new Intent(MainActivity.this , HomeActivity.class);
            finish();
            startActivity(i);
        }
        else if(fbUser!=null && !fbUser.isEmailVerified()){
            Intent i = new Intent(MainActivity.this , RescueActivity.class);
            finish();
            startActivity(i);
        }

        frgtpass=findViewById(R.id.frgtpassbutton);
        signUp = findViewById(R.id.Signupbtn);
        loginBtn = findViewById(R.id.Loginbtn);
        userName = findViewById(R.id.LoginUsername);
        userPassword = findViewById(R.id.LoginPassword);


        frgtpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ForgotPass.class));
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Signup.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate()) {
                    fb.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                checkIfEmailIsVerified();
                            }
                            else {

                                Toast.makeText(MainActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    public void checkIfEmailIsVerified(){
        FirebaseUser currentUser = fb.getCurrentUser();
        if(currentUser.isEmailVerified()){
            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            finish();
            startActivity(i);

        }
        else {
            Toast.makeText(MainActivity.this, "User Not Verified, Please Check your mail for verification purpose !", Toast.LENGTH_LONG).show();
        }
    }

    public boolean validate() {
         email = userName.getText().toString();
         pwd = userPassword.getText().toString();
        fb = fb.getInstance();

        if (email.isEmpty()) {
            userName.setError("Please enter your email id");
            userName.requestFocus();
        } else if (pwd.isEmpty()) {
            userPassword.setError("Enter your password");
            userPassword.requestFocus();

        } else if(!email.isEmpty() && !pwd.isEmpty()) {
            return true;
        }
        return false;
    }
}
