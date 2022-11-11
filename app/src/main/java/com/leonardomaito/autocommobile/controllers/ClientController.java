package com.leonardomaito.autocommobile.controllers;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.leonardomaito.autocommobile.models.Client;
import com.santalu.maskara.widget.MaskEditText;

import autocommobile.R;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

public class ClientController {

    private String clientName;
    private String clientCpf;
    private String clientAddress;
    private String clientTelephone;
    CPFValidator cpfValidator = new CPFValidator();

     public Client returnNewClient(EditText etClientName,
                                   MaskEditText etClientCpf,
                                   EditText etClientAddress,
                                   EditText etClientTelephone,
                                   Button nextOs){

             clientName = etClientName.getText().toString().isEmpty() ? "Teste" : etClientName.getText().toString() ;
             clientCpf = etClientCpf.getText().toString();
             clientAddress = etClientAddress.getText().toString();
             clientTelephone = etClientTelephone.getText().toString();

         Client newClient = new Client.ClientBuilder(clientName, clientCpf)
                 .address(clientAddress)
                 .telephone(clientTelephone)
                 .build();

         return newClient;

     }

    public boolean checkAllClientFields(EditText etClientName, MaskEditText etClientCpf, Button nextOs){
        int yourDesiredLength = 3;

        if (etClientName.getText().length() < yourDesiredLength) {
            etClientName.setError("Mínimo de 3 Caracteres");
            etClientName.requestFocus();
            return false;
        }

        try{
            cpfValidator.assertValid(etClientCpf.getUnMasked());

        } catch (InvalidStateException e){
            etClientCpf.setError("Insira um CPF válido");
            return false;
        }
        nextOs.setBackgroundColor(nextOs.getContext().getResources().getColor(R.color.autocom_blue));
        nextOs.setEnabled(true);
        return true;
    }
}
