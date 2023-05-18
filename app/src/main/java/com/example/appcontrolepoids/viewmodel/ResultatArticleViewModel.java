package com.example.appcontrolepoids.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.appcontrolepoids.util.CombinedThreeLiveData;
import com.example.appcontrolepoids.util.CombinedTwoLiveData;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.BorderRadius;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResultatArticleViewModel extends ViewModel {

    private String codeArticle;
    private String nomArticle;
    private String numeroLot;
    private String codeOperateur;
    private String ddm;
    private int poidsNet;
    private String eanArticle;

    private final MutableLiveData<float[]> pesees = new MutableLiveData<>();
    public LiveData<Float> moyenne = Transformations.map(pesees, _pesees -> {
        float somme = 0;

        for (float p : _pesees) {
            somme += p;
        }

        float moyenne = somme / _pesees.length;

        //Arrondi le résultat à 2 chiffres après la virgule
        return Math.round(moyenne * 10.0) / 10.0f;
    });

    public LiveData<Float> variance = new CombinedTwoLiveData<>(pesees, moyenne, (_pesees, _moyenne) -> {
        float sommeEcartsCarres = 0;

        for (float p : _pesees) {
            float ecart = p - _moyenne;
            sommeEcartsCarres += ecart * ecart;
        }

        float variance = sommeEcartsCarres / _pesees.length;

        //Arrondi le résultat à 2 chiffres après la virgule
        return Math.round(variance * 10.0) / 10.0f;
    });


    public LiveData<Float> ecartType = Transformations.map(variance, _variance -> {
        float ecartType = (float) Math.sqrt(_variance);

        //Arrondi le résultat à 2 chiffres après la virgule
        return Math.round(ecartType * 10.0) / 10.0f;
    });

    public MutableLiveData<Integer> poidsBrut = new MutableLiveData<>();

    public MutableLiveData<Float> coefficient = new MutableLiveData<>();

    public MutableLiveData<Float> formule = new CombinedThreeLiveData<>(poidsBrut, coefficient, ecartType,
            (_poidsBrut, _coefficient, _ecartType) -> {
                float resultat = _poidsBrut + (_coefficient * _ecartType);
                return Math.round(resultat * 10.0) / 10.0f;
            }
    );

    public LiveData<Boolean> lotValide = new CombinedTwoLiveData<>(moyenne, formule, (_moyenne, _formule) -> _moyenne >= _formule);

    public void init(float[] pesees, int poidsBrut, float coefficient, String codeArticle, String nomArticle, String numeroLot, String codeOperateur, String ddm, int poidsNet, String eanArticle) {
        this.pesees.postValue(pesees);
        this.poidsBrut.postValue(poidsBrut);
        this.coefficient.postValue(coefficient);
        this.codeArticle = codeArticle;
        this.nomArticle = nomArticle;
        this.numeroLot = numeroLot;
        this.codeOperateur = codeOperateur;
        this.ddm = ddm;
        this.poidsNet = poidsNet;
        this.eanArticle = eanArticle;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public void genererPDF(File file) {
        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDoc = new PdfDocument(writer);

            Document document = new Document(pdfDoc, PageSize.A4);
            document.setBackgroundColor(ColorConstants.WHITE);

            Paragraph titre = new Paragraph(codeArticle + " - " + nomArticle + "\n")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(25);
            document.add(titre);

            Paragraph ean = new Paragraph(eanArticle + "\n\n")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(16);
            document.add(ean);

            //Crée le tableau à une ligne et trois colonnes
            Table tableInfos = new Table(3).useAllAvailableWidth();

            //Crée les cellules du tableau
            Cell cell1 = new Cell().add(new Paragraph("Date contrôle : " + dateFormat.format(new Date())));
            Cell cell2 = new Cell().add(new Paragraph("\tLot n°" + numeroLot));
            Cell cell3 = new Cell().add(new Paragraph("DDM : " + ddm));
            Cell cell4 = new Cell().add(new Paragraph("Code opérateur : " + codeOperateur));
            Cell cell5 = new Cell().add(new Paragraph("\tPoids net : " + poidsNet + " g"));
            Cell cell6 = new Cell().add(new Paragraph("Poids brut : " + poidsBrut.getValue() + " g"));

            //Définit la largeur de la première colonne à 40%
            cell1.setWidth(UnitValue.createPercentValue(40));


            //Rend les bordures des cellules invisibles
            cell1.setBorder(Border.NO_BORDER);
            cell2.setBorder(Border.NO_BORDER);
            cell3.setBorder(Border.NO_BORDER);
            cell4.setBorder(Border.NO_BORDER);
            cell5.setBorder(Border.NO_BORDER);
            cell6.setBorder(Border.NO_BORDER);

            //Ajoute les cellules au tableau
            tableInfos.addCell(cell1);
            tableInfos.addCell(cell2);
            tableInfos.addCell(cell3);
            tableInfos.addCell(cell4);
            tableInfos.addCell(cell5);
            tableInfos.addCell(cell6);

            //Ajoute le tableau au document
            document.add(tableInfos);

            document.add(new Paragraph("\nListe des pesées\n").setBold());

            List<Float> listPesees = new ArrayList<>();
            if (pesees.getValue() != null) {
                for (float f : pesees.getValue()) {
                    listPesees.add(f);
                }
            }

            Table tablePesees = new Table(5);

            int peseeNumero = 1;
            for (float p : listPesees) {
                Cell cell = new Cell().add(new Paragraph(String.format(Locale.FRANCE, "%02d : %.1f", peseeNumero, p)).setTextAlignment(TextAlignment.JUSTIFIED));
                cell.setBorder(Border.NO_BORDER);
                tablePesees.addCell(cell);
                peseeNumero++;
            }

            tablePesees.setHorizontalAlignment(HorizontalAlignment.CENTER);
            tablePesees.useAllAvailableWidth();

            document.add(tablePesees);

            int nombrePesees = listPesees.size();
            int nombreDeLignesAAjouter = (80 - nombrePesees) / 5;
            int nombreLignes;
            for (nombreLignes = 0; nombreLignes < nombreDeLignesAAjouter; nombreLignes++) {
                Paragraph sautLigne = new Paragraph("\n");
                document.add(sautLigne);
            }

            if (listPesees.size() == 80){
                Paragraph sautLigne = new Paragraph("\n\n");
                document.add(sautLigne);
            }

            document.add(new Paragraph("Résultat des pesées\n").setBold());

            Table tableResultat = new Table(UnitValue.createPercentArray(4)).useAllAvailableWidth();

            Cell cellule1 = new Cell().add(new Paragraph("Moyenne").setBold());
            Cell cellule2 = new Cell().add(new Paragraph("Variance").setBold());
            Cell cellule3 = new Cell().add(new Paragraph("Ecart-type").setBold());
            Cell cellule4 = new Cell().add(new Paragraph("Critère d'acceptation").setBold());
            Cell cellule5 = new Cell().add(new Paragraph(String.valueOf(moyenne.getValue())));
            Cell cellule6 = new Cell().add(new Paragraph(String.valueOf(variance.getValue())));
            Cell cellule7 = new Cell().add(new Paragraph(String.valueOf(ecartType.getValue())));
            Cell cellule8 = new Cell().add(new Paragraph(String.valueOf(formule.getValue())));

            tableResultat.addCell(cellule1);
            tableResultat.addCell(cellule2);
            tableResultat.addCell(cellule3);
            tableResultat.addCell(cellule4);
            tableResultat.addCell(cellule5);
            tableResultat.addCell(cellule6);
            tableResultat.addCell(cellule7);
            tableResultat.addCell(cellule8);

            document.add(tableResultat);

            DeviceRgb greenColor = new DeviceRgb(67, 160, 71);
            Paragraph critere = new Paragraph("\nMoyenne des pesées > Critère d'acceptation")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(17)
                    .setFontColor(greenColor);
            document.add(critere);

            Table tableValidation = new Table(3);
            tableValidation.setWidth(UnitValue.createPercentValue(30)).setHorizontalAlignment(HorizontalAlignment.CENTER);

            Cell cell = new Cell()
                    .add(new Paragraph("LOT VALIDÉ")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBold()
                            .setFontSize(17)
                            .setFontColor(greenColor)
                            .setPadding(5f)
                            .setBorder(new SolidBorder(greenColor, 2))
                            .setBorderRadius(new BorderRadius(10f)));

            cell.setBorder(Border.NO_BORDER);

            tableValidation.addCell(cell);

            document.add(tableValidation);

            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
