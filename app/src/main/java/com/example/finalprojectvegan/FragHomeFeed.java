package com.example.finalprojectvegan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FragHomeFeed extends Fragment {
    public static final String ARG_OBJECT = "object";


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragHomeFeed() {
        // Required empty public constructor
    }

    public static FragHomeFeed newInstance(String param1, String param2) {
        FragHomeFeed fragment = new FragHomeFeed();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_frag_home_feed, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<WritePostInfo> postList = new ArrayList<>();


                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                postList.add(new WritePostInfo(
                                        documentSnapshot.getData().get("title").toString(),
                                        documentSnapshot.getData().get("contents").toString(),
                                        documentSnapshot.getData().get("publisher").toString(),
                                        documentSnapshot.getData().get("imagePath").toString(),
                                        new Date(documentSnapshot.getDate("createdAt").getTime())));
                            }
                            RecyclerView recyclerView = view.findViewById(R.id.homefeed_recyclerView);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                            RecyclerView.Adapter mAdapter = new HomefeedAdapter(getActivity(), postList);
                            recyclerView.setAdapter(mAdapter);

                        } else {
                            Log.d("error", "Error getting documents", task.getException());
                        }
                    }
                });

//        db.collection("user")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//
//                            ArrayList<UserInfo> postUserList = new ArrayList<>();
//
//
//                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                                Log.d("success", documentSnapshot.getId() + " => " + documentSnapshot.getData());
//                                postUserList.add(new UserInfo(
//                                        documentSnapshot.getData().get("userID").toString(),
//                                        documentSnapshot.getData().get("userEmail").toString(),
//                                        documentSnapshot.getData().get("userPassword").toString()));
//                            }
//                            RecyclerView recyclerView = view.findViewById(R.id.homefeed_recyclerView);
//                            recyclerView.setHasFixedSize(true);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//                            RecyclerView.Adapter mAdapter = new UserInfoAdapter(getActivity(), postUserList);
//                            recyclerView.setAdapter(mAdapter);
//
//                        } else {
//                            Log.d("error", "Error getting documents", task.getException());
//                        }
//                    }
//                });
//
        return view;
    }
}