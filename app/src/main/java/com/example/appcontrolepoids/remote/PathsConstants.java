package com.example.appcontrolepoids.remote;

import android.os.Environment;

public class PathsConstants {
    public static final String LOCAL_STORAGE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Controles Poids PF/";
    public static final String SMB_DESTINATION_PATH = "/Qualité/Production/Controle Poids PF/";
    public static final String VOLUME_PATH = "\\\\VM-GED\\public$\\Qualité\\Production\\Controle Poids PF\\";
}
