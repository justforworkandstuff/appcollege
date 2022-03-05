package com.example.appcollege.User;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.example.appcollege.R;
import com.example.appcollege.UserResetPassword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class UserEdit extends AppCompatActivity {

    private ImageView _userImage;
    private TextView _userID, _userEmail, _userCategory;
    private EditText _userName, _userNumber, _userAddress;
    private Button _updateButton, _resetButton;

    private String _image, _name, _email, _number, _address, _key, _category;
    private Bitmap _bitmap = null;
    private final int _REQ = 1;
    private ProgressDialog _dialog;
    private DatabaseReference _dbReference, _childReference, _childReference2;
    private StorageReference _stoReference;
    private FirebaseAuth _authentication;
    private AuthCredential _credentials;
    private FirebaseUser _user;
    private String _downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        _userImage = findViewById(R.id.user_edit_userImage);
        _userID = findViewById(R.id.user_edit_userID);
        _userName = findViewById(R.id.user_edit_userName);
        _userEmail = findViewById(R.id.user_edit_userEmail);
        _userCategory = findViewById(R.id.user_edit_userCategory);
        _userNumber = findViewById(R.id.user_edit_userNumber);
        _userAddress = findViewById(R.id.user_edit_userAddress);
        _updateButton = findViewById(R.id.user_edit_updateButton);
        _resetButton = findViewById(R.id.user_edit_resetButton);

        _dialog = new ProgressDialog(this);

        //connect to Firebase Database & Storage
        _dbReference = FirebaseDatabase.getInstance().getReference().child("User");
        _childReference = _dbReference.child("Student");
        _childReference2 = _dbReference.child("Lecturer");
        _stoReference = FirebaseStorage.getInstance().getReference();

        _user = FirebaseAuth.getInstance().getCurrentUser();
        _authentication = FirebaseAuth.getInstance();

        _childReference.child(_authentication.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String usertype;
                usertype = snapshot.child("category").getValue(String.class);

                if(usertype != null && usertype.equals("Student"))
                {
                    userRead();
                    _userCategory.setText("Student");
                }
                else
                {
                    lectRead();
                    _userCategory.setText("Lecturer");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        _userImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openGallery();
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

        _resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder _builder = new AlertDialog.Builder(UserEdit.this);
                _builder.setTitle("Reset Password");
                _builder.setMessage("Are you sure you want to reset password?");
                _builder.setCancelable(true);

                _builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
                {
                    @Override
                   public void onClick(DialogInterface dialog, int which)
                    {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(UserEdit.this, UserResetPassword.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

                _builder.setNegativeButton("NO", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

                _builder.show();
            }
        });
    }

    private void userRead()
    {
        _childReference.child(_authentication.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _image = snapshot.child("image").getValue(String.class);
                _key = snapshot.child("key").getValue(String.class);
                _name = snapshot.child("name").getValue(String.class);
                _email = snapshot.child("email").getValue(String.class);
                _number = snapshot.child("number").getValue(String.class);
                _address = snapshot.child("address").getValue(String.class);

                Picasso.get().load(_image).into(_userImage);
                _userID.setText("User ID: " + _key);
                _userName.setText(_name);
                _userEmail.setText(_email);
                _userNumber.setText(_number);
                _userAddress.setText(_address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    private void lectRead()
    {
        _childReference2.child(_authentication.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                _image = snapshot.child("image").getValue(String.class);
                _key = snapshot.child("key").getValue(String.class);
                _name = snapshot.child("name").getValue(String.class);
                _email = snapshot.child("email").getValue(String.class);
                _number = snapshot.child("number").getValue(String.class);
                _address = snapshot.child("address").getValue(String.class);

                Picasso.get().load(_image).into(_userImage);
                _userID.setText("User ID: " + _key);
                _userName.setText(_name);;
                _userEmail.setText(_email);
                _userNumber.setText(_number);
                _userAddress.setText(_address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    /*private void updateEmailAndPassword()
    {
        _credentials = EmailAuthProvider.getCredential(_user.getEmail(), _oldpw);
        _user.reauthenticate(_credentials).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    _user.updateEmail(_userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(UserEdit.this, "Something went wrong on the email, please try again.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                _user.sendEmailVerification();
                                Toast.makeText(UserEdit.this,
                                        "User's email has been successfully updated, please re-verify your email again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(UserEdit.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                    _user.updatePassword(_userPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(UserEdit.this, "Something went wrong on the password, please try again.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(UserEdit.this, "User's password has been successfully updated.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(UserEdit.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(UserEdit.this, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(UserEdit.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }*/

    private void checkValidation()
    {
        _name = _userName.getText().toString();
        _email = _userEmail.getText().toString();
        _number = _userNumber.getText().toString();
        _address = _userAddress.getText().toString();

        if(_name.isEmpty())
        {
            _userName.setError("Please enter your name.");
            _userName.requestFocus();
            return;
        }
        else if(_number.isEmpty())
        {
            _userNumber.setError("Please enter your contact number.");
            _userNumber.requestFocus();
            return;
        }
        else if(_address.isEmpty())
        {
            _userAddress.setError("Please enter your address.");
            _userAddress.requestFocus();
            return;
        }
        else if(_bitmap == null)
        {
            _dialog.setMessage("Updating...");
            _dialog.show();
            updateData(_image);
        }
        else
        {
            _dialog.setMessage("Updating...");
            _dialog.show();

            updateDataWithImage();
        }
    }

    private void updateData(String s)
    {
        if(_userCategory.getText().toString().equals("Student"))
        {
            HashMap _hm = new HashMap();
            _hm.put("image", s);
            _hm.put("name", _name);
            _hm.put("number", _number);
            _hm.put("address", _address);

            _childReference.child(_authentication.getCurrentUser().getUid()).updateChildren(_hm).addOnCompleteListener(new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        _dialog.dismiss();
                        Toast.makeText(UserEdit.this, "Student has been updated successfully.", Toast.LENGTH_SHORT).show();
                        Intent _intent = new Intent(UserEdit.this, UserProfile.class);
                        startActivity(_intent);
                        finish();
                    }
                    else
                    {
                        _dialog.dismiss();
                        Toast.makeText(UserEdit.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else if(_userCategory.getText().toString().equals("Lecturer"))
        {
            HashMap _hm2 = new HashMap();
            _hm2.put("image", s);
            _hm2.put("name", _name);
            _hm2.put("number", _number);
            _hm2.put("address", _address);

            _childReference2.child(_authentication.getCurrentUser().getUid()).updateChildren(_hm2).addOnCompleteListener(new OnCompleteListener()
            {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        _dialog.dismiss();
                        Toast.makeText(UserEdit.this, "Lecturer has been updated successfully.", Toast.LENGTH_SHORT).show();
                        Intent _intent = new Intent(UserEdit.this, UserProfile.class);
                        startActivity(_intent);
                        finish();
                    }
                    else
                    {
                        _dialog.dismiss();
                        Toast.makeText(UserEdit.this, "Something went wrong, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void updateDataWithImage()
    {
        ByteArrayOutputStream _baos = new ByteArrayOutputStream();
        _bitmap.compress(Bitmap.CompressFormat.JPEG, 50, _baos);
        byte[] _finalImage = _baos.toByteArray();
        final StorageReference _filePath;
        _filePath = _stoReference.child("User").child(_finalImage+"jpg");

        final UploadTask _uploadTask = _filePath.putBytes(_finalImage);

        _uploadTask.addOnCompleteListener(UserEdit.this, new OnCompleteListener<UploadTask.TaskSnapshot>()
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
                                    updateData(_downloadUrl);
                                }
                            });
                        }
                    });
                }
                else
                {
                    _dialog.dismiss();
                    Toast.makeText(UserEdit.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
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
            _userImage.setImageBitmap(_bitmap);
        }
    }

    /*private void deleteData()
    {
        AlertDialog.Builder _builder = new AlertDialog.Builder(UserEdit.this);
        _builder.setTitle("Delete Account");
        _builder.setMessage("Are you sure you want to delete this account?");
        _builder.setCancelable(true);

        _builder.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                _childReference.child(_authentication.getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            _user.delete();
                            Toast.makeText(UserEdit.this, "User deleted successfully.", Toast.LENGTH_SHORT).show();
                            Intent _intent = new Intent(UserEdit.this, UserLogin.class);
                            _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(_intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(UserEdit.this, "Error, please try again later.", Toast.LENGTH_SHORT).show();
                        }
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
    }*/
}