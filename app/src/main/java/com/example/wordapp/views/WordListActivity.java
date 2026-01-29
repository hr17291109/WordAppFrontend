package com.example.wordapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.widget.Button;

import com.example.wordapp.R;
import com.example.wordapp.adapters.WordAdapter;
import com.example.wordapp.models.Account;
import com.example.wordapp.models.Word;
import com.example.wordapp.rest.AccountsRest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class WordListActivity extends AppCompatActivity {

    private String accountId;
    private String password;

    private AccountsRest accountsRest;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_word_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        accountId = intent.getStringExtra("account_id");
        password = intent.getStringExtra("password");

        if (accountId == null || password == null) {
            Toast.makeText(this, "認証情報がありません", Toast.LENGTH_SHORT).show();
            return;
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        accountsRest = retrofit.create(AccountsRest.class);

        FloatingActionButton fab = findViewById(R.id.fabAddWord);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddWordDialog();
            }
        });
        loadWords();

        Button btnChangePass = findViewById(R.id.buttonChangePassword);
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WordListActivity.this, SettingActivity.class);
                // 誰のパスワードを変えるのか伝えるためにIDを渡す
                intent.putExtra("account_id", accountId);
                startActivity(intent);
            }
        });

        Button logoutButton = findViewById(R.id.buttonLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. SignUp画面へのIntentを作成
                // Intent intent = new Intent(WordListActivity.this, SignUpActivity.class);
                Intent intent = new Intent(WordListActivity.this, SignInActivity.class);

                // 2. ★重要: これまでの画面履歴を消去するフラグ
                // これを入れないと、ログアウト後に「戻るボタン」でまたリストが見れてしまいます
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                // 3. 画面遷移と、今の画面の終了
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadWords() {
        Call<Account> call = accountsRest.getAccountInfo(accountId, password);
        call.enqueue(new Callback<Account>() {

            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Account account = response.body();

                    // Accountクラスから単語Mapを取得
                    // getWords()がHashMap<String, String>を返す仕様
                    HashMap<String, String> wordMap = account.getWords();

                    // 5. HashMap -> List<Word> に変換
                    List<Word> wordList = new ArrayList<>();
                    int idCounter = 1;

                    // マップの中身をループしてWordオブジェクトを作成
                    for (Map.Entry<String, String> entry : wordMap.entrySet()) {
                        String english = entry.getKey();
                        String japanese = entry.getValue();

                        // IDは連番を仮定、Keyを単語、Valueを意味としてセット
                        wordList.add(new Word(idCounter++, english, japanese));
                    }

                    // 6. Adapterにデータをセットして表示
                    WordAdapter adapter = new WordAdapter(wordList, new WordAdapter.OnWordDeleteListener() {
                        @Override
                        public void onDeleteClick(Word word) {
                            // 削除確認ダイアログなどを出しても良いですが、ここでは直接削除します
                            deleteWord(word.getText());
                        }
                    });
                    // WordAdapter adapter = new WordAdapter(wordList);
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(WordListActivity.this, "データ取得失敗: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(WordListActivity.this, "通信エラー: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
    // ★単語追加用のダイアログを表示
    private void showAddWordDialog() {
        // ダイアログ内のレイアウトを動的に作成（XMLファイルを作っても良いですが簡単のためコードで）
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_word, null);
        // ※注意: 別途 dialog_add_word.xml を作る必要があります（後述）

        final EditText editEnglish = dialogView.findViewById(R.id.editEnglish);
        final EditText editJapanese = dialogView.findViewById(R.id.editJapanese);

        new AlertDialog.Builder(this)
                .setTitle("Add Word")
                .setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String eng = editEnglish.getText().toString();
                        String jpn = editJapanese.getText().toString();
                        addWord(eng, jpn);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ★APIを呼んで単語を追加
    private void addWord(String english, String japanese) {
        Call<Void> call = accountsRest.addWord(accountId, password, english, japanese);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(WordListActivity.this, "追加しました", Toast.LENGTH_SHORT).show();
                    loadWords(); // リストを再読み込み
                } else {
                    Toast.makeText(WordListActivity.this, "追加失敗", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(WordListActivity.this, "エラー: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ★APIを呼んで単語を削除
    private void deleteWord(String englishWord) {
        Call<Void> call = accountsRest.deleteWord(accountId, password, englishWord);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(WordListActivity.this, "削除しました", Toast.LENGTH_SHORT).show();
                    loadWords(); // リストを再読み込み
                } else {
                    Toast.makeText(WordListActivity.this, "削除失敗", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(WordListActivity.this, "エラー", Toast.LENGTH_SHORT).show();
            }
        });
    }

}