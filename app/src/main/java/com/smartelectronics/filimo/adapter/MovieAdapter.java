package com.smartelectronics.filimo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smartelectronics.filimo.R;
import com.smartelectronics.filimo.activity.SingleMovieActivity;
import com.smartelectronics.filimo.model.MovieModel;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    public final static int SMALL_VIEW_TYPE  = 1;
    public final static int NORMAL_VIEW_TYPE = 2;

    private int mViewType;

    private Context mContext;
    private List<MovieModel> moviesList;
    private final int limit = 5;
    private String uniqId, archiveId;

    public MovieAdapter(Context context, List<MovieModel> moviesList, int viewType) {

        this.mContext   = context;
        this.moviesList = moviesList;

        switch (viewType){

            case 1: mViewType = 1; break;
            case 2: mViewType = 2; break;
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View items = null;

        if(mViewType == 1) {
            items = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.movie_item_small, parent, false);
        }
        if(mViewType == 2) {
            items = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.movie_item, parent, false);
        }

        return new MyViewHolder(items);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final MovieModel movie = moviesList.get(position);

        holder.txtPersianTitle.setText(movie.getPersianMovieTitle());
        holder.txtEnglishTitle.setText(movie.getEnglishMovieTitle());
        holder.txtYear.setText(movie.getMovieYear());
        Glide.with(mContext).load(movie.getImageUrl()).into(holder.movieCover);

        holder.setIsRecyclable(false);

        holder.coverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                archiveId = moviesList.get(position).getArchiveId();
                uniqId    = moviesList.get(position).getObjectID();

                //Intent intent = new Intent(mContext, SingleMovieActivity.class);
                //intent.putExtra("uniqId", uniqId);
                //intent.putExtra("archiveId", archiveId);

                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,holder.movieCover,"film_cover");
                Intent intent = new Intent(mContext,SingleMovieActivity.class);
                intent.putExtra("uniqId", uniqId);
                intent.putExtra("archiveId", archiveId);

                mContext.startActivity(intent,activityOptionsCompat.toBundle());
                Log.i("uniqId", "onClick: "+ uniqId);
                //mContext.startActivity(intent);

            }
        });


        Log.i("List", "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {

        if(moviesList.size() > limit)
            return limit;
        else
            return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout coverLayout;
        public TextView txtPersianTitle, txtEnglishTitle, txtYear;
        public ImageView movieCover;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            coverLayout = itemView.findViewById(R.id.cover_layout);
            txtPersianTitle = itemView.findViewById(R.id.movie_title);
            txtEnglishTitle = itemView.findViewById(R.id.en_movie_title);
            txtYear     = itemView.findViewById(R.id.movie_year);
            movieCover  = itemView.findViewById(R.id.movie_cover);
        }
    }
}
