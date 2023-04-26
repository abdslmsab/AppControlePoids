package com.example.appcontrolepoids.alertdialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.appcontrolepoids.R;
import com.example.appcontrolepoids.databinding.AlertDialogLayoutBinding;

public class DialogAlerte extends DialogFragment {

    private static AlertDialogInterface monRappel;

    public DialogAlerte() {
    }

    public static DialogAlerte newInstance(AlertDialogInterface rappel) {
        monRappel = rappel;
        return new DialogAlerte();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //Obtenir le modèle de vue partagé
        DialogAlerteViewModel viewModel = new ViewModelProvider(requireActivity()).get(DialogAlerteViewModel.class);

        //Obtient AlertOptions qui décide de l'aspect et du comportement du Dialog affiché
        final OptionsAlerte mOptions = viewModel.getOptions().getValue() != null
                ? viewModel.getOptions().getValue()
                : OptionsAlerte.creer(TypeAlerte.erreurInconnue);

        //-----------------------------------Chargement de l'interface utilisateur------------------------------------------------
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(requireContext());
        AlertDialogLayoutBinding binding = AlertDialogLayoutBinding.inflate(requireActivity().getLayoutInflater());
        binding.setOptions(mOptions);
        binding.setViewModel(viewModel);
        alertBuilder.setView(binding.getRoot());
        AlertDialog dialog = alertBuilder.create();

        //Affichage de la forme personnalisé
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        final FrameLayout circleFL = binding.alertDialogCircle;
        final TextView textTV = binding.alertDialogText;
        final TextView alternativeOptionTV = binding.alertDialogAlternativeOption;
        final TextView mainOptionTV = binding.alertDialogMainOption;
        final EditText texteCode = binding.alertDialogInput;

        binding.alertDialogIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), mOptions.getIcon()));
        setCancelable(mOptions.isCancelable());

        mainOptionTV.setOnClickListener(v -> {
            monRappel.alertDialogMainOption(mOptions.getType());
            dismiss();
        });

        alternativeOptionTV.setOnClickListener(v -> {
            monRappel.alertDialogAlternativeOption(mOptions.getType());
            dismiss();
        });

        //Si le type d'alerte n'est pas codeVerrouillage, on enlève le champ pour taper un code
        if (mOptions.getType() == TypeAlerte.erreurInconnue || mOptions.getType() == TypeAlerte.confirmationSupprimerArticle || mOptions.getType() == TypeAlerte.articleSupprime || mOptions.getType() == TypeAlerte.articleExistePas || mOptions.getType() == TypeAlerte.peseeAberrante ){
            texteCode.setVisibility(View.GONE);
        }

        //Changement de couleur en fonction de la catégorie d'alerte
        CategorieAlerte alertCategory = getCategorieAlerte(mOptions.getType());
        if (alertCategory == CategorieAlerte.avertissement) {
            circleFL.setBackgroundResource(R.drawable.alert_dialog_circle_warning);
            alternativeOptionTV.setTextColor(requireContext().getColor(R.color.rouge));
            mainOptionTV.setBackgroundResource(R.drawable.background_rounded_button_warning);
        } else if (alertCategory == CategorieAlerte.reussite) {
            circleFL.setBackgroundResource(R.drawable.alert_dialog_circle_success);
            alternativeOptionTV.setTextColor(requireContext().getColor(R.color.vert));
            mainOptionTV.setBackgroundResource(R.drawable.background_rounded_button_success);
        }

        //------------------------Observation des changements dans le ViewModel---------------------------------------

        viewModel.getOptions().observe(this, options -> {
            textTV.setText(options.getText());
            // titleTV.setText(options.getTitle()); <---[Exemple]
        });

        // Si MyAlertDialogViewModel.estFerme.getValue() == true, alors on enlève le Dialog
        viewModel.estFerme().observe(this, fermer -> {
            if (fermer) {
                dismiss();
            }
        });

        return dialog;
    }

    /**
     * @param type prend le type AlertType qui est unique pour chaque Dialog ayant le même objet
     * @return AlertCategory (actuellement: primaire, avertissement ou reussite)
     */
    private CategorieAlerte getCategorieAlerte(TypeAlerte type) {
        //Catégorie réussite
        if (type == TypeAlerte.articleSupprime) {
            return CategorieAlerte.reussite;
        }
        //Catégorie primaire
        if (type == TypeAlerte.verrouillageCode) {
            return CategorieAlerte.primaire;
        } else {
            //Catégorie avertissement
            return CategorieAlerte.avertissement;
        }
    }

    public interface AlertDialogInterface {
        void alertDialogMainOption(TypeAlerte type);

        void alertDialogAlternativeOption(TypeAlerte type);
    }
}
