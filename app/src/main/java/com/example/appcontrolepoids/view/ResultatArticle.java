package com.example.appcontrolepoids.view;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.appcontrolepoids.R;
import com.example.appcontrolepoids.databinding.ActivityResultatsArticleBinding;
import com.example.appcontrolepoids.model.Article;
import com.example.appcontrolepoids.viewmodel.ResultatArticleViewModel;

import java.util.Arrays;
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
        ResultatArticleViewModel resultatArticleViewModel = new ViewModelProvider(this).get(ResultatArticleViewModel.class);

        binding.setResultatArticleViewModel(resultatArticleViewModel);
        binding.setLifecycleOwner(this);

        Article article = (Article) getIntent().getSerializableExtra("article");
        binding.nomArticle.setText(article.getNom());
        binding.valeurPoidsCible.setText(article.getPoidsBrut() + " g");

        float[] listePesees = getIntent().getFloatArrayExtra("listePesees");
        float coefficient = getIntent().getFloatExtra("coefficient", -1);

        resultatArticleViewModel.init(listePesees, article.getPoidsBrut(), coefficient);
    }
}
