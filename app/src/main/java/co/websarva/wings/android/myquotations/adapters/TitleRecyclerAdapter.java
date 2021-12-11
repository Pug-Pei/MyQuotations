package co.websarva.wings.android.myquotations.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.websarva.wings.android.myquotations.R;
import co.websarva.wings.android.myquotations.persistence.QuotationRepository;
import co.websarva.wings.android.myquotations.quotations.Quotations;


public class TitleRecyclerAdapter extends RecyclerView.Adapter<TitleRecyclerAdapter.ViewHolder>{

    private static final String TAG = "QuotationTitleRecycler";


    private ArrayList<Quotations> arrayListTitle = new ArrayList<>();
    private OnTitleListener mOnTitleListener;
    private boolean mIsDeleteMode;

    public TitleRecyclerAdapter(ArrayList<Quotations> arrayListQuotation, OnTitleListener mOnTitleListener, boolean mIsDeleteMode) {
        this.arrayListTitle = arrayListQuotation;
        this.mOnTitleListener = mOnTitleListener;
        this.mIsDeleteMode = mIsDeleteMode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        int mLayoutId = R.layout.layout_quotation_title;

        if(mIsDeleteMode){
            mLayoutId =R.layout.layout_quotation_title_delete_mode;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId,parent,false);
        return new ViewHolder(view, mOnTitleListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(arrayListTitle.get(position).getTitle());
        holder.authorName.setText(arrayListTitle.get(position).getAuthorName());
        holder.timeStamp.setText(arrayListTitle.get(position).getTimeStamp());

    }

    @Override
    public int getItemCount() {
        return arrayListTitle.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title, authorName, timeStamp;
        OnTitleListener mOnTitleListener;

        public ViewHolder(@NonNull View itemView, OnTitleListener onQuotationTitleListener) {
            super(itemView);

            title = itemView.findViewById(R.id.list_title);
            authorName= itemView.findViewById(R.id.list_author);
            timeStamp = itemView.findViewById(R.id.list_timeStamp);
            mOnTitleListener = onQuotationTitleListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Log.d(TAG,"onClick:"+ getAdapterPosition());
            mOnTitleListener.onTitleClick(getAdapterPosition());
        }
    }

    //TitleListActivityが実装
    public interface OnTitleListener {
        void onTitleClick(int position);
    }
}


