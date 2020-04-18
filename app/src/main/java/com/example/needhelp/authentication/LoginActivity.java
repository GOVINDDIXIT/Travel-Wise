package com.example.needhelp.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.needhelp.R;
import com.example.needhelp.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog loading;
    private FirebaseAuth mauth;
    private Button signin;
    private EditText uuser, ppaswrd;
    private TextView newuser, reset;
    private ImageView close;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signin = findViewById(R.id.signin_btn);
        uuser = findViewById(R.id.email_input_log);
        ppaswrd = findViewById(R.id.pass_input_log);
        newuser = findViewById(R.id.need_account);
        mauth = FirebaseAuth.getInstance();
        reset = findViewById(R.id.reset);
        close = findViewById(R.id.close);

        Toolbar tlbr = findViewById(R.id.toolbar_log);
        setSupportActionBar(tlbr);


        message = getIntent().getStringExtra("name");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPassword.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin();
            }
        });
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void signin() {

        String Email = uuser.getText().toString();
        String pass = ppaswrd.getText().toString();

        loading = new ProgressDialog(this);
        loading.setTitle("Logging In...");
        loading.setMessage("Please Wait .....");


        if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(LoginActivity.this, "All Fields Are Required...", Toast.LENGTH_SHORT).show();
        } else {
            loading.show();

            mauth.signInWithEmailAndPassword(Email, pass).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).isEmailVerified()) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("namee", message);
                                    startActivity(intent);
                                    loading.dismiss();
                                    finish();
                                } else {
                                    loading.dismiss();
                                    Toast.makeText(LoginActivity.this, "Please Verify Your email first", Toast.LENGTH_SHORT).show();

                                }
                            } else {

                                Toast.makeText(LoginActivity.this, "" + Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        }
                    });
        }
    }
}
