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

import com.example.walterzhang.instagram2.R;
import com.example.walterzhang.instagram2.Models.User;
import com.example.walterzhang.instagram2.Models.UserAccountSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtName;
    private EditText txtemail;
    private EditText txtPassword;
    private EditText txtUsername;
    private TextView txtError;
    private String user_ID;
    private String name,username,password,email;
    private Button btnsignup;
    private FirebaseAuth mAuth;
    private final String TAG="RegisterActivity";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtName=(EditText) findViewById(R.id.textName);
        txtemail=(EditText) findViewById(R.id.textEmail);
        txtUsername=(EditText)findViewById(R.id.textUsername);
        txtPassword=(EditText)findViewById(R.id.textPassword);
        txtError=(TextView)findViewById(R.id.txtError);
        btnsignup=(Button)findViewById(R.id.buttonLogin);
        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference();

        init();

        if(mAuth.getCurrentUser()!=null)
            user_ID=mAuth.getCurrentUser().getUid();



    }


// Check if Username Exists Already
    public boolean checkIfUsernameExists(String username, DataSnapshot dataSnapshot)
    {
        Log.d(TAG,"checkIfUsernameExists"+ username);
        User user=new User();
        for (DataSnapshot ds:dataSnapshot.getChildren())
        {
            Log.d(TAG,"checkIfUsernameExists: datasnapshot"+ds);
                    user.setUsername(ds.getValue(User.class).getUsername());
            Log.d(TAG,"checkIfUsernameExists: datasnapshot"+user.getUsername());

            if(user.getUsername().equals(username))
            {
                Log.d(TAG, "checkifUsernameExists: Username already taken");
                txtError.setText("Username Already Taken");
                return true;
            }


        }
        return false;
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
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG,"OnDataChange"+dataSnapshot);
                            if(!checkIfUsernameExists(username,dataSnapshot))
                            {
                                Log.d(TAG,"Username is unique"+username);
                                registerNewEmail(email,password,username);

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                }
            }
        });
    }


    private void registerNewEmail(final String email, String password, final String username)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            if (mAuth.getCurrentUser() != null) {
                                user_ID=mAuth.getCurrentUser().getUid();
                                addNewUser(email,username,"","");// Adds new user's information to the database
                                sendEmailVerification();
                                mAuth.signOut();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                txtError.setText("Email is used by another account");
                            }
                            else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                txtError.setText("Invalid Email");
                            }
                            else if(task.getException() instanceof FirebaseAuthWeakPasswordException)
                                txtError.setText("Password is Weak! Should be atleast 6 characters");

                        }

                        // ...
                    }
                });
    }

    public void addNewUser(String email,String username, String description, String profile_photo)
    {

        User user=new User(user_ID,1,email,username);

        databaseReference.child("users").child(user_ID).setValue(user);

        UserAccountSettings uas=new UserAccountSettings(description,0,0,0,username,profile_photo,username);
        databaseReference.child("user_account_settings").child(user_ID).setValue(uas);
    }

    public void sendEmailVerification(){
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("sendEmailVerification","Email not Sent");
                        Toast.makeText(RegisterActivity.this,"Couldn't send Email Verification. Please register again!",Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        user.delete();
                    }
                    else
                    {
                        Log.d("sendEmailVerification","Email Sent");
                        Toast.makeText(RegisterActivity.this,"Email Verification Sent",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    private boolean validateForm() {
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
