package com.example.appcollege.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.appcollege.MainActivity;
import com.example.appcollege.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AboutUs extends AppCompatActivity
{
    private DrawerLayout drawerLayout;
    private DatabaseReference _dbReference;
    private TextView _toolbarText, _userDes, _userEmail, _userNum;
    private ImageView _detailsImage;
    private DatabaseReference _userRef, _userStudRef;
    private FirebaseAuth _authentication;

    private String _image, _description, _email, _phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        drawerLayout = findViewById(R.id.main_drawer_layout);
        _toolbarText = findViewById(R.id.toolbar_text);
        _userDes = findViewById(R.id.app_details_user_description);
        _userEmail = findViewById(R.id.app_details_user_email);
        _userNum = findViewById(R.id.app_details_user_phoneNumber);
        _detailsImage = findViewById(R.id.app_details_user_image);

        _dbReference = FirebaseDatabase.getInstance().getReference().child("AppDetails");
        _userRef = FirebaseDatabase.getInstance().getReference().child("User");
        _userStudRef = _userRef.child("Student");
        _authentication = FirebaseAuth.getInstance();
        
        _toolbarText.setText("About Us");

        detailsRead();
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
                    startActivity(new Intent(AboutUs.this, NotesManage.class));
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

    @Override
    protected void onPause()
    {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
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

    public void ClickNotes(View view)
    {
        userChecking();
    }

    public void ClickAboutUs(View view)
    {
        recreate();
    }

    public void ClickLogOut(View view)
    {
        MainActivity.logOut(this);
    }

    private void detailsRead()
    {
        _dbReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _image = snapshot.child("image").getValue(String.class);
                _description = snapshot.child("description").getValue(String.class);
                _email = snapshot.child("email").getValue(String.class);
                _phoneNumber = snapshot.child("number").getValue(String.class);

                Picasso.get().load(_image).into(_detailsImage);
                _userDes.setText(_description);
                _userEmail.setText(_email);
                _userNum.setText(_phoneNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}