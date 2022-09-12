package com.leonardomaito.autocommobile.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.leonardomaito.autocommobile.fragments.AliasFragment;


import autocommobile.R;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private EditText etEmailInput;
    private EditText etPasswordInput;
    private Spinner spEmpresa;
    private Button btLogin;
    private FirebaseAuth mAuth;

    DialogFragment aliasFragment = new AliasFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmailInput = findViewById(R.id.etEmailInput);
        etPasswordInput = findViewById(R.id.etPasswordInput);
        spEmpresa = findViewById(R.id.spEmpresa);
        btLogin = findViewById(R.id.btLogin);
        mAuth = FirebaseAuth.getInstance();

        }

    public void openAliasConfig(View view) {
        aliasFragment.show(getSupportFragmentManager(), "Notice");
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
