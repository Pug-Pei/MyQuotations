package co.websarva.wings.android.myquotations.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import co.websarva.wings.android.myquotations.R;
import co.websarva.wings.android.myquotations.persistence.QuotationRepository;
import co.websarva.wings.android.myquotations.quotations.Quotations;
import co.websarva.wings.android.myquotations.util.MyApplication;
import co.websarva.wings.android.myquotations.util.MyFragment;

public class QuotationRecyclerAdapter extends RecyclerView.Adapter<QuotationRecyclerAdapter.QuotationViewHolder>{

    private static final String TAG = "QuotationRecyclerAdap";

    private ArrayList<Quotations> arrayListQuotations = new ArrayList<>();
    private OnQuotationListener mOnQuotationListener;
    private boolean mIsDeleteMode;


    //アダプターのコンストラクタ
    public QuotationRecyclerAdapter(ArrayList<Quotations> arrayListQuotations,OnQuotationListener mOnQuotationListener,boolean mIsDeleteMode) {
        this.arrayListQuotations = arrayListQuotations;
        this.mOnQuotationListener = mOnQuotationListener;
        this.mIsDeleteMode = mIsDeleteMode;
    }

    @NonNull
    @Override
    public QuotationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int mLayoutId = R.layout.layout_quotations;

        if (mIsDeleteMode){
            mLayoutId = R.layout.layout_quotations_delete_mode;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new QuotationViewHolder(view,mOnQuotationListener);
    }

    @Override
    public void onBindViewHolder(@NonNull QuotationViewHolder holder, int position) {


        //削除モードのときはnullではないようにする。
        if (!mIsDeleteMode){
            holder.mNotificationCheckBox.setOnCheckedChangeListener(null);
        }

        //レポジトリー
        QuotationRepository mQuotationRepository = new QuotationRepository(MyApplication.getInstance());
        QuotationRepository mQuotationAsyncRepository = new QuotationRepository(MyFragment.getInstance());

        try {
            holder.content.setText(arrayListQuotations.get(position).getContent());
            //各ホルダーのチェックボックスのON/OFFをチェックする
            //ONの場合、インスタンスをarrayListNotificationに追加する
            //OFFの場合、インスタンスをarrayListNotificationから削除する
            if(arrayListQuotations.get(position).getCheckBox().equals("check_on")){
                Log.d(TAG,"CheckBoxがcheck_onのインスタンス："+arrayListQuotations.get(position).toString());
                holder.mNotificationCheckBox.setChecked(true);

            }else{
                Log.d(TAG,"CheckBoxがcheck_offのインスタンス："+arrayListQuotations.get(position).toString());
                holder.mNotificationCheckBox.setChecked(false);
            }

            //チェックボックスのON・OFFに連動し、インスタンス中の"CheckBox"の中身を更新する。
                holder.mNotificationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (!mIsDeleteMode){
                            Quotations mCheckBoxQuotation = arrayListQuotations.get(position);
                            Log.d(TAG,"タップされた✓ボックスのposition:   "+position);
                            holder.mNotificationCheckBox.setChecked(isChecked);

                            if (isChecked){
                                mCheckBoxQuotation.setCheckBox("check_on");
                                Log.d(TAG,"check_onになったインスタンス："+ "position："+ position +"   "+arrayListQuotations.get(position).toString());
                            }else{
                                mCheckBoxQuotation.setCheckBox("check_off");
                                Log.d(TAG,"check_offになったインスタンス："+ "position："+ position +"   "+arrayListQuotations.get(position).toString());
                            }
                            mQuotationRepository.updateQuotationTask(mCheckBoxQuotation);
                            mQuotationAsyncRepository.getWorkManagerInfo();
                        }else{

                        }

                    }
                });

        } catch (NullPointerException e) {
            Log.e(TAG, "onBindViewHolder: Null Pointer: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return arrayListQuotations.size();
    }


    public class QuotationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView content;
        OnQuotationListener mOnQuotationListener;
        CheckBox mNotificationCheckBox;

        public QuotationViewHolder(@NonNull View itemView,OnQuotationListener onQuotationListener) {
            super(itemView);
            content = itemView.findViewById(R.id.tv_quotationlist_content);
            mOnQuotationListener = onQuotationListener;
            mNotificationCheckBox = itemView.findViewById(R.id.checkBox);

/*            mNotificationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (!mIsDeleteMode){
                        Quotations mCheckBoxQuotation = arrayListQuotations.get(position);
                        Log.d(TAG,"タップされた✓ボックスのposition:   "+position);

                        if (isChecked){
                            mCheckBoxQuotation.setCheckBox("check_on");
                            Log.d(TAG,"check_onになったインスタンス："+ "position："+ position +"   "+arrayListQuotations.get(position).toString());
                        }else{
                            mCheckBoxQuotation.setCheckBox("check_off");
                            Log.d(TAG,"check_offになったインスタンス："+ "position："+ position +"   "+arrayListQuotations.get(position).toString());
                        }
                        mQuotationRepository.updateQuotationTask(mCheckBoxQuotation);
                        mQuotationAsyncRepository.getWorkManagerInfo();
                    }else{

                    }
                }
            });*/




            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
                    mOnQuotationListener.onQuotationClick(view, getAdapterPosition());
            }
        }

    public interface OnQuotationListener {
        void onQuotationClick(View view , int position);
    }

}
