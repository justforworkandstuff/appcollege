package com.example.appcollege.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appcollege.Class.CourseData;
import com.example.appcollege.Class.UserData;
import com.example.appcollege.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class LecturerUpdate extends AppCompatActivity
{
    private EditText _userName;
    private Spinner _userProgramme, _userCourseID;
    private TextView _userCourseName, _subject1, _subject2, _subject3, _subject4;
    private Button _updateButton;

    private String _name, _programme, _courseID, _courseName, _sub1, _sub2, _sub3, _sub4, _userID;
    private ProgressDialog _dialog;
    private DatabaseReference _dbReference, _childReference, _rootReference, _courseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_update);

        _userName = findViewById(R.id.lecturer_update_name);
        _userProgramme = findViewById(R.id.lecturer_update_programme);
        _userCourseID = findViewById(R.id.lecturer_update_courseID);
        _userCourseName = findViewById(R.id.lecturer_update_courseName);
        _subject1 = findViewById(R.id.lecturer_update_sub1);
        _subject2 = findViewById(R.id.lecturer_update_sub2);
        _subject3 = findViewById(R.id.lecturer_update_sub3);
        _subject4 = findViewById(R.id.lecturer_update_sub4);
        _updateButton = findViewById(R.id.lecturer_update_button);

        _dialog = new ProgressDialog(this);

        _dbReference = FirebaseDatabase.getInstance().getReference().child("User");
        _childReference = _dbReference.child("Lecturer");
        _rootReference = FirebaseDatabase.getInstance().getReference().child("Course");

        _name = getIntent().getStringExtra("name");
        _userID = getIntent().getStringExtra("userID");

        _userName.setText(_name);

        String[] _programme = new String[]{"Programme Name", "Business", "IT", "Finance"};
        _userProgramme.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, _programme));

        String[] _nullList = new String[]{"Course ID"};
        _userCourseID.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, _nullList));

        _userProgramme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch(position)
                {
                    case 0:
                        break;
                    case 1:
                        BusinessProgramme();
                        break;
                    case 2:
                        ITProgramme();
                        break;
                    case 3:
                        FinanceProgramme();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        _updateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkValidation();
            }
        });
    }

    private void BusinessProgramme()
    {
        ArrayList<String> items_BUS = new ArrayList<String>();
        ArrayList<String> items_BUS1 = new ArrayList<String>();
        ArrayList<String> items_BUS2 = new ArrayList<String>();
        ArrayList<String> items_BUS3 = new ArrayList<String>();
        ArrayList<String> items_BUS4 = new ArrayList<String>();
        ArrayList<String> _list = new ArrayList<String>();
        _courseReference = _rootReference.child("Business");
        _courseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    UserData _data = snapshot1.getValue(UserData.class);
                    _list.add(_data.getCourseID());

                    CourseData _data1 = snapshot1.getValue(CourseData.class);
                    items_BUS.add(_data1.getCourseName());

                    items_BUS1.add(_data1.getSubject2());
                    items_BUS2.add(_data1.getSubject3());
                    items_BUS3.add(_data1.getSubject4());
                    items_BUS4.add(_data1.getSubject5());
                }
                ArrayAdapter<String> _programme_adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, _list);
                _programme_adapter.notifyDataSetChanged();
                _userCourseID.setAdapter(_programme_adapter);

                _userCourseID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (items_BUS.get(position) != null && items_BUS1.get(position) != null && items_BUS2.get(position) != null
                                && items_BUS3.get(position) != null && items_BUS4.get(position) != null)
                        {
                            String _getCourseName = items_BUS.get(position);
                            _userCourseName.setVisibility(View.VISIBLE);
                            _userCourseName.setText(_getCourseName);

                            String _getSub1 = items_BUS1.get(position);
                            _subject1.setVisibility(View.VISIBLE);
                            _subject1.setText(_getSub1);

                            String _getSub2 = items_BUS2.get(position);
                            _subject2.setVisibility(View.VISIBLE);
                            _subject2.setText(_getSub2);

                            String _getSub3 = items_BUS3.get(position);
                            _subject3.setVisibility(View.VISIBLE);
                            _subject3.setText(_getSub3);

                            String _getSub4 = items_BUS4.get(position);
                            _subject4.setVisibility(View.VISIBLE);
                            _subject4.setText(_getSub4);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }

                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(LecturerUpdate.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ITProgramme()
    {
        ArrayList<String> items_IT = new ArrayList<String>();
        ArrayList<String> items_IT1 = new ArrayList<String>();
        ArrayList<String> items_IT2 = new ArrayList<String>();
        ArrayList<String> items_IT3 = new ArrayList<String>();
        ArrayList<String> items_IT4 = new ArrayList<String>();

        ArrayList<String> _list = new ArrayList<String>();
        _courseReference = _rootReference.child("IT");
        _courseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    UserData _data = snapshot1.getValue(UserData.class);
                    _list.add(_data.getCourseID());

                    CourseData _data1 = snapshot1.getValue(CourseData.class);
                    items_IT.add(_data1.getCourseName());

                    items_IT1.add(_data1.getSubject2());
                    items_IT2.add(_data1.getSubject3());
                    items_IT3.add(_data1.getSubject4());
                    items_IT4.add(_data1.getSubject5());

                }
                ArrayAdapter<String> _programme_adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, _list);
                _programme_adapter.notifyDataSetChanged();
                _userCourseID.setAdapter(_programme_adapter);

                _userCourseID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (items_IT.get(position) != null && items_IT1.get(position) != null && items_IT2.get(position) != null
                            && items_IT3.get(position) != null && items_IT4.get(position) != null)
                        {
                            String _getCourseName = items_IT.get(position);
                            _userCourseName.setVisibility(View.VISIBLE);
                            _userCourseName.setText(_getCourseName);

                            String _getSub1 = items_IT1.get(position);
                            _subject1.setVisibility(View.VISIBLE);
                            _subject1.setText(_getSub1);

                            String _getSub2 = items_IT2.get(position);
                            _subject2.setVisibility(View.VISIBLE);
                            _subject2.setText(_getSub2);

                            String _getSub3 = items_IT3.get(position);
                            _subject3.setVisibility(View.VISIBLE);
                            _subject3.setText(_getSub3);

                            String _getSub4 = items_IT4.get(position);
                            _subject4.setVisibility(View.VISIBLE);
                            _subject4.setText(_getSub4);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }

                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(LecturerUpdate.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void FinanceProgramme()
    {
        ArrayList<String> items_FIN = new ArrayList<String>();
        ArrayList<String> items_FIN1 = new ArrayList<String>();
        ArrayList<String> items_FIN2 = new ArrayList<String>();
        ArrayList<String> items_FIN3 = new ArrayList<String>();
        ArrayList<String> items_FIN4 = new ArrayList<String>();

        ArrayList<String> _list = new ArrayList<String>();
        _courseReference = _rootReference.child("Finance");
        _courseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    UserData _data = snapshot1.getValue(UserData.class);
                    _list.add(_data.getCourseID());

                    CourseData _data1 = snapshot1.getValue(CourseData.class);
                    items_FIN.add(_data1.getCourseName());

                    items_FIN1.add(_data1.getSubject2());
                    items_FIN2.add(_data1.getSubject3());
                    items_FIN3.add(_data1.getSubject4());
                    items_FIN4.add(_data1.getSubject5());
                }
                ArrayAdapter<String> _programme_adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, _list);
                _programme_adapter.notifyDataSetChanged();
                _userCourseID.setAdapter(_programme_adapter);

                _userCourseID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (items_FIN.get(position) != null && items_FIN1.get(position) != null && items_FIN2.get(position) != null
                            && items_FIN3.get(position) != null && items_FIN4.get(position) != null)
                        {
                            String _getCourseName = items_FIN.get(position);
                            _userCourseName.setVisibility(View.VISIBLE);
                            _userCourseName.setText(_getCourseName);

                            String _getSub1 = items_FIN1.get(position);
                            _subject1.setVisibility(View.VISIBLE);
                            _subject1.setText(_getSub1);

                            String _getSub2 = items_FIN2.get(position);
                            _subject2.setVisibility(View.VISIBLE);
                            _subject2.setText(_getSub2);

                            String _getSub3 = items_FIN3.get(position);
                            _subject3.setVisibility(View.VISIBLE);
                            _subject3.setText(_getSub3);

                            String _getSub4 = items_FIN4.get(position);
                            _subject4.setVisibility(View.VISIBLE);
                            _subject4.setText(_getSub4);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }

                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(LecturerUpdate.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkValidation()
    {
        _name = _userName.getText().toString();
        _programme = _userProgramme.getSelectedItem().toString();
        _courseID = _userCourseID.getSelectedItem().toString();
        _courseName = _userCourseName.getText().toString();
        _sub1 = _subject1.getText().toString();
        _sub2 = _subject2.getText().toString();
        _sub3 = _subject3.getText().toString();
        _sub4 = _subject4.getText().toString();

        if(_name.isEmpty())
        {
            _userName.setError("Please enter your name.");
            _userName.requestFocus();
            return;
        }
        else if(_programme.equals("Programme Name"))
        {
            Toast.makeText(this, "Please select a programme!", Toast.LENGTH_SHORT).show();
        }
        else if(_courseID.equals("Course ID"))
        {
            Toast.makeText(this, "Please select a course!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            _dialog.setMessage("Updating user...");
            _dialog.show();

            updateData();
        }
    }

    private void updateData()
    {
        HashMap _hm = new HashMap();
        _hm.put("name", _name);
        _hm.put("programme", _programme);
        _hm.put("courseID", _courseID);
        _hm.put("courseName", _courseName);
        _hm.put("sub1", _sub1);
        _hm.put("sub2", _sub2);
        _hm.put("sub3", _sub3);
        _hm.put("sub4", _sub4);

        _childReference.child(_userID).updateChildren(_hm).addOnCompleteListener(new OnCompleteListener()
        {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if(task.isSuccessful())
                {
                    _dialog.dismiss();
                    Toast.makeText(LecturerUpdate.this, "Lecturer has been updated successfully.", Toast.LENGTH_SHORT).show();
                    Intent _intent = new Intent(LecturerUpdate.this, LecturerManage.class);
                    _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(_intent);
                    finish();
                }
                else
                {
                    _dialog.dismiss();
                    Toast.makeText(LecturerUpdate.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(LecturerUpdate.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}