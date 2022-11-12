package com.prateek.newsbreeze.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prateek.newsbreeze.R;
import com.prateek.newsbreeze.databinding.SingleNewsViewBinding;
import com.prateek.newsbreeze.interfaces.NewsAdapterListener;
import com.prateek.newsbreeze.models.Article;
import com.prateek.newsbreeze.util.DateUtils;
import com.prateek.newsbreeze.util.MyLogger;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllNewsAdapter extends RecyclerView.Adapter<AllNewsAdapter.NewsViewHolder>{
    private final NewsAdapterListener listener;
    protected List<Article> articleList;
    private final String TAG = "All News Adapter";

    public AllNewsAdapter(List<Article> list, NewsAdapterListener listener){
        this.articleList = list;
        this.listener = listener;
    }


    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(
                LayoutInflater.from(
                        parent.getContext())
                        .inflate(
                                R.layout.single_news_view,
                                parent,
                                false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.binding.newsHeading.setText(article.title);
        holder.binding.newsDescription.setText(article.description);
        String source = (article.source == null || article.source.name == null) ? "Unknown Source" : article.source.name;
        holder.binding.newsPusblishedDate.setText(DateUtils.toDisplayDate(article.publishedAt)+" by "+source);
        if(article.urlToImage != null) {
            try{
                Picasso.get().load(article.urlToImage)
                        .fit()
                        .error(R.drawable.error)
                        .into(holder.binding.newsMainImage);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else
            Picasso.get().load(R.drawable.error).fit().into(holder.binding.newsMainImage);
    }

    @Override
    public int getItemCount() {
        return (articleList == null) ? 0 : articleList.size();
    }

    public void setArticlesToBeDisplayed(List<Article> articles){
        MyLogger.d(TAG, "Articles List Updated in Adapter");
        this.articleList = articles;
        notifyDataSetChanged();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final SingleNewsViewBinding binding;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SingleNewsViewBinding.bind(itemView);
            binding.getRoot().setOnClickListener(this);
            binding.newsReadButton.setOnClickListener(this);
            binding.newsSaveButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = this.getAdapterPosition();
            switch (v.getId()){
                case R.id.news_read_button:

                case R.id.root_single_news_view_main:
                    listener.onNewsItemClicked(articleList.get(index));
                    break;

                case R.id.news_save_button:
                    listener.onNewsSaveButtonClicked(articleList.get(index));
                    articleList.get(index).isAlreadySaved = true;
                    binding.newsSaveButton.setText(R.string.saved_button_text);
                    break;

            }
        }
    }
}
