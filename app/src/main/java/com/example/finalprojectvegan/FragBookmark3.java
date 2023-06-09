package com.example.finalprojectvegan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectvegan.Model.RecipeData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// 레시피 북마크
public class FragBookmark3 extends Fragment {
    private ArrayList<String> itemKeyList = new ArrayList<>();
    private ArrayList<String> bookmarkIdList = new ArrayList<>();
    ArrayList<String> listTitle = new ArrayList<>();
    ArrayList<String> listThumb = new ArrayList<>();
    ArrayList<String> clickUrl = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private View view;
    RecyclerView recyclerView;
    BookmarkAdapter adapter;
    public FragBookmark3() {

    }

    public static FragBookmark3 newInstance(String param1, String param2) {
        FragBookmark3 fragment = new FragBookmark3();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        view = inflater.inflate(R.layout.fragment_frag_bookmark3, container, false);

        recyclerView = view.findViewById(R.id.bookmark3_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new BookmarkAdapter();

        getBookmarkData();

        return view;
    }
    private void getCategoryData() {
        mDatabase = FirebaseDatabase.getInstance().getReference("recipe");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("FragBookmark3","KEY : " + snapshot.getKey());
                    if(bookmarkIdList.contains(snapshot.getKey())) {
                        itemKeyList.add(snapshot.getKey());
                    }

                }

                for(int i=0; i<listTitle.size(); i++) {
                    RecipeData data = new RecipeData(listThumb.get(i), listTitle.get(i), clickUrl.get(i));

                    data.setItemKeyList(itemKeyList.get(i));
                    data.setBookmarkIdList(bookmarkIdList);
                    adapter.addItem(data);
                }

                Log.d("FragBookmark3",listTitle.toString());
                Log.d("FragBookmark3",listThumb.toString());
                Log.d("FragBookmark3",clickUrl.toString());
                Log.d("FragBookmark3",itemKeyList.toString());
                Log.d("FragBookmark3",bookmarkIdList.toString());

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("RecipeFragment", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    private void getBookmarkData() {
        mDatabase = FirebaseDatabase.getInstance().getReference("bookmark");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bookmarkIdList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    bookmarkIdList.add(snapshot.getKey());
                    listTitle.add(snapshot.child("title").getValue().toString());
                    listThumb.add(snapshot.child("imageUrl").getValue().toString());
                    clickUrl.add(snapshot.child("clickUrl").getValue().toString());

                    Log.d("booktitle",snapshot.child("title").getValue().toString());
                }

                getCategoryData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("RecipeFragment", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child(mAuth.getCurrentUser().getUid()).child("recipe_bookmark").addValueEventListener(postListener);
    }
}