package com.example.appcontrolepoids.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appcontrolepoids.R;

import java.util.Objects;

public class TicketArticle extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_article);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }
}
