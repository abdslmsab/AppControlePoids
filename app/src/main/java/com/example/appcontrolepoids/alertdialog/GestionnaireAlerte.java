package com.example.appcontrolepoids.alertdialog;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

public class GestionnaireAlerte {

    /**
     * @param owner si hors de l'activité, il suffit généralement d'appeler 'this' ou 'ActivityName.this'.
     *              si initialisé dans un fragment, toujours appeler requireActivity() à la place
     * @return MyAlertDialogViewModel qui contient les options des dialogues d'alerte
     */
    public static DialogAlerteViewModel initialiseViewModel(ViewModelStoreOwner owner) {
        return new ViewModelProvider(owner).get(DialogAlerteViewModel.class);
    }

    /**
     * @param rappel  habituellement this, ou ActivityName.this. doit être une classe qui étend n'importe quelle FragmentActivity
     * @param type      le type d'alerte qui définit le type d'alerte à afficher
     * @param viewModel l'instance MyAlertDialogViewModel de l'activité où cette fonction est appelée
     * @param contexte   Si elle est appelée à l'intérieur d'un fragment, mettre requireActivity()
     */
    public static void showMyDialog(DialogAlerte.AlertDialogInterface rappel,
                                    TypeAlerte type,
                                    DialogAlerteViewModel viewModel,
                                    Context contexte
    ) {
        DialogAlerte dialog = DialogAlerte.newInstance(rappel);
        viewModel.setOptions(OptionsAlerte.creer(type));
        viewModel.afficherDialog();
        dialog.show(((FragmentActivity) contexte).getSupportFragmentManager(), type.toString());
    }
}
