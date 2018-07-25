package com.example.android.busbookings.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.busbookings.Objects.User;
import com.example.android.busbookings.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.common.oob.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.realm.Realm;
import io.realm.RealmResults;

public class SignIn extends AppCompatActivity {

    //For Google Sign in------------------
    public static boolean Gsign = false;
    private static int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    //-------------------------------------

    EditText emailEt,password;
    Button done;

    Button openSignUp;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailEt = findViewById(R.id.EmailField);
        password = findViewById(R.id.PasswordField);
        done = findViewById(R.id.done);
        openSignUp = findViewById(R.id.sign_up);
        mAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        SignInButton signInButton = findViewById(R.id.Gsign_in_button);
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gsign = true;
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email= emailEt.getText().toString();
                String pass = password.getText().toString();

                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(SignIn.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful())
                                {
                                    Intent launchSearch = new Intent(SignIn.this,MainActivity.class);
                                    launchSearch.putExtra("Email",email);
                                    startActivity(launchSearch);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Email not registered! Please sign up",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        openSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchSignUp = new Intent(SignIn.this, com.example.android.busbookings.Activitys.SignUp.class);
                startActivity(launchSignUp);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google sign in failed", e);
                // ...
            }

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("firebaseAuthWithGoogle:",acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent launchSearch = new Intent(SignIn.this,MainActivity.class);
                            launchSearch.putExtra("Email",user.getEmail());
                            startActivity(launchSearch);
                            finish();

                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignIn.this,"Google sign in failed",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
