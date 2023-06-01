package com.example.appcontrolepoids.remote.sage;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.appcontrolepoids.remote.PathsConstants;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InsertionTicketSAGE {

    public static void insererArticle(File file, String codeArticle, String dateControle) throws SQLException {
        String serverName = "192.168.100.11";
        String dbName = "VITAL";
        String url = "jdbc:jtds:sqlserver://" + serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;";
        String user = "apps";
        String password = "apps@VITAL31";
        String sql = "INSERT INTO F_ARTICLEMEDIA (AR_Ref, ME_Fichier, ME_Commentaire) VALUES (?, ?, ?)";

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement pstmt = connection.prepareStatement(sql);

        pstmt.setString(1, codeArticle);
        pstmt.setString(2, PathsConstants.VOLUME_PATH + file.getName());
        pstmt.setString(3, "Contrôle poids du " + dateControle);

        pstmt.executeUpdate();
    }
}
