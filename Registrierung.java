package com.example.smartpostbox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Registrierung extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrierung);

        //Zurück Button
        Button btnZuruckReg = (Button) findViewById(R.id.btnZuruckReg);
        btnZuruckReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });

        // Weiter Button
        Button btnWeiterReg = (Button) findViewById(R.id.btnWeiterReg);
        btnWeiterReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSmartPostbox();
            }
        });
    }

    public void openMain(){
        Intent MainRegMai = new Intent(this, MainActivity.class);
        startActivity(MainRegMai);
    }

    public void openSmartPostbox(){
        System.out.println("Geht");
        Intent SmartPostboxRegSma = new Intent(this,SmartpostboxMain.class);
        startActivity(SmartPostboxRegSma);
    }

}