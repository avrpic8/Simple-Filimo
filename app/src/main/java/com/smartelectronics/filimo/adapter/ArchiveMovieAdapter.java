package com.smartelectronics.filimo.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smartelectronics.filimo.R;
import com.smartelectronics.filimo.activity.GenreActivity;
import com.smartelectronics.filimo.model.MovieModel;

import java.util.List;

public class ArchiveMovieAdapter extends RecyclerView.Adapter<ArchiveMovieAdapter.MyViewHolder> {


    private Context mContext;
    private List<MovieModel> moviesList;
    private final int limit = 5;


    public ArchiveMovieAdapter(Context context, List<MovieModel> moviesList) {

        this.mContext   = context;
        this.moviesList = moviesList;
    }

    @NonNull
    @Override
    public ArchiveMovieAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View items = null;

        items = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.archive_movie_item, parent, false);


        return new ArchiveMovieAdapter.MyViewHolder(items);
    }


    @Override
    public void onBindViewHolder(final ArchiveMovieAdapter.MyViewHolder holder, int position) {

        final MovieModel movie = moviesList.get(position);

        holder.txtPersianTitle.setText(movie.getPersianMovieTitle());
        holder.txtEnglishTitle.setText(movie.getEnglishMovieTitle());
        Glide.with(mContext).load(movie.getImageUrl()).into(holder.movieCover);

        holder.setIsRecyclable(false);
        holder.coverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String archiveID = movie.getArchiveId();
                Intent intent    = new Intent(mContext, GenreActivity.class);
                intent.putExtra("archiveId", archiveID);
                mContext.startActivity(intent);
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

        public RelativeLayout coverLayout;
        public TextView txtPersianTitle, txtEnglishTitle, txtYear;
        public ImageView movieCover;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPersianTitle = itemView.findViewById(R.id.archive_title);
            txtEnglishTitle = itemView.findViewById(R.id.archive_title_en);
            movieCover = itemView.findViewById(R.id.archive_cover);
            coverLayout = itemView.findViewById(R.id.archive_layout);
        }
    }
}
