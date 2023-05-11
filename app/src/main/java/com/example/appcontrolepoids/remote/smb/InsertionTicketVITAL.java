package com.example.appcontrolepoids.remote.smb;

import com.example.appcontrolepoids.remote.PathsConstants;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.utils.SmbFiles;

import java.io.File;

public class InsertionTicketVITAL {

    public static void insererArticle(File file) {
        SMBClient client = new SMBClient();

        try {
            Connection connection = client.connect("VM-GED");
            AuthenticationContext ac = new AuthenticationContext("PRAMI", "PR170772".toCharArray(), "VITAL");
            Session session = connection.authenticate(ac);

            // Connexion à Share
            DiskShare share = (DiskShare) session.connectShare("public$");

            String destinationPath = PathsConstants.SMB_DESTINATION_PATH + file.getName();

            SmbFiles.copy(file, share, destinationPath, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
