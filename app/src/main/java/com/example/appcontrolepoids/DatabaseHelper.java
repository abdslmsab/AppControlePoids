package com.example.appcontrolepoids;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String NOM_DATABASE = "SQLite_ControlePoids";
    private static final int DATABASE_VERSION = 1;
    public static final String NOM_TABLE = "CP_Articles";
    public static final String COLONNE_ID = "Id_Article";
    public static final String COLONNE_CODE = "Code_Article";
    public static final String COLONNE_NOM = "Nom_Article";
    public static final String COLONNE_EAN = "EAN_Article";
    public static final String COLONNE_POIDS_NET = "Poids_Net_Article";
    public static final String COLONNE_POIDS_BRUT = "Poids_Brut_Article";
    public static final String COLONNE_RENDEMENT = "Rendement_Article";
    public static final String COLONNE_CODE_OPERATEUR = "Code_Operateur";

    //Script SQL pour générer la table CP_Articles
    private static final String SQL_CREATION_TABLE =
            "CREATE TABLE " + NOM_TABLE + " (" +
                    //Clé primaire : Id_Article
                    COLONNE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                    //Code de l'article ne pouvant pas être nul et contenant 13 caractères maximum
                    COLONNE_CODE + " TEXT(13) NOT NULL," +

                    //Nom de l'article ne pouvant pas être nul et contenant 69 caractères maximmum
                    COLONNE_NOM + " TEXT(69) NOT NULL," +

                    //EAN de l'article ne pouvant pas être nul et contenant 13 caractères maximum
                    COLONNE_EAN + " TEXT(13) NOT NULL CHECK(length(" + COLONNE_EAN + ") <= 13 AND " + COLONNE_EAN + " != '0')," +

                    //Poids net de l'article ne pouvant pas être nul et contenant 3 chiffres maximum
                    COLONNE_POIDS_NET + " INTEGER NOT NULL CHECK(" + COLONNE_POIDS_NET + " > 0 AND " + COLONNE_POIDS_NET + " < 1000)," +

                    //Poids brut de l'article ne pouvant pas être nul et contenant 3 chiffres maximum
                    COLONNE_POIDS_BRUT + " INTEGER NOT NULL CHECK(" + COLONNE_POIDS_BRUT + " > 0 AND " + COLONNE_POIDS_BRUT + " < 1000)," +

                    //Rendement de l'article ne pouvant pas être nul et contenant 3 chiffres maximum
                    COLONNE_RENDEMENT + " INTEGER NOT NULL CHECK(" + COLONNE_RENDEMENT + " > 0 AND " + COLONNE_RENDEMENT + " < 1000)," +
                    
                    //Matricule ne pouvant pas être nul et contenant 4 chiffres
                    COLONNE_CODE_OPERATEUR + " TEXT(4) NOT NULL CHECK(length(" + COLONNE_CODE_OPERATEUR + ") = 4 AND " + COLONNE_CODE_OPERATEUR + " != '0'))";

    public DatabaseHelper(Context context) {
        super(context, NOM_DATABASE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATION_TABLE);

        // Insertion des données de test dans la table CP_Articles
        ContentValues values = new ContentValues();
        values.put(COLONNE_CODE, "CAKE087");
        values.put(COLONNE_NOM, "CAKE 2 CERISES 340 G");
        values.put(COLONNE_EAN, "3333040008062");
        values.put(COLONNE_POIDS_NET, 340);
        values.put(COLONNE_POIDS_BRUT, 353);
        values.put(COLONNE_RENDEMENT, 144);
        values.put(COLONNE_CODE_OPERATEUR, "0001");
        sqLiteDatabase.insert(NOM_TABLE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
