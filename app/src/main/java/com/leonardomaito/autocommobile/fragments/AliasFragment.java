package com.leonardomaito.autocommobile.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import autocommobile.R;


public class AliasFragment extends DialogFragment {

    private EditText etAliasInput;
    private Button btAliasConfirm;
    private Button btAliasCancel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater dialogInflater = requireActivity().getLayoutInflater();
        View viewAlias = dialogInflater.inflate(R.layout.dialog_fragment_alias, null);

        etAliasInput = viewAlias.findViewById(R.id.etAliasInput);
        btAliasConfirm = viewAlias.findViewById(R.id.btAliasConfirm);
        btAliasCancel = viewAlias.findViewById(R.id.btAliasCancel);

        dialogBuilder.setView(viewAlias);

        btAliasConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Alias inserido com sucesso sem verificação", Toast.LENGTH_SHORT).show();

            }
        });

        btAliasCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Retornando a tela de login", Toast.LENGTH_SHORT).show();

            }
        });
        return dialogBuilder.create();
    }
}
