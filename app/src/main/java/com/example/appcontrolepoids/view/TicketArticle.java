package com.example.appcontrolepoids.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.appcontrolepoids.R;
import com.example.appcontrolepoids.databinding.ActivityTicketArticleBinding;
import com.example.appcontrolepoids.model.Article;
import com.example.appcontrolepoids.remote.PathsConstants;
import com.example.appcontrolepoids.remote.sage.InsertionTicketSAGE;
import com.example.appcontrolepoids.remote.smb.InsertionTicketVITAL;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TicketArticle extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_article);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        //Variable de liaison (binding) permettant d'accéder aux éléments de l'interface utilisateur
        ActivityTicketArticleBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_ticket_article);

        binding.setLifecycleOwner(this);

        String pdfName = getIntent().getStringExtra("pdf_name");

        File file =  new File(PathsConstants.LOCAL_STORAGE, pdfName);

        //Vérifie si le fichier existe
        if (file.exists()) {
            try {
                //Création d'un objet PdfRenderer à partir du fichier
                PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));

                //Sélection de la première page pour l'afficher dans ImageView
                PdfRenderer.Page page = renderer.openPage(0);

                //Création d'un objet Bitmap pour stocker le contenu de la page PDF
                Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(),
                        Bitmap.Config.ARGB_8888);

                //Dessin du contenu de la page PDF dans le Bitmap
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                //Affichage du Bitmap dans ImageView
                binding.pdfGenere.setImageBitmap(bitmap);

                //Libération de la mémoire utilisée par la page PDF
                page.close();

                //Libération de la mémoire utilisée par le PdfRenderer
                renderer.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Le PDF est introuvable et ne peut être visualisé", Toast.LENGTH_LONG).show();
            this.finish();
            Intent intent = new Intent(TicketArticle.this, MainActivity.class);
            startActivity(intent);
        }

        binding.boutonTerminer.setOnClickListener(view -> {
            this.finish();
            Intent intent = new Intent(TicketArticle.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
