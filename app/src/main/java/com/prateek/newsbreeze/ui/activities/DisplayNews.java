package com.prateek.newsbreeze.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.prateek.newsbreeze.R;
import com.prateek.newsbreeze.constants.PassNews;
import com.prateek.newsbreeze.databinding.ActivityDisplayNewsBinding;
import com.prateek.newsbreeze.models.Article;
import com.prateek.newsbreeze.models.Source;
import com.prateek.newsbreeze.repository.SavedArticlesRepository;
import com.prateek.newsbreeze.room.ArticlesDao;
import com.prateek.newsbreeze.room.ArticlesDatabase;
import com.prateek.newsbreeze.util.DateUtils;
import com.prateek.newsbreeze.viewmodel.SavedArticlesViewModel;
import com.prateek.newsbreeze.viewmodel.SavedArticlesViewModelFactory;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisplayNews extends BaseActivity{
    private SavedArticlesViewModel savedArticlesViewModel;
    private com.prateek.newsbreeze.databinding.ActivityDisplayNewsBinding binding;
    private boolean isAlreadySaved = false;
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Make a Article Object with data received from intent
        article = new Article(
                new Source(getIntent().getStringExtra(PassNews.KEY_AUTHOR_ORGANIZATION)),
                getIntent().getStringExtra(PassNews.KEY_AUTHOR_NAME),
                getIntent().getStringExtra(PassNews.KEY_NEWS_TITLE),
                getIntent().getStringExtra(PassNews.KEY_DESCRIPTION),
                getIntent().getStringExtra(PassNews.KEY_NEWS_IMAGE_URL),
                new Date(getIntent().getStringExtra(PassNews.KEY_NEWS_DATE)),
                getIntent().getStringExtra(PassNews.KEY_NEWS_CONTENT)
        );

        isAlreadySaved = getIntent().getBooleanExtra(PassNews.KEY_IS_ALREADY_SAVED, false);
        if(!isAlreadySaved){
            try {
                this.isAlreadySaved = article.isAlreadySaved;
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        if(isAlreadySaved){
            binding.viewNewsSaveButton.setText(R.string.delete_button_text);
            article.id = getIntent().getIntExtra(PassNews.KEY_ID, Integer.MAX_VALUE);
        }

        // set unknown author on author name section or display name if exists
        if(article.author == null || article.author.length() < 1){
            binding.viewNewsAuthorName.setText(R.string.display_news_unknown_author);
        }
        else{
            binding.viewNewsAuthorName.setText(article.author);
        }
        binding.viewNewsContent.setText(article.content);
        binding.viewNewsOrganizationName.setText(article.source.name);
        binding.viewNewsTitle.setText(article.title);
        binding.viewNewsDate.setText(DateUtils.toDisplayDate(article.publishedAt));

        // loading the main image
        try{
            Picasso.get()
                    .load(article.urlToImage)
                    .fit()
                    .error(R.drawable.error)
                    .into(binding.viewNewsMainImage);
        }catch (Exception e){
            Toast.makeText(this, "Error Loading Image: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        ArticlesDao articlesDao = ArticlesDatabase.getInstance(this.getApplicationContext()).dao();
        SavedArticlesRepository savedArticlesRepository = new SavedArticlesRepository(articlesDao);
        SavedArticlesViewModelFactory savedArticlesViewModelFactory = new SavedArticlesViewModelFactory(savedArticlesRepository);
        savedArticlesViewModel = new ViewModelProvider(
                this,
                savedArticlesViewModelFactory
        ).get(SavedArticlesViewModel.class);

        binding.viewNewsSaveButton.setOnClickListener(v-> saveDeleteButtonClicked());

    }


    private void saveDeleteButtonClicked(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        if(isAlreadySaved){
            //now delete
            executor.execute(()->{
                savedArticlesViewModel.deleteFromDb(article.id);
                handler.post(()->{
                    Toast.makeText(DisplayNews.this, "Article Deleted from Saved Articles", Toast.LENGTH_SHORT).show();
                    binding.viewNewsSaveButton.setText(R.string.save_button_text);
                    isAlreadySaved = false;
                });
            });
        }else{
            //now save
            executor.execute(()->{
                savedArticlesViewModel.insertInDb(article);
                handler.post(()->{
                    Toast.makeText(DisplayNews.this, "Article Saved in Saved Articles", Toast.LENGTH_SHORT).show();
                    binding.viewNewsSaveButton.setText(R.string.delete_button_text);
                    isAlreadySaved = true;
                });
            });
        }
    }
}