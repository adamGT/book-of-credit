package inc.bado.app.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import inc.bado.app.R;
import inc.bado.app.home.HomeActivity;

public class WelcomeActivity extends AppCompatActivity implements
        LoginFragment.OnWelcomeFragmentListener,
        SignupFragment.OnSignUpFragmentListener,
        ForgotPasswordFragment.OnForgotPasswordListener,
        VerificationFragment.OnVerificationListener{

    private Context mContext;

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
    public void login() {
        Toast.makeText(mContext,"Under Construction",Toast.LENGTH_LONG).show();

        //start the welcome page
        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
        finishAffinity();
        startActivity(intent);

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
    public void signUp() {
        Toast.makeText(mContext,"Under Construction",Toast.LENGTH_LONG).show();

        //start the welcome page
        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
        finishAffinity();
        startActivity(intent);
    }

    @Override
    public void onSubmit() {

//        Toast.makeText(mContext,"Under Construction",Toast.LENGTH_LONG).show();

        fm.beginTransaction()
//                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
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
