package com.example.appcollege.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcollege.Class.UserData;
import com.example.appcollege.R;
import com.example.appcollege.Adapter.UserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserManage extends AppCompatActivity
{
    private RecyclerView _studentSection;
    private LinearLayout _studNoData;
    private List<UserData> _list2;
    private UserAdapter _adapter;
    private DatabaseReference _dbReference, _anotherReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);

        _dbReference = FirebaseDatabase.getInstance().getReference().child("User");

        _studentSection = findViewById(R.id.layout_Student);
        _studNoData = findViewById(R.id.layout_noData_student);

        studentRead();
    }

    private void studentRead()
    {
        _anotherReference = _dbReference.child("Student");
        _anotherReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _list2 = new ArrayList<>();
                if(!snapshot.exists())
                {
                    _studNoData.setVisibility(View.VISIBLE);
                    _studentSection.setVisibility(View.GONE);
                }
                else
                {
                    _studNoData.setVisibility(View.GONE);
                    _studentSection.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        UserData _data = snapshot1.getValue(UserData.class);
                        _list2.add(_data);
                    }
                    _studentSection.setHasFixedSize(true);
                    _studentSection.setLayoutManager(new LinearLayoutManager(UserManage.this));
                    _adapter = new UserAdapter(_list2, UserManage.this, "Student");
                    _studentSection.setAdapter(_adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(UserManage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}