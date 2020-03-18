package inc.bado.app.welcome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import inc.bado.app.R;
import inc.bado.app.home.HomeActivity;
import inc.bado.app.models.User;

public class WelcomeActivity extends AppCompatActivity implements
        LoginFragment.OnWelcomeFragmentListener,
        SignupFragment.OnSignUpFragmentListener,
        ForgotPasswordFragment.OnForgotPasswordListener,
        VerificationFragment.OnVerificationListener{

    private static final String TAG = "WelcomeActivity";

    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private enum Fragments {

        Login("Login"),
        Signup("Signup"),
        ForgotPassword("ForgotPassword"),
        Verification("Verification");


        private final String name;
        Fragments(String s) {
            name = s;
        }
        public String toString() {
            return this.name;
        }


    }

    private Fragment currentActiveFragment;
    private LoginFragment loginFragment = new LoginFragment();
    private SignupFragment signupFragment = SignupFragment.newInstance();

    private ForgotPasswordFragment forgotPasswordFragment = ForgotPasswordFragment.newInstance();
    private VerificationFragment verificationFragment = VerificationFragment.newInstance();

    // The fragment manager
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        fm = getSupportFragmentManager();
        mContext = getApplicationContext();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("shame");



        fm.beginTransaction()
                .add(R.id.container, loginFragment,Fragments.Login.name)
                .commit();

        fm.beginTransaction()
                .add(R.id.container, signupFragment,Fragments.Signup.name)
                .hide(signupFragment)
                .commit();

        fm.beginTransaction()
                .add(R.id.container, forgotPasswordFragment,Fragments.ForgotPassword.name)
                .hide(forgotPasswordFragment)
                .commit();

        fm.beginTransaction()
                .add(R.id.container, verificationFragment,Fragments.Verification.name)
                .hide(verificationFragment)
                .commit();

        currentActiveFragment =loginFragment;

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                    if (fm.getBackStackEntryCount() == 1) {
                        showLoginFragments();
                    }
                } else {
                    onBackpressedOptions("options");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            if (fm.getBackStackEntryCount() == 1) {
                showLoginFragments();
            }

        } else {

            onBackpressedOptions("device");
        }
    }


    public void showLoginFragments() {
        fm.beginTransaction()
                .show(loginFragment)
                .commit();
    }





    @Override
    public void forgotPassword() {
//        Toast.makeText(mContext,"Under Construction",Toast.LENGTH_LONG).show();

        fm.beginTransaction()
//                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(forgotPasswordFragment)
                .hide(loginFragment)
                .commit();

        currentActiveFragment = forgotPasswordFragment;

    }

    @Override
    public void newUser() {
        fm.beginTransaction()
//                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .hide(loginFragment)
                .show(signupFragment)
                .commit();

        currentActiveFragment = signupFragment;
    }

    @Override
    public void login(User user) {
//        Toast.makeText(mContext,"Under Construction",Toast.LENGTH_LONG).show();

        //start the home page
//        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
//        finishAffinity();
//        startActivity(intent);

        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "failure: "+task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });

    }

    @Override
    public void alreadyMember() {
        fm.beginTransaction()
//                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .hide(signupFragment)
                .show(loginFragment)
                .commit();

        currentActiveFragment = loginFragment;
    }

    @Override
    public void signUp(User mUser) {
        mAuth.createUserWithEmailAndPassword(mUser.getEmail(), mUser.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            addUser(mUser);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "failure: "+task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onSubmit() {

        fm.beginTransaction()
                .hide(forgotPasswordFragment)
                .show(verificationFragment)
                .commit();

        currentActiveFragment = verificationFragment;

    }

    @Override
    public void onVerified(String vCode) {
        Toast.makeText(mContext,"Under Construction",Toast.LENGTH_LONG).show();

        //start the welcome page
        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
        finishAffinity();
        startActivity(intent);
    }


    public void updateUI(FirebaseUser user){

        if(user != null) {
            //start the home page
            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
            finishAffinity();
            startActivity(intent);
        }else {

        }
    }

    private void addUser(User mUser){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String uID = user.getUid();
            String name = user.getDisplayName();
            String email = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();

            myRef.child("users").child(uID).child("Name").setValue(mUser.getName());
            myRef.child("users").child(uID).child("Email").setValue(email);
//            myRef.child("users").child(uID).child("Profile Picture").setValue(photoUrl);
        }
    }


    public void onBackpressedOptions(String option){

        if (currentActiveFragment == forgotPasswordFragment){

            fm.beginTransaction()
                    .hide(forgotPasswordFragment)
                    .show(loginFragment)
                    .commit();

            currentActiveFragment = loginFragment;
        }
        else if (currentActiveFragment == verificationFragment){

            fm.beginTransaction()
                    .hide(verificationFragment)
                    .show(forgotPasswordFragment)
                    .commit();

            currentActiveFragment = forgotPasswordFragment;
        }
        else if (currentActiveFragment == loginFragment || currentActiveFragment == signupFragment){

            finish();

        }
    }
}
