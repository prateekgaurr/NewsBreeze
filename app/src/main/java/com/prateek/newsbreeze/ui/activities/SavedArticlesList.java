


package com.prateek.newsbreeze.ui.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.prateek.newsbreeze.constants.PassNews;
import com.prateek.newsbreeze.databinding.ActivitySavedArticlesBinding;
import com.prateek.newsbreeze.interfaces.SavedArticleClickListener;
import com.prateek.newsbreeze.models.Article;
import com.prateek.newsbreeze.repository.SavedArticlesRepository;
import com.prateek.newsbreeze.room.ArticlesDao;
import com.prateek.newsbreeze.room.ArticlesDatabase;
import com.prateek.newsbreeze.ui.adapters.NewsByDaysOldAdapter;
import com.prateek.newsbreeze.util.DateUtils;
import com.prateek.newsbreeze.util.MyLogger;
import com.prateek.newsbreeze.viewmodel.SavedArticlesViewModel;
import com.prateek.newsbreeze.viewmodel.SavedArticlesViewModelFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class SavedArticlesList extends BaseActivity implements SavedArticleClickListener {
    private SavedArticlesViewModel viewModel;
    private Map<Long, List<Article>> articlesListGrouped;
    private Date todayDate;
    private List<Article> articles = new ArrayList<>();
    private NewsByDaysOldAdapter adapter;
    public static final String TAG = "Saved Article List Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.prateek.newsbreeze.databinding.ActivitySavedArticlesBinding binding
                = ActivitySavedArticlesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(v->{
            startActivity(new Intent(SavedArticlesList.this, MainActivity.class));
        });


        // show a snack bar
        final Snackbar snackBar = Snackbar.make(binding.getRoot(), "Long Hold the Article to Delete", Snackbar.LENGTH_LONG);
        snackBar.setAction("OK", v -> snackBar.dismiss());
        snackBar.show();

        // initializing recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new NewsByDaysOldAdapter(articlesListGrouped, this, this);
        binding.recyclerViewForDaysOldView.setLayoutManager(linearLayoutManager);
        binding.recyclerViewForDaysOldView.setAdapter(adapter);
        binding.recyclerViewForDaysOldView.setNestedScrollingEnabled(false);

        // initializing dao, repository, and viewmodel from viewmodel factory
        todayDate = new Date();
        ArticlesDao dao = ArticlesDatabase.getInstance(this.getApplicationContext()).dao();
        SavedArticlesRepository savedArticlesRepository = new SavedArticlesRepository(dao);
        SavedArticlesViewModelFactory savedArticlesViewModelFactory = new SavedArticlesViewModelFactory(savedArticlesRepository);
        viewModel = new ViewModelProvider(this,
                savedArticlesViewModelFactory)
                .get(SavedArticlesViewModel.class);

        // regularly observe all articles
        viewModel.getAllArticles()
                .observe(this, articles -> {
                    this.articles = articles;
                    if(articles == null || articles.size() < 1)
                        binding.emptyListParent.setVisibility(View.VISIBLE);
                    else{
                        adapter.updateData(groupArticlesAgain());
                        binding.emptyListParent.setVisibility(View.INVISIBLE);
                    }
        });



        // when typing on search edit text
        binding.searchBox2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence == null || charSequence.toString().length() < 1) {
                    // no text in search box, bring all data
                    MyLogger.d(TAG, "going to search from all data");
                    viewModel.getAllArticles();
                    adapter.updateData(groupArticlesAgain());
                }
                else{
                    viewModel.getFilteredArticles(charSequence.toString()).observe(SavedArticlesList.this, articles -> {
                        SavedArticlesList.this.articles = articles;
                        adapter.updateData(groupArticlesAgain());
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                MyLogger.d(TAG, "After Text changed");
                adapter.updateData(groupArticlesAgain());
            }
        });

    }

    private Map<Long, List<Article>> groupArticlesAgain() {
                return articlesListGrouped = articles
                        .stream()
                        .collect(Collectors
                                .groupingBy(
                                article -> DateUtils.calculateNoOfDays(article.publishedAt, todayDate)));
    }

    @Override
    public void onSavedArticleClicked(Article article) {
        // open the article to read
        Intent intent = new Intent(SavedArticlesList.this, DisplayNews.class);
        intent.putExtra(PassNews.KEY_AUTHOR_NAME, article.author);
        intent.putExtra(PassNews.KEY_NEWS_TITLE, article.title);
        intent.putExtra(PassNews.KEY_NEWS_CONTENT, article.description+"  "+article.content);
        intent.putExtra(PassNews.KEY_NEWS_DATE, article.publishedAt.toString());
        intent.putExtra(PassNews.KEY_DESCRIPTION, article.description);
        intent.putExtra(PassNews.KEY_IS_ALREADY_SAVED, true);
        intent.putExtra(PassNews.KEY_ID, article.id);
//        intent.putExtra(PassNews.KEY_LISTENER, (NewsAdapterListener)this);
        if(article.source !=null && article.source.name != null)
            intent.putExtra(PassNews.KEY_AUTHOR_ORGANIZATION, article.source.name);
        if(article.urlToImage != null)
            intent.putExtra(PassNews.KEY_NEWS_IMAGE_URL, article.urlToImage);
        startActivity(intent);

    }

    @Override
    public void onSavedArticleLongClick(int articleId) {
        // delete article from db on long click
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(()->{
            viewModel.deleteFromDb(articleId);
            handler.post(()->{
                Toast.makeText(this, "Article Deleted from DB", Toast.LENGTH_SHORT).show();
            });
        });
    }
}