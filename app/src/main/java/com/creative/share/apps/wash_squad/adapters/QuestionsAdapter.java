package com.creative.share.apps.wash_squad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.wash_squad.R;
import com.creative.share.apps.wash_squad.databinding.QuestionRowBinding;
import com.creative.share.apps.wash_squad.models.QuestionDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.MyHolder> {

    private List<QuestionDataModel.QuestionModel> questionModelList;
    private Context context;
    private String lang;

    public QuestionsAdapter(List<QuestionDataModel.QuestionModel> questionModelList, Context context) {
        this.questionModelList = questionModelList;
        this.context = context;
        Paper.init(context);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuestionRowBinding questionRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.question_row,parent,false);
        return new MyHolder(questionRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        QuestionDataModel.QuestionModel questionModel = questionModelList.get(position);
        holder.questionRowBinding.setLang(lang);
        holder.questionRowBinding.setQuestionModel(questionModel);





    }

    @Override
    public int getItemCount() {
        return questionModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private QuestionRowBinding questionRowBinding;
        public MyHolder(@NonNull QuestionRowBinding questionRowBinding) {
            super(questionRowBinding.getRoot());
            this.questionRowBinding = questionRowBinding;
        }
    }

}
