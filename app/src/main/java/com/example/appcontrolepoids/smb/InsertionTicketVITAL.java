package com.example.appcontrolepoids.smb;

import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.utils.SmbFiles;

import java.io.File;

public class InsertionTicketVITAL {

    public static void main(String[] args) {
        SMBClient client = new SMBClient();

        try {
            Connection connection = client.connect("VM-GED");
            AuthenticationContext ac = new AuthenticationContext("PRAMI", "".toCharArray(), "VITAL");
            Session session = connection.authenticate(ac);

            // Connect to Share
            DiskShare share = (DiskShare) session.connectShare("public$");
            for (FileIdBothDirectoryInformation f : share.list("/Qualité/Production/Controle Poids PF")) {
                System.out.println("File : " + f.getFileName());
            }

            //Below line is to use when run on android device only
            //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/CAKE087-23115.pdf";
            //File file = new File(path);

            File file = new File("./build.gradle");

            SmbFiles.copy(file, share, "/Qualité/Production/Controle Poids PF/didier.gradle", false);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
