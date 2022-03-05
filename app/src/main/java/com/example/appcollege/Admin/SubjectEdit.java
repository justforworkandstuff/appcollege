package com.example.appcollege.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appcollege.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SubjectEdit extends AppCompatActivity
{
    private TextView _courseNumber;
    private EditText _courseName, _subject2, _subject3, _subject4, _subject5, _feeAmount;
    private Button _updateButton, _deleteButton;
    private String _courseID, _cName, _sub2, _sub3, _sub4, _sub5, _fee, _category;
    private DatabaseReference _dbReference, _departmentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_edit);

        _courseID = getIntent().getStringExtra("courseID");
        _cName = getIntent().getStringExtra("courseName");
        _sub2 = getIntent().getStringExtra("subject2");
        _sub3 = getIntent().getStringExtra("subject3");
        _sub4 = getIntent().getStringExtra("subject4");
        _sub5 = getIntent().getStringExtra("subject5");
        _fee = getIntent().getStringExtra("feeAmount");
        _category = getIntent().getStringExtra("category");

        _courseNumber = findViewById(R.id.subject_edit_ID);
        _courseName = findViewById(R.id.subject_edit_name);
        _subject2 = findViewById(R.id.subject_edit_2);
        _subject3 = findViewById(R.id.subject_edit_3);
        _subject4 = findViewById(R.id.subject_edit_4);
        _subject5 = findViewById(R.id.subject_edit_5);
        _feeAmount = findViewById(R.id.subject_edit_feeAmount);
        _updateButton = findViewById(R.id.subject_edit_button);
        _deleteButton = findViewById(R.id.subject_delete_button);

        _dbReference = FirebaseDatabase.getInstance().getReference().child("Course");
        _departmentRef = _dbReference.child(_category);

        _courseNumber.setText(_courseID);
        _courseName.setText(_cName);
        _subject2.setText((_sub2));
        _subject3.setText((_sub3));
        _subject4.setText((_sub4));
        _subject5.setText((_sub5));
        _feeAmount.setText(_fee);

        _updateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _courseID = _courseNumber.getText().toString();
                _cName = _courseName.getText().toString();
                _sub2 = _subject2.getText().toString();
                _sub3 = _subject3.getText().toString();
                _sub4 = _subject4.getText().toString();
                _sub5 = _subject5.getText().toString();
                _fee = _feeAmount.getText().toString();
                checkValidation();
            }
        });

        _deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                deleteData();
            }
        });
    }

    private void checkValidation()
    {
        if(_cName.isEmpty())
        {
            _courseName.setError("Empty");
            _courseName.requestFocus();
            return;
        }
        else if(_sub2.isEmpty())
        {
            _subject2.setError("Empty");
            _subject2.requestFocus();
            return;
        }
        else if(_sub3.isEmpty())
        {
            _subject3.setError("Empty");
            _subject3.requestFocus();
            return;
        }
        else if(_sub4.isEmpty())
        {
            _subject4.setError("Empty");
            _subject4.requestFocus();
            return;
        }
        else if(_sub5.isEmpty())
        {
            _subject5.setError("Empty");
            _subject5.requestFocus();
            return;
        }
        else if(_fee.equals("RM "))
        {
            _feeAmount.setError("Please enter the fee amount.");
            _feeAmount.requestFocus();
            return;
        }
        else
        {
            updateData();
        }
    }

    private void updateData()
    {
        HashMap _hm = new HashMap();
        _hm.put("courseName", _cName);
        _hm.put("subject2", _sub2);
        _hm.put("subject3", _sub3);
        _hm.put("subject4", _sub4);
        _hm.put("subject5", _sub5);
        _hm.put("feeAmount", _fee);

        _departmentRef.child(_courseID).updateChildren(_hm).addOnSuccessListener(new OnSuccessListener()
        {
            @Override
            public void onSuccess(Object o)
            {
                Toast.makeText(SubjectEdit.this, "Subject update successfully.", Toast.LENGTH_SHORT).show();
                Intent _intent = new Intent(SubjectEdit.this, SubjectManage.class);
                _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(_intent);
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(SubjectEdit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteData()
    {
        _departmentRef.child(_courseID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                Toast.makeText(SubjectEdit.this, "Course deleted successfully.", Toast.LENGTH_SHORT).show();
                Intent _intent = new Intent(SubjectEdit.this, SubjectManage.class);
                _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(_intent);
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(SubjectEdit.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}