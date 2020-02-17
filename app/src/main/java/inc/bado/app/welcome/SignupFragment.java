package inc.bado.app.welcome;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.bado.app.R;
import inc.bado.app.models.User;

public class SignupFragment extends Fragment {


    @BindView(R.id.name_text_input) TextInputLayout nameTextInput;
    @BindView(R.id.email_text_input) TextInputLayout emailTextInput;
    @BindView(R.id.password_text_input) TextInputLayout passwordTextInput;
    @BindView(R.id.confirm_password_text_input) TextInputLayout confirmPasswordTextInput;

    @BindView(R.id.name_edit_text) EditText name;
    @BindView(R.id.email_edit_text) EditText email;
    @BindView(R.id.password_edit_text) EditText password;
    @BindView(R.id.confirm_password_edit_text) EditText confirmPassword;
    @BindView(R.id.signup_button) MaterialButton signUp;
    @BindView(R.id.already_member) TextView alreadyMember;

    private View view;
    private Context mContext;
    private OnSignUpFragmentListener mListener;

    private SignupFragment() {
    }

    public static SignupFragment newInstance() {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);

        mContext = getContext();


        signUp.setOnClickListener(v -> signUp());
        alreadyMember.setOnClickListener(v -> alreadyMember());


        return view;
    }

    public void signUp() {
        String userName = name.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString();
        String userConfirmPassword = confirmPassword.getText().toString();

        if (userName != null) {
            if (userEmail != null) {
                if (userPassword != null) {
//                    if (userPassword.toCharArray().length < 6){
                        if (userConfirmPassword != null) {
                            if(userPassword.contentEquals(userConfirmPassword)) {
                                User user = new User(userName,userEmail,userPassword);
                                if (mListener != null) {
                                    mListener.signUp(user);
    //                                Snackbar.make(view,userEmail, BaseTransientBottomBar.LENGTH_SHORT).show();

                                }
                            }else{
                                Snackbar.make(view,"Your two passwords doesn't match,please try again", BaseTransientBottomBar.LENGTH_SHORT).show();
                                password.setText("");
                                confirmPassword.setText("");
                            }

                        } else {
                            confirmPasswordTextInput.setError("Please confirm your password");
                        }
//                    }else {
//                        passwordTextInput.setError("Password must be at least 6 characters");
//                    }
                }else{
                        passwordTextInput.setError("Please enter a password");
                    }
                } else {
                    emailTextInput.setError("Please enter your email");
                }
            } else {
                nameTextInput.setError("Please enter your name");
            }
    }
    public void alreadyMember() {
        if (mListener != null){
            mListener.alreadyMember();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSignUpFragmentListener) {
            mListener = (OnSignUpFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSignUpFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSignUpFragmentListener {
        void alreadyMember();
        void signUp(User user);
    }
}
