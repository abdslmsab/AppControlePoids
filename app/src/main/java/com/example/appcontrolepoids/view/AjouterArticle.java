package com.example.appcontrolepoids.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.appcontrolepoids.R;

import java.util.Objects;

public class AjouterArticle extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_article);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }
}
