package inc.bado.app.welcome;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import butterknife.BindView;
import butterknife.ButterKnife;
import inc.bado.app.R;

public class VerificationFragment extends Fragment {


    @BindView(R.id.pin_iput) OtpView vCode;
    @BindView(R.id.submit_button) MaterialButton submit;

    private OnVerificationListener mListener;

    private VerificationFragment() {
        // Required empty public constructor
    }

    public static VerificationFragment newInstance() {
        VerificationFragment fragment = new VerificationFragment();
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
        View view = inflater.inflate(R.layout.fragment_verification, container, false);
        ButterKnife.bind(this, view);

//        vCode.setListener(new OnOtpCompletionListener() {
//            @Override public void onOtpCompleted(String otp) {
//
//                // do Stuff
//            }
//        });

        submit.setOnClickListener(v -> verify("CODE"));

        return view;
    }

    public void verify(String vCode) {
        if (mListener != null) {
            mListener.onVerified(vCode);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVerificationListener) {
            mListener = (OnVerificationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVerificationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnVerificationListener {
        void onVerified(String vCode);
    }
}
