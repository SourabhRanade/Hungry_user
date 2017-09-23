package com.example.android.hungry_user;

import android.content.Intent;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    boolean flag = true;
    GregorianCalendar calendar;
    String email;
    String password;
    String name;
    String email_id;

    EditText emailInput;
    EditText passwordInput;
    Button authSubmit;
Button login;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        emailInput = (EditText) findViewById(R.id.userEmailInputActivityMain);
        passwordInput = (EditText) findViewById(R.id.userPasswordInputActivityMain);
        authSubmit = (Button) findViewById(R.id.authenticateButtonActivityMain);
        login=(Button)findViewById(R.id.login);



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
//                    FirebaseUser user=mAuth.getCurrentUser();
//                    Toast.makeText(MainActivity.this, "USER HAS LOGGED IN", Toast.LENGTH_SHORT).show();


                }
            }
        };

        authSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
                CreateAccount();


//
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startSignIn();

//
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent onAuthSuccess = new Intent(MainActivity.this, LoginClass.class);
            startActivity(onAuthSuccess);
//        }else finish();
        }
    }




    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void CreateAccount() {
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();
        if (passwordInput.equals("") || emailInput.equals("")) {

            Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String User_id = mAuth.getCurrentUser().getUid();
                                Toast.makeText(MainActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                emailverify();
                                mAuth.signOut();
                            }
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Account Creation Failed",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }

    }

    public void emailverify() {

        final FirebaseUser user = mAuth.getInstance().getCurrentUser();

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if (task.isSuccessful()) {
                    // email sent

                    Toast.makeText(MainActivity.this, "Email sent to" + user.getEmail(), Toast.LENGTH_SHORT).show();
                    // after email is sent just logout the user and finish this activity
//                                mAuth.signOut();
//                                startActivity(new Intent(MainActivity.this, LoginClass.class));
//                                finish();


                } else {
                    // email not sent, so display message and restart the activity or do whatever you wish to do

                    //restart this activity
                    Toast.makeText(MainActivity.this, "Could not send email please retry", Toast.LENGTH_SHORT).show();
                    overridePendingTransition(0, 0);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());


                }
            }
        });


    }

    //    //Needed only to sign in when already the cunt is created
    private void startSignIn() {

        email=emailInput.getText().toString();
        password=passwordInput.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

            Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
        } else {


            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    FirebaseUser user = mAuth.getCurrentUser();


                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Sign in Problem", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        try {

                            if (user.isEmailVerified()) {

                                Toast.makeText(MainActivity.this, "Success,Email verified", Toast.LENGTH_SHORT).show();
                                Intent intent =new Intent(MainActivity.this,LoginClass.class);
                                startActivity(intent);

                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Email is not verified", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                            }
                        } catch (NullPointerException e) {
                            Toast.makeText(MainActivity.this, "Null point exception", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }


//    public void userAccess() {
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            // Name, email address, and profile photo Url
//          name = user.getDisplayName();
//            email_id = user.getEmail();
//
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            String uid = user.getUid();
//
//
//        }
//
//    }
}