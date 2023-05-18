package com.example.appcontrolepoids.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.appcontrolepoids.R;
import com.example.appcontrolepoids.databinding.ActivityInformationsArticleBinding;
import com.example.appcontrolepoids.model.Article;
import com.example.appcontrolepoids.viewmodel.DateViewModel;
import com.example.appcontrolepoids.viewmodel.InformationsArticleViewModel;
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
        //Initialisation de l'objet DateViewModel avec le contexte de l'activité
        DateViewModel dateViewModel = new ViewModelProvider(this).get(DateViewModel.class);
        //Définition de l'objet dateViewModel comme variable dans le layout pour le Data Binding
        binding.setDateViewModel(dateViewModel);

        //Afficher la date du jour
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateViewModel.setDateActuelle(dateFormat.format(new Date()));

        Article article = (Article) getIntent().getSerializableExtra("article");

        binding.nomArticle.setText(article.getNom());
        binding.valeurCodeArticle.setText(article.getCode());
        binding.valeurPoidsNet.setText(article.getPoidsNet() + " g");
        binding.valeurPoidsBrut.setText(article.getPoidsBrut() + " g");
        binding.valeurRendement.setText(article.getRendement() + "");

        //Format de l'entrée désirée (voir https://github.com/RedMadRobot/input-mask-android/wiki/1.-Mask-Syntax)
        final MaskedTextChangedListener listener = new MaskedTextChangedListener("[00]/[00]/[00]", binding.ddmEditText);
        binding.ddmEditText.addTextChangedListener(listener);

        //Lorsque l'on clique sur le bouton "Annuler" on ferme cette activité afin de revenir sur l'activité principale
        binding.boutonAnnuler.setOnClickListener(view -> this.finish());

        //Initialisation de l'objet InformationsArticleViewModel avec le contexte de l'activité
        InformationsArticleViewModel informationsArticleViewModel = new ViewModelProvider(this).get(InformationsArticleViewModel.class);
        //Définition de l'objet informationsArticleViewModel comme variable dans le layout pour le Data Binding
        binding.setInformationsArticleViewModel(informationsArticleViewModel);

        // Vérification du champ "Matricule"
        informationsArticleViewModel.estCodeOperateurValide.observe(this, estCodeOperateurValide -> {
            if (!estCodeOperateurValide) {
                binding.texteOperateur.setError("Le code opérateur requiert 4 caractères");
            } else {
                binding.texteOperateur.setErrorEnabled(false);
            }
        });

        // Vérification du champ "Nombre de venues"
        informationsArticleViewModel.estNombreVenuesValide.observe(this, estNombreVenuesValide -> {
            if (!estNombreVenuesValide) {
                binding.texteVenues.setError("Le nombre de venues doit être au minimum égal à 1");
            } else {
                binding.texteVenues.setErrorEnabled(false);
            }
        });

        // Vérification du champ "Numéro de lot"
        informationsArticleViewModel.estNumeroLotValide.observe(this, estNumeroLotValide -> {
            if (!estNumeroLotValide) {
                binding.texteLot.setError("Le numéro de lot n'est pas conforme");
            } else {
                binding.texteLot.setErrorEnabled(false);
            }
        });

        // Vérification du champ "DDM"
        informationsArticleViewModel.estDdmValide.observe(this, estDdmValide -> {
            if (!estDdmValide) {
                binding.texteDdm.setError("La date de durabilité minimale n'est pas conforme");
            } else {
                binding.texteDdm.setErrorEnabled(false);
            }
        });

        binding.boutonValider.setOnClickListener(view -> {
            informationsArticleViewModel.verifierSaisiesValide();
            if(Boolean.TRUE.equals(informationsArticleViewModel.estDdmValide.getValue()) && Boolean.TRUE.equals(informationsArticleViewModel.estNumeroLotValide.getValue()) && Boolean.TRUE.equals(informationsArticleViewModel.estNombreVenuesValide.getValue()) && Boolean.TRUE.equals(informationsArticleViewModel.estCodeOperateurValide.getValue())) {
                Intent intent = new Intent(InformationsArticle.this, PeseesArticle.class);
                intent.putExtra("nombre_venues", informationsArticleViewModel.saisieNombreVenuesEntier.getValue());
                intent.putExtra("article", article);
                intent.putExtra("numero_lot", informationsArticleViewModel.numeroLotSaisi.getValue());
                intent.putExtra("code_operateur", informationsArticleViewModel.codeOperateurSaisi.getValue());
                intent.putExtra("ddm", informationsArticleViewModel.ddmSaisie.getValue());
                startActivity(intent);
            }
        });
    }
}
