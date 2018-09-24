package com.example.walterzhang.instagram2.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walterzhang.instagram2.Home.HomeActivity;
import com.example.walterzhang.instagram2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnlogin;
    private FirebaseAuth mAuth;
    private TextView txtError;

    private TextView txtRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail=(EditText)findViewById(R.id.textUsername);
        txtError=(TextView)findViewById(R.id.txtError);
        txtRegister=(TextView)findViewById(R.id.link_signUp);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        txtPassword=(EditText) findViewById(R.id.textPassword);
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

        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        // It will check if the user is already logged in.

    }






    public void login(String email, String password)
    {
        Log.d("onClickl","Attempting to Log In");
        if(!validateForm())
        {
            return;
        }



        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    Log.d("login","Successful Login");


                }
                else
                {
                    Log.d("login","Login Failed");
                    txtError.setText("Authentication Failed");
                    Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                }

            }
        });



        if(mAuth.getCurrentUser() !=null)
        {
            Intent intent=new Intent(this, HomeActivity.class);
            txtError.setText("");
            startActivity(intent);
            finish();
        }
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


