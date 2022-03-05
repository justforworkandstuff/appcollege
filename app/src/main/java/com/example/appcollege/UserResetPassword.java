package com.example.appcollege;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class UserResetPassword extends AppCompatActivity
{
    private EditText _userEmail;
    private Button _resetButton;
    private ProgressBar _pBar;

    private FirebaseAuth _authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reset_password);

        _userEmail = findViewById(R.id.user_reset_email);
        _resetButton = findViewById(R.id.user_reset_button);
        _pBar = findViewById(R.id.user_reset_progressBar);

        _authentication = FirebaseAuth.getInstance();

        _resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                resetPassword();
            }
        });
    }

    private void resetPassword()
    {
        String _email = _userEmail.getText().toString().trim();

        if(_email.isEmpty())
        {
            _userEmail.setError("Please enter your email.");
            _userEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(_email).matches())
        {
            _userEmail.setError("Please enter a valid email!");
            _userEmail.requestFocus();
            return;
        }

        _pBar.setVisibility(View.VISIBLE);

        _authentication.sendPasswordResetEmail(_email).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    _pBar.setVisibility(View.GONE);
                    Intent _intent = new Intent(UserResetPassword.this, UserLogin.class);
                    _intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(_intent);
                    Toast.makeText(UserResetPassword.this, "Password has been reseted. Please check your email account.", Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    _pBar.setVisibility(View.GONE);
                    Toast.makeText(UserResetPassword.this, "Error, please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}