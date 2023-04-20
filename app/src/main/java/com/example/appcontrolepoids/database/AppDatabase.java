package com.example.appcontrolepoids.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.appcontrolepoids.dao.ArticleDao;
import com.example.appcontrolepoids.model.Article;

//Classe destinée à contenir la base de données
@Database(entities = {Article.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract ArticleDao articleDao();

    public static void initialiser(Context context) {
        if (instance == null) {
            //Si elle n'est pas nulle, on crée une nouvelle instance de la base de données
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    //TODO : Juste pour tester. A enlever.
                    .allowMainThreadQueries()
                    .build();
        }
    }

    //Vérification si l'instance actuelle de la base de données est nulle
    public static synchronized AppDatabase getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Impossible d'accéder à la base de données sans l'avoir initialisée");
        }
        //Sinon on retourne l'instance actuelle
        return instance;
    }
}
