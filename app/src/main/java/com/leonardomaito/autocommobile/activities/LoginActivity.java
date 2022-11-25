package com.leonardomaito.autocommobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.leonardomaito.autocommobile.controllers.LoginController;


import autocommobile.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmailInput;
    private EditText etPasswordInput;
    private Button btLogin;
    private FirebaseAuth mAuth;
    private LoginController loginController = new LoginController();
    private Boolean newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Intent loginIntent = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(loginIntent);
            finish();

        } else

        mAuth = FirebaseAuth.getInstance();

        etEmailInput = findViewById(R.id.etEmailInput);
        etPasswordInput = findViewById(R.id.etPasswordInput);
        btLogin = findViewById(R.id.btLogin);
        mAuth = FirebaseAuth.getInstance();

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginController.checkAllFields(etEmailInput,etPasswordInput)){
                    loginVerification();
                }
            }
        });
    }

  public void loginVerification() {
            mAuth.signInWithEmailAndPassword(loginController.getEmail(),loginController.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent loginIntent = new Intent(LoginActivity.this, MenuActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "E-mail e/ou Senha Inválida !", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    public void register(View view) {
        Intent loginIntent = new Intent(this, RegisterActivity.class);
        startActivity(loginIntent);
        finish();
    }
}

