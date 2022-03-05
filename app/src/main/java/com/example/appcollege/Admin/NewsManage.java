package com.example.appcollege.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcollege.Class.NewsData;
import com.example.appcollege.Adapter.NewsDisplayAdapter;
import com.example.appcollege.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewsManage extends AppCompatActivity
{
    private RecyclerView _newsReadRecycler;
    private ProgressBar _progressBar;
    private ArrayList<NewsData> _list;
    private NewsDisplayAdapter _adapter;
    private DatabaseReference _dbReference;
    private FloatingActionButton _newsAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_manage);

        _newsReadRecycler = findViewById(R.id.news_manage_recycler);
        _progressBar = findViewById(R.id.news_manage_pBar);
        _newsAddButton = findViewById(R.id.news_manage_addButton);

        _newsAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(NewsManage.this, NewsAdd.class));
            }
        });

        _dbReference = FirebaseDatabase.getInstance().getReference().child("News");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(NewsManage.this, 1);
        _newsReadRecycler.setLayoutManager(gridLayoutManager);

        newsRead();
    }

    private void newsRead()
    {
        _dbReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _list = new ArrayList<>();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    NewsData _data = snapshot1.getValue(NewsData.class);
                    _list.add(_data);
                }
                _adapter = new NewsDisplayAdapter(NewsManage.this, _list);
                _adapter.notifyDataSetChanged();
                _progressBar.setVisibility(View.GONE);
                _newsReadRecycler.setAdapter(_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                _progressBar.setVisibility(View.GONE);
                Toast.makeText(NewsManage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}