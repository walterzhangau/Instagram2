package com.example.walterzhang.instagram2.Login;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walterzhang.instagram2.Home.HomeActivity;
import com.example.walterzhang.instagram2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.function.ToDoubleBiFunction;

public class LoginActivity extends AppCompatActivity {


    private AutoCompleteTextView txtEmail;
    private TextView txtPassword;
    private Button btnlogin;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmail=(AutoCompleteTextView)findViewById(R.id.textUsername);
        txtPassword=(TextView)findViewById(R.id.textPassword);
        btnlogin=(Button)findViewById(R.id.buttonLogin);
        mAuth=FirebaseAuth.getInstance();
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(txtEmail.getText().toString().trim(),txtPassword.getText().toString().trim());
            }
        });

            }

    @Override
    protected void onStart() {
        FirebaseUser currentUser=mAuth.getCurrentUser(); // It will check if the user is already logged in.
        super.onStart();
    }

    public void login(String email,String password)
    {
        if(!validateForm())
        {
            return;
        }

        // TODO: 10/09/18 Add Progress Dialog

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    Log.d("login","Successful Login");
                    FirebaseUser user=mAuth.getCurrentUser();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class).putExtra("Username",user.getEmail()));
                }
                else
                {
                    Log.d("login","Login Failed");
                    Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean validateForm() { // TODO: 10/09/18 Add more validations.
        boolean valid = true;

        String email = txtEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            txtEmail.setError("Required.");
            valid = false;
        } else {
            txtEmail.setError(null);
        }

        String password = txtPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            txtPassword.setError("Required.");
            valid = false;
        } else {
            txtPassword.setError(null);
        }

        return valid;
    }

    }


