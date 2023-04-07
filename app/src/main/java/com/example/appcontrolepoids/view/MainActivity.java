package com.example.appcontrolepoids.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.appcontrolepoids.alertdialog.GestionnaireAlerte;
import com.example.appcontrolepoids.alertdialog.TypeAlerte;
import com.example.appcontrolepoids.alertdialog.DialogAlerte;
import com.example.appcontrolepoids.alertdialog.DialogAlerteViewModel;
import com.example.appcontrolepoids.database.AppDatabase;
import com.example.appcontrolepoids.databinding.ActivityMainBinding;
import com.example.appcontrolepoids.model.Article;
import com.example.appcontrolepoids.R;
import com.example.appcontrolepoids.viewmodel.ArticleViewModel;

public class MainActivity extends AppCompatActivity implements DialogAlerte.AlertDialogInterface {


    private DialogAlerteViewModel dialogAlerteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppDatabase.initialiser(this);
        //Article servant de test
        new Thread() {
            @Override
            public void run() {
                AppDatabase.getInstance().articleDao().nukeTable();

                Article articleTest = new Article("CAKE087", "CAKE 2 CERISES 340 G", "3333040008062", 340, 353, 144, "0001");
                AppDatabase.getInstance().articleDao().insert(articleTest);
            }
        }.start();

        //Initialisation de l'objet binding avec le layout activity_main.xml
        //Variable de liaison (binding) permettant d'accéder aux éléments de l'interface utilisateur
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //Initialisation de l'objet ArticleViewModel avec le contexte de l'activité
        ArticleViewModel articleViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        //Définition de l'objet articleViewModel comme variable dans le layout pour le Data Binding
        binding.setArticleViewModel(articleViewModel);
        //Définition du cycle de vie pour le Data Binding
        binding.setLifecycleOwner(this);

        articleViewModel.articleExiste.observe(this, articleExiste -> Toast.makeText(this, articleExiste ? "L'article existe" : "L'article n'existe pas. Veuillez l'ajouter", Toast.LENGTH_LONG).show());

        //Initialisation de l'objet DialogAlerteViewModel avec le contexte de l'activité
        dialogAlerteViewModel = new ViewModelProvider(this).get(DialogAlerteViewModel.class);

        //Permet d'afficher la pop-up d'alerte en fonction du type d'alerte
        binding.boutonAjouter.setOnClickListener(v -> GestionnaireAlerte.showMyDialog(this,
                TypeAlerte.verrouillageCode, dialogAlerteViewModel, this));

        dialogAlerteViewModel.codeValide.observe(this, codeValide -> {
            if (codeValide) {
                Intent intent = new Intent(MainActivity.this, AjouterArticle.class);
                startActivity(intent);
            } else {
                //TODO : Afficher une popup erreur
                Toast.makeText(this, "Code faux !", Toast.LENGTH_LONG).show();
            }
            dialogAlerteViewModel.reinitialiserCodeSaisi();
        });
    }

    //Permet d'afficher une notification lorsque l'on clique sur le bouton principal de la pop-up d'alerte
    @Override
    public void alertDialogMainOption(TypeAlerte type) {
        //TODO exempleAvertissement : Si validation -> afficher un Toast "L'article a bien été supprimé"
        dialogAlerteViewModel.verifierCodeValide();
    }

    //Permet d'afficher une notification lorsque l'on clique sur le bouton alternatif de la pop-up d'alerte
    @Override
    public void alertDialogAlternativeOption(TypeAlerte type) {
    }
}
