package com.food.android.app.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.food.android.app.R;
import com.food.android.app.adapters.AdapterReviews;
import com.food.android.app.models.Item;
import com.food.android.app.models.ReviewModel;
import com.food.android.app.utils.SharedPreferences;
import com.food.android.app.utils.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private SliderLayout imageSlider;
    private LinearLayout submitReview;
    private EditText editTextReview;
    private RatingBar ratingBar;
    private RecyclerView recyclerViewReview;
    private AdapterReviews adapterReviews;
    private ArrayList<ReviewModel> reviewModelList;
    private DatabaseReference databaseReference;
    private DatabaseReference itemdatabaseReference;
    private TextView titleTv;
    private TextView descriptionTv,priceTv,categoryTv,communityTv,countryTv,shopTv,toolbarTitle;
    private Item data;
    private String userId;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("reviews");
        itemdatabaseReference = FirebaseDatabase.getInstance().getReference().child("table_items");

        SharedPreferences.getPrefernces(this);

        initViews();

        data = getIntent().getParcelableExtra("data");

        if(data == null) {
            userId = getIntent().getStringExtra("userId");
            getItem();
        }

        if(data!=null) {
            initToolbar();

            setData();
            getReviews();
           // setImage();
            //imageSlider();

        }



    }

    private void initViews() {
        titleTv = findViewById(R.id.titleTv);
        descriptionTv = findViewById(R.id.descriptionTv);
        priceTv = findViewById(R.id.price);
        shopTv = findViewById(R.id.shop);
        categoryTv = findViewById(R.id.category);
        communityTv = findViewById(R.id.community);
        countryTv = findViewById(R.id.country);
        imageView = findViewById(R.id.image);
        ratingBar = findViewById(R.id.rating);
        submitReview = findViewById(R.id.submitReview);
        editTextReview = findViewById(R.id.editTextReview);
        recyclerViewReview = findViewById(R.id.recyclerViewReview);
        submitReview.setOnClickListener(this);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingBar.setRating(rating);
            }
        });
    }

    public void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbar.setTitle(data.getName());
        toolbarTitle.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        toolbarTitle.setText(Utility.toTitleCase(data.getName()));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setData(){

        reviewModelList = new ArrayList<>();
        titleTv.setText(data.getName());
        descriptionTv.setText("Customer Says: " + data.getDescription());
        priceTv.setText("Price: " + data.getPrice());
        shopTv.setText("Shop Location: " +data.getLocation());
        categoryTv.setText("Category: " +data.getCategory());
        communityTv.setText("Community: " + data.getCommunity());
        countryTv.setText("Country: " + data.getCountry());
        Glide.with(this)
                .load(data.getImage())
                .into(imageView);

        recyclerViewReview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        adapterReviews = new AdapterReviews(DetailsActivity.this, reviewModelList);
        recyclerViewReview.setAdapter(adapterReviews);
    }

    private void setImage(){
        Glide.with(this)
                .load(data.getImage())
                .into(imageView);
    }

    private void imageSlider(){

        imageSlider = findViewById(R.id.imageSlider);
        imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :

        for (int i=0; i<4 ; i++) {

            DefaultSliderView sliderView = new DefaultSliderView(this);

            switch (i) {
                case  0: sliderView.setImageDrawable(R.drawable.ic_launcher_background); break;
                case 1: sliderView.setImageUrl( "https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"); break;
                case 2: sliderView.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260"); break;
                case 3: sliderView.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"); break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
            sliderView.setDescription("");
            imageSlider.addSliderView(sliderView);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.submitReview:

                if (editTextReview.getText().toString().isEmpty()){
                    editTextReview.setError("empty");
                    editTextReview.setFocusable(true);
                    return;
                }

                uploadReview(editTextReview.getText().toString(),ratingBar.getRating());

                editTextReview.setText("");
                break;
        }
    }

    private void getItem(){
        itemdatabaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot!=null) {
                    data = dataSnapshot.getValue(Item.class);
                    initToolbar();
                    setData();
                    getReviews();
                   // setImage();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e( "onCancelled: ",databaseError.getMessage());
            }
        });
    }


    private void getReviews(){

        reviewModelList.clear();
        databaseReference.child(data.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    ReviewModel reviewModel = snapshot.getValue(ReviewModel.class);
                    reviewModelList.add(reviewModel);
                }

                adapterReviews.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void uploadReview(String s,float rating){

        String key = databaseReference.push().getKey();

        ReviewModel reviewModel = new ReviewModel(key, SharedPreferences.getUserName(),s,rating);

        databaseReference.child(data.getId()).child(key).setValue(reviewModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    reviewModelList.add(reviewModel);
                    adapterReviews.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
