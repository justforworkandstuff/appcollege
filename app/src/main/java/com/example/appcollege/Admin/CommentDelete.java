package com.example.appcollege.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appcollege.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CommentDelete extends AppCompatActivity
{
    private TextView _userName, _commentDate, _commentTime, _commentContent, _keyHolder, _newsKeyHolder, _commentCounter;
    private Button _deleteButton;

    private DatabaseReference _dbReference, _secondRef, _thirdRef;
    private String _name, _date, _time, _content, _key, _newsKey;
    private Integer _commentsNumber, _newCommentsNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_delete);

        _userName = findViewById(R.id.comment_delete_name);
        _commentDate = findViewById(R.id.comment_delete_date);
        _commentTime = findViewById(R.id.comment_delete_time);
        _commentContent = findViewById(R.id.comment_delete_commentContent);
        _keyHolder = findViewById(R.id.comment_delete_key);
        _newsKeyHolder = findViewById(R.id.comment_delete_newsKey);
        _commentCounter = findViewById(R.id.comment_delete_commentNumber);
        _deleteButton = findViewById(R.id.comment_delete_Button);

        _name = getIntent().getStringExtra("name");
        _date = getIntent().getStringExtra("date");
        _time = getIntent().getStringExtra("time");
        _content = getIntent().getStringExtra("comment");
        _key = getIntent().getStringExtra("key");
        _newsKey = getIntent().getStringExtra("newsKey");

        _userName.setText(_name);
        _commentDate.setText(_date);
        _commentTime.setText(_time);
        _commentContent.setText(_content);
        _keyHolder.setText(_key);
        _newsKeyHolder.setText(_newsKey);

        _dbReference = FirebaseDatabase.getInstance().getReference().child("News");

        commentsNumberRead();

        _deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                deleteData();
            }
        });
    }

    private void deleteData()
    {

        AlertDialog.Builder _builder = new AlertDialog.Builder(CommentDelete.this);
        _builder.setTitle("Delete Comment");
        _builder.setMessage("Are you sure you want to delete this comment?");
        _builder.setCancelable(true);

        _builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                _secondRef = _dbReference.child(_newsKey);
                _thirdRef = _secondRef.child("Comments");

                _thirdRef.child(_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            updateCommentsNumber();
                            Toast.makeText(CommentDelete.this, "Comment deleted successfully.", Toast.LENGTH_SHORT).show();
                            Intent _intent = new Intent(CommentDelete.this, NewsManage.class);
                            _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(_intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(CommentDelete.this, "Error, please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(CommentDelete.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void commentsNumberRead()
    {
        _secondRef = _dbReference.child(_newsKey);

        _secondRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    _commentsNumber = snapshot.child("comments").getValue(Integer.class);
                }
                _commentCounter.setText(_commentsNumber.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(CommentDelete.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCommentsNumber()
    {
        _newCommentsNumber = _commentsNumber-1;
        HashMap _hm = new HashMap();
        _hm.put("comments", _newCommentsNumber);

        _secondRef.updateChildren(_hm).addOnCompleteListener(new OnCompleteListener()
        {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if(task.isSuccessful())
                {
                    //Toast.makeText(NewsDetails.this, "Comments count has been updated successfully.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(CommentDelete.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(CommentDelete.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}