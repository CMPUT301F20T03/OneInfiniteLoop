package com.example.booksies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText rEmail,rPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize UI refs

        rEmail = (EditText) findViewById(R.id.uname_register);
        rPassword = (EditText) findViewById(R.id.pass_register);
        btnRegister = (Button) findViewById(R.id.register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= rEmail.getText().toString();
                String pass = rPassword.getText().toString();
                if(!email.contains("@") && !(pass.length()<6))
                {
                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("RegisterActivity", "createUserWithEmail:success");
                                        Toast.makeText(RegisterActivity.this, "Authentication successful.",
                                                Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("RegisterActivity", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }

                                    // ...
                                }
                            });

                }
                else{
                    Toast.makeText(RegisterActivity.this, "Enter valid email and password must be atleast 6 characters",
                            Toast.LENGTH_SHORT).show();

                }


            }
        });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);

    }
};
