package com.example.appcollege.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcollege.Class.NotesData;
import com.example.appcollege.R;
import com.example.appcollege.User.NotesDetails2;

import java.util.ArrayList;

public class NotesAdapter2 extends RecyclerView.Adapter<NotesAdapter2.NotesViewHolder2>
{
    private Context _context;
    private ArrayList<NotesData> _list;

    public NotesAdapter2(Context _context, ArrayList<NotesData> _list)
    {
        this._context = _context;
        this._list = _list;
    }


    @NonNull
    @Override
    public NotesViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View _view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjectsfeed_item, parent, false);

        return new NotesViewHolder2(_view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder2 holder, int position)
    {
        NotesData _notesItem = _list.get(position);

        holder._notesTitle.setText(_notesItem.getTitle());
        holder._notesDescription.setText(_notesItem.getNotes());
        holder._notesDate.setText(_notesItem.getDate());
        holder._notesTime.setText(_notesItem.getTime());

        holder._cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent _intent = new Intent(_context, NotesDetails2.class);
                _intent.putExtra("title", _notesItem.getTitle());
                _intent.putExtra("notes", _notesItem.getNotes());
                _intent.putExtra("date", _notesItem.getDate());
                _intent.putExtra("time", _notesItem.getTime());
                _intent.putExtra("key", _notesItem.getKey());
                _context.startActivity(_intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return _list.size();
    }

    public class NotesViewHolder2 extends RecyclerView.ViewHolder
    {
        private TextView _notesTitle;
        private TextView _notesDescription;
        private TextView _notesDate;
        private TextView _notesTime;
        private CardView _cardView;

        public NotesViewHolder2(@NonNull View itemView)
        {
            super(itemView);

            _notesTitle = itemView.findViewById(R.id.notes_cardTitle);
            _notesDescription = itemView.findViewById(R.id.notes_cardDescription);
            _notesDate = itemView.findViewById(R.id.notes_cardDate);
            _notesTime = itemView.findViewById(R.id.notes_cardTime);
            _cardView = itemView.findViewById(R.id.notes_cardView);
        }
    }
}
