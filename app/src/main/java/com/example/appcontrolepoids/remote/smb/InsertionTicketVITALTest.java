package com.example.appcontrolepoids.remote.smb;

import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.utils.SmbFiles;

import java.io.File;

public class InsertionTicketVITALTest {

    public static void main(String[] args) {
        SMBClient client = new SMBClient();

        try {
            Connection connection = client.connect("VM-GED");
            AuthenticationContext ac = new AuthenticationContext("PRAMI", "PR170772".toCharArray(), "VITAL");
            Session session = connection.authenticate(ac);

            // Connect to Share
            DiskShare share = (DiskShare) session.connectShare("public$");
            for (FileIdBothDirectoryInformation f : share.list("/Qualité/Production/Controle Poids PF")) {
                System.out.println("File : " + f.getFileName());
            }

            File file = new File("./build.gradle");

            SmbFiles.copy(file, share, "/Qualité/Production/Controle Poids PF/robert.gradle", false);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
