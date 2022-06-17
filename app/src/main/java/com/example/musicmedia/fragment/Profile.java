package com.example.musicmedia.fragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicmedia.R;
//import com.example.musicmedia.activity.ChangPassword;

import javax.annotation.Nullable;

public class Profile extends Fragment {
    TextView textViewAccount,textViewPassword,textViewDate,textViewAccount2,textViewPassword2,textViewDate2;

    Button buttonChangepw;
    ImageView btnBack;
    View view;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.activity_profile, container,false);
        Initt();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getFragmentManager()!=null){
                    getFragmentManager().popBackStack();

                }
            }
        });
        buttonChangepw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ChangePassword.class);
                startActivity(intent);
            }
        });

        return view;

    }

    private void Initt() {
        textViewAccount =view.findViewById(R.id.textViewAccount);
        textViewPassword =view.findViewById(R.id.textViewPassword);
        textViewDate =view.findViewById(R.id.textViewDate);
        textViewAccount2 =view.findViewById(R.id.textViewAccount2);
        textViewPassword2 =view.findViewById(R.id.textViewPassword2);
        textViewDate2 =view.findViewById(R.id.textViewDate2);
        buttonChangepw= view.findViewById(R.id.buttonChangepw);
        btnBack=view.findViewById(R.id.btnBack);
    }




}