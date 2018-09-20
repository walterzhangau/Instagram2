package com.example.walterzhang.instagram2.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walterzhang.instagram2.Home.HomeActivity;
import com.example.walterzhang.instagram2.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {


    private AutoCompleteTextView txtEmail;
    private TextView txtPassword;
    private Button btnlogin;
    private FirebaseAuth mAuth;
    public GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        txtEmail=(AutoCompleteTextView)findViewById(R.id.textUsername);
        signInButton= (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

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

    private void signIn()
    {
        Intent signInIntent=mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,1234);
    }

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser !=null)
        {
            // TODO: 12/09/18 We can keep the client connected and log in directly! We can decide this later.

        }
        // It will check if the user is already logged in.

    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Log In With Google", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class).putExtra("Name",user.getDisplayName()));
                        } else {
                            // If sign in fails, display a message to the user.
                            AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Account not Logged In").setMessage("Google Log In Failed");


                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1234)
        {
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Account not Logged In").setMessage("Google Log In Failed");
            }

        }
    }

    public void login(String email, String password)
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


