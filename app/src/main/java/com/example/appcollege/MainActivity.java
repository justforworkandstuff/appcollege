package com.example.appcollege;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcollege.Adapter.NewsDisplayAdapter;
import com.example.appcollege.Class.NewsData;
import com.example.appcollege.User.AboutUs;
import com.example.appcollege.User.NotesManage;
import com.example.appcollege.User.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private DrawerLayout drawerLayout;
    private TextView _toolbarText;
    private RecyclerView _newsReadRecycler;
    private ProgressBar _progressBar;
    private ArrayList<NewsData> _list;
    private NewsDisplayAdapter _adapter;
    private DatabaseReference _dbReference, _userRef, _userStudRef;
    private FirebaseAuth _authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.main_drawer_layout);
        _toolbarText = findViewById(R.id.toolbar_text);
        _newsReadRecycler = findViewById(R.id.news_readRecycler);
        _progressBar = findViewById(R.id.news_read_progressBar);

        _toolbarText.setText("Home");

        _dbReference = FirebaseDatabase.getInstance().getReference().child("News");
        _userRef = FirebaseDatabase.getInstance().getReference().child("User");
        _userStudRef = _userRef.child("Student");
        _authentication = FirebaseAuth.getInstance();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        _newsReadRecycler.setLayoutManager(gridLayoutManager);

        newsRead();

    }

    public void userChecking()
    {
        _userStudRef.child(_authentication.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String usertype;
                usertype = snapshot.child("category").getValue(String.class);

                if(usertype != null && usertype.equals("Student"))
                {
                    startActivity(new Intent(MainActivity.this, NotesManage.class));
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Only students can use this feature.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    public void ClickMenu(View view)
    {
        openDrawer(drawerLayout);
    }

    public void ClickLogo(View view)
    {
        closeDrawer(drawerLayout);
    }

    public void ClickHome(View view)
    {
        recreate();
    }

    public void ClickProfile(View view)
    {
        redirectActivity(this, UserProfile.class);
    }

    public void ClickNotes(View view)
    {
        userChecking();
    }

    public void ClickAboutUs(View view)
    {
        redirectActivity(this, AboutUs.class);
    }

    public void ClickLogOut(View view)
    {
        logOut(this);
    }

    public static void openDrawer(DrawerLayout drawerLayout)
    {
        //open drawer
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout)
    {
        //check condition if drawer is open
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public static void logOut(Activity activity)
    {
        AlertDialog.Builder _builder = new AlertDialog.Builder(activity);
        _builder.setTitle("Logout");
        _builder.setMessage("Are you sure you want to log out?");
        _builder.setCancelable(true);

        _builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                FirebaseAuth.getInstance().signOut();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null)
                {
                    Toast.makeText(activity, "Sign out successfully. ", Toast.LENGTH_LONG).show();
                    redirectActivity(activity, UserLogin.class);
                    activity.finishAffinity();
                }
            }
        });

        _builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        _builder.show();
    }

    public static void redirectActivity(Activity activity, Class _class)
    {
        Intent intent = new Intent(activity, _class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        closeDrawer(drawerLayout);
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
                _adapter = new NewsDisplayAdapter(MainActivity.this, _list);
                _adapter.notifyDataSetChanged();
                _progressBar.setVisibility(View.GONE);
                _newsReadRecycler.setAdapter(_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                _progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}