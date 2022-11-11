package com.prateek.newsbreeze.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prateek.newsbreeze.R;
import com.prateek.newsbreeze.databinding.SingleDayWiseListViewBinding;
import com.prateek.newsbreeze.interfaces.SavedArticleClickListener;
import com.prateek.newsbreeze.models.Article;

import java.util.List;
import java.util.Map;

public class NewsByDaysOldAdapter extends RecyclerView.Adapter<NewsByDaysOldAdapter.ViewHolder>{
    private Map<Long, List<Article>> articleListByDaysOld;
    private final SavedArticleClickListener listener;
    private final Context context;

    public NewsByDaysOldAdapter(Map<Long, List<Article>> articleListByDaysOld, SavedArticleClickListener listener, Context context) {
        this.articleListByDaysOld = articleListByDaysOld;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_day_wise_list_view,
                        parent,
                        false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int index = (position > articleListByDaysOld.keySet().size()) ? position-1 : position;
        Long daysOldNo = (long) articleListByDaysOld.keySet().toArray()[index];

        String daysOld = "";

        if(daysOldNo == 0)
            daysOld = "Today";
        else if(daysOldNo == 1)
            daysOld = "Yesterday";
        else
            daysOld = daysOldNo + " Days Ago";


        holder.binding.dayViewDayName.setText(daysOld);
        holder.binding.dayViewArticlesRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.binding.dayViewArticlesRecyclerView.setAdapter(new SavedNewsAdapter(context, listener, articleListByDaysOld.get(daysOldNo)));
    }

    @Override
    public int getItemCount() {
        return (articleListByDaysOld == null) ? 0 : articleListByDaysOld.keySet().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        com.prateek.newsbreeze.databinding.SingleDayWiseListViewBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SingleDayWiseListViewBinding.bind(itemView);
        }
    }

    public void updateData(Map<Long, List<Article>> articleListByDaysOld){
        this.articleListByDaysOld = articleListByDaysOld;
        notifyDataSetChanged();
    }
}
