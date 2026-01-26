package com.example.wordapp.adapters; // ★ここが adapters になります

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordapp.R; // R（リソース）を使うために必要
import com.example.wordapp.models.Word; // Wordモデルをインポート

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private List<Word> wordList;
    private OnWordDeleteListener deleteListener;

    public interface OnWordDeleteListener {
        void onDeleteClick(Word word);
    }

    public WordAdapter(List<Word> wordList, OnWordDeleteListener listener) {
        this.wordList = wordList;
        this.deleteListener = listener;
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        public TextView textWord;
        public TextView textMeaning;
        public Button buttonDelete;

        public WordViewHolder(View view) {
            super(view);
            textWord = view.findViewById(R.id.textWord);
            textMeaning = view.findViewById(R.id.textMeaning);
            buttonDelete = view.findViewById(R.id.buttonDelete);
        }
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word word = wordList.get(position);
        holder.textWord.setText(word.getText());
        holder.textMeaning.setText(word.getMeaning());
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // リスナーを通じてActivityに「この単語を削除して！」と伝える
                deleteListener.onDeleteClick(word);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }
}