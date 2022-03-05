package com.example.appcollege.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.appcollege.Class.NewsData;
import com.example.appcollege.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewsAdd extends AppCompatActivity
{
    private CardView _imageAddButton;
    private ImageView _imageViewer;
    private EditText _newsTitle, _newsDescription;
    private Button _newsAddButton;
    private TextView _newsLikes, _newsComments;

    private final int _REQ = 1;
    private Bitmap _bitmap;
    private DatabaseReference _dbReference, _anotherReference;
    private StorageReference _stoReference;
    private String _title, _description, _date, _time, _convertLikes, _convertComments;
    private Integer _likes, _comments;
    private String _downloadUrl = "";
    private ProgressDialog _dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_add);

        //connect to Firebase Database & Storage
        _dbReference = FirebaseDatabase.getInstance().getReference();
        _stoReference = FirebaseStorage.getInstance().getReference();

        _dialog = new ProgressDialog(this);

        _newsLikes = findViewById(R.id.news_add_likes);
        _newsComments = findViewById(R.id.news_add_comments);
        _imageAddButton = findViewById(R.id.news_add_image);
        _imageViewer = findViewById(R.id.news_add_imageView);
        _newsTitle = findViewById(R.id.news_add_title);
        _newsDescription = findViewById(R.id.news_add_description);
        _newsAddButton = findViewById(R.id.news_add_button);

        _imageAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openGallery();
            }
        });

        _newsAddButton.setOnClickListener(new View.OnClickListener()
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
        _title = _newsTitle.getText().toString();
        _description = _newsDescription.getText().toString();

        _convertLikes = _newsLikes.getText().toString();
        _likes = Integer.parseInt(_convertLikes.replaceAll("[\\D]", ""));

        _convertComments = _newsComments.getText().toString();
        _comments = Integer.parseInt(_convertComments.replaceAll("[\\D]", ""));

        if(_title.isEmpty())
        {
            _newsTitle.setError("Please enter a news title.");
            _newsTitle.requestFocus();
        }
        else if(_description.isEmpty())
        {
            _newsDescription.setError("Please enter news description");
            _newsDescription.requestFocus();
        }
        else if(_bitmap == null)
        {
            _dialog.setMessage("Uploading...");
            _dialog.show();

            uploadData();
        }
        else
        {
            _dialog.setMessage("Uploading...");
            _dialog.show();

            uploadDataWithImage();
        }
    }

    private void uploadData()
    {
        _anotherReference = _dbReference.child("News");

        //Generating Unique Key
        final String _uniqueKey = _anotherReference.push().getKey();

        //Date
        Calendar _calForDate = Calendar.getInstance();
        SimpleDateFormat _currentDate = new SimpleDateFormat("dd-MM-yy");
        _date = _currentDate.format((_calForDate.getTime()));

        //Time
        Calendar _calForTime = Calendar.getInstance();
        SimpleDateFormat _currentTime = new SimpleDateFormat("hh:mm a");
        _time = _currentTime.format((_calForTime.getTime()));

        NewsData _newsData = new NewsData(_title, _description, _downloadUrl, _date, _time, _likes, _comments, _uniqueKey);

        _anotherReference.child(_uniqueKey).setValue(_newsData).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                _dialog.dismiss();
                Toast.makeText(NewsAdd.this, "News Uploaded!", Toast.LENGTH_SHORT).show();

                Intent _intent = new Intent(NewsAdd.this, NewsManage.class);
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
                Toast.makeText(NewsAdd.this, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadDataWithImage()
    {
        ByteArrayOutputStream _baos = new ByteArrayOutputStream();
        _bitmap.compress(Bitmap.CompressFormat.JPEG, 50, _baos);
        byte[] _finalImage = _baos.toByteArray();
        final StorageReference _filePath;
        _filePath = _stoReference.child("News").child(_finalImage+"jpg");

        final UploadTask _uploadTask = _filePath.putBytes(_finalImage);

        _uploadTask.addOnCompleteListener(NewsAdd.this, new OnCompleteListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    _uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            _filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {
                                @Override
                                public void onSuccess(Uri uri)
                                {
                                    _downloadUrl = String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                }
                else
                {
                    _dialog.dismiss();
                    Toast.makeText(NewsAdd.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery()
    {
        Intent _pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(_pickImage, _REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == _REQ && resultCode == RESULT_OK)
        {
            Uri uri = data.getData();
            try
            {
                _bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            _imageViewer.setVisibility(View.VISIBLE);
            _imageViewer.setImageBitmap(_bitmap);
        }
    }
}