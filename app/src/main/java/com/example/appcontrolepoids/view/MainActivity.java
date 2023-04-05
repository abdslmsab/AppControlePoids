package com.example.appcontrolepoids.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appcontrolepoids.dao.ArticleDao;
import com.example.appcontrolepoids.database.AppDatabase;
import com.example.appcontrolepoids.databinding.ActivityMainBinding;
import com.example.appcontrolepoids.model.Article;
import com.example.appcontrolepoids.R;
import com.example.appcontrolepoids.viewmodel.ArticleViewModel;

import java.util.List;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    private EditText texteEAN;
    private Button boutonValider;
    private Button boutonAjouter;

    //Variable de liaison (binding) permettant d'accéder aux éléments de l'interface utilisateur
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppDatabase.initialiser(this);
        //Juste un code pas joli pour ajouter un article exemple
        new Thread() {
            @Override
            public void run() {
                Article article = new Article();
                article.setCode("robert");
                AppDatabase.getInstance().articleDao().insert(article);
            }
        }.start();

        //Initialisation de l'objet binding avec le layout activity_main.xml
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //Initialisation de l'objet ArticleViewModel avec le contexte de l'activité
        ArticleViewModel articleViewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        //Définition de l'objet articleViewModel comme variable dans le layout pour le Data Binding
        binding.setArticleViewModel(articleViewModel);
        //Définition du cycle de vie pour le Data Binding
        binding.setLifecycleOwner(this);

        binding.boutonAjouter.setOnClickListener(v -> {
            articleViewModel.articleExiste().observe(this, articleExiste -> {
                if (articleExiste) {
                    Toast.makeText(this, "L'article existe", Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
