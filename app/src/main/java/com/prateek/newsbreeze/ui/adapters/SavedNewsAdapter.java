package com.prateek.newsbreeze.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prateek.newsbreeze.R;
import com.prateek.newsbreeze.databinding.SingleSavedNewsViewBinding;
import com.prateek.newsbreeze.interfaces.SavedArticleClickListener;
import com.prateek.newsbreeze.models.Article;
import com.prateek.newsbreeze.util.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsAdapter.SavedNewsViewHolder>{
    private final List<Article> articleList;
    private final SavedArticleClickListener listener;
    private final Context context;

    public SavedNewsAdapter(Context context, SavedArticleClickListener listener, List<Article> articleList) {
        this.articleList = articleList;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public SavedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_saved_news_view,
                        parent,
                        false);
        return new SavedNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedNewsViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.binding.singleSavedNewsTitle.setText(article.title);

        String date = DateUtils.toDisplayDate(article.publishedAt);
        String author = (article.author == null || article.author.length()<1) ? "Unknown Author" : article.author;
        holder.binding.singleSavedNewsDateAuthor.setText(date+" ※ "+author);

        //load image
        Picasso.get()
                .load(article.urlToImage)
                .fit()
                .error(R.drawable.error)
                .into(holder.binding.singleSavedNewsImage);

        holder.binding.singleSavedNewsHashtag.setText("#news");


    }

    @Override
    public int getItemCount() {
        return (articleList == null) ? 0 : articleList.size();
    }


    class SavedNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        com.prateek.newsbreeze.databinding.SingleSavedNewsViewBinding binding;
        public SavedNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SingleSavedNewsViewBinding.bind(itemView);
            binding.singleSavedNewsParent.setOnClickListener(this);
            binding.singleSavedNewsParent.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            Article article = articleList.get(position);
            listener.onSavedArticleClicked(article);
        }

        @Override
        public boolean onLongClick(View view) {
            int position = this.getAdapterPosition();
            Article article = articleList.get(position);
            listener.onSavedArticleLongClick(article.id);
            return true;
        }
    }
}
