package com.originals.johnevans.tubonge.loginsignup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.originals.johnevans.tubonge.R;

/**
 * Created by John on 4/8/2017.
 */

public class SignUpEmailFragment extends Fragment {

    OnNextClicked onNextClicked;
    Button next;
    EditText email;
    EditText password;
    EditText confPassword;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNextClicked) {
            onNextClicked = (OnNextClicked) context;
        } else {
            throw new ClassCastException(context.toString() + "implement OnNextClicked");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_email_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = (EditText) view.findViewById(R.id.Email);
        password = (EditText) view.findViewById(R.id.password);
        confPassword = (EditText) view.findViewById(R.id.confPassword);

        next = (Button) view.findViewById(R.id.next2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                String confPassString = confPassword.getText().toString();
                if (passwordString.equals(confPassString) && (!emailString.isEmpty() && !passwordString.isEmpty()
                        && !confPassString.isEmpty()) && emailString.contains("@")) {
                    onNextClicked.getDataFromFragment(emailString, passwordString);
                } else if (emailString.isEmpty() || passwordString.isEmpty()|| confPassString.isEmpty()) {
                    Toast.makeText(getContext(), "Insert your information", Toast.LENGTH_LONG).show();
                } else if (passwordString.equals(confPassString)) {
                    Toast.makeText(getContext(), "Passwords do not math", Toast.LENGTH_LONG).show();
                } else if (!emailString.contains("@")) {
                    Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public interface OnNextClicked {
        void getDataFromFragment(String email, String password);
    }
}
