package com.food.android.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.food.android.app.R;
import com.food.android.app.models.ReviewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterReviews extends RecyclerView.Adapter<AdapterReviews.RecyclerViewHolder> {

    private Context context;
    private List<ReviewModel> reviewModelList = new ArrayList<>();

    public AdapterReviews(Context context, List<ReviewModel> reviewModelList) {
        this.context = context;
        this.reviewModelList = reviewModelList;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_review, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {

        final ReviewModel review = reviewModelList.get(position);

        holder.name_review.setText(review.getUserName());
        System.out.println("NAME: "+review.getUserName());
        char result = review.getUserName().charAt(0);
        holder.circular_text_view.setText(result+"");
        holder.desc_review.setText(review.getUserRevDesc());
        holder.rating.setRating(review.getRating());
    }

    @Override
    public int getItemCount() {
        return reviewModelList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView circular_text_view;
        private TextView name_review,desc_review;
        RatingBar rating;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            circular_text_view = itemView.findViewById(R.id.circular_text_view);
            name_review = itemView.findViewById(R.id.name_review);
            desc_review = itemView.findViewById(R.id.desc_review);
            rating = itemView.findViewById(R.id.rating);
        }
    }


}
