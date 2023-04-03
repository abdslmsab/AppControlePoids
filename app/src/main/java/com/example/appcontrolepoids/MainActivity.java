package com.example.appcontrolepoids;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText texteEAN;
    private SQLiteDatabase database;
    private Button boutonValider;
    private Button boutonAjouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texteEAN = findViewById(R.id.texte_EAN);

        // Initialisation de la base de données
        DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
        database = dbHelper.getWritableDatabase();

        boutonValider = findViewById(R.id.bouton_valider);
        boutonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ean = texteEAN.getText().toString();
                // TODO : Vérifier si l'EAN est valide ici

                //Requête SELECT avec une clause WHERE pour filtrer les enregistrements en fonction de l'EAN_Article entré
                String requeteSelect = "SELECT * FROM " + DatabaseHelper.NOM_TABLE + " WHERE " + DatabaseHelper.COLONNE_EAN + " = '" + ean + "'";

                //Exécution de la requête
                Cursor cursor = database.rawQuery(requeteSelect, null);

                //Vérification si un enregistrement a été trouvé
                if (cursor.getCount() > 0) {
                    // TODO : L'EAN existe dans la base de données, on passe à la page suivante
                    Toast.makeText(MainActivity.this, "L'EAN existe", Toast.LENGTH_SHORT).show();
                } else {
                    //Si l'EAN n'existe pas, on active le bouton "Ajouter"
                    Toast.makeText(MainActivity.this, "L'EAN n'existe pas. Veuillez l'ajouter.", Toast.LENGTH_SHORT).show();
                    boutonAjouter = findViewById(R.id.bouton_ajouter);
                    boutonAjouter.setEnabled(true);
                }

                // Fermeture du curseur
                cursor.close();
            }
        });

        boutonAjouter = findViewById(R.id.bouton_ajouter);
        boutonAjouter.setEnabled(false);

        //Permet de griser le bouton "Ajouter" lorsque le champ de l'EAN de l'article est vide
        texteEAN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    boutonAjouter.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Permet d'ajouter l'article dans la base de donnée
        boutonAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ean = texteEAN.getText().toString();
                // TODO : Ajouter l'article à la base de données ici
                // TODO : Afficher un message de confirmation d'ajout
                Toast.makeText(MainActivity.this, "Article ajouté à la base de données", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
