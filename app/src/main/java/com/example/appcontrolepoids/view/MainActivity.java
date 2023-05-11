package com.example.appcontrolepoids.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.appcontrolepoids.R;
import com.example.appcontrolepoids.alertdialog.DialogAlerte;
import com.example.appcontrolepoids.alertdialog.DialogAlerteViewModel;
import com.example.appcontrolepoids.alertdialog.GestionnaireAlerte;
import com.example.appcontrolepoids.alertdialog.TypeAlerte;
import com.example.appcontrolepoids.database.AppDatabase;
import com.example.appcontrolepoids.databinding.ActivityMainBinding;
import com.example.appcontrolepoids.model.Article;
import com.example.appcontrolepoids.viewmodel.ArticleViewModel;


import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogAlerte.AlertDialogInterface {

    private static boolean TEST_FIRST_RUN = true;

    private DialogAlerteViewModel dialogAlerteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        AppDatabase.initialiser(this);
        //Article servant de test
        if(TEST_FIRST_RUN) {
            TEST_FIRST_RUN = false;
            new Thread() {
                @Override
                public void run() {
                    AppDatabase.getInstance().articleDao().nukeTable();

                    Article articleTest = new Article("CAKE087", "CAKE 2 CERISES 340 G", "3333040008062", 340, 353, 144, "0001");
                    AppDatabase.getInstance().articleDao().insert(articleTest);
                    Article articleTestDeux = new Article("CAKE087", "CAKE 2 CERISES 340 G", "1111111111111", 3, 6, 144, "0001");
                    AppDatabase.getInstance().articleDao().insert(articleTestDeux);
                }
            }.start();
        }

        //Initialisation de l'objet binding avec le layout activity_main.xml
        //Variable de liaison (binding) permettant d'accéder aux éléments de l'interface utilisateur
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //Initialisation de l'objet ArticleViewModel avec le contexte de l'activité
        ArticleViewModel articleViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        //Définition de l'objet articleViewModel comme variable dans le layout pour le Data Binding
        binding.setArticleViewModel(articleViewModel);
        //Définition du cycle de vie pour le Data Binding
        binding.setLifecycleOwner(this);

        //Initialisation de l'objet DialogAlerteViewModel avec le contexte de l'activité
        dialogAlerteViewModel = new ViewModelProvider(this).get(DialogAlerteViewModel.class);

        articleViewModel.articleExiste.observe(this, articleExiste -> {
            if (articleViewModel.action == ArticleViewModel.ArticleAction.ACTION_VALIDER) {
                if (articleExiste) {
                    Intent intent = new Intent(MainActivity.this, InformationsArticle.class);
                    intent.putExtra("article", articleViewModel.article.getValue());
                    articleViewModel.reinitialiserChamp();
                    startActivity(intent);
                } else {
                    GestionnaireAlerte.showMyDialog(this, TypeAlerte.articleExistePas, dialogAlerteViewModel, this);
                }
            } else if (articleViewModel.action == ArticleViewModel.ArticleAction.ACTION_MODIFIER_AJOUTER) {
                if (Boolean.TRUE.equals(articleViewModel.estEanSaisiValide.getValue())) {
                    GestionnaireAlerte.showMyDialog(this, TypeAlerte.verrouillageCode, dialogAlerteViewModel, this);
                }
            }
        });

        // Vérification du champ "EAN"
        articleViewModel.estEanSaisiValide.observe(this, estEanSaisiValide -> {
            if (!estEanSaisiValide) {
                binding.texteEAN.setError("Le code barre saisi n'est pas conforme");
            } else {
                binding.texteEAN.setErrorEnabled(false);
            }
        });

        dialogAlerteViewModel.codeValide.observe(this, codeValide -> {
            if (codeValide) {
                Intent intent = new Intent(MainActivity.this, AjouterArticle.class);
                intent.putExtra("article", articleViewModel.article.getValue());
                intent.putExtra("ean", articleViewModel.eanSaisi.getValue());
                articleViewModel.reinitialiserChamp();
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
    /*
    private void supprimerFichiersEnAttente() {
        ArrayList<String> fichiersASupprimer = getIntent().getStringArrayListExtra("fichiersASupprimer");

        for (String filePath : fichiersASupprimer) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
        fichiersASupprimer.clear();
    }

    private boolean haveNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String numeroLot = getIntent().getStringExtra("numeroLot");
        Article article = (Article) getIntent().getSerializableExtra("article");

        if (haveNetworkConnection()){
            InsertionTicketVITAL.insererArticle(article, numeroLot);
            InsertionTicketSAGE.insererArticle(article, numeroLot);
            supprimerFichiersEnAttente();
        }
    }*/
}