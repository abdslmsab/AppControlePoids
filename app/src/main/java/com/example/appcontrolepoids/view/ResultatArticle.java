package com.example.appcontrolepoids.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.appcontrolepoids.R;
import com.example.appcontrolepoids.databinding.ActivityPeseesArticleBinding;
import com.example.appcontrolepoids.databinding.ActivityResultatsArticleBinding;
import com.example.appcontrolepoids.viewmodel.ArticleViewModel;

import java.util.Objects;

public class ResultatArticle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultats_article);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        //Variable de liaison (binding) permettant d'accéder aux éléments de l'interface utilisateur
        ActivityResultatsArticleBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_resultats_article);

        //Initialisation de l'objet ArticleViewModel avec le contexte de l'activité
        ArticleViewModel articleViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);

        String ean = getIntent().getStringExtra("ean");
        articleViewModel.getArticleByEAN(ean).observe(this, article -> {
            binding.nomArticle.setText(article.getNom());
            binding.valeurPoidsCible.setText(article.getPoidsNet() + " g");
        });
    }
}
