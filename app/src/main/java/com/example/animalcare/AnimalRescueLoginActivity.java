package com.example.animalcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnimalRescueLoginActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AnimalRescueLoginActivity.this,MainActivity.class);
        finish();
        startActivity(intent);
    }

    EditText usernameET , passwordET;
    Button loginbtn;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String tempPwd = "";
    boolean pwdMatched= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_rescue_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        usernameET = findViewById(R.id.EditTextRescueUsername);
        passwordET = findViewById(R.id.EditTextRescuePassword);
        loginbtn = findViewById(R.id.BtnRescueLogin);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = usernameET.getText().toString();
                String password = passwordET.getText().toString();

                if(TextUtils.isEmpty(userName)){
                    usernameET.setError("Please enter the email!");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    passwordET.setError("Please enter the password!");
                    return;
                }
                if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)){

                    firebaseAuth.signInWithEmailAndPassword(userName,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AnimalRescueLoginActivity.this, "Login Successful :)", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AnimalRescueLoginActivity.this,RescueActivity.class);
                                finish();
                                startActivity(intent);
                            }else{
                                Toast.makeText(AnimalRescueLoginActivity.this, "Login Failed :)", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AnimalRescueLoginActivity.this,MainActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        }
                    });

//                    databaseReference = firebaseDatabase.getReference("RescueData");
//                    databaseReference.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if(dataSnapshot.getValue()!=null){
//                               tempPwd = dataSnapshot.getValue().toString();
//                            }
//                        }
//


                }

            }
        });

    }
}
