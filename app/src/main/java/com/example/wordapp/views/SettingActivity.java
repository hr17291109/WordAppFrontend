package com.example.wordapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String str_account_id = getIntent().getStringExtra("account_id");
        ((TextView) findViewById(R.id.username)).setText(str_account_id);

        //通信の初期化
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        AccountsRest accountsRest = retrofit.create(AccountsRest.class);

        // listボタンを押してlist画面に遷移
        Button button_list = (Button) findViewById(R.id.listButton);
        button_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SettingActivity.this, WordListActivity.class);
//                intent.putExtra("account_id", str_account_id);
//                EditText passwordField = findViewById(R.id.pass);
//                String password = passwordField.getText().toString().trim();
//                intent.putExtra("password", password);
//                startActivity(intent);
                finish();
            }
        });

        // changeボタンを押してメッセージ表示
        Button button_change = (Button) findViewById(R.id.changeButton);

        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText pw = (EditText) findViewById(R.id.password);
                String oldpass = pw.getText().toString().trim();
                EditText newpw = (EditText) findViewById(R.id.newpass);
                String newpass = newpw.getText().toString().trim();


                Call<Void> call = accountsRest.changePW(str_account_id, newpass, oldpass);
                call.enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            ((TextView) findViewById(R.id.testView_error2)).setText("パスワードが変更されました");
                        } else {
                            ((TextView) findViewById(R.id.testView_error2)).setText("パスワードが違います");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        ((TextView) findViewById(R.id.testView_error2)).setText("Failure");
                    }
                });


            }


        });
    }
}