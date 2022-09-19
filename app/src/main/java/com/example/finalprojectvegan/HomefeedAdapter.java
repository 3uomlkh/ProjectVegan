package com.example.finalprojectvegan;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HomefeedAdapter extends RecyclerView.Adapter<HomefeedAdapter.ViewHolder>{

    Fragment fragment;
    private Context context;
    private ArrayList<WritePostInfo> mDataset;

    ImageView homefeed_item_imageView;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public ViewHolder(CardView view) {
            super(view);
            cardView = view;
        }
    }

    public HomefeedAdapter(Context context, ArrayList<WritePostInfo> myDataset) {
        mDataset = myDataset;
        this.context = context;
    }

    @NonNull
    @Override
    public HomefeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.homefeed_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(cardView);
        homefeed_item_imageView = cardView.findViewById(R.id.homefeed_item_imageView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;

        TextView publisherTextView = cardView.findViewById(R.id.homefeed_item_publisher);
        publisherTextView.setText(mDataset.get(position).getPublisher());

        TextView titleTextView = cardView.findViewById(R.id.homefeed_item_title);
        titleTextView.setText(mDataset.get(position).getTitle());

        TextView createdAtTextView = cardView.findViewById(R.id.homefeed_item_createdAt);
        createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));

        TextView contentsTextView = cardView.findViewById(R.id.homefeed_item_contents);
        contentsTextView.setText(mDataset.get(position).getContents());

        String url = mDataset.get(position).getImagePath();
//        Glide.with(holder.cardView)
//                .load(url)
//                .into(holder.homefeed_item_imageView);

//        TextView imagePathTextView = cardView.findViewById(R.id.homefeed_item_imagePath);
//        imagePathTextView.setText(mDataset.get(position).getImagePath());
        Glide.with(cardView).load(url).override(800, 800).into(homefeed_item_imageView);
//        Log.d("url", "url : " + imagePathTextView);
//        loadImage();


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

//    public void loadImage() {
//
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//        StorageReference storageReference = firebaseStorage.getReference();
//        StorageReference pathReference = storageReference.child("posts");
//        if (pathReference == null) {
//
//        } else {
//            StorageReference submitProfile = storageReference.child("posts/" + firebaseUser.getUid() + "/postImage" + System.currentTimeMillis() + ".jpg");
//            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Glide.with(context).load(uri).into(homefeed_item_imageView);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            });
//        }
//    }

}