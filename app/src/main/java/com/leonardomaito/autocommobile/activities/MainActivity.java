package com.leonardomaito.autocommobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;


import autocommobile.R;

public class MainActivity extends AppCompatActivity {

    private EditText etEmailInput;
    private EditText etPasswordInput;
    private Button btLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmailInput = findViewById(R.id.etEmailInput);
        etPasswordInput = findViewById(R.id.etPasswordInput);
        btLogin = findViewById(R.id.btLogin);
        mAuth = FirebaseAuth.getInstance();

        }

    public void login(View view){
        Intent loginIntent = new Intent(this, MenuActivity.class);
        startActivity(loginIntent);
    }

  /*  public void loginVerification(View view) {
        String email = etEmailInput.getText().toString();
        String password = etPasswordInput.getText().toString();

        if(email.isEmpty()){
            etEmailInput.setError("E-Mail não pode ser vazio");
            etEmailInput.requestFocus();
        }
        else if(password.isEmpty()){
            etPasswordInput.setError("Senha não pode ser vazia");
            etPasswordInput.requestFocus();
        }
        else{

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        login();
                    }
                }
            });
        }
    }*/
}
