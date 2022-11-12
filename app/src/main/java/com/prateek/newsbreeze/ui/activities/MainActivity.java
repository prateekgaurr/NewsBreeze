package com.prateek.newsbreeze.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.prateek.newsbreeze.R;
import com.prateek.newsbreeze.constants.PassNews;
import com.prateek.newsbreeze.constants.SortingTypes;
import com.prateek.newsbreeze.databinding.ActivityMainBinding;
import com.prateek.newsbreeze.interfaces.NewsAdapterListener;
import com.prateek.newsbreeze.models.Article;
import com.prateek.newsbreeze.repository.NewsRepository;
import com.prateek.newsbreeze.repository.SavedArticlesRepository;
import com.prateek.newsbreeze.retrofit.NewsService;
import com.prateek.newsbreeze.retrofit.RetrofitHelper;
import com.prateek.newsbreeze.room.ArticlesDao;
import com.prateek.newsbreeze.room.ArticlesDatabase;
import com.prateek.newsbreeze.ui.adapters.AllNewsAdapter;
import com.prateek.newsbreeze.util.MyLogger;
import com.prateek.newsbreeze.util.SortArticles;
import com.prateek.newsbreeze.viewmodel.NewsViewModel;
import com.prateek.newsbreeze.viewmodel.NewsViewModelFactory;
import com.prateek.newsbreeze.viewmodel.SavedArticlesViewModel;
import com.prateek.newsbreeze.viewmodel.SavedArticlesViewModelFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity implements NewsAdapterListener {
    private ActivityMainBinding binding;
    private List<Article> articleList;
    private SavedArticlesViewModel savedArticlesViewModel;
    private SortingTypes sortingType = SortingTypes.SORT_BY_DATE;
    private AllNewsAdapter adapter;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final String TAG = getClass().getSimpleName();

        NewsService newsService = RetrofitHelper.getInstance().getApi();
        NewsRepository newsRepository = new NewsRepository(newsService);
        NewsViewModelFactory newsViewModelFactory = new NewsViewModelFactory(newsRepository);
        NewsViewModel viewModel = new ViewModelProvider(
                this,
                newsViewModelFactory
        ).get(NewsViewModel.class);

        MyLogger.d(TAG, "Viewmodel Initialized");

        adapter = new AllNewsAdapter(articleList, this);
        binding.mainNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.mainNewsRecyclerView.setAdapter(adapter);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getDrawable(R.drawable.my_divider));
        binding.mainNewsRecyclerView.addItemDecoration(decoration);

        binding.savedIcon.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SavedArticlesList.class)));

        viewModel.getNews().observe(this, newsCallResponse -> {
            MyLogger.d(TAG, "API RESPONSE RECEIVED");
            if(newsCallResponse != null) {
                MyLogger.d(TAG, newsCallResponse.status + " and no of entries are" + newsCallResponse.totalResults);
                articleList = newsCallResponse.articles;
                    if(newsCallResponse.status.equals("error")){
                        binding.errorShortMessage.setText(newsCallResponse.code);
                        binding.errorDescription.setText(newsCallResponse.message);
                    }
                    updateData();
            }
            else {
                MyLogger.d(TAG, "currently null");
            }
        });

        viewModel.getRequestStatus().observe(this, requestStatusTypes -> {
            MyLogger.d(TAG, "REQUEST STATUS CHANGED "+requestStatusTypes.toString());
            switch(requestStatusTypes){
                case PROCESSING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;

                case FAILED:
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    binding.errorOrListEmptyParent.setVisibility(View.VISIBLE);
                    binding.mainNewsRecyclerView.setVisibility(View.INVISIBLE);
                    break;

                case COMPLETED:
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    binding.mainNewsRecyclerView.setVisibility(View.VISIBLE);
                    binding.errorOrListEmptyParent.setVisibility(View.INVISIBLE);
                    break;

                case EMPTY:
                    binding.mainNewsRecyclerView.setVisibility(View.INVISIBLE);
                    binding.errorOrListEmptyParent.setVisibility(View.VISIBLE);
                    binding.errorShortMessage.setText(R.string.no_results_found_for_query);
                    binding.errorDescription.setText(R.string.no_results_long_message);
                    break;
            }
        });


        binding.searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence == null || charSequence.toString().length()<1){
                    viewModel.getNews();
                }
                else{
                    String text = charSequence.toString();
                    viewModel.getNews(text.toLowerCase().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ArticlesDao articlesDao = ArticlesDatabase.getInstance(this.getApplicationContext()).dao();
        SavedArticlesRepository savedArticlesRepository = new SavedArticlesRepository(articlesDao);
        SavedArticlesViewModelFactory savedArticlesViewModelFactory = new SavedArticlesViewModelFactory(savedArticlesRepository);
        savedArticlesViewModel = new ViewModelProvider(
                this,
                savedArticlesViewModelFactory
        ).get(SavedArticlesViewModel.class);

        binding.sortByIcon.setOnClickListener(v->{
            if(sortingType == SortingTypes.SORT_BY_PUBLISHER){
                sortingType = SortingTypes.SORT_BY_DATE;
                Toast.makeText(this, "Sorting by Date", Toast.LENGTH_SHORT).show();
            }else{
                sortingType = SortingTypes.SORT_BY_PUBLISHER;
                Toast.makeText(this, "Sorting By Publisher", Toast.LENGTH_SHORT).show();
            }
            updateData();
        });
    }

    private void updateData(){
        adapter.setArticlesToBeDisplayed(SortArticles.sortArticles(articleList, sortingType));
    }

    @Override
    public void onNewsItemClicked(Article article) {
        Intent intent = new Intent(MainActivity.this, DisplayNews.class);
        intent.putExtra(PassNews.KEY_AUTHOR_NAME, article.author);
        intent.putExtra(PassNews.KEY_NEWS_TITLE, article.title);
        intent.putExtra(PassNews.KEY_NEWS_CONTENT, article.description+"  "+article.content);
        intent.putExtra(PassNews.KEY_NEWS_DATE, article.publishedAt.toString());
        intent.putExtra(PassNews.KEY_DESCRIPTION, article.description);
        intent.putExtra(PassNews.KEY_IS_ALREADY_SAVED, article.isAlreadySaved);
        if(article.source !=null && article.source.name != null)
            intent.putExtra(PassNews.KEY_AUTHOR_ORGANIZATION, article.source.name);
        if(article.urlToImage != null)
            intent.putExtra(PassNews.KEY_NEWS_IMAGE_URL, article.urlToImage);
        startActivity(intent);
    }

    @Override
    public void onNewsSaveButtonClicked(Article article) {
        if(article.isAlreadySaved){
            Toast.makeText(this, "Already Saved", Toast.LENGTH_SHORT).show();
        }else{
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(() -> {
                savedArticlesViewModel.insertInDb(article);
                article.isAlreadySaved = true;
                handler.post(() -> Toast.makeText(MainActivity.this, "Article Saved", Toast.LENGTH_SHORT).show());
            });
        }
    }
}