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
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);

        mContext = getContext();



        signUp.setOnClickListener(v -> signUp());
        alreadyMember.setOnClickListener(v -> alreadyMember());


        return view;
    }

    public void signUp() {
        if (mListener != null){
            mListener.signUp();
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
        void signUp();
    }
}
