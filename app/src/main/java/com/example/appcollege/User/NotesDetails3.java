package com.example.appcollege.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appcollege.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class NotesDetails3 extends AppCompatActivity
{
    private TextView _notesDate, _notesTime;
    private EditText _notesTitle, _notesDescription;
    private Button _saveButton, _deleteButton;

    private String _title, _notes, _date, _time, _key;
    private String _noteName, _noteContent, _noteDateInfo, _noteTimeInfo;
    private DatabaseReference _dbReference, _secondRef, _thirdRef, _fourthRef;
    private FirebaseAuth _authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details3);

        _title = getIntent().getStringExtra("title");
        _notes = getIntent().getStringExtra("notes");
        _date = getIntent().getStringExtra("date");
        _time = getIntent().getStringExtra("time");
        _key = getIntent().getStringExtra("key");

        _notesTitle = findViewById(R.id.notes_details_title3);
        _notesDescription = findViewById(R.id.notes_details_notes3);
        _notesDate = findViewById(R.id.notes_details_date3);
        _notesTime = findViewById(R.id.notes_details_time3);
        _saveButton = findViewById(R.id.notes_details_saveButton3);
        _deleteButton = findViewById(R.id.notes_details_deleteButton3);

        _notesTitle.setText(_title);
        _notesDescription.setText(_notes);
        _notesDate.setText(_date);
        _notesTime.setText(_time);

        _dbReference = FirebaseDatabase.getInstance().getReference().child("User");
        _authentication = FirebaseAuth.getInstance();

        _saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateData();
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

    private void updateData()
    {
        _noteName = _notesTitle.getText().toString();
        _noteContent = _notesDescription.getText().toString();

        //Date
        Calendar _calForDate = Calendar.getInstance();
        SimpleDateFormat _currentDate = new SimpleDateFormat("dd-MM-yy");
        _noteDateInfo = _currentDate.format((_calForDate.getTime()));

        //Time
        Calendar _calForTime = Calendar.getInstance();
        SimpleDateFormat _currentTime = new SimpleDateFormat("hh:mm a");
        _noteTimeInfo = _currentTime.format((_calForTime.getTime()));

        if(_noteName.isEmpty())
        {
            _notesTitle.setError("Please enter a title for your note.");
            _notesTitle.requestFocus();
            return;
        }
        else if(_noteContent.isEmpty())
        {
            _notesDescription.setError("Please enter your note.");
            _notesDescription.requestFocus();
            return;
        }
        else
        {
            HashMap _hm = new HashMap();
            _hm.put("title", _noteName);
            _hm.put("notes", _noteContent);
            _hm.put("date", _noteDateInfo);
            _hm.put("time", _noteTimeInfo);

            _secondRef = _dbReference.child("Student");
            _thirdRef = _secondRef.child(_authentication.getCurrentUser().getUid());
            _fourthRef = _thirdRef.child("Sub3");

            _fourthRef.child(_key).updateChildren(_hm).addOnCompleteListener(new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(NotesDetails3.this, "Note has been updated successfully.", Toast.LENGTH_SHORT).show();
                        Intent _intent = new Intent(NotesDetails3.this, NotesManage.class);
                        startActivity(_intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(NotesDetails3.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void deleteData()
    {
        AlertDialog.Builder _builder = new AlertDialog.Builder(NotesDetails3.this);
        _builder.setTitle("Delete Note");
        _builder.setMessage("Are you sure you want to delete this note?");
        _builder.setCancelable(true);

        _builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                _secondRef = _dbReference.child("Student");
                _thirdRef = _secondRef.child(_authentication.getCurrentUser().getUid());
                _fourthRef = _thirdRef.child("Sub3");

                _fourthRef.child(_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(NotesDetails3.this, "Note deleted successfully.", Toast.LENGTH_SHORT).show();
                            Intent _intent = new Intent(NotesDetails3.this, NotesManage.class);
                            _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(_intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(NotesDetails3.this, "Error, please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(NotesDetails3.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        _builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.dismiss();
            }
        });

        _builder.show();
    }
}