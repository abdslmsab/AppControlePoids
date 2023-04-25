package com.example.appcontrolepoids.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.appcontrolepoids.R;
import com.example.appcontrolepoids.databinding.ActivityResultatsArticleBinding;
import com.example.appcontrolepoids.databinding.ActivityTicketArticleBinding;
import com.example.appcontrolepoids.viewmodel.ResultatArticleViewModel;

import java.io.File;
import java.io.IOException;
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

        String pdfPath = getIntent().getStringExtra("pdf_path");

        File file = new File(pdfPath);

        //Vérifie si le fichier existe
        if (file.exists()) {
            try {
                //Création d'un objet PdfRenderer à partir du fichier
                PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));

                //N nombre de pages dans le PDF
                final int pageCount = renderer.getPageCount();

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
