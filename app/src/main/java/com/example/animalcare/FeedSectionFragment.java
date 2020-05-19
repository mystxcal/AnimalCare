package com.example.animalcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedSectionFragment extends Fragment {

    private ArrayList<BlogData> blogDataArrayList;
    private ArrayList<BlogData> tempList;
    private CustomListViewAdapter customListViewAdapter;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;
    private FirebaseAuth firebaseAuth;
    private static FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String ImageUrlsList;

    public FeedSectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed_section, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.listView);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);

        firebaseAuth =FirebaseAuth.getInstance();
        firebaseDatabase = HomeActivity.firebaseDatabase;

        updateTheData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Add a new Post", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(),CreatePostActivity.class);
                //getActivity().finish();
                startActivity(i);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setSize(0);
                Toast.makeText(getContext(), "Feed Refreshed", Toast.LENGTH_LONG).show();
               updateTheData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

//    public void refreshData(){
//
//        blogDataArrayList = new ArrayList<>();
//        for(int i=tempList.size()-1;i>=0;i--){
//            Log.i("ISFROMCACHE","->"+blogDataArrayList.size());
//            if(blogDataArrayList.size()==0){
//                blogDataArrayList.add(tempList.get(i));
//                Log.i("ARRCOUNT","->"+blogDataArrayList.size());
//                customListViewAdapter = new CustomListViewAdapter(getContext(),blogDataArrayList);
//                listView.setAdapter(customListViewAdapter);
//            }else {
//                blogDataArrayList.add(tempList.get(i));
//                customListViewAdapter.notifyDataSetChanged();
//            }
//        }
//
//    }

    public void updateTheData(){
        databaseReference = firebaseDatabase.getReference("BlogData");

        //Query query = databaseReference.orderByChild("blogtimeStamp").limitToLast(20);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                blogDataArrayList = new ArrayList<>();
                tempList = new ArrayList<>();
                for(DataSnapshot blogPost: dataSnapshot.getChildren()){

                    tempList.add(blogPost.getValue(BlogData.class));
                }

                for(int i=tempList.size()-1;i>=0;i--){
                    Log.i("ISFROMCACHE","->"+blogDataArrayList.size());
                    if(blogDataArrayList.size()==0){
                        blogDataArrayList.add(tempList.get(i));
                        Log.i("ARRCOUNT","->"+blogDataArrayList.size());
                        customListViewAdapter = new CustomListViewAdapter(getContext(),blogDataArrayList);
                        listView.setAdapter(customListViewAdapter);
                    }else {
                        blogDataArrayList.add(tempList.get(i));
                        customListViewAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
