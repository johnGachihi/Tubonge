package com.originals.johnevans.tubonge.loginsignup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.originals.johnevans.tubonge.R;

/**
 * Created by John on 4/3/2017.
 */

public class SignUpNamesFragment extends Fragment {
    OnNextClicked onNextClicked;
    EditText firstname;
    EditText username;

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
        View view = inflater.inflate(R.layout.signup_names_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final SignUpEmailFragment signUpEmailFragment = new SignUpEmailFragment();
        super.onViewCreated(view, savedInstanceState);
        firstname = (EditText) view.findViewById(R.id.firstname);
        username = (EditText) view.findViewById(R.id.username);
        Button button = (Button) view.findViewById(R.id.names_nextBt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextClicked.onNextClicked(firstname.getText().toString(), username.getText().toString());
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.signup_activity, signUpEmailFragment)
                                   .addToBackStack(null)
                                   .commit();
            }
        });
    }

    public interface OnNextClicked {
        void onNextClicked(String firstname, String username);
    }
}
