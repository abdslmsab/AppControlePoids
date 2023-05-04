package com.example.appcontrolepoids.sage;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InsertionTicketSAGETest {

    public static void main(String[] args) {
        Connection connection = null;

        try {
            String serverName = "192.168.100.11";
            String dbName = "VITAL";
            //String url = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;";
            String url = "jdbc:sqlserver://VM-SQL;databaseName=VITAL;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
            String user = "PRAMI";
            String password = "PR170772";
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                DatabaseMetaData dm = (DatabaseMetaData) connection.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
                System.out.println("Product name: " + dm.getDatabaseProductName());
                System.out.println("Product version: " + dm.getDatabaseProductVersion());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
