package com.example.musicmedia.fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.musicmedia.R;

public class ChangePassword extends AppCompatActivity {

    TextView textViewoldPass,textViewnewPass,textViewnewPass2;
    EditText editTextoldPass,editTextnewPass,editTextnewPass2;
    Button buttonsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Inittt();
//        //Tao nut back
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    private void Inittt() {
        textViewoldPass = (TextView) findViewById(R.id.textViewoldPass);
        textViewnewPass = (TextView) findViewById(R.id.textViewnewPass);
        textViewnewPass2 = (TextView) findViewById(R.id.textViewnewPass2);
        editTextnewPass = (EditText) findViewById(R.id.editTextnewPass);
        editTextnewPass2 = (EditText) findViewById(R.id.editTextnewPass2);
        editTextoldPass = (EditText) findViewById(R.id.editTextoldPass);
        buttonsave =(Button) findViewById(R.id.buttonsave);
    }
}