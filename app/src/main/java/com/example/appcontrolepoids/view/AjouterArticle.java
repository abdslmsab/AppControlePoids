package com.example.appcontrolepoids.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.appcontrolepoids.R;
import com.example.appcontrolepoids.database.AppDatabase;
import com.example.appcontrolepoids.databinding.ActivityAjouterArticleBinding;
import com.example.appcontrolepoids.databinding.ActivityPeseesArticleBinding;
import com.example.appcontrolepoids.model.Article;
import com.example.appcontrolepoids.viewmodel.AjouterArticleViewModel;
import com.example.appcontrolepoids.viewmodel.DateViewModel;
import com.example.appcontrolepoids.viewmodel.PeseesArticleViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AjouterArticle extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_article);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        //Variable de liaison (binding) permettant d'accéder aux éléments de l'interface utilisateur
        ActivityAjouterArticleBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_ajouter_article);
        //Initialisation de l'objet AjouterArticleViewModel avec le contexte de l'activité
        AjouterArticleViewModel ajouterArticleViewModel = new ViewModelProvider(this).get(AjouterArticleViewModel.class);
        //Définition de l'objet AjouterArticleViewModel comme variable dans le layout pour le Data Binding
        binding.setAjouterArticleViewModel(ajouterArticleViewModel);
        binding.setLifecycleOwner(this);

        //Initialisation de l'objet DateViewModel avec le contexte de l'activité
        DateViewModel dateViewModel = new ViewModelProvider(this).get(DateViewModel.class);
        //Définition de l'objet dateViewModel comme variable dans le layout pour le Data Binding
        binding.setDateViewModel(dateViewModel);

        //Afficher la date du jour
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateViewModel.setDateActuelle(dateFormat.format(new Date()));

        String ean = getIntent().getStringExtra("ean");
        Article article = (Article) getIntent().getSerializableExtra("article");

        if (article == null){
            binding.titreAjouter.setText("Ajouter un article");
            binding.eanArticleEditText.setText(ean);
        } else {
            binding.titreAjouter.setText("Modifier un article");
            binding.eanArticleEditText.setText(article.getEan());
        }

        //Lorsque l'on clique sur le bouton "Annuler" on ferme cette activité afin de revenir sur l'activité principale
        binding.boutonAnnuler.setOnClickListener(view -> {
            this.finish();
        });

        // Vérification du champ "Code opérateur"
        ajouterArticleViewModel.estCodeOperateurValide.observe(this, estCodeOperateurValide -> {
            if (!estCodeOperateurValide) {
                binding.texteCodeOperateur.setError("Le code opérateur requiert 4 caractères");
            } else {
                binding.texteCodeOperateur.setErrorEnabled(false);
            }
        });

        // Vérification du champ "EAN"
        ajouterArticleViewModel.estEanValide.observe(this, estEanValide -> {
            if (!estEanValide) {
                binding.texteEan.setError("L'EAN requiert 13 caractères");
            } else {
                binding.texteEan.setErrorEnabled(false);
            }
        });

        // Vérification du champ "Nom article"
        ajouterArticleViewModel.estNomValide.observe(this, estNomValide -> {
            if (!estNomValide) {
                binding.texteNomArticle.setError("Le nom de l'article ne peut être nul");
            } else {
                binding.texteNomArticle.setErrorEnabled(false);
            }
        });

        // Vérification du champ "Code article"
        ajouterArticleViewModel.estCodeArticleSaisiValide.observe(this, estCodeArticleSaisiValide -> {
            if (!estCodeArticleSaisiValide) {
                binding.texteCodeArticle.setError("Le code de l'article ne peut être nul");
            } else {
                binding.texteCodeArticle.setErrorEnabled(false);
            }
        });

        // Vérification du champ "Poids net"
        ajouterArticleViewModel.estPoidsNetSaisiValide.observe(this, estPoidsNetSaisiValide -> {
            if (!estPoidsNetSaisiValide) {
                binding.textePoidsNet.setError("Le poids net de l'article ne peut être nul");
            } else {
                binding.textePoidsNet.setErrorEnabled(false);
            }
        });

        // Vérification du champ "Poids brut"
        ajouterArticleViewModel.estPoidsBrutSaisiValide.observe(this, estPoidsBrutSaisiValide -> {
            if (!estPoidsBrutSaisiValide) {
                binding.textePoidsBrut.setError("Le poids brut de l'article ne peut être nul");
            } else {
                binding.textePoidsBrut.setErrorEnabled(false);
            }
        });

        // Vérification du champ "Rendement"
        ajouterArticleViewModel.estRendementSaisiValide.observe(this, estRendementSaisiValide -> {
            if (!estRendementSaisiValide) {
                binding.texteRendement.setError("Le rendement de l'article ne peut être nul");
            } else {
                binding.texteRendement.setErrorEnabled(false);
            }
        });

        binding.boutonValider.setOnClickListener(view -> {
            ajouterArticleViewModel.verifierSaisiesValide();
            if(Boolean.TRUE.equals(ajouterArticleViewModel.estCodeOperateurValide.getValue()) && Boolean.TRUE.equals(ajouterArticleViewModel.estEanValide.getValue()) && Boolean.TRUE.equals(ajouterArticleViewModel.estNomValide.getValue()) && Boolean.TRUE.equals(ajouterArticleViewModel.estPoidsNetSaisiValide.getValue()) && Boolean.TRUE.equals(ajouterArticleViewModel.estPoidsBrutSaisiValide.getValue()) && Boolean.TRUE.equals(ajouterArticleViewModel.estRendementSaisiValide.getValue())) {
                Article nouvelArticle = new Article(ajouterArticleViewModel.codeArticleSaisi.getValue(), ajouterArticleViewModel.nomSaisi.getValue(), ajouterArticleViewModel.eanSaisi.getValue(), Integer.parseInt(Objects.requireNonNull(ajouterArticleViewModel.poidsNetSaisi.getValue())), Integer.parseInt(Objects.requireNonNull(ajouterArticleViewModel.poidsBrutSaisi.getValue())), Integer.parseInt(Objects.requireNonNull(ajouterArticleViewModel.rendementSaisi.getValue())), ajouterArticleViewModel.codeOperateurSaisi.getValue());
                ajouterArticleViewModel.insererArticle(nouvelArticle);
                this.finish();
            }
        });
    }
}
