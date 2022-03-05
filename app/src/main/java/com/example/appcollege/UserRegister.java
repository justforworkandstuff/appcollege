package com.example.appcollege;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appcollege.Class.CourseData;
import com.example.appcollege.Class.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class UserRegister extends AppCompatActivity
{

    private ImageView _userImage;
    private EditText _userName, _userPassword, _userEmail, _userNumber, _userAddress;
    private TextView _userCourseName, _userfeeStatus, _userfeeAmount, _subject1, _subject2, _subject3, _subject4;
    private Spinner _userCategory, _userProgramme, _userCourseID;
    private Button _registerButton;
    private String _name, _password, _email, _number, _address, _courseID, _courseName,
            _feeStatus, _feeAmount, _sub1, _sub2, _sub3, _sub4, _userType, _programmeCategory;

    private Bitmap _bitmap = null;
    private final int _REQ = 1;
    private String _downloadUrl = "";
    private ProgressDialog _dialog;
    private DatabaseReference _dbReference, _categoryReference, _rootReference, _courseReference;
    private StorageReference _stoReference;
    private FirebaseAuth _authentication;
    private FirebaseUser _user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        //connect to Firebase Database & Storage
        _dbReference = FirebaseDatabase.getInstance().getReference().child("User");
        _stoReference = FirebaseStorage.getInstance().getReference();
        _authentication = FirebaseAuth.getInstance();
        _rootReference = FirebaseDatabase.getInstance().getReference().child("Course");

        _userImage = findViewById(R.id.user_register_image);
        _userName = findViewById(R.id.user_register_name);
        _userPassword = findViewById(R.id.user_register_password);
        _userEmail = findViewById(R.id.user_register_email);
        _userNumber = findViewById(R.id.user_register_number);
        _userAddress = findViewById(R.id.user_register_address);
        _userCourseID = findViewById(R.id.user_register_courseID);
        _userCourseName = findViewById(R.id.user_register_courseName);
        _userfeeStatus = findViewById(R.id.user_register_feeStatus);
        _userfeeAmount = findViewById(R.id.user_register_feeAmount);
        _subject1 = findViewById(R.id.user_register_sub1);
        _subject2 = findViewById(R.id.user_register_sub2);
        _subject3 = findViewById(R.id.user_register_sub3);
        _subject4 = findViewById(R.id.user_register_sub4);
        _userCategory = findViewById(R.id.user_register_category);
        _userProgramme = findViewById(R.id.user_register_programme);
        _registerButton = findViewById(R.id.user_register_button);

        _dialog = new ProgressDialog(this);

        String[] _items = new String[]{"User Category", "Lecturer", "Student"};
        _userCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, _items));

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

        _userImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openGallery();
            }
        });

        _registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkValidation();
            }
        });

    }

    private void BusinessProgramme()
    {
        ArrayList<String> items_BUS = new ArrayList<String>();
        ArrayList<String> items_BUS1 = new ArrayList<String>();
        ArrayList<String> items_BUS2 = new ArrayList<String>();
        ArrayList<String> items_BUS3 = new ArrayList<String>();
        ArrayList<String> items_BUS4 = new ArrayList<String>();
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

                    items_BUS1.add(_data1.getSubject2());
                    items_BUS2.add(_data1.getSubject3());
                    items_BUS3.add(_data1.getSubject4());
                    items_BUS4.add(_data1.getSubject5());

                    if(_userCategory.getSelectedItem().toString().equals("Student"))
                    {
                        _subject1.setVisibility(View.VISIBLE);
                        _subject2.setVisibility(View.VISIBLE);
                        _subject3.setVisibility(View.VISIBLE);
                        _subject4.setVisibility(View.VISIBLE);

                        String _getFeeAmount = _data1.getFeeAmount();
                        _userfeeStatus.setVisibility(View.VISIBLE);
                        _userfeeStatus.setText("Unpaid");

                        _userfeeAmount.setVisibility(View.VISIBLE);
                        _userfeeAmount.setText(_getFeeAmount);
                    }
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
                        if(items_BUS.get(position) != null && items_BUS1.get(position) != null && items_BUS2.get(position) != null
                                && items_BUS3.get(position) != null && items_BUS4.get(position) != null)
                        {
                            String _getCourseName = items_BUS.get(position);
                            _userCourseName.setVisibility(View.VISIBLE);
                            _userCourseName.setText(_getCourseName);

                            String _getSub1 = items_BUS1.get(position);
                            _subject1.setVisibility(View.VISIBLE);
                            _subject1.setText(_getSub1);

                            String _getSub2 = items_BUS2.get(position);
                            _subject2.setVisibility(View.VISIBLE);
                            _subject2.setText(_getSub2);

                            String _getSub3 = items_BUS3.get(position);
                            _subject3.setVisibility(View.VISIBLE);
                            _subject3.setText(_getSub3);

                            String _getSub4 = items_BUS4.get(position);
                            _subject4.setVisibility(View.VISIBLE);
                            _subject4.setText(_getSub4);

                            if(_userCategory.getSelectedItem().toString().equals("Lecturer"))
                            {
                                _userfeeStatus.setVisibility(View.GONE);
                                _userfeeAmount.setVisibility(View.GONE);
                            }
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
                Toast.makeText(UserRegister.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ITProgramme()
    {
        ArrayList<String> items_IT = new ArrayList<String>();
        ArrayList<String> items_IT1 = new ArrayList<String>();
        ArrayList<String> items_IT2 = new ArrayList<String>();
        ArrayList<String> items_IT3 = new ArrayList<String>();
        ArrayList<String> items_IT4 = new ArrayList<String>();
        ArrayList<String> _list1 = new ArrayList<String>();

        _courseReference = _rootReference.child("IT");
        _courseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    UserData _data = snapshot1.getValue(UserData.class);
                    _list1.add(_data.getCourseID());

                    CourseData _data1 = snapshot1.getValue(CourseData.class);
                    items_IT.add(_data1.getCourseName());

                    items_IT1.add(_data1.getSubject2());
                    items_IT2.add(_data1.getSubject3());
                    items_IT3.add(_data1.getSubject4());
                    items_IT4.add(_data1.getSubject5());

                    if(_userCategory.getSelectedItem().toString().equals("Student"))
                    {
                        _subject1.setVisibility(View.VISIBLE);
                        _subject2.setVisibility(View.VISIBLE);
                        _subject3.setVisibility(View.VISIBLE);
                        _subject4.setVisibility(View.VISIBLE);

                        String _getFeeAmount = _data1.getFeeAmount();
                        _userfeeStatus.setVisibility(View.VISIBLE);
                        _userfeeStatus.setText("Unpaid");

                        _userfeeAmount.setVisibility(View.VISIBLE);
                        _userfeeAmount.setText(_getFeeAmount);
                    }
                }
                ArrayAdapter<String> _programme_adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, _list1);
                _programme_adapter.notifyDataSetChanged();
                _userCourseID.setAdapter(_programme_adapter);

                _userCourseID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (items_IT.get(position) != null && items_IT1.get(position) != null && items_IT2.get(position) != null
                                && items_IT3.get(position) != null && items_IT4.get(position) != null)
                        {
                            String _getCourseName = items_IT.get(position);
                            _userCourseName.setVisibility(View.VISIBLE);
                            _userCourseName.setText(_getCourseName);

                            String _getSub1 = items_IT1.get(position);
                            _subject1.setVisibility(View.VISIBLE);
                            _subject1.setText(_getSub1);

                            String _getSub2 = items_IT2.get(position);
                            _subject2.setVisibility(View.VISIBLE);
                            _subject2.setText(_getSub2);

                            String _getSub3 = items_IT3.get(position);
                            _subject3.setVisibility(View.VISIBLE);
                            _subject3.setText(_getSub3);

                            String _getSub4 = items_IT4.get(position);
                            _subject4.setVisibility(View.VISIBLE);
                            _subject4.setText(_getSub4);

                            if(_userCategory.getSelectedItem().toString().equals("Lecturer"))
                            {
                                _userfeeStatus.setVisibility(View.GONE);
                                _userfeeAmount.setVisibility(View.GONE);
                            }
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
                Toast.makeText(UserRegister.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void FinanceProgramme()
    {
        ArrayList<String> items_FIN = new ArrayList<String>();
        ArrayList<String> items_FIN1 = new ArrayList<String>();
        ArrayList<String> items_FIN2 = new ArrayList<String>();
        ArrayList<String> items_FIN3 = new ArrayList<String>();
        ArrayList<String> items_FIN4 = new ArrayList<String>();
        ArrayList<String> _list2 = new ArrayList<String>();

        _courseReference = _rootReference.child("Finance");
        _courseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    UserData _data = snapshot1.getValue(UserData.class);
                    _list2.add(_data.getCourseID());

                    CourseData _data1 = snapshot1.getValue(CourseData.class);
                    items_FIN.add(_data1.getCourseName());

                    items_FIN1.add(_data1.getSubject2());
                    items_FIN2.add(_data1.getSubject3());
                    items_FIN3.add(_data1.getSubject4());
                    items_FIN4.add(_data1.getSubject5());

                    if(_userCategory.getSelectedItem().toString().equals("Student"))
                    {
                        _subject1.setVisibility(View.VISIBLE);
                        _subject2.setVisibility(View.VISIBLE);
                        _subject3.setVisibility(View.VISIBLE);
                        _subject4.setVisibility(View.VISIBLE);

                        String _getFeeAmount = _data1.getFeeAmount();
                        _userfeeStatus.setVisibility(View.VISIBLE);
                        _userfeeStatus.setText("Unpaid");

                        _userfeeAmount.setVisibility(View.VISIBLE);
                        _userfeeAmount.setText(_getFeeAmount);
                    }
                }
                ArrayAdapter<String> _programme_adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, _list2);
                _programme_adapter.notifyDataSetChanged();
                _userCourseID.setAdapter(_programme_adapter);

                _userCourseID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        if (items_FIN.get(position) != null && items_FIN1.get(position) != null && items_FIN2.get(position) != null
                                && items_FIN3.get(position) != null && items_FIN4.get(position) != null)
                        {
                            String _getCourseName = items_FIN.get(position);
                            _userCourseName.setVisibility(View.VISIBLE);
                            _userCourseName.setText(_getCourseName);

                            String _getSub1 = items_FIN1.get(position);
                            _subject1.setVisibility(View.VISIBLE);
                            _subject1.setText(_getSub1);

                            String _getSub2 = items_FIN2.get(position);
                            _subject2.setVisibility(View.VISIBLE);
                            _subject2.setText(_getSub2);

                            String _getSub3 = items_FIN3.get(position);
                            _subject3.setVisibility(View.VISIBLE);
                            _subject3.setText(_getSub3);

                            String _getSub4 = items_FIN4.get(position);
                            _subject4.setVisibility(View.VISIBLE);
                            _subject4.setText(_getSub4);

                            if(_userCategory.getSelectedItem().toString().equals("Lecturer"))
                            {
                                _userfeeStatus.setVisibility(View.GONE);
                                _userfeeAmount.setVisibility(View.GONE);
                            }
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
                Toast.makeText(UserRegister.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkValidation()
    {
        _name = _userName.getText().toString();
        _password = _userPassword.getText().toString();
        _email = _userEmail.getText().toString();
        _userType = _userCategory.getSelectedItem().toString();
        _number = _userNumber.getText().toString();
        _address = _userAddress.getText().toString();
        _programmeCategory = _userProgramme.getSelectedItem().toString();
        _courseID = _userCourseID.getSelectedItem().toString();
        _courseName = _userCourseName.getText().toString();
        _sub1 = _subject1.getText().toString();
        _sub2 = _subject2.getText().toString();
        _sub3 = _subject3.getText().toString();
        _sub4 = _subject4.getText().toString();
        _feeStatus = _userfeeStatus.getText().toString();
        _feeAmount = _userfeeAmount.getText().toString();

        if(_name.isEmpty())
        {
            _userName.setError("Please enter your name.");
            _userName.requestFocus();
            return;
        }
        else if(_password.isEmpty())
        {
            _userPassword.setError("Please enter your password.");
            _userPassword.requestFocus();
            return;
        }
        else if(_password.length() < 6)
        {
            _userPassword.setError("Minumum of 6 characters is needed for the password.");
            _userPassword.requestFocus();
            return;
        }
        else if(_email.isEmpty())
        {
            _userEmail.setError("Please enter your email.");
            _userEmail.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(_email).matches())
        {
            _userEmail.setError("Your email is invalid.");
            _userEmail.requestFocus();
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
        else if(_userType.equals("User Category"))
        {
            Toast.makeText(this, "Please select a category!", Toast.LENGTH_SHORT).show();
        }
        else if(_programmeCategory.equals("Programme Name"))
        {
            Toast.makeText(this, "Please select a programme!", Toast.LENGTH_SHORT).show();
        }
        else if(_courseID.equals("Course ID"))
        {
            Toast.makeText(this, "Please select a course!", Toast.LENGTH_SHORT).show();
        }
        else if(_bitmap == null)
        {
            Toast.makeText(this, "Please select an image for avatar.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            _dialog.setMessage("Registering user...");
            _dialog.show();

            RegisterUserWithImage();
        }
    }

    private void RegisterUser()
    {
        //firebase authentication creation
        _authentication.createUserWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            _categoryReference = _dbReference.child(_userType);

                            if(_userType.equals("Student"))
                            {
                                String _userID = _authentication.getCurrentUser().getUid();
                                UserData user = new UserData(_name, _email, _userType, _number, _address, _programmeCategory,
                                        _courseID, _courseName, _sub1, _sub2, _sub3, _sub4, _feeStatus, _feeAmount, _downloadUrl, _userID);

                                //firebase realtime database Insertion
                                _categoryReference.child(_authentication.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            _dialog.dismiss();
                                            Toast.makeText(UserRegister.this,
                                                    "Student account created. Please verify your email before trying to log in.", Toast.LENGTH_SHORT).show();
                                            Intent _intent = new Intent(UserRegister.this, UserLogin.class);
                                            _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(_intent);

                                            _user = FirebaseAuth.getInstance().getCurrentUser();
                                            _user.sendEmailVerification();
                                        }
                                        else
                                        {
                                            _dialog.dismiss();
                                            Toast.makeText(UserRegister.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else if(_userType.equals("Lecturer"))
                            {
                                String _userID = _authentication.getCurrentUser().getUid();
                                UserData user = new UserData(_name, _email, _userType, _number, _address, _programmeCategory,
                                        _courseID, _courseName, _sub1, _sub2, _sub3, _sub4, _downloadUrl, _userID);

                                //firebase realtime database Insertion
                                _categoryReference.child(_authentication.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            _dialog.dismiss();
                                            Toast.makeText(UserRegister.this,
                                                    "Lecturer account created. Please verify your email before trying to log in.", Toast.LENGTH_SHORT).show();
                                            Intent _intent = new Intent(UserRegister.this, UserLogin.class);
                                            _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(_intent);

                                            _user = FirebaseAuth.getInstance().getCurrentUser();
                                            _user.sendEmailVerification();
                                        }
                                        else
                                        {
                                            _dialog.dismiss();
                                            Toast.makeText(UserRegister.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                        else
                        {
                            _dialog.dismiss();
                            Toast.makeText(UserRegister.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void RegisterUserWithImage()
    {
        ByteArrayOutputStream _baos = new ByteArrayOutputStream();
        _bitmap.compress(Bitmap.CompressFormat.JPEG, 50, _baos);
        byte[] _finalImage = _baos.toByteArray();
        final StorageReference _filePath;
        _filePath = _stoReference.child("User").child(_finalImage+"jpg");

        final UploadTask _uploadTask = _filePath.putBytes(_finalImage);

        _uploadTask.addOnCompleteListener(UserRegister.this, new OnCompleteListener<UploadTask.TaskSnapshot>()
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
                                    RegisterUser();
                                }
                            });
                        }
                    });
                }
                else
                {
                    _dialog.dismiss();
                    Toast.makeText(UserRegister.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
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
}