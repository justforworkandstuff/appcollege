package com.example.appcollege.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcollege.Adapter.NotesAdapter;
import com.example.appcollege.Adapter.NotesAdapter2;
import com.example.appcollege.Adapter.NotesAdapter3;
import com.example.appcollege.Adapter.NotesAdapter4;
import com.example.appcollege.Class.NotesData;
import com.example.appcollege.MainActivity;
import com.example.appcollege.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotesManage extends AppCompatActivity
{
    private FloatingActionButton _notesAddButton, _notesAddButton2, _notesAddButton3, _notesAddButton4;
    private RecyclerView _notesSection;
    private ArrayList<NotesData> _list;
    private NotesAdapter _adapter;
    private NotesAdapter2 _adapter2;
    private NotesAdapter3 _adapter3;
    private NotesAdapter4 _adapter4;
    private DatabaseReference _dbReference, _secondRef, _thirdRef, _fourthRef;
    private Spinner _subjectsList;
    private String _subjects[] = {"Subjects", "Subject 1", "Subject 2", "Subject 3", "Subject 4"};
    private ArrayAdapter<String> _subjects_adapter;
    private FirebaseAuth _authentication;
    private DrawerLayout drawerLayout;
    private TextView _toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_manage);

        drawerLayout = findViewById(R.id.main_drawer_layout);

        _notesSection = findViewById(R.id.notes_manage_recycler);
        _notesAddButton = findViewById(R.id.notes_manage_addButton);
        _notesAddButton2 = findViewById(R.id.notes_manage_addButton2);
        _notesAddButton3 = findViewById(R.id.notes_manage_addButton3);
        _notesAddButton4 = findViewById(R.id.notes_manage_addButton4);
        _subjectsList = findViewById(R.id.notes_manage_subjects);

        _toolbarText = findViewById(R.id.toolbar_text);
        _toolbarText.setText("Notes");

        _subjects_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, _subjects);
        _subjectsList.setAdapter(_subjects_adapter);

        _dbReference = FirebaseDatabase.getInstance().getReference().child("User");
        _authentication = FirebaseAuth.getInstance();

        _notesAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(NotesManage.this, NotesAdd.class));
            }
        });

        _notesAddButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesManage.this, NotesAdd2.class));
            }
        });

        _notesAddButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesManage.this, NotesAdd3.class));
            }
        });

        _notesAddButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesManage.this, NotesAdd4.class));
            }
        });

        _subjectsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch(position)
                {
                    case 0:
                        break;
                    case 1:
                        Subject1Read();
                        _notesAddButton.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        Subject2Read();
                        _notesAddButton2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        Subject3Read();
                        _notesAddButton3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        Subject4Read();
                        _notesAddButton4.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void Subject1Read()
    {
        _secondRef = _dbReference.child("Student");
        _thirdRef = _secondRef.child(_authentication.getCurrentUser().getUid());
        _fourthRef = _thirdRef.child("Sub1");

        _fourthRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _list = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    NotesData _data = snapshot1.getValue(NotesData.class);
                    _list.add(_data);
                }
                _adapter = new NotesAdapter(NotesManage.this, _list);
                _adapter.notifyDataSetChanged();

                _notesSection.setLayoutManager(new LinearLayoutManager(NotesManage.this));
                _notesSection.setAdapter(_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(NotesManage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Subject2Read()
    {
        _secondRef = _dbReference.child("Student");
        _thirdRef = _secondRef.child(_authentication.getCurrentUser().getUid());
        _fourthRef = _thirdRef.child("Sub2");

        _fourthRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _list = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    NotesData _data = snapshot1.getValue(NotesData.class);
                    _list.add(_data);
                }
                _adapter2 = new NotesAdapter2(NotesManage.this, _list);
                _adapter2.notifyDataSetChanged();

                _notesSection.setLayoutManager(new LinearLayoutManager(NotesManage.this));
                _notesSection.setAdapter(_adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(NotesManage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Subject3Read()
    {
        _secondRef = _dbReference.child("Student");
        _thirdRef = _secondRef.child(_authentication.getCurrentUser().getUid());
        _fourthRef = _thirdRef.child("Sub3");

        _fourthRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _list = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    NotesData _data = snapshot1.getValue(NotesData.class);
                    _list.add(_data);
                }
                _adapter3 = new NotesAdapter3(NotesManage.this, _list);
                _adapter3.notifyDataSetChanged();

                _notesSection.setLayoutManager(new LinearLayoutManager(NotesManage.this));
                _notesSection.setAdapter(_adapter3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(NotesManage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Subject4Read()
    {
        _secondRef = _dbReference.child("Student");
        _thirdRef = _secondRef.child(_authentication.getCurrentUser().getUid());
        _fourthRef = _thirdRef.child("Sub4");

        _fourthRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _list = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    NotesData _data = snapshot1.getValue(NotesData.class);
                    _list.add(_data);
                }
                _adapter4 = new NotesAdapter4(NotesManage.this, _list);
                _adapter4.notifyDataSetChanged();

                _notesSection.setLayoutManager(new LinearLayoutManager(NotesManage.this));
                _notesSection.setAdapter(_adapter4);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(NotesManage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        MainActivity.redirectActivity(this, UserProfile.class);
    }

    public void ClickNotes(View view)
    {
        recreate();
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