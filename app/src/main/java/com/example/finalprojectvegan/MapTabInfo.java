package com.example.finalprojectvegan;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.finalprojectvegan.Model.MapData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapTabInfo extends Fragment {
    RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    String addr;
    RestaurantAdapter adapter;
    private ArrayList<String> itemKeyList = new ArrayList<>();
    private ArrayList<String> bookmarkIdList = new ArrayList<>();
    ArrayList<String> listName = new ArrayList<>();
    ArrayList<String> listCategory = new ArrayList<>();
    ArrayList<String> listAddr = new ArrayList<>();
    ArrayList<String> listMenu = new ArrayList<>();
    ArrayList<String> listTime = new ArrayList<>();
    ArrayList<String> listDayoff = new ArrayList<>();
    ArrayList<String> listImage = new ArrayList<>();
    public MapTabInfo() {
        // Required empty public constructor
    }

    public static MapTabInfo newInstance(String param1, String param2) {
        MapTabInfo fragment = new MapTabInfo();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map_tab_info, container, false);
        mAuth = FirebaseAuth.getInstance();

        Bundle bundle = getArguments();
        addr = bundle.getString("addr");

        recyclerView = view.findViewById(R.id.restaurant_recyclerView);
        recyclerView.setItemAnimator(null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RestaurantAdapter();

        mDatabase = FirebaseDatabase.getInstance().getReference("Maps");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    itemKeyList.add(snapshot.getKey());
                    listName.add(snapshot.child("storeName").getValue().toString());
                    listAddr.add(snapshot.child("storeAddr").getValue().toString());
                    listCategory.add(snapshot.child("storeCategory").getValue().toString());
                    listDayoff.add(snapshot.child("storeDayOff").getValue().toString());
                    listTime.add(snapshot.child("storeTime").getValue().toString());
                    listMenu.add(snapshot.child("storeMenu").getValue().toString());
                    listImage.add(snapshot.child("storeImage").getValue().toString());
                }
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                for(int i=0; i<listName.size(); i++) {
                    if(addr.equals(listAddr.get(i))) {
                        MapData data = new MapData(listName.get(i), listAddr.get(i), listCategory.get(i), listImage.get(i));
                        data.setMenu(listMenu.get(i));
                        data.setDayoff(listDayoff.get(i));
                        data.setTime(listTime.get(i));
                        data.setItemKeyList(itemKeyList.get(i));
                        data.setBookmarkIdList(bookmarkIdList);
                        adapter.addItem(data);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        getBooomarkData();

        return view;
    }

    private void getBooomarkData() {
        Log.d("MapTabInfo4", "Map");
        mDatabase = FirebaseDatabase.getInstance().getReference("bookmark");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bookmarkIdList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    bookmarkIdList.add(snapshot.getKey());
                }
                adapter.notifyDataSetChanged();
                Log.d("mapBMList", bookmarkIdList.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("MapTabInfo", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child(mAuth.getCurrentUser().getUid()).child("map_bookmark").addValueEventListener(postListener);
    }
}