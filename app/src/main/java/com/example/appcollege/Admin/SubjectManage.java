package com.example.appcollege.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcollege.Adapter.SubjectAdapter;
import com.example.appcollege.Class.CourseData;
import com.example.appcollege.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SubjectManage extends AppCompatActivity
{
    private FloatingActionButton _subAddButton;
    private RecyclerView _subjectSection;
    private List<CourseData> _list;
    private SubjectAdapter _adapter;
    private DatabaseReference _dbReference, _departmentRef;
    private Spinner _subjectList;
    private String _subjects[] = {"Programme Name", "Business", "IT", "Finance"};
    private ArrayAdapter<String> _subjects_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_manage);

        _dbReference = FirebaseDatabase.getInstance().getReference().child("Course");

        _subjectSection = findViewById(R.id.subject_Recycler);
        _subAddButton = findViewById(R.id.subject_AddButton);
        _subjectList = findViewById(R.id.subject_Category);

        _subjects_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, _subjects);
        _subjectList.setAdapter(_subjects_adapter);

        _subAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(SubjectManage.this, SubjectAdd.class));
            }
        });

        _subjectList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch(position)
                {
                    case 0:

                        break;
                    case 1:
                        BusinessCourseRead();
                        break;
                    case 2:
                        ITCourseRead();
                        break;
                    case 3:
                        FinanceCourseRead();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void BusinessCourseRead()
    {
        _departmentRef = _dbReference.child("Business");
        _departmentRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _list = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    CourseData _data = snapshot1.getValue(CourseData.class);
                    _list.add(_data);
                }
                _adapter = new SubjectAdapter(_list, SubjectManage.this, "Business");
                _adapter.notifyDataSetChanged();
                _subjectSection.setLayoutManager(new LinearLayoutManager(SubjectManage.this));
                _subjectSection.setAdapter(_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SubjectManage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ITCourseRead()
    {
        _departmentRef = _dbReference.child("IT");
        _departmentRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _list = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    CourseData _data = snapshot1.getValue(CourseData.class);
                    _list.add(_data);
                }
                _adapter = new SubjectAdapter(_list, SubjectManage.this, "IT");
                _adapter.notifyDataSetChanged();
                _subjectSection.setLayoutManager(new LinearLayoutManager(SubjectManage.this));
                _subjectSection.setAdapter(_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SubjectManage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void FinanceCourseRead()
    {
        _departmentRef = _dbReference.child("Finance");
        _departmentRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _list = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    CourseData _data = snapshot1.getValue(CourseData.class);
                    _list.add(_data);
                }
                _adapter = new SubjectAdapter(_list, SubjectManage.this, "Finance");
                _adapter.notifyDataSetChanged();
                _subjectSection.setLayoutManager(new LinearLayoutManager(SubjectManage.this));
                _subjectSection.setAdapter(_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SubjectManage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}