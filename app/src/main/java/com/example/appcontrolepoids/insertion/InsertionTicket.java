package com.example.appcontrolepoids.insertion;

import android.os.Environment;

import com.example.appcontrolepoids.model.Article;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InsertionTicket {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public static void insererArticle(Article article, String numeroLot) {
        String url = "jdbc:sqlserver://VM-SQL;databaseName=VITAL";
        String user = "PRAMI";
        String password = "PR170772";
        String sql = "INSERT INTO F_ARTICLEMEDIA (AR_Ref, ME_Fichier, ME_Commentaire) VALUES (?, ?, ?)";

        String fileName = article.getCode() + "-" + numeroLot + ".pdf";
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/" + fileName;

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, article.getCode());
            pstmt.setBinaryStream(2, new FileInputStream(pdfPath));
            pstmt.setString(3, "Contrôle poids du " + dateFormat.format(new Date()));
            pstmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
