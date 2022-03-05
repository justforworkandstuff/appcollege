package com.example.appcollege.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.appcollege.R;
import com.example.appcollege.UserLogin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminHome extends AppCompatActivity implements View.OnClickListener
{
    private CardView _readNews, _user, _lecturer, _subjects, _appDetails;
    private Button _logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        _readNews = findViewById(R.id.news_Read);
        _readNews.setOnClickListener(this);

        _user = findViewById(R.id.manage_Users);
        _user.setOnClickListener(this);

        _lecturer = findViewById(R.id.manage_Lecturer);
        _lecturer.setOnClickListener(this);

        _subjects = findViewById(R.id.manage_Subjects);
        _subjects.setOnClickListener(this);

        _appDetails = findViewById(R.id.manage_Details);
        _appDetails.setOnClickListener(this);

        _logoutButton = findViewById(R.id.admin_logout_button);
        _logoutButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent _intent;
        switch(v.getId())
        {
            case R.id.news_Read:
                _intent = new Intent(AdminHome.this, NewsManage.class);
                startActivity(_intent);
                break;
            case R.id.manage_Users:
                _intent = new Intent(AdminHome.this, UserManage.class);
                startActivity(_intent);
                break;
            case R.id.manage_Lecturer:
                _intent = new Intent(AdminHome.this, LecturerManage.class);
                startActivity(_intent);
                break;
            case R.id.manage_Subjects:
                _intent = new Intent(AdminHome.this, SubjectManage.class);
                startActivity(_intent);
                break;
            case R.id.manage_Details:
                _intent = new Intent(AdminHome.this, AppDetails.class);
                startActivity(_intent);
                break;
            case R.id.admin_logout_button:
                logOut(AdminHome.this);
                break;
        }
    }

    private void logOut(Activity activity)
    {
        AlertDialog.Builder _builder = new AlertDialog.Builder(activity);
        _builder.setTitle("Logout");
        _builder.setMessage("Are you sure you want to log out?");

        _builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                FirebaseAuth.getInstance().signOut();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user == null)
                {
                    Toast.makeText(AdminHome.this, "Sign out successfully. ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AdminHome.this, UserLogin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
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
}