package unipu.hr.unibooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    EditText email_reg;
    EditText password_reg;
    EditText password_reg2;
    Button signup_reg;
    ProgressBar progressBar_reg;
    Toolbar toolbar_reg;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        toolbar_reg = findViewById(R.id.toolbar_reg);
        email_reg = findViewById(R.id.etEmailRegistration);
        password_reg = findViewById(R.id.etPasswordRegistration);
        signup_reg = findViewById(R.id.btnSignUpRegistration);
        progressBar_reg = findViewById(R.id.progressBar_reg);
        password_reg2 = findViewById(R.id.etPasswordRegistration2);

        firebaseAuth = FirebaseAuth.getInstance();

        signup_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email_reg.getText().toString().isEmpty() || password_reg.getText().toString().isEmpty()){
                    Toast.makeText(RegistrationActivity.this, "Sva polja moraju biti popunjena!"
                            , Toast.LENGTH_LONG).show();
                } else{
                    if (password_reg.getText().toString().equals(password_reg2.getText().toString())) {
                        progressBar_reg.setVisibility(View.VISIBLE);
                        firebaseAuth.createUserWithEmailAndPassword(email_reg.getText().toString(), password_reg.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar_reg.setVisibility(View.GONE);
                                        if (task.isSuccessful()) {
                                            Toast.makeText(RegistrationActivity.this, "Registracija uspješna!"
                                                    , Toast.LENGTH_LONG).show();
                                            email_reg.setText("");
                                            password_reg.setText("");
                                            startActivity(new Intent(RegistrationActivity.this, ProfileActivity.class));
                                        } else {
                                            Toast.makeText(RegistrationActivity.this, task.getException().getMessage()
                                                    , Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Lozinke ne odgovaraju! Pokušajte ponovno."
                                , Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
