package com.cyning.apt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.cyning.aptcore.LCJViewBinder;
import com.example.BindView;
import com.example.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvTitle)
     TextView tvTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LCJViewBinder.bind(this);
    }
}
