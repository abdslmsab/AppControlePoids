package com.example.appcontrolepoids.insertion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InsertionTicketTest {

    public static void main(String[] args) {
        String url = "jdbc:sqlserver://VM-SQL;databaseName=VITAL";
        String user = "PRAMI";
        String password = "PR170772";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connexion réussie !");
        } catch (SQLException e) {
            System.err.println("Echec de la connexion : " + e.getMessage());
        }
    }
}
