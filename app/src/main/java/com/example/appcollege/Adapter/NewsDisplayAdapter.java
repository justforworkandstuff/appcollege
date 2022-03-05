package com.example.appcollege.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcollege.Class.NewsData;
import com.example.appcollege.News.NewsDetails;
import com.example.appcollege.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsDisplayAdapter extends RecyclerView.Adapter<NewsDisplayAdapter.NewsDisplayViewHolder>
{
    private Context _context;
    private ArrayList<NewsData> _list;
    private int lastPosition = -1;

    public NewsDisplayAdapter(Context _context, ArrayList<NewsData> _list)
    {
        this._context = _context;
        this._list = _list;
    }

    @NonNull
    @Override
    public NewsDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View _view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newsfeed_item, parent, false);

        return new NewsDisplayViewHolder(_view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsDisplayViewHolder holder, int position)
    {
        NewsData _newsItem = _list.get(position);

        try
        {
            if(_newsItem.getImage() != null)
                Picasso.get().load(_newsItem.getImage()).into(holder._newsImage);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        holder._newsTitle.setText(_newsItem.getTitle());
        holder._newsDescription.setText(_newsItem.getDescription());
        holder._newsDescription.setVisibility(View.INVISIBLE);
        holder._newsDate.setText(_newsItem.getDate());
        holder._newsTime.setText(_newsItem.getTime());
        holder._newsTime.setVisibility(View.INVISIBLE);
        holder._newsLikes.setText(_newsItem.getLikes() + "");
        holder._newsComment.setText(_newsItem.getComments() + "");

        holder._cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent _intent = new Intent(_context, NewsDetails.class);
                _intent.putExtra("title", _newsItem.getTitle());
                _intent.putExtra("description", _newsItem.getDescription());
                _intent.putExtra("date", _newsItem.getDate());
                _intent.putExtra("time", _newsItem.getTime());
                _intent.putExtra("likes", _newsItem.getLikes());
                _intent.putExtra("comments", _newsItem.getComments());
                _intent.putExtra("image", _newsItem.getImage());
                _intent.putExtra("key", _newsItem.getKey());
                _context.startActivity(_intent);
            }
        });
        setAnimation(holder.itemView, position);
    }

    public void setAnimation(View viewtoAnimate, int position)
    {
        if(position > lastPosition)
        {
            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(1500);
            viewtoAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount()
    {
        return _list.size();
    }

    public class NewsDisplayViewHolder extends RecyclerView.ViewHolder
    {
        private TextView _newsTitle, _newsDescription, _newsDate, _newsTime, _newsLikes, _newsComment;
        private ImageView _newsImage;
        private CardView _cardView;

        public NewsDisplayViewHolder(@NonNull View itemView)
        {
            super(itemView);

            _newsTitle = itemView.findViewById(R.id.news_cardTitle);
            _newsDescription = itemView.findViewById(R.id.news_cardDescription);
            _newsDate = itemView.findViewById(R.id.news_cardDate);
            _newsTime = itemView.findViewById(R.id.news_cardTime);
            _newsLikes = itemView.findViewById(R.id.news_cardLikes);
            _newsComment = itemView.findViewById(R.id.news_cardComments);
            _newsImage = itemView.findViewById(R.id.news_cardImage);
            _cardView = itemView.findViewById(R.id.news_cardView);
        }
    }
}
