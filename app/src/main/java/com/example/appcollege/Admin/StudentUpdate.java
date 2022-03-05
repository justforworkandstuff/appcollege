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

public class StudentUpdate extends AppCompatActivity
{
    private EditText _userName, _userfeeAmount;
    private Spinner _userProgramme, _userCourseID, _userFeeStatus;
    private TextView _userCourseName, _userFeeBalance;
    private Button _updateButton;

    private String _name, _programme, _courseID, _courseName, _feeStatus, _feeBalance, _feeAmount, _userID;
    private ProgressDialog _dialog;
    private DatabaseReference _dbReference, _childReference, _rootReference, _courseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_update);

        _userName = findViewById(R.id.student_update_name);
        _userProgramme = findViewById(R.id.student_update_programme);
        _userCourseID = findViewById(R.id.student_update_courseID);
        _userCourseName = findViewById(R.id.student_update_courseName);
        _userFeeStatus = findViewById(R.id.student_update_feeStatus);
        _userFeeBalance = findViewById(R.id.student_update_feeBalance);
        _userfeeAmount = findViewById(R.id.student_update_feeAmount);
        _updateButton = findViewById(R.id.student_update_button);

        _dialog = new ProgressDialog(this);

        _dbReference = FirebaseDatabase.getInstance().getReference().child("User");
        _childReference = _dbReference.child("Student");
        _rootReference = FirebaseDatabase.getInstance().getReference().child("Course");

        _name = getIntent().getStringExtra("name");
        _feeBalance = getIntent().getStringExtra("feeAmount");
        _userID = getIntent().getStringExtra("userID");

        _userName.setText(_name);
        _userFeeBalance.setText("Current fee balance: " + _feeBalance);

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

        String[] _booList = new String[]{"Unpaid", "Paid"};
        _userFeeStatus.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, _booList));

        _userFeeStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch(position)
                {
                    case 0:
                        _userfeeAmount.setEnabled(true);
                        break;
                    case 1:
                        _userfeeAmount.setText("0");
                        _userfeeAmount.setEnabled(false);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    private void BusinessProgramme()
    {
        ArrayList<String> items_BUS = new ArrayList<String>();
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
                        if (items_BUS.get(position) != null)
                        {
                            String _getCourseName = items_BUS.get(position);
                            _userCourseName.setVisibility(View.VISIBLE);
                            _userCourseName.setText(_getCourseName);
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
                Toast.makeText(StudentUpdate.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ITProgramme()
    {
        ArrayList<String> items_IT = new ArrayList<String>();
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
                        if (items_IT.get(position) != null)
                        {
                            String _getCourseName = items_IT.get(position);
                            _userCourseName.setVisibility(View.VISIBLE);
                            _userCourseName.setText(_getCourseName);
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
                Toast.makeText(StudentUpdate.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void FinanceProgramme()
    {
        ArrayList<String> items_FIN = new ArrayList<String>();
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
                        if (items_FIN.get(position) != null)
                        {
                            String _getCourseName = items_FIN.get(position);
                            _userCourseName.setVisibility(View.VISIBLE);
                            _userCourseName.setText(_getCourseName);
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
                Toast.makeText(StudentUpdate.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkValidation()
    {
        _name = _userName.getText().toString();
        _programme = _userProgramme.getSelectedItem().toString();
        _courseID = _userCourseID.getSelectedItem().toString();
        _courseName = _userCourseName.getText().toString();
        _feeStatus = _userFeeStatus.getSelectedItem().toString();
        _feeAmount = "RM " + _userfeeAmount.getText().toString();

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
        else if(_feeAmount.isEmpty())
        {
            _userfeeAmount.setError("Please enter a fee amount.");
            _userfeeAmount.requestFocus();
            return;
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
        _hm.put("feeStatus", _feeStatus);
        _hm.put("feeAmount", _feeAmount);

        _childReference.child(_userID).updateChildren(_hm).addOnCompleteListener(new OnCompleteListener()
        {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if(task.isSuccessful())
                {
                    _dialog.dismiss();
                    Toast.makeText(StudentUpdate.this, "Student has been updated successfully.", Toast.LENGTH_SHORT).show();
                    Intent _intent = new Intent(StudentUpdate.this, UserManage.class);
                    _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(_intent);
                    finish();
                }
                else
                {
                    _dialog.dismiss();
                    Toast.makeText(StudentUpdate.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(StudentUpdate.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}