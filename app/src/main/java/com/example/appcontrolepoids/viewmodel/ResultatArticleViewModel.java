package com.example.appcontrolepoids.viewmodel;

import android.os.Environment;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.appcontrolepoids.util.CombinedThreeLiveData;
import com.example.appcontrolepoids.util.CombinedTwoLiveData;
import com.example.appcontrolepoids.view.ResultatArticle;
import com.example.appcontrolepoids.view.TicketArticle;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResultatArticleViewModel extends ViewModel {

    private String codeArticle;
    private String nomArticle;
    private String numeroLot;
    private  String codeOperateur;
    private String ddm;

    private final MutableLiveData<float[]> pesees = new MutableLiveData<>();
    public LiveData<Float> moyenne = Transformations.map(pesees, _pesees -> {
        float somme = 0;

        for (float p : _pesees) {
            somme += p;
        }

        float moyenne = somme / _pesees.length;

        //Arrondi le résultat à 2 chiffres après la virgule
        return Math.round(moyenne * 100.0) / 100.0f;
    });

    public LiveData<Float> variance = new CombinedTwoLiveData<>(pesees, moyenne, (_pesees, _moyenne) -> {
        float sommeEcartsCarres = 0;

        for (float p : _pesees) {
            float ecart = p - _moyenne;
            sommeEcartsCarres += ecart * ecart;
        }

        float variance = sommeEcartsCarres / _pesees.length;

        //Arrondi le résultat à 2 chiffres après la virgule
        return Math.round(variance * 100.0) / 100.0f;
    });


    public LiveData<Float> ecartType = Transformations.map(variance, _variance -> {
        float ecartType = (float) Math.sqrt(_variance);

        //Arrondi le résultat à 2 chiffres après la virgule
        return Math.round(ecartType * 100.0) / 100.0f;
    });

    public MutableLiveData<Integer> poidsBrut = new MutableLiveData<>();

    public MutableLiveData<Float> coefficient = new MutableLiveData<>();

    public MutableLiveData<Float> formule = new CombinedThreeLiveData<>(poidsBrut, coefficient, ecartType,
            (_poidsBrut, _coefficient, _ecartType) -> _poidsBrut + (_coefficient * _ecartType)
    );

    public LiveData<Boolean> lotValide = new CombinedTwoLiveData<>(moyenne, formule, (_moyenne, _formule) -> _moyenne >= _formule);

    public void init(float[] pesees, int poidsBrut, float coefficient, String codeArticle, String nomArticle, String numeroLot, String codeOperateur, String ddm) {
        this.pesees.postValue(pesees);
        this.poidsBrut.postValue(poidsBrut);
        this.coefficient.postValue(coefficient);
        this.codeArticle = codeArticle;
        this.nomArticle = nomArticle;
        this.numeroLot = numeroLot;
        this.codeOperateur = codeOperateur;
        this.ddm = ddm;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public void genererPDF() {
        String fileName = codeArticle + "-" + numeroLot + ".pdf";
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/" + fileName;

        try {
            PdfWriter writer = new PdfWriter(pdfPath);
            PdfDocument pdfDoc = new PdfDocument(writer);

            Document document = new Document(pdfDoc, PageSize.A4);

            document.add(new Paragraph("Contrôle réalisé le " + dateFormat.format(new Date())));
            Paragraph titre = new Paragraph(codeArticle + " - LOT N° " + numeroLot + "\n" + nomArticle + "\n\n");
            titre.setTextAlignment(TextAlignment.CENTER);
            document.add(titre);

            //Crée le tableau à une ligne et deux colonnes
            Table table = new Table(2);

            //Rend le fond du tableau transparent
            table.setBackgroundColor(new DeviceRgb(255, 255, 255));
            table.setWidth(UnitValue.createPercentValue(80));
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            //Crée les cellules du tableau
            Cell cell1 = new Cell().add(new Paragraph("Code opérateur : " + codeOperateur));
            Cell cell2 = new Cell().add(new Paragraph(""));
            Cell cell3 = new Cell().add(new Paragraph("DDM : " + ddm));
            Cell cell4 = new Cell().add(new Paragraph("Numéro de lot : " + numeroLot));

            //Rend les bordures des cellules invisibles
            cell1.setBorder(Border.NO_BORDER);
            cell2.setBorder(Border.NO_BORDER);
            cell3.setBorder(Border.NO_BORDER);
            cell4.setBorder(Border.NO_BORDER);

            //Ajoute les cellules au tableau
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);

            //Ajoute le tableau au document
            document.add(table);

            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
