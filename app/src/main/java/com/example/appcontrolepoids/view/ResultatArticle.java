package com.example.appcontrolepoids.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.appcontrolepoids.R;
import com.example.appcontrolepoids.alertdialog.DialogAlerte;
import com.example.appcontrolepoids.alertdialog.DialogAlerteViewModel;
import com.example.appcontrolepoids.alertdialog.GestionnaireAlerte;
import com.example.appcontrolepoids.alertdialog.TypeAlerte;
import com.example.appcontrolepoids.databinding.ActivityResultatsArticleBinding;
import com.example.appcontrolepoids.model.Article;
import com.example.appcontrolepoids.viewmodel.ResultatArticleViewModel;

import java.util.Arrays;
import java.util.Objects;

public class ResultatArticle extends AppCompatActivity implements DialogAlerte.AlertDialogInterface {
    private DialogAlerteViewModel dialogAlerteViewModel;

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

        //Initialisation de l'objet DialogAlerteViewModel avec le contexte de l'activité
        dialogAlerteViewModel = new ViewModelProvider(this).get(DialogAlerteViewModel.class);

        binding.boutonContinuer.setOnClickListener(view -> {
            if (Boolean.TRUE.equals(resultatArticleViewModel.lotValide.getValue())){
                Intent intent = new Intent(ResultatArticle.this, TicketArticle.class);
                startActivity(intent);
            } else {
                GestionnaireAlerte.showMyDialog(this, TypeAlerte.verrouillageCode, dialogAlerteViewModel, this);
            }
        });

        dialogAlerteViewModel.codeValide.observe(this, codeValide -> {
            if (codeValide) {
                Intent intent = new Intent(ResultatArticle.this, PeseesArticle.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Code faux !", Toast.LENGTH_LONG).show();
            }
            dialogAlerteViewModel.reinitialiserCodeSaisi();
        });
    }

    //Permet d'afficher une notification lorsque l'on clique sur le bouton principal de la pop-up d'alerte
    @Override
    public void alertDialogMainOption(TypeAlerte type) {
        if (type == TypeAlerte.verrouillageCode) {
            dialogAlerteViewModel.verifierCodeValide();
        }
    }

    //Permet d'afficher une notification lorsque l'on clique sur le bouton alternatif de la pop-up d'alerte
    @Override
    public void alertDialogAlternativeOption(TypeAlerte type) {

    }
}