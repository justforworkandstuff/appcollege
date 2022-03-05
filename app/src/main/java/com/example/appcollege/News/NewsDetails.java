package com.example.appcollege.News;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcollege.Adapter.CommentsAdapter;
import com.example.appcollege.Admin.NewsManage;
import com.example.appcollege.Class.CommentData;
import com.example.appcollege.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class NewsDetails extends AppCompatActivity
{
    private TextView _newsTitle, _newsDescription, _newsDate, _newsTime, _newsLikes, _newsComments, _userName;
    private Button _deleteButton;
    private ImageView _newsImage, _likesButton;
    private EditText _addComment;
    private CardView _addCommentButton;
    private RecyclerView _commentsRecycler;
    private ArrayList<CommentData> _list;
    private CommentsAdapter _adapter;

    private String _title, _description, _date, _time, _image, _userID, _name, _comments, _key;
    private Integer _likesNumber, _commentsNumber, _newLikesNumber, _newCommentsNumber;

    private DatabaseReference _dbReference, _secondRef, _thirdRef, _userRef, _userStudRef, _userLectRef;
    private FirebaseAuth _authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        _title = getIntent().getStringExtra("title");
        _description = getIntent().getStringExtra("description");
        _date = getIntent().getStringExtra("date");
        _time = getIntent().getStringExtra("time");
        _image = getIntent().getStringExtra("image");
        _key = getIntent().getStringExtra("key");

        _newsTitle = findViewById(R.id.news_details_title);
        _newsDescription = findViewById(R.id.news_details_description);
        _newsImage = findViewById(R.id.news_details_image);
        _newsDate = findViewById(R.id.news_details_date);
        _newsTime = findViewById(R.id.news_details_time);
        _newsLikes = findViewById(R.id.news_details_likes);
        _newsComments = findViewById(R.id.news_details_comments);
        _deleteButton = findViewById(R.id.news_details_manageButton);
        _addComment = findViewById(R.id.news_details_addComment);
        _addCommentButton = findViewById(R.id.news_details_addCommentButton);
        _likesButton = findViewById(R.id.news_details_likeButton);
        _userName = findViewById(R.id.news_details_userName);
        _commentsRecycler = findViewById(R.id.news_details_recycler);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(NewsDetails.this, 1);
        _commentsRecycler.setLayoutManager(gridLayoutManager);

        if(_image != null)
        {
            try
            {
                Picasso.get().load(_image).into(_newsImage);
                _newsImage.setVisibility(View.VISIBLE);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        _newsTitle.setText(_title);
        _newsDescription.setText(_description);
        _newsDate.setText(_date);
        _newsTime.setText(_time);

        _dbReference = FirebaseDatabase.getInstance().getReference().child("News");
        _userRef = FirebaseDatabase.getInstance().getReference().child("User");
        _userStudRef = _userRef.child("Student");
        _userLectRef = _userRef.child("Lecturer");
        _authentication = FirebaseAuth.getInstance();

        userChecking();
        likesNumberRead();
        commentsNumberRead();

        commentsRead();

        _addCommentButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkValidation();
                _addCommentButton.setEnabled(false);

                final Runnable _enableButton = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        _addCommentButton.setEnabled(true);
                    }
                };

                new Handler().postDelayed(_enableButton, 2000);
            }
        });

        _likesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateLikesNumber();
                _likesButton.setBackgroundResource(R.drawable.yellow_circle);

                _likesButton.setEnabled(false);

                final Runnable _enableButton = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        _likesButton.setBackgroundResource(R.drawable.purple_circle);
                        _likesButton.setEnabled(true);
                    }
                };

                new Handler().postDelayed(_enableButton, 500);
            }
        });
    }

    private void userChecking()
    {
        _userStudRef.child(_authentication.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String usertype;
                usertype = snapshot.child("category").getValue(String.class);

                if(usertype != null && usertype.equals("Student"))
                {
                    studentNameReader();
                }
                else if(_authentication.getCurrentUser().getEmail().equals("yee8896752@gmail.com"))
                {
                    _deleteButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    lecturerNameReader();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

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

    private void deleteData()
    {
        AlertDialog.Builder _builder = new AlertDialog.Builder(NewsDetails.this);
        _builder.setTitle("Delete News");
        _builder.setMessage("Are you sure you want to delete this news?");
        _builder.setCancelable(true);

        _builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                _dbReference.child(_key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(NewsDetails.this, "News deleted successfully.", Toast.LENGTH_SHORT).show();
                            Intent _intent = new Intent(NewsDetails.this, NewsManage.class);
                            _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(_intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(NewsDetails.this, "Error, please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(NewsDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void checkValidation()
    {
        _comments = _addComment.getText().toString();

        if(_comments.isEmpty())
        {
            _addComment.setError("Please write your comment first.");
            _addComment.requestFocus();
            return;
        }
        else
        {
            uploadCommentContent();
        }
    }

    private void uploadCommentContent()
    {
        _secondRef = _dbReference.child(_key);

        _userID = _authentication.getCurrentUser().getUid();

        //Generating Unique Key
        final String _uniqueKey = _secondRef.push().getKey();

        //Date
        Calendar _calForDate = Calendar.getInstance();
        SimpleDateFormat _currentDate = new SimpleDateFormat("dd-MM-yy");
        _date = _currentDate.format((_calForDate.getTime()));

        //Time
        Calendar _calForTime = Calendar.getInstance();
        SimpleDateFormat _currentTime = new SimpleDateFormat("hh:mm a");
        _time = _currentTime.format((_calForTime.getTime()));

        CommentData _commentData = new CommentData(_userID, _userName.getText().toString(), _date, _time, _comments, _uniqueKey, _key);

        _secondRef.child("Comments").child(_uniqueKey).setValue(_commentData).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                updateCommentsNumber();
                Toast.makeText(NewsDetails.this, "New comment added!", Toast.LENGTH_SHORT).show();
                _addComment.setText(null);
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(NewsDetails.this, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCommentsNumber()
    {
        _newCommentsNumber = _commentsNumber+1;
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
                    Toast.makeText(NewsDetails.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(NewsDetails.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void commentsNumberRead()
    {
        _secondRef = _dbReference.child(_key);

        _secondRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    _commentsNumber = snapshot.child("comments").getValue(Integer.class);
                }
                _newsComments.setText(_commentsNumber.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(NewsDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLikesNumber()
    {
        _newLikesNumber = _likesNumber+1;
        HashMap _hm = new HashMap();
        _hm.put("likes", _newLikesNumber);

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
                    Toast.makeText(NewsDetails.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(NewsDetails.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void likesNumberRead()
    {
        _secondRef = _dbReference.child(_key);

        _secondRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    _likesNumber = snapshot.child("likes").getValue(Integer.class);
                }
                _newsLikes.setText(_likesNumber.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(NewsDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void commentsRead()
    {
        _secondRef = _dbReference.child(_key);
        _thirdRef = _secondRef.child("Comments");

        _thirdRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _list = new ArrayList<>();
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    CommentData _data = snapshot1.getValue(CommentData.class);
                    _list.add(_data);
                }
                _adapter = new CommentsAdapter(NewsDetails.this, _list);
                _adapter.notifyDataSetChanged();
                _commentsRecycler.setAdapter(_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(NewsDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void lecturerNameReader()
    {
        _userLectRef.child(_authentication.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _name = snapshot.child("name").getValue(String.class);

                _userName.setText(_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private void studentNameReader()
    {
        _userStudRef.child(_authentication.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _name = snapshot.child("name").getValue(String.class);

                _userName.setText(_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}