package com.antonio.mycarcentinel;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Inicio extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();


    }


    private void init() {

        Button btnInicio = findViewById(R.id.btnInicio);
        btnInicio.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        Intent intent=new Intent(Inicio.this,Login.class);
        startActivity(intent);
    }
}
