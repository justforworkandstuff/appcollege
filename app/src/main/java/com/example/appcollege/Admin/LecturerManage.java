package com.example.appcollege.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcollege.Adapter.LecturerAdapter;
import com.example.appcollege.Class.UserData;
import com.example.appcollege.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LecturerManage extends AppCompatActivity
{
    private RecyclerView _lecturerSection;
    private LinearLayout _lectNoData;
    private List<UserData> _list1;
    private LecturerAdapter _adapter;
    private DatabaseReference _dbReference, _anotherReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_manage);

        _dbReference = FirebaseDatabase.getInstance().getReference().child("User");

        _lecturerSection = findViewById(R.id.layout_Lecturer);
        _lectNoData = findViewById(R.id.layout_noData_lecturer);

        lecturerRead();

    }

    private void lecturerRead()
    {
        _anotherReference = _dbReference.child("Lecturer");
        _anotherReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _list1 = new ArrayList<>();
                if(!snapshot.exists())
                {
                    _lectNoData.setVisibility(View.VISIBLE);
                    _lecturerSection.setVisibility(View.GONE);
                }
                else
                {
                    _lectNoData.setVisibility(View.GONE);
                    _lecturerSection.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        UserData _data = snapshot1.getValue(UserData.class);
                        _list1.add(_data);
                    }
                    _lecturerSection.setHasFixedSize(true);
                    _lecturerSection.setLayoutManager(new LinearLayoutManager(LecturerManage.this));
                    _adapter = new LecturerAdapter(_list1, LecturerManage.this, "Lecturer");
                    _lecturerSection.setAdapter(_adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(LecturerManage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}