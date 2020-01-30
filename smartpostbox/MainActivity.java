package com.example.smartpostbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView SmartPostbox;
    private Button btnnnn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmartPostbox = (ImageView) findViewById(R.id.SmartPostbox);
        SmartPostbox.setVisibility(View.VISIBLE);

        btnnnn = (Button) findViewById(R.id.btnnnn);

        btnnnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAnm1();
            }
        });

        //Registrierung Button
        Button btnRegistrierung = (Button) findViewById(R.id.btnRegistrierung);
        btnRegistrierung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistrierung();
            }
        });

    }

    public void openRegistrierung(){
        Intent RegisierungMainReg = new Intent(this, Registrierung.class);
        startActivity(RegisierungMainReg);
    }

    public void openAnm1(){
        Intent an = new Intent(this, Anmeldung1.class);
        startActivity(an);
    }
}
