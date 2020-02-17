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
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.bado.app.R;
import inc.bado.app.models.User;

public class LoginFragment extends Fragment {

    @BindView(R.id.email_text_input) TextInputLayout emailTextInput;
    @BindView(R.id.password_text_input) TextInputLayout passwordTextInput;
    @BindView(R.id.email_edit_text) EditText email;
    @BindView(R.id.password_edit_text) EditText password;
    @BindView(R.id.login_button) MaterialButton loginButton;
    @BindView(R.id.forgot_password_tv) TextView forgotPassword;
    @BindView(R.id.new_sign_up) TextView newMember;

    private Context mContext;
    private OnWelcomeFragmentListener mListener;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        mContext = getContext();


        loginButton.setOnClickListener(v -> login());
        forgotPassword.setOnClickListener(v -> forgotPassword());
        newMember.setOnClickListener(v -> newMember());


        return view;
    }

    private void login() {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        if (userEmail != null) {
            if (userPassword != null) {
                User user = new User(userEmail,userPassword);
                if (mListener != null){
                    mListener.login(user);
                }

            }else{
                passwordTextInput.setError("Please enter a password");
            }
        } else {
            emailTextInput.setError("Please enter your email");
        }
    }
    private void forgotPassword() {
        if (mListener != null){
            mListener.forgotPassword();
        }
    }
    private void newMember() {
        if (mListener != null){
            mListener.newUser();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWelcomeFragmentListener) {
            mListener = (OnWelcomeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnWelcomeFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnWelcomeFragmentListener {
        void forgotPassword();
        void newUser();
        void login(User user);
    }
}
