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
import com.example.appcollege.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SubjectAdd extends AppCompatActivity
{
    private TextView _departmentID;
    private EditText _courseID, _courseName, _subject2, _subject3, _subject4, _subject5, _feeAmount;
    private Spinner _courseSelection;
    private Button _addButton;
    private String _courseCategory;
    private ProgressDialog _dialog;
    private DatabaseReference _dbReference, _departmentRef, _thirdReference;
    private StorageReference _stoReference;
    private String cID, cName, sub2, sub3, sub4, sub5, _fee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_add);

        //connect to Firebase Database & Storage
        _dbReference = FirebaseDatabase.getInstance().getReference().child("Course");
        _stoReference = FirebaseStorage.getInstance().getReference();

        _departmentID = findViewById(R.id.subject_add_ID_display);
        _courseID = findViewById(R.id.subject_add_courseID);
        _courseName = findViewById(R.id.subject_add_name);
        _subject2 = findViewById(R.id.subject_add_2);
        _subject3 = findViewById(R.id.subject_add_3);
        _subject4 = findViewById(R.id.subject_add_4);
        _subject5 = findViewById(R.id.subject_add_5);
        _feeAmount = findViewById(R.id.subject_add_feeAmount);
        _courseSelection = findViewById(R.id.subject_addCategory);
        _addButton = findViewById(R.id.subject_add_Button);

        _dialog = new ProgressDialog(this);

        String[] _course = new String[]{"Business", "IT", "Finance"};
        _courseSelection.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, _course));

        _courseSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                _courseCategory = _courseSelection.getSelectedItem().toString();
                if(_courseCategory.equals("Business"))
                {
                    _departmentID.setText("BUS");
                }
                else if(_courseCategory.equals("IT"))
                {
                    _departmentID.setText("IT");
                }
                else if(_courseCategory.equals("Finance"))
                {
                    _departmentID.setText("FIN");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        _addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkValidation();
            }
        });
    }

    private void checkValidation()
    {
        cID = _departmentID.getText().toString() + _courseID.getText().toString();
        cName = _courseName.getText().toString();
        sub2 = _subject2.getText().toString();
        sub3 = _subject3.getText().toString();
        sub4 = _subject4.getText().toString();
        sub5 = _subject5.getText().toString();
        _fee = "RM " + _feeAmount.getText().toString();

        if(cID.equals("BUS"))
        {
            _courseID.setError("Please enter a courseID.");
            _courseID.requestFocus();
            return;

        }
        else if(cName.isEmpty())
        {
            _courseName.setError("Empty");
            _courseName.requestFocus();
            return;

        }
        else if(sub2.isEmpty())
        {
            _subject2.setError("Empty");
            _subject2.requestFocus();
            return;
        }
        else if(sub3.isEmpty())
        {
            _subject3.setError("Empty");
            _subject3.requestFocus();
            return;
        }
        else if(sub4.isEmpty())
        {
            _subject4.setError("Empty");
            _subject4.requestFocus();
            return;

        }
        else if(sub5.isEmpty())
        {
            _subject5.setError("Empty");
            _subject5.requestFocus();
            return;
        }
        else if(_fee.equals("RM "))
        {
            _feeAmount.setError("Please enter the fee amount. ");
            _feeAmount.requestFocus();
            return;
        }
        else if(_courseCategory.equals("Select Category"))
        {
            Toast.makeText(this, "Please select category!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            _dialog.setMessage("Uploading...");
            _dialog.show();

            uploadData();
        }
    }

    private void uploadData()
    {
        _departmentRef = _dbReference.child(_courseCategory);
        _thirdReference = _departmentRef.child(cID);
        //final String _uniqueKey = _thirdReference.push().getKey();

        CourseData _courseData = new CourseData(cID, cName, sub2, sub3, sub4, sub5, _fee);

        _thirdReference.setValue(_courseData).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                _dialog.dismiss();
                Toast.makeText(SubjectAdd.this, "Course added successfully.", Toast.LENGTH_SHORT).show();

                Intent _intent = new Intent(SubjectAdd.this, SubjectManage.class);
                _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(_intent);
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                _dialog.dismiss();
                Toast.makeText(SubjectAdd.this, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}