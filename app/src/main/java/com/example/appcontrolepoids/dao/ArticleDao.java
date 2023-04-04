package com.example.appcontrolepoids.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.appcontrolepoids.model.Article;

import java.util.List;

//Le DAO (Objet d'accès aux données) fournit les méthodes utilisées par le reste de l'application pour interagir avec les données de la table CP_Articles
@Dao
public interface ArticleDao {
    //Permet d'insérer un article
    @Insert
    void insert(Article article);

    //Permet de mettre à jour un article
    @Update
    void update(Article article);

    //Permet de supprimer un article
    @Delete
    void delete(Article article);

    //Retourne tous les articles
    @Query("SELECT * FROM CP_Articles")
    LiveData<List<Article>> getAllArticles();

    //Requête SELECT avec une clause WHERE pour filtrer les enregistrements en fonction de l'EAN_Article entré
    @Query("SELECT * FROM CP_Articles WHERE Code_Article = :ean")
    LiveData<Article> getArticleByEAN(String ean);
}