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
import android.widget.Toast;

import com.example.walterzhang.instagram2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtName;
    private EditText txtemail;
    private EditText txtPassword;
    private EditText txtUsername;
    private String name,username,password,email;
    private Button btnsignup;
    private FirebaseAuth mAuth;
    private final String TAG="RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtName=(EditText) findViewById(R.id.textName);
        txtemail=(EditText) findViewById(R.id.textEmail);
        txtUsername=(EditText)findViewById(R.id.textUsername);
        txtPassword=(EditText)findViewById(R.id.textPassword);
        btnsignup=(Button)findViewById(R.id.buttonLogin);
        mAuth=FirebaseAuth.getInstance();
        init();

    }

    private void init()
    {
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=txtemail.getText().toString();
                password=txtPassword.getText().toString();
                name=txtName.getText().toString();
                username=txtUsername.getText().toString();
                if(!validateForm())
                {
                    return;
                }
                else
                {

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            if (mAuth.getCurrentUser() != null) {
                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }


                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                                            {
                                                Toast.makeText(RegisterActivity.this, "User Already Exist", Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                                        }

                                        // ...
                                    }
                                });



                }
            }
        });
    }


    private boolean validateForm() { // TODO: 10/09/18 Add more validations.
        boolean valid = true;
        String name=txtName.getText().toString();
        if(TextUtils.isEmpty(name))
        {
            txtName.setError("Required");
            valid=false;

        }
        else
            txtName.setError(null);
        String email = txtemail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            txtemail.setError("Required.");
            valid = false;
        } else {
            txtemail.setError(null);
        }
        String username = txtUsername.getText().toString();
        if (TextUtils.isEmpty(email)) {
            txtUsername.setError("Required.");
            valid = false;
        } else {
            txtUsername.setError(null);
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
