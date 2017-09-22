package com.example.android.hungry_user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

    String email;
    String password;

    EditText emailInput;
    EditText passwordInput;
    Button authSubmit;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        emailInput = (EditText) findViewById(R.id.userEmailInputActivityMain);
        passwordInput = (EditText) findViewById(R.id.userPasswordInputActivityMain);


        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    Toast.makeText(MainActivity.this, "USER HAS LOGGED IN", Toast.LENGTH_SHORT).show();

                }
            }
        };
        authSubmit = (Button) findViewById(R.id.authenticateButtonActivityMain);
        authSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
//                startSignIn();
            }
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void CreateAccount()
    {
        if (passwordInput.equals("") || emailInput.equals("")) {

            Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
     if(task.isSuccessful())
     {

         Toast.makeText(MainActivity.this,"Account Created Successfully",Toast.LENGTH_SHORT).show();
         userAccess();

     }
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this,"Account Creation Failed",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }

    }

//    private void sendVerificationEmail()
//    {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        user.sendEmailVerification()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            // email sent
//
//
//                            // after email is sent just logout the user and finish this activity
////                            FirebaseAuth.getInstance().signOut();
//                            startActivity(new Intent(MainActivity.this, LoginClass.class));
//                            finish();
//                        }
//                        else
//                        {
//                            // email not sent, so display message and restart the activity or do whatever you wish to do
//
//                            //restart this activity
//                            overridePendingTransition(0, 0);
//                            finish();
//                            overridePendingTransition(0, 0);
//                            startActivity(getIntent());
//
//                        }
//                    }
//                });
//    }
//    //Needed only to sign in when already the cunt is created
//    private void startSignIn() {
//
//
//        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//
//            Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
//        } else {
//
//
//            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                    if (!task.isSuccessful()) {
//                        Toast.makeText(MainActivity.this, "Sign in Problem", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            });
//        }
//    }
//

    private void userAccess() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();


            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }

    }
}