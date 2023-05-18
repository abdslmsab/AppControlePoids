package com.example.appcontrolepoids.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appcontrolepoids.remote.PathsConstants;
import com.example.appcontrolepoids.remote.sage.InsertionTicketSAGE;
import com.example.appcontrolepoids.remote.smb.InsertionTicketVITAL;
import com.example.appcontrolepoids.util.Async;

import java.io.File;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivityViewModel extends ViewModel {

    public MutableLiveData<String> messageErreur = new MutableLiveData<>();
    public MutableLiveData<String> messageInfos = new MutableLiveData<>();

    public void synchroniserPDFs(boolean hasNetworkConnection) {
        File directory = new File(PathsConstants.LOCAL_STORAGE);
        directory.mkdirs();

        File[] files = directory.listFiles();

        if (files == null) {
            return;
        }

        if(!hasNetworkConnection && files.length > 0) {
            messageInfos.postValue("Des PDFs sont en attente de transfert. Merci de vous connecter au réseau.");
        }

        if (hasNetworkConnection && files.length > 0) {
            StringBuilder error = new StringBuilder();
            for (File file : files) {
                Pattern pattern = Pattern.compile("(.*)-\\d{5}\\.pdf");
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.matches()) {
                    String codeArticle = matcher.group(1);
                    if (codeArticle != null) {
                        FutureTask<Void> task = new FutureTask<>(() -> {
                            InsertionTicketVITAL.insererArticle(file);
                            InsertionTicketSAGE.insererArticle(file, codeArticle);
                            return null;
                        });
                        Async.EXECUTOR_SERVICE.execute(task);

                        try {
                            task.get();
                            file.delete();
                        } catch (ExecutionException e) {
                            e.printStackTrace();

                            error.append(file.getName());
                            error.append(" : ");
                            error.append("Une erreur est survenue lors de la synchronisation des PDFs. (");
                            if(e.getCause() != null) {
                                if(e.getCause().getClass().equals(UnknownHostException.class)) {
                                    error.append("Hôte introuvable : ");
                                }
                            }
                            error.append(e.getCause().getMessage());
                            error.append(")\n");
                        } catch (InterruptedException e) {
                            e.printStackTrace();

                            error.append(file.getName());
                            error.append(" : ");
                            error.append("Une erreur est survenue lors de la synchronisation des PDFs.");
                            error.append("\n");                        }
                    }
                }
            }
            if(!error.toString().isEmpty()) {
                messageErreur.postValue(error.toString());
            } else {
                messageInfos.postValue("PDFs synchronisés avec succès.");
            }
        }
    }
}
