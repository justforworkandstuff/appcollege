package com.example.appcollege.News;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcollege.Adapter.NewsDisplayAdapter;
import com.example.appcollege.Admin.AppDetails;
import com.example.appcollege.Class.NewsData;
import com.example.appcollege.MainActivity;
import com.example.appcollege.R;
import com.example.appcollege.User.NotesManage;
import com.example.appcollege.User.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewsRead extends AppCompatActivity {

    private RecyclerView _newsReadRecycler;
    private ProgressBar _progressBar;
    private ArrayList<NewsData> _list;
    private NewsDisplayAdapter _adapter;
    private DatabaseReference _dbReference;
    private DrawerLayout drawerLayout;
    private TextView _toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_read);

        drawerLayout = findViewById(R.id.main_drawer_layout);
        _newsReadRecycler = findViewById(R.id.news_readRecycler);
        _progressBar = findViewById(R.id.news_read_progressBar);
        _toolbarText = findViewById(R.id.toolbar_text);

        _toolbarText.setText("Latest News");

        _dbReference = FirebaseDatabase.getInstance().getReference().child("News");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(NewsRead.this, 1);
        _newsReadRecycler.setLayoutManager(gridLayoutManager);
        //_newsReadRecycler.setHasFixedSize(true);

        newsRead();
    }

    public void ClickMenu(View view)
    {
        MainActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view)
    {
        MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view)
    {
        MainActivity.redirectActivity(this, MainActivity.class);
    }

    public void ClickProfile(View view)
    {
        MainActivity.redirectActivity(this, UserProfile.class);
    }

    public void ClickNews(View view)
    {
        recreate();
    }

    public void ClickNotes(View view)
    {
        MainActivity.redirectActivity(this, NotesManage.class);
    }

    public void ClickAboutUs(View view)
    {
        MainActivity.redirectActivity(this, AppDetails.class);
    }

    public void ClickLogOut(View view)
    {
        MainActivity.logOut(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
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
                _adapter = new NewsDisplayAdapter(NewsRead.this, _list);
                _adapter.notifyDataSetChanged();
                _progressBar.setVisibility(View.GONE);
                _newsReadRecycler.setAdapter(_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                _progressBar.setVisibility(View.GONE);
                Toast.makeText(NewsRead.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}