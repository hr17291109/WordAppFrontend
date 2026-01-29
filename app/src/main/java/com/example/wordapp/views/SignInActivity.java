package com.example.wordapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wordapp.R;
import com.example.wordapp.models.Account;
import com.example.wordapp.rest.AccountsRest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Retrofit設定
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        AccountsRest accountsRest = retrofit.create(AccountsRest.class);

        // Loginボタンの処理
        Button btnLogin = findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editUser = findViewById(R.id.username);
                EditText editPass = findViewById(R.id.password);
                String id = editUser.getText().toString().trim();
                String pass = editPass.getText().toString().trim();

                // アカウント情報取得APIをログイン判定に使用
                Call<Account> call = accountsRest.getAccountInfo(id, pass);
                call.enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // ログイン成功 -> リスト画面へ
                            Intent intent = new Intent(SignInActivity.this, WordListActivity.class);
                            intent.putExtra("account_id", id);
                            intent.putExtra("password", pass);
                            startActivity(intent);
                            finish(); // ログイン画面を閉じる
                        } else {
                            Toast.makeText(SignInActivity.this, "IDまたはパスワードが違います", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        Toast.makeText(SignInActivity.this, "通信エラー", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Sign Up画面へ移動するボタン
        Button btnGoToSignUp = findViewById(R.id.buttonGoToSignUp);
        btnGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
