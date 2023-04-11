package com.example.appcontrolepoids.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.appcontrolepoids.R;
import com.example.appcontrolepoids.databinding.ActivityInformationsArticleBinding;
import com.example.appcontrolepoids.databinding.ActivityMainBinding;
import com.example.appcontrolepoids.viewmodel.ArticleViewModel;
import com.example.appcontrolepoids.viewmodel.DateViewModel;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class InformationsArticle extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations_article);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        //Variable de liaison (binding) permettant d'accéder aux éléments de l'interface utilisateur
        ActivityInformationsArticleBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_informations_article);
        //Initialisation de l'objet ArticleViewModel avec le contexte de l'activité
        DateViewModel dateViewModel = new ViewModelProvider(this).get(DateViewModel.class);
        //Définition de l'objet articleViewModel comme variable dans le layout pour le Data Binding
        binding.setDateViewModel(dateViewModel);

        //Afficher ld date du jour
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateViewModel.setDateActuelle(dateFormat.format(new Date()));

        //Initialisation de l'objet ArticleViewModel avec le contexte de l'activité
        ArticleViewModel articleViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);

        String ean = getIntent().getStringExtra("ean");
        articleViewModel.getArticleByEAN(ean).observe(this, article -> {
            // On utilise l'article récupéré avec son EAN (dans MainActivity) pour afficher les informations sur l'interface
            binding.codeArticle.setText(article.getCode());
            binding.valeurNomArticle.setText(article.getNom());
            binding.valeurPoidsNet.setText(article.getPoidsNet() + " g");
            binding.valeurPoidsBrut.setText(article.getPoidsBrut() + " g");
            binding.valeurRendement.setText(article.getRendement() + "");
        });

        //Format de l'entrée désirée (voir https://github.com/RedMadRobot/input-mask-android/wiki/1.-Mask-Syntax)
        final MaskedTextChangedListener listener = new MaskedTextChangedListener("[00]/[00]/[00]", binding.ddmEditText);
        binding.ddmEditText.addTextChangedListener(listener);

        binding.boutonAnnuler.setOnClickListener(view -> this.finish());
    }
}
