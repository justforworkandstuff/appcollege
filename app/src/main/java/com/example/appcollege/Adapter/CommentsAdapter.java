package com.example.appcollege.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcollege.Admin.CommentDelete;
import com.example.appcollege.Class.CommentData;
import com.example.appcollege.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>
{
    private Context _context;
    private ArrayList<CommentData> _list;
    private FirebaseAuth _authentication;

    public CommentsAdapter(Context _context, ArrayList<CommentData> _list)
    {
        this._context = _context;
        this._list = _list;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View _view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_feed_items, parent, false);

        return new CommentsViewHolder(_view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position)
    {
        _authentication = FirebaseAuth.getInstance();
        _authentication.getCurrentUser().getEmail();

        CommentData _commentItem = _list.get(position);

        holder._userID.setText(_commentItem.getUserID());
        holder._userName.setText(_commentItem.getUserName());
        holder._date.setText(_commentItem.getDate());
        holder._time.setText(_commentItem.getTime());
        holder._commentContent.setText(_commentItem.getCommentContent());
        holder._key.setText(_commentItem.getKey());
        holder._newsKey.setText(_commentItem.getNewsKey());

        if(_authentication.getCurrentUser().getEmail().equals("yee8896752@gmail.com"))
        {
            holder._deleteButton.setVisibility(View.VISIBLE);
            holder._deleteButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent _intent = new Intent(_context, CommentDelete.class);
                    _intent.putExtra("name", _commentItem.getUserName());
                    _intent.putExtra("date", _commentItem.getDate());
                    _intent.putExtra("time", _commentItem.getTime());
                    _intent.putExtra("comment", _commentItem.getCommentContent());
                    _intent.putExtra("key", _commentItem.getKey());
                    _intent.putExtra("newsKey", _commentItem.getNewsKey());
                    _context.startActivity(_intent);
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return _list.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder
    {
        private TextView _userID, _userName, _date, _time, _commentContent, _key, _newsKey;
        private ImageView _deleteButton;

        public CommentsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            _userID = itemView.findViewById(R.id.comments_feed_userID);
            _userName = itemView.findViewById(R.id.comments_feed_userName);
            _date = itemView.findViewById(R.id.comments_feed_date);
            _time = itemView.findViewById(R.id.comments_feed_time);
            _commentContent = itemView.findViewById(R.id.comments_feed_comment);
            _key = itemView.findViewById(R.id.comments_feed_key);
            _newsKey = itemView.findViewById(R.id.comments_feed_newsKey);
            _deleteButton = itemView.findViewById(R.id.comments_feed_deleteButton);
        }
    }
}
