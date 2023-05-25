package com.example.appcontrolepoids.view;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.appcontrolepoids.R;
import com.example.appcontrolepoids.alertdialog.DialogAlerte;
import com.example.appcontrolepoids.alertdialog.DialogAlerteViewModel;
import com.example.appcontrolepoids.alertdialog.GestionnaireAlerte;
import com.example.appcontrolepoids.alertdialog.TypeAlerte;
import com.example.appcontrolepoids.databinding.ActivityPeseesArticleBinding;
import com.example.appcontrolepoids.model.Article;
import com.example.appcontrolepoids.viewmodel.PeseesArticleViewModel;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PeseesArticle extends AppCompatActivity implements DialogAlerte.AlertDialogInterface, Runnable {


    private View root = null;

    private PeseesArticleViewModel peseesArticleViewModel;
    private DialogAlerteViewModel dialogAlerteViewModel;
    private ActivityPeseesArticleBinding binding;
    private static final int READ_WAIT_MILLIS = 100;
    private static final int PERIOD = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesees_article);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        //Variable de liaison (binding) permettant d'accéder aux éléments de l'interface utilisateur
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pesees_article);
        //Initialisation de l'objet PeseesArticleViewModel avec le contexte de l'activité
        peseesArticleViewModel = new ViewModelProvider(this).get(PeseesArticleViewModel.class);
        //Définition de l'objet PeseesArticleViewModel comme variable dans le layout pour le Data Binding
        binding.setPeseesArticleViewModel(peseesArticleViewModel);
        binding.setLifecycleOwner(this);
        this.root = binding.getRoot();

        //Initialisation de l'objet DialogAlerteViewModel avec le contexte de l'activité
        dialogAlerteViewModel = new ViewModelProvider(this).get(DialogAlerteViewModel.class);

        String numeroLot = getIntent().getStringExtra("numero_lot");
        String codeOperateur = getIntent().getStringExtra("code_operateur");
        String ddm = getIntent().getStringExtra("ddm");

        Article article = (Article) getIntent().getSerializableExtra("article");
        binding.nomArticle.setText(article.getNom());
        binding.valeurPoidsCible.setText(" : " + article.getPoidsBrut() + " g");

        //Lorsque l'on clique sur le bouton "Annuler" on ferme cette activité afin de revenir sur l'activité principale
        binding.boutonAnnuler.setOnClickListener(view -> this.finish());

        //On récupère les données entrées dans la précédente activité
        int nombreVenues = getIntent().getIntExtra("nombre_venues", -1);

        peseesArticleViewModel.calculerCompteur(article.getRendement(), nombreVenues);

        // Vérification du champ de saisie
        peseesArticleViewModel.estPeseeValide.observe(this, estPeseeValide -> {
            if (!estPeseeValide) {
                binding.textePoidsBrut.setError("La pesée ne peut être nulle");
            } else {
                binding.textePoidsBrut.setErrorEnabled(false);
            }
        });

        // Vérification des données aberrantes
        peseesArticleViewModel.estPeseeAberrante.observe(this, estPeseeAberrante -> {
            if (estPeseeAberrante) {
                GestionnaireAlerte.showMyDialog(this, TypeAlerte.peseeAberrante, dialogAlerteViewModel, this);
            }
        });

        //Format de l'entrée désirée (voir https://github.com/RedMadRobot/input-mask-android/wiki/1.-Mask-Syntax)
        final MaskedTextChangedListener listener = new MaskedTextChangedListener("[990]{.}[9]", binding.poidsBrutEditText);
        binding.poidsBrutEditText.addTextChangedListener(listener);

        //Permet d'utiliser la valeur du poids brut entre la vue et la vue-modèle
        peseesArticleViewModel.init(article.getPoidsBrut());

        //Permet de pouvoir cliquer sur le bouton "Valider" si on clique sur la touche "Ok" du clavier
        binding.poidsBrutEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    peseesArticleViewModel.actionValiderSaisie();
                }
                return false;
            }
        });

        binding.boutonValider.setOnClickListener(view -> {
            peseesArticleViewModel.actionValiderSaisie();
        });

        peseesArticleViewModel.nombrePeseesRestantes.observe(this, nombrePeseesRestantes -> {
            if (nombrePeseesRestantes != null && nombrePeseesRestantes <= 0) {
                binding.poidsBrutEditText.clearFocus();

                Intent intent = new Intent(PeseesArticle.this, ResultatArticle.class);
                intent.putExtra("article", article);
                intent.putExtra("numeroLot", numeroLot);
                intent.putExtra("codeOperateur", codeOperateur);
                intent.putExtra("ddm", ddm);
                intent.putExtra("nombreVenues", nombreVenues);

                List<Float> listePesees = peseesArticleViewModel.listePesees.getValue();
                float[] listePeseesTableau = new float[listePesees.size()];
                for (int i = 0; i < listePesees.size(); i++) {
                    listePeseesTableau[i] = listePesees.get(i);
                }
                intent.putExtra("listePesees", listePeseesTableau);
                intent.putExtra("coefficient", peseesArticleViewModel.coefficient);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        run();
        peseesArticleViewModel.reinitialiserListePesees();
    }

    @Override
    public void alertDialogMainOption(TypeAlerte type) {
        peseesArticleViewModel.actionAjouterPeseeAberrante();
    }

    @Override
    public void alertDialogAlternativeOption(TypeAlerte type) {

    }

    @Override
    public void onPause() {
        root.removeCallbacks(this);

        super.onPause();
    }

    @Override
    public void run() {
        //Partie RS232 vers USB-C

        // Trouve tous les pilotes disponibles à partir des périphériques connectés
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);

        HashMap<String, UsbDevice> usbDevices = manager.getDeviceList();
        Collection<UsbDevice> ite = usbDevices.values();
        UsbDevice[] usbs = ite.toArray(new UsbDevice[]{});
        for (UsbDevice usb : usbs) {
            Log.d("USB","Les périphériques USB connectés sont : "+ usb.getDeviceName());
        }

        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        Log.d("USB", "Liste des pilotes disponibles : " + Arrays.toString(availableDrivers.toArray()));

        if (availableDrivers.isEmpty()) {
            return;
        }

        // Ouvre une connexion avec le premier pilote disponible
        UsbSerialDriver driver = availableDrivers.get(0);
        Log.d("USB", "Premier pilote : " + availableDrivers.get(0));

        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            Log.d("USB", "Impossible d'établir la connexion");
        } else {
            Log.d("USB", "Connexion établie");
        }

        UsbSerialPort port = driver.getPorts().get(0); // La plupart des appareils n'ont qu'un seul port (port 0)
        Log.d("USB", "Tous les ports : " + driver.getPorts().toString());
        try {
            port.open(connection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            port.setParameters(9600, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            // Crée un tableau pour stocker les données lues
            byte[] buffer = new byte[8192];
            StringBuilder dataBuilder = new StringBuilder();

            // Boucle de lecture pour accumuler toutes les données disponibles jusqu'à ce que la transmission soit terminée
            while (true) {
                int len = port.read(buffer, READ_WAIT_MILLIS); // Lit les données avec un délai de 100 millisecondes
                if (len > 0) {
                    // Convertir les données lues en chaîne
                    String data = new String(buffer, 0, len);
                    dataBuilder.append(data);
                } else {
                    break; // Aucune donnée lue, la transmission est terminée
                }
            }

            String data = dataBuilder.toString();
            String result = data.replace("/", "").replace("g", "").replace("-", "").replace(" ", "");

            // Affichez les données de poids dans le champ "poids_brut_edit_text" de la vue "PeseesArticle"
            if (!(result.equals(""))){
                binding.poidsBrutEditText.setText(result);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        root.postDelayed(this, PERIOD);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
