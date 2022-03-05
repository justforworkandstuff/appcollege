package com.example.appcollege.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class UserProfile extends AppCompatActivity
{
    private ImageView _userAvatar;
    private TextView _userName, _userID, _courseID, _programme, _courseName;
    private TextView _userEmail, _userNumber, _userAddress, _userfeeStatus, _userfeeAmount, _toolbarText, _category;
    private TextView _feeDetails, _courseDetails, _subject1, _subject2, _subject3, _subject4;
    private Button _editDetailsButton;
    private DrawerLayout drawerLayout;
    private DatabaseReference _dbReference, _childReference, _childReference2;
    private FirebaseAuth _authentication;
    private String _image, _name, _id, _cID, _pName, _cName, _email, _number, _address, _feeStatus, _feeAmount, _sub1, _sub2, _sub3, _sub4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        _category = findViewById(R.id.user_profile_userCategory);
        _userAvatar = findViewById(R.id.user_profile_avatar);
        _userName = findViewById(R.id.user_profile_name);
        _userID = findViewById(R.id.user_profile_userID);
        _courseID = findViewById(R.id.user_profile_courseID);
        _programme = findViewById(R.id.user_profile_programme);
        _courseName = findViewById(R.id.user_profile_courseName);
        _userEmail = findViewById(R.id.user_profile_userEmail);
        _userNumber = findViewById(R.id.user_profile_userNumber);
        _userAddress = findViewById(R.id.user_profile_userAddress);
        _userfeeStatus = findViewById(R.id.user_profile_feeStatus);
        _userfeeAmount = findViewById(R.id.user_profile_feeAmount);
        _editDetailsButton = findViewById(R.id.user_profile_editButton);

        _feeDetails = findViewById(R.id.user_profile_FeeDetails);
        _courseDetails = findViewById(R.id.user_profile_courseDetails);
        _subject1 = findViewById(R.id.user_profile_sub1);
        _subject2 = findViewById(R.id.user_profile_sub2);
        _subject3 = findViewById(R.id.user_profile_sub3);
        _subject4 = findViewById(R.id.user_profile_sub4);

        _toolbarText = findViewById(R.id.toolbar_text);
        _toolbarText.setText("Profile");

        drawerLayout = findViewById(R.id.main_drawer_layout);

        _dbReference = FirebaseDatabase.getInstance().getReference().child("User");
        _childReference = _dbReference.child("Student");
        _childReference2 = _dbReference.child("Lecturer");
        _authentication = FirebaseAuth.getInstance();

        _childReference.child(_authentication.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String usertype;
                usertype = snapshot.child("category").getValue(String.class);

                if(usertype != null && usertype.equals("Student"))
                {
                    userRead();
                }
                else
                {
                    _courseDetails.setVisibility(View.VISIBLE);
                    _subject1.setVisibility(View.VISIBLE);
                    _subject2.setVisibility(View.VISIBLE);
                    _subject3.setVisibility(View.VISIBLE);
                    _subject4.setVisibility(View.VISIBLE);

                    _feeDetails.setVisibility(View.GONE);
                    _userfeeStatus.setVisibility(View.GONE);
                    _userfeeAmount.setVisibility(View.GONE);
                    lectRead();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        _editDetailsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(UserProfile.this, UserEdit.class));
            }
        });
    }

    private void userRead()
    {
        _childReference.child(_authentication.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _image = snapshot.child("image").getValue(String.class);
                _name = snapshot.child("name").getValue(String.class);
                _id = snapshot.child("key").getValue(String.class);
                _cID = snapshot.child("courseID").getValue(String.class);
                _pName = snapshot.child("programme").getValue(String.class);
                _cName = snapshot.child("courseName").getValue(String.class);
                _email = snapshot.child("email").getValue(String.class);
                _number = snapshot.child("number").getValue(String.class);
                _address = snapshot.child("address").getValue(String.class);
                _feeStatus = snapshot.child("feeStatus").getValue(String.class);
                _feeAmount = snapshot.child("feeAmount").getValue(String.class);

                Picasso.get().load(_image).into(_userAvatar);
                _userName.setText(_name);
                _userID.setText(_id);
                _courseID.setText(_cID);
                _programme.setText(_pName);
                _courseName.setText(_cName);
                _userEmail.setText(_email);
                _userNumber.setText(_number);
                _userAddress.setText(_address);
                _userfeeStatus.setText(_feeStatus);
                _userfeeAmount.setText(_feeAmount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private void lectRead()
    {
        _childReference2.child(_authentication.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _image = snapshot.child("image").getValue(String.class);
                _name = snapshot.child("name").getValue(String.class);
                _id = snapshot.child("key").getValue(String.class);
                _cID = snapshot.child("courseID").getValue(String.class);
                _pName = snapshot.child("programme").getValue(String.class);
                _cName = snapshot.child("courseName").getValue(String.class);
                _email = snapshot.child("email").getValue(String.class);
                _number = snapshot.child("number").getValue(String.class);
                _address = snapshot.child("address").getValue(String.class);
                _sub1 = snapshot.child("sub1").getValue(String.class);
                _sub2 = snapshot.child("sub2").getValue(String.class);
                _sub3 = snapshot.child("sub3").getValue(String.class);
                _sub4 = snapshot.child("sub4").getValue(String.class);

                Picasso.get().load(_image).into(_userAvatar);
                _userName.setText(_name);
                _userID.setText(_id);
                _courseID.setText(_cID);
                _programme.setText(_pName);
                _courseName.setText(_cName);
                _userEmail.setText(_email);
                _userNumber.setText(_number);
                _userAddress.setText(_address);
                _subject1.setText(_sub1);
                _subject2.setText(_sub2);
                _subject3.setText(_sub3);
                _subject4.setText(_sub4);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    public void userChecking()
    {
        _childReference.child(_authentication.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String usertype;
                usertype = snapshot.child("category").getValue(String.class);

                if(usertype != null && usertype.equals("Student"))
                {
                    startActivity(new Intent(UserProfile.this, NotesManage.class));
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
        recreate();
    }

    public void ClickNotes(View view)
    {
        userChecking();
    }

    public void ClickAboutUs(View view)
    {
        MainActivity.redirectActivity(this, AboutUs.class);
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
}