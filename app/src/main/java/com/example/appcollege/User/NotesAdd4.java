package com.example.appcollege.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appcollege.Class.NotesData;
import com.example.appcollege.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NotesAdd4 extends AppCompatActivity {

    private EditText _notesTitle, _notesDescription;
    private Button _notesAddButton;
    private String _title, _notes, _date, _time, _key;

    private DatabaseReference _dbReference, _secondRef, _thirdRef, _fourthRef;
    private ProgressDialog _dialog;
    private FirebaseAuth _authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_add4);

        _notesTitle = findViewById(R.id.notes_add_title4);
        _notesDescription = findViewById(R.id.notes_add_Notes4);
        _notesAddButton = findViewById(R.id.notes_add_button4);

        //connect to Firebase Database & Storage
        _dbReference = FirebaseDatabase.getInstance().getReference().child("User");
        _authentication = FirebaseAuth.getInstance();

        _dialog = new ProgressDialog(this);

        _notesAddButton.setOnClickListener(new View.OnClickListener()
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
        _title = _notesTitle.getText().toString();
        _notes = _notesDescription.getText().toString();

        if(_title.isEmpty())
        {
            _notesTitle.setError("Please enter a title for your note.");
            _notesTitle.requestFocus();
            return;
        }
        else if(_notes.isEmpty())
        {
            _notesDescription.setError("Please enter your notes.");
            _notesDescription.requestFocus();
            return;
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
        _secondRef = _dbReference.child("Student");
        _thirdRef = _secondRef.child(_authentication.getCurrentUser().getUid());

        //Generating Unique Key
        final String _uniqueKey = _thirdRef.push().getKey();

        //Date
        Calendar _calForDate = Calendar.getInstance();
        SimpleDateFormat _currentDate = new SimpleDateFormat("dd-MM-yy");
        _date = _currentDate.format((_calForDate.getTime()));

        //Time
        Calendar _calForTime = Calendar.getInstance();
        SimpleDateFormat _currentTime = new SimpleDateFormat("hh:mm a");
        _time = _currentTime.format((_calForTime.getTime()));

        NotesData _notesData = new NotesData(_title, _notes, _date, _time, _uniqueKey);

        _thirdRef.child("Sub4").child(_uniqueKey).setValue(_notesData).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                _dialog.dismiss();
                Toast.makeText(NotesAdd4.this, "New notes added!", Toast.LENGTH_SHORT).show();

                Intent _intent = new Intent(NotesAdd4.this, NotesManage.class);
                _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(_intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                _dialog.dismiss();
                Toast.makeText(NotesAdd4.this, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}