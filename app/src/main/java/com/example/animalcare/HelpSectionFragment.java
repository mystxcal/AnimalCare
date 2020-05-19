package com.example.animalcare;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpSectionFragment extends Fragment {

    private LocationListener locationListener;
    private LocationManager locationManager;
    private TextView userLocationTv;
    private List<Address> addressList;
    private Button getHelpBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase  firebaseDatabase;
    private Location location;
    private ImageView animalImageView;
    private Bitmap animalBitmap;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private UUID uniqueId;
    private Spinner spinner;
    private String animalType,imageurl;
    private Uri imageUri;
    private ProgressDialog progressDialog;


    public HelpSectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help_section, container, false);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i("HEREIS","FALSE");
            getLiveLocation();
        }

        if(requestCode == 2 &&  grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED ){
            getImageFromCamera();
        }

    }

    public void getLiveLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.i("HEREIS","TRUE");
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Geocoder geocoder =new Geocoder(getContext());
        try {
            if(location != null) {
                addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addressList.get(0) != null) {
                    Address address = addressList.get(0);
                    userLocationTv.setText(address.getAddressLine(0));
                    Log.i("USERLOC", addressList.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("HEREIS","TRUE");
        Log.i("IMGURI","-> "+ imageUri.toString());
       // imageurl =getRealPathFromURI(imageUri);
        //Log.i("IMGurl","-> "+ imageurl);


        if(requestCode == 1 && resultCode == RESULT_OK ) {

            try {
                animalBitmap = MediaStore.Images.Media.getBitmap(
                        getContext().getContentResolver(), imageUri);
                animalImageView.setImageBitmap(animalBitmap);
                Log.i("IMGURI","-> "+ imageUri.toString());
               // imageurl =getRealPathFromURI(imageUri);
                Log.i("IMGurl","-> "+ imageurl);
            } catch (Exception e) {
                e.printStackTrace();
            }

//                Bundle bundle = data.getExtras();
//             animalBitmap = (Bitmap) bundle.get("data");
//            animalImageView.setImageBitmap(animalBitmap);
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void getImageFromCamera(){
//        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (i.resolveActivity(getContext().getPackageManager()) != null) {
//            startActivityForResult(i,1);
//        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContext().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Log.i("IMGURI2","-> "+ imageUri.toString());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        userLocationTv = view.findViewById(R.id.TvLocation);
        animalImageView = view.findViewById(R.id.imageViewAnimal);
        getHelpBtn = view.findViewById(R.id.BtnGetHelp);
        firebaseAuth = firebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
        firebaseStorage = firebaseStorage.getInstance();
        final ArrayList<String> animals = new ArrayList<>();
        animals.add("Select Animal");
        animals.add("Dog");
        animals.add("Cat");

        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,animals);

        spinner = view.findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);


        final ImageButton locationBtn = view.findViewById(R.id.BtnGetLocation);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }
                else{
                    getLiveLocation();
                }
            }
        });


        animalImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
                }
                else{
                    getImageFromCamera();
                }
            }
        });

        getHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                animalType = spinner.getSelectedItem().toString();

                if(userLocationTv.getText().toString().equals("Location Details")){
                    Toast.makeText(getContext(), "Please update your Live Location !", Toast.LENGTH_SHORT).show();
                }

                if(animalType.equals("Select Animal")){
                    Toast.makeText(getContext(), "Please select Animal Type !", Toast.LENGTH_SHORT).show();
                }

                if(animalBitmap==null){
                    Toast.makeText(getContext(), "Please capture the Image of injured Animal !", Toast.LENGTH_SHORT).show();
                }
                else if(!userLocationTv.getText().toString().equals("Location Details") && !animalType.equals("Select Animal")) {

                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Getting help");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    uniqueId = UUID.randomUUID();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    animalBitmap.compress(Bitmap.CompressFormat.JPEG,65, byteArrayOutputStream);
                    byte[] animalImageByteArray = byteArrayOutputStream.toByteArray();
                    storageReference = firebaseStorage.getReference("Animal Case Images");
                    UploadTask uploadTask = storageReference.child(uniqueId.toString()).putBytes(animalImageByteArray);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Toast.makeText(getContext(), "Image Uploading Successful !", Toast.LENGTH_SHORT).show();
                            uploadData();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.cancel();
                            Toast.makeText(getContext(), "Image Uploading Failed !", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    public void uploadData(){
        animalType = spinner.getSelectedItem().toString();

        if(!userLocationTv.getText().toString().equals("Location Details") && !userLocationTv.getText().toString().equals("")){
            if(location!=null && !animalType.equals("Select Animal")) {
                String UserName = firebaseAuth.getCurrentUser().getEmail();
                String userLocation = userLocationTv.getText().toString();
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                AnimalHelpCase helpCase = new AnimalHelpCase(UserName,animalType,userLocation,lat,lng);
                DatabaseReference databaseReference = firebaseDatabase.getReference("Cases");
                databaseReference.child(uniqueId.toString()).setValue(helpCase).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            progressDialog.cancel();
                            Toast.makeText(getContext(), "Case Created Succesfully", Toast.LENGTH_SHORT).show();
                            Log.i("HELPCASE","Case Created Succesfully");
                        }
                        else {
                            progressDialog.cancel();
                            Toast.makeText(getContext(), "Can't create a case", Toast.LENGTH_SHORT).show();
                            Log.i("HELPCASE","Can't create a case");
                        }
                    }
                });
            }
        }
    }

}
