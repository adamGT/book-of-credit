package inc.bado.app.welcome;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.bado.app.R;

public class ForgotPasswordFragment extends Fragment {


    @BindView(R.id.email_text_input) TextInputLayout emailTextInput;
    @BindView(R.id.email_edit_text) EditText email;
    @BindView(R.id.submit_button) MaterialButton submit;

    private Context mContext;
    private OnForgotPasswordListener mListener;

    private ForgotPasswordFragment() {
        // Required empty public constructor
    }

    public static ForgotPasswordFragment newInstance() {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ButterKnife.bind(this, view);


        mContext = getContext();

        submit.setOnClickListener(v -> submit());

        return view;
    }

    public void submit() {
        if (mListener != null){
            mListener.onSubmit();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnForgotPasswordListener) {
            mListener = (OnForgotPasswordListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnForgotPasswordListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnForgotPasswordListener {
        void onSubmit();
    }
}
