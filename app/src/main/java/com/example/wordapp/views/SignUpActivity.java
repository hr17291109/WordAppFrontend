package com.example.wordapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.wordapp.R;
import com.example.wordapp.rest.AccountsRest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        AccountsRest accountsRest = retrofit.create(AccountsRest.class);

        Button buttonRegister = (Button) findViewById(R.id.registerButton);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editPassword = (EditText)findViewById(R.id.password);
                EditText editUsername = (EditText)findViewById(R.id.username);
                String password = editPassword.getText().toString().trim();
                String username = editUsername.getText().toString().trim();

                Call<Void> call = accountsRest.signup(username, password);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Intent intent = new Intent(SignUpActivity.this, WordListActivity.class);
                        intent.putExtra("account_id", username);
                        intent.putExtra("password", password);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        System.out.println("!!!");
                    }
                });

            }
        });

        Button btnGoToSignIn = findViewById(R.id.buttonGoToSignIn);
        btnGoToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

//        buttonRegister.setOnClickListener((View view) -> {
//                Intent intent = new Intent(SignUpActivity.this, SettingActivity.class);
//                startActivity(intent);
//        });
//
    }
}