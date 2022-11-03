package com.leonardomaito.autocommobile.controllers;

import android.widget.Button;
import android.widget.EditText;

import com.leonardomaito.autocommobile.models.Client;

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
                                   EditText etClientCpf,
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

     public boolean verifyCpf(EditText etClientCpf){
             try{
                 cpfValidator.assertValid(etClientCpf.getText().toString());
                 return true;
             } catch (InvalidStateException e){
                 return false;
             }
     }

    public boolean checkAllClientFields(EditText etClientName, EditText etClientCpf, Button nextOs){
        int yourDesiredLength = 3;
        int cpfLength = 11;

        if (etClientName.getText().length() < yourDesiredLength) {
            etClientName.setError("Mínimo de 3 Caracteres");
            etClientName.requestFocus();
            return false;
        }
        else if(etClientCpf.getText().length() < cpfLength || etClientCpf.getText().length() > cpfLength ){
                etClientCpf.setError("CPF precisa conter 11 dígitos");
                etClientCpf.requestFocus();
                return false;
            }
       else if(etClientCpf.getText().length() == cpfLength){
                if(!verifyCpf(etClientCpf)){
                    etClientCpf.setError("Insira um CPF válido");
                    etClientCpf.requestFocus();
                    return false;
                }
        }
        nextOs.setBackgroundColor(nextOs.getContext().getResources().getColor(R.color.autocom_blue));
        nextOs.setEnabled(true);
        return true;
    }
}
