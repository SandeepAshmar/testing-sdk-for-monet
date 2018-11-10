package com.monet.librarydemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static void btnFirstClick(View view){
        Toast.makeText(view.getContext(), "btn 1", Toast.LENGTH_SHORT).show();
    }

    public static void btnSecoundClick(View view){
        Toast.makeText(view.getContext(), "btn 2", Toast.LENGTH_SHORT).show();
    }

    public static void btnThirdClick(View view){
        Toast.makeText(view.getContext(), "btn 3", Toast.LENGTH_SHORT).show();
    }

    public static void btnForthClick(View view){
        Toast.makeText(view.getContext(), "btn 4", Toast.LENGTH_SHORT).show();
    }
}
