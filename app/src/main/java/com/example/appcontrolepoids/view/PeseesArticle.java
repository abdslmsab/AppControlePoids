package com.example.appcontrolepoids.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.appcontrolepoids.R;
import com.example.appcontrolepoids.alertdialog.DialogAlerte;
import com.example.appcontrolepoids.alertdialog.DialogAlerteViewModel;
import com.example.appcontrolepoids.alertdialog.GestionnaireAlerte;
import com.example.appcontrolepoids.alertdialog.TypeAlerte;
import com.example.appcontrolepoids.databinding.ActivityPeseesArticleBinding;
import com.example.appcontrolepoids.model.Article;
import com.example.appcontrolepoids.viewmodel.PeseesArticleViewModel;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import java.util.List;
import java.util.Objects;

public class PeseesArticle extends AppCompatActivity implements DialogAlerte.AlertDialogInterface {

    private PeseesArticleViewModel peseesArticleViewModel;
    private DialogAlerteViewModel dialogAlerteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesees_article);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        //Variable de liaison (binding) permettant d'accéder aux éléments de l'interface utilisateur
        ActivityPeseesArticleBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_pesees_article);
        //Initialisation de l'objet PeseesArticleViewModel avec le contexte de l'activité
        peseesArticleViewModel = new ViewModelProvider(this).get(PeseesArticleViewModel.class);
        //Définition de l'objet PeseesArticleViewModel comme variable dans le layout pour le Data Binding
        binding.setPeseesArticleViewModel(peseesArticleViewModel);
        binding.setLifecycleOwner(this);

        //Initialisation de l'objet DialogAlerteViewModel avec le contexte de l'activité
        dialogAlerteViewModel = new ViewModelProvider(this).get(DialogAlerteViewModel.class);

        String numeroLot = getIntent().getStringExtra("numero_lot");
        String codeOperateur = getIntent().getStringExtra("code_operateur");
        String ddm = getIntent().getStringExtra("ddm");

        Article article = (Article) getIntent().getSerializableExtra("article");
        binding.nomArticle.setText(article.getNom());
        binding.valeurPoidsCible.setText(" : " + article.getPoidsBrut() + " g");

        //Lorsque l'on clique sur le bouton "Annuler" on ferme cette activité afin de revenir sur l'activité principale
        binding.boutonAnnuler.setOnClickListener(view -> this.finish());

        //On récupère les données entrées dans la précédente activité
        int nombreVenues = getIntent().getIntExtra("nombre_venues", -1);

        peseesArticleViewModel.calculerCompteur(article.getRendement(), nombreVenues);

        // Vérification du champ de saisie
        peseesArticleViewModel.estPeseeValide.observe(this, estPeseeValide -> {
            if (!estPeseeValide) {
                binding.textePoidsBrut.setError("La pesée ne peut être nulle");
            } else {
                binding.textePoidsBrut.setErrorEnabled(false);
            }
        });

        // Vérification des données aberrantes
        peseesArticleViewModel.estPeseeAberrante.observe(this, estPeseeAberrante -> {
            if (estPeseeAberrante) {
                GestionnaireAlerte.showMyDialog(this, TypeAlerte.peseeAberrante, dialogAlerteViewModel, this);
            }
        });

        //Format de l'entrée désirée (voir https://github.com/RedMadRobot/input-mask-android/wiki/1.-Mask-Syntax)
        final MaskedTextChangedListener listener = new MaskedTextChangedListener("[990]{.}[9]", binding.poidsBrutEditText);
        binding.poidsBrutEditText.addTextChangedListener(listener);

        //Permet d'utiliser la valeur du poids brut entre la vue et la vue-modèle
        peseesArticleViewModel.init(article.getPoidsBrut());

        //Permet de pouvoir cliquer sur le bouton "Valider" si on clique sur la touche "Ok" du clavier
        binding.poidsBrutEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    peseesArticleViewModel.actionValiderSaisie();
                }
                return false;
            }
        });

        binding.boutonValider.setOnClickListener(view -> {
            peseesArticleViewModel.actionValiderSaisie();
        });

        peseesArticleViewModel.nombrePeseesRestantes.observe(this, nombrePeseesRestantes -> {
            if (nombrePeseesRestantes != null && nombrePeseesRestantes <= 0) {
                binding.poidsBrutEditText.clearFocus();

                Intent intent = new Intent(PeseesArticle.this, ResultatArticle.class);
                intent.putExtra("article", article);
                intent.putExtra("numeroLot", numeroLot);
                intent.putExtra("codeOperateur", codeOperateur);
                intent.putExtra("ddm", ddm);
                intent.putExtra("nombreVenues", nombreVenues);

                List<Float> listePesees = peseesArticleViewModel.listePesees.getValue();
                float[] listePeseesTableau = new float[listePesees.size()];
                for (int i = 0; i < listePesees.size(); i++) {
                    listePeseesTableau[i] = listePesees.get(i);
                }
                intent.putExtra("listePesees", listePeseesTableau);
                intent.putExtra("coefficient", peseesArticleViewModel.coefficient);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        peseesArticleViewModel.reinitialiserListePesees();
    }

    @Override
    public void alertDialogMainOption(TypeAlerte type) {
        peseesArticleViewModel.actionAjouterPeseeAberrante();
    }

    @Override
    public void alertDialogAlternativeOption(TypeAlerte type) {

    }
}
