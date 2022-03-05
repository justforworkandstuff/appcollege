package com.example.appcollege;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appcollege.Admin.AdminHome;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class UserLogin extends AppCompatActivity
{
    private EditText _userEmail, _userPassword;
    private TextView _userForgetPassword, _userRegister;
    private Button _loginButton;
    private ProgressBar _pBar;
    private FirebaseAuth _authentication;
    private FirebaseAuth.AuthStateListener _firebaseAuthListener;
    private FirebaseUser _user;
    private DatabaseReference _loginDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        _userEmail = findViewById(R.id.user_login_email);
        _userPassword = findViewById(R.id.user_login_password);
        _loginButton = findViewById(R.id.user_login_button);
        _userRegister = findViewById(R.id.user_login_register);
        _userForgetPassword = findViewById(R.id.user_login_forgetPassword);
        _pBar = findViewById(R.id.user_login_bar);

        _user = FirebaseAuth.getInstance().getCurrentUser();
        _authentication = FirebaseAuth.getInstance();

        _firebaseAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                if(_user != null)
                {
                    try
                    {
                        if(_user.getEmail().equals("yee8896752@gmail.com"))
                        {
                            Intent intent = new Intent(UserLogin.this, AdminHome.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(UserLogin.this, "Welcome back admin.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else if(_user.isEmailVerified())
                        {
                            Intent intent = new Intent(UserLogin.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(UserLogin.this, "Welcome back , " + _user.getEmail(), Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                    catch(Exception e)
                    {
                        e.getMessage();
                    }
                }
            }
        };

        _loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LoginUser();
            }
        });

        _userRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(UserLogin.this, UserRegister.class));
            }
        });

        _userForgetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(UserLogin.this, UserResetPassword.class));
            }
        });
    }

    private void LoginUser()
    {
        final String email = _userEmail.getText().toString();
        final String password = _userPassword.getText().toString();

        if(email.isEmpty())
        {
            _userEmail.requestFocus();
            _userEmail.setError("Please enter your email.");
            return;
        }
        if(password.isEmpty())
        {
            _userPassword.requestFocus();
            _userPassword.setError("Please enter your password.");
            return;
        }
        else
        {
            _pBar.setVisibility(View.VISIBLE);
            _authentication.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>()
            {
                @Override
                public void onSuccess(AuthResult authResult)
                {
                    if(authResult.getUser().getEmail().equals("yee8896752@gmail.com"))
                    {
                        _pBar.setVisibility(View.GONE);
                        Intent intent = new Intent(UserLogin.this, AdminHome.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(UserLogin.this, "Welcome back admin.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else if(authResult.getUser() != null && authResult.getUser().isEmailVerified())
                    {
                        _pBar.setVisibility(View.GONE);
                        Intent intent = new Intent(UserLogin.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(UserLogin.this, "Welcome back, " + authResult.getUser().getEmail(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        _pBar.setVisibility(View.GONE);
                        authResult.getUser().sendEmailVerification();
                        Toast.makeText(UserLogin.this,
                                "Your email is not verified yet, please verify your email before proceeding.", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    _pBar.setVisibility(View.GONE);
                    Toast.makeText(UserLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        _authentication.addAuthStateListener(_firebaseAuthListener);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        _authentication.removeAuthStateListener(_firebaseAuthListener);
    }
}