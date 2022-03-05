package com.example.appcollege.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appcollege.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class AppDetails extends AppCompatActivity
{
    private TextView _adminDes, _adminEmail, _adminNum;
    private EditText _textDescription, _textEmail, _textNumber;
    private Button _editButton, _saveButton;
    private ImageView _detailsImage;

    private Bitmap _bitmap = null;
    private final int _REQ = 1;
    private String _downloadUrl = "";
    private ProgressDialog _dialog;
    private String _description, _email, _phoneNumber, _image;
    private DatabaseReference _dbReference;
    private StorageReference _stoReference;;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_details);

        _detailsImage = findViewById(R.id.app_details_admin_image);

        _adminDes = findViewById(R.id.app_details_admin_description);
        _adminEmail = findViewById(R.id.app_details_admin_email);
        _adminNum = findViewById(R.id.app_details_admin_phoneNumber);

        _textDescription = findViewById(R.id.app_details_edit_description);
        _textEmail = findViewById(R.id.app_details_edit_email);
        _textNumber = findViewById(R.id.app_details_edit_phoneNumber);

        _editButton = findViewById(R.id.app_details_updateButton);
        _saveButton = findViewById(R.id.app_details_saveButton);

        _dialog = new ProgressDialog(this);

        //connect to Firebase Database & Storage
        _dbReference = FirebaseDatabase.getInstance().getReference().child("AppDetails");
        _stoReference = FirebaseStorage.getInstance().getReference();

        adminRead();

        _detailsImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openGallery();
            }
        });

        _editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _textDescription.setText(_adminDes.getText().toString());
                _textDescription.setVisibility(View.VISIBLE);

                _textEmail.setText(_adminEmail.getText().toString());
                _textEmail.setVisibility(View.VISIBLE);

                _textNumber.setText(_adminNum.getText().toString());
                _textNumber.setVisibility(View.VISIBLE);

                _saveButton.setVisibility(View.VISIBLE);
            }
        });

        _saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkValidation();
            }
        });
    }

    private void adminRead()
    {
        _dbReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _image = snapshot.child("image").getValue(String.class);
                _description = snapshot.child("description").getValue(String.class);
                _email = snapshot.child("email").getValue(String.class);
                _phoneNumber = snapshot.child("number").getValue(String.class);

                if(_image.isEmpty())
                {
                    _detailsImage.setImageResource(R.drawable.imagenotavailable);
                }
                else
                {
                    Picasso.get().load(_image).into(_detailsImage);
                }

                _adminDes.setText(_description);
                _adminEmail.setText(_email);
                _adminNum.setText(_phoneNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private void checkValidation()
    {
        _description = _textDescription.getText().toString();
        _email = _textEmail.getText().toString();
        _phoneNumber = _textNumber.getText().toString();

        if(_description.isEmpty())
        {
            _textDescription.setError("Please enter description.");
            _textDescription.requestFocus();
            return;
        }
        else if(_email.isEmpty())
        {
            _textEmail.setError("Please enter a email.");
            _textEmail.requestFocus();
            return;
        }
        else if(_phoneNumber.isEmpty())
        {
            _textNumber.setError("Please enter a phone number.");
            _textNumber.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(_email).matches())
        {
            _textEmail.setError("Your email address is invalid.");
            _textEmail.requestFocus();
        }
        else if(_bitmap == null)
        {
            _dialog.setMessage("Updating without Image...");
            _dialog.show();

            saveData();
        }
        else
        {
            _dialog.setMessage("Updating...");
            _dialog.show();

            saveImageAndData();
        }
    }

    private void saveData()
    {
        HashMap _hm = new HashMap();
        _hm.put("description", _description);
        _hm.put("email", _email);
        _hm.put("number", _phoneNumber);

        //AppDetailsData _detailsData = new AppDetailsData(_description, _email, _phoneNumber);

        _dbReference.updateChildren(_hm).addOnCompleteListener(new OnCompleteListener()
        {
            @Override
            public void onComplete(@NonNull Task task)
            {
                _dialog.dismiss();
                Toast.makeText(AppDetails.this, "App Details updated successfully.", Toast.LENGTH_SHORT).show();

                recreate();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                _dialog.dismiss();
                Toast.makeText(AppDetails.this, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveImageAndData()
    {
        ByteArrayOutputStream _baos = new ByteArrayOutputStream();
        _bitmap.compress(Bitmap.CompressFormat.JPEG, 50, _baos);
        byte[] _finalImage = _baos.toByteArray();
        final StorageReference _filePath;
        _filePath = _stoReference.child("AppDetails").child(_finalImage+"jpg");

        final UploadTask _uploadTask = _filePath.putBytes(_finalImage);

        _uploadTask.addOnCompleteListener(AppDetails.this, new OnCompleteListener<UploadTask.TaskSnapshot>()
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

                                    HashMap _hm = new HashMap();
                                    _hm.put("description", _description);
                                    _hm.put("email", _email);
                                    _hm.put("number", _phoneNumber);
                                    _hm.put("image", _downloadUrl);

                                    //AppDetailsData _detailsData = new AppDetailsData(_description, _email, _phoneNumber, _downloadUrl);

                                    _dbReference.updateChildren(_hm).addOnCompleteListener(new OnCompleteListener()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task task)
                                        {
                                            _dialog.dismiss();
                                            Toast.makeText(AppDetails.this, "App Details updated successfully.", Toast.LENGTH_SHORT).show();

                                            recreate();
                                        }
                                    }).addOnFailureListener(new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                        {
                                            _dialog.dismiss();
                                            Toast.makeText(AppDetails.this, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else
                {
                    _dialog.dismiss();
                    Toast.makeText(AppDetails.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
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
            _detailsImage.setImageBitmap(_bitmap);
        }
    }
}