package com.example.appcontrolepoids.alertdialog;

import androidx.annotation.DrawableRes;

import com.example.appcontrolepoids.R;


/**
 * Class which holds the options for MyAlertDialog
 */
public class OptionsAlerte {
    private final String title;
    private final String text;
    private final String alternativeText;
    private final String mainText;
    private final int icon;
    private final boolean isCancelable;
    private final TypeAlerte type;

    /**
     * Class which holds the options for MyAlertDialog
     *
     * @param title           String to show as title
     * @param text            String to show as text
     * @param alternativeText String to be displayed at alternative button
     * @param mainText        String to be displayed at main button
     * @param icon            Resource ID of the icon drawable
     * @param isCancelable    True if should be cancelable
     * @param type            The AlertType (to be returned as callback to calling activity)
     */
    public OptionsAlerte(String title,
                         String text,
                         String alternativeText,
                         String mainText,
                         @DrawableRes int icon,
                         boolean isCancelable,
                         TypeAlerte type) {
        this.title = title;
        this.text = text;
        this.alternativeText = alternativeText;
        this.mainText = mainText;
        this.icon = icon;
        this.isCancelable = isCancelable;
        this.type = type;
    }

    public static OptionsAlerte creer(TypeAlerte type) {
        switch (type) {
            case verrouillageCode:
                return new OptionsAlerte(
                        "Verrouillage",
                        "Veuillez entrer le code pour continuer",
                        "Annuler",
                        "Valider",
                        R.drawable.icon_primary,
                        false,
                        type);
            case exempleAvertissement:
                return new OptionsAlerte(
                        "Attention !",
                        "Êtes-vous sûr de vouloir supprimer cet article ?",
                        "Non",
                        "Oui",
                        R.drawable.icon_warning,
                        false,
                        type);
            case exempleReussite:
                return new OptionsAlerte(
                        "Supprimé !",
                        "L'article a bien été supprimé.",
                        "",
                        "Fermer",
                        R.drawable.icon_success,
                        true,
                        type);
            case articleExistePas:
                return new OptionsAlerte(
                        "L'article n'existe pas",
                        "Veuillez l'ajouter",
                        "",
                        "Ok",
                        R.drawable.icon_warning,
                        true,
                        type);
            default:
                return new OptionsAlerte(
                        "Erreur inconnue",
                        "Quelque chose ne va pas, redémarrez l'application.",
                        "",
                        "Fermer",
                        R.drawable.icon_warning,
                        false,
                        TypeAlerte.erreurInconnue
                );
        }
    }

    public TypeAlerte getType() {
        return type;
    }

    public int getIcon() {
        return icon;
    }

    public String getAlternativeText() {
        return alternativeText;
    }

    public String getMainText() {
        return mainText;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCancelable() {
        return isCancelable;
    }

}