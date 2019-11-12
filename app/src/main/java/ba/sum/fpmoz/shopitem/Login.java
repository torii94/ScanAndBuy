package ba.sum.fpmoz.shopitem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.sql.Array;
import java.util.Arrays;

import ba.sum.fpmoz.shopitem.model.User;

public class Login extends AppCompatActivity {
   SignInButton googlesignInButton;
    private FirebaseAuth auth;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleSignInClient;
    private static final String TAG="login";
    private ProgressDialog loadingBar;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private DatabaseReference mDatabase;
    Toolbar toolbar;
    private Button Anonymus;



    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        toolbar= findViewById(R.id.toolbarlogin);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        mDatabase= FirebaseDatabase.getInstance().getReference().child("todos/users");
        Anonymus=findViewById(R.id.AnonymusLoginBtn);






        loadingBar= new ProgressDialog(this);
        callbackManager = CallbackManager.Factory.create();


        SignInButton googlesignInButton = (SignInButton) findViewById(R.id.sign_in_button);

        //googlesignInButton=(ImageView)findViewById(R.id.google_sign_in);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("email"));


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());

                //Log.d("fbLog", "proso");

            }

            @Override
            public void onCancel() {
                //Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                //Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });

        auth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    SendUserToMain();

                }
            }
        };




        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))

                .requestEmail()
                .build();


        mGoogleSignInClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(Login.this, "Connection failed", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();


        auth= FirebaseAuth.getInstance();
        googlesignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });



        Anonymus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnonimusLogin();
            }
        });


    }


    private void AnonimusLogin() {
        auth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            User user = new User();
                            user.name = "Anonymus";
                            user.lastName = "Anonymus";
                            user.image = String.valueOf(auth.getCurrentUser().getPhotoUrl());
                            user.bonus = 0;
                            mDatabase = FirebaseDatabase.getInstance().getReference().child("todos/users/" + auth.getCurrentUser().getUid());

                            mDatabase.setValue(user);

                            SendUserToMain();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());

                        }

                        // ...
                    }
                });

    }


        private void handleFacebookAccessToken(AccessToken accessToken) {
        loginButton.setVisibility(View.GONE);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    boolean newuser = task.getResult().getAdditionalUserInfo().isNewUser();

                    if(newuser){
                        User user = new User();

                        String[] splited = auth.getCurrentUser().getDisplayName().split("\\s+");

                        user.name=splited[0];
                        user.lastName= splited[1];
                        user.image= String.valueOf(auth.getCurrentUser().getPhotoUrl());
                        user.bonus=0;
                        mDatabase= FirebaseDatabase.getInstance().getReference().child("todos/users/"+auth.getCurrentUser().getUid());

                        mDatabase.setValue(user);

                        Log.d("baza", mDatabase.getRef().toString());

                        SendUserToMain();

                    }else{
                        SendUserToMain();

                    }


                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    SendUserToLogin();

                }


            }
        });
    }



    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("aaa", String.valueOf(result.isSuccess()));
            if(result.isSuccess()){
                GoogleSignInAccount account=result.getSignInAccount();
                firebaseAuthWithGoogle(account);

                Toast.makeText(this, "Please wait, while are getting your auth result", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            boolean newuser = task.getResult().getAdditionalUserInfo().isNewUser();

                            if(newuser){
                                User user = new User();

                                String[] splited = auth.getCurrentUser().getDisplayName().split("\\s+");

                                user.name=splited[0];
                                user.lastName= splited[1];
                                user.image= String.valueOf(auth.getCurrentUser().getPhotoUrl());
                                user.bonus=0;
                                mDatabase= FirebaseDatabase.getInstance().getReference().child("todos/users/"+auth.getCurrentUser().getUid());

                                mDatabase.setValue(user);

                                Log.d("baza", mDatabase.getRef().toString());

                                SendUserToMain();

                            }else{
                                SendUserToMain();

                            }


                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            SendUserToLogin();

                        }

                        // ...
                    }
                });
    }

    private void SendUserToMain(){
        Intent intent= new Intent(Login.this, Main.class);
        startActivity(intent);
        finish();
    }


    private void SendUserToLogin(){
        Intent intent= new Intent(Login.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    OnCompleteListener<AuthResult> completeListener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                Log.d("Userpostoji", "ne postoji");
            }
        }
    };


    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser= auth.getCurrentUser();

        auth.addAuthStateListener(firebaseAuthListener);


        if(currentUser != null){
            SendUserToMain();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Menu menu1= (Menu) menu.findItem(R.menu.loginmenu);
        return super.onCreateOptionsMenu(menu1);
    }
}
