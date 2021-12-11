package co.websarva.wings.android.myquotations.async;

import android.os.AsyncTask;
import android.util.Log;

import co.websarva.wings.android.myquotations.persistence.QuotationDao;
import co.websarva.wings.android.myquotations.persistence.QuotationRepository;
import co.websarva.wings.android.myquotations.quotations.Quotations;
import co.websarva.wings.android.myquotations.util.MyApplication;

public class MakeSortIdAscAsyncTask extends AsyncTask<Quotations, Void, Quotations> {

    private static final String TAG = "makeSortIdAscAsyncTask";
    private MyApplication myApplication = new MyApplication();
    private QuotationDao mQuotationDao;
    private QuotationRepository mQuotationRepository = new QuotationRepository(myApplication);


    public MakeSortIdAscAsyncTask(QuotationDao dao) {
        mQuotationDao = dao;
    }


    protected Quotations doInBackground(Quotations... quotations) {
        //並び替え変数持ったインスタンスをデータベースから取得
        return mQuotationDao.getSortTitleList();
            }

    @Override
    protected void onPostExecute(Quotations quotations) {

        if(quotations == null){

            //並び替えボタンをタップした時に、並び替え変数を持ったインスタンスを生成する
            Quotations mInsertSortQuotation = new Quotations(
                    "sortTitle",
                    "authorNothing!",
                    "timeNothing!",
                    "id_order_by_ASC",
                    "checkNothing!");

            Log.d(TAG,"バックグラウンドでswitchの条件値設定インスタンス生成："+mInsertSortQuotation.toString());
            mQuotationRepository.insertQuotationTask(mInsertSortQuotation);

        }else{

            //既に並び替えインスタンスが存在しているなら、content（並び替え識別変数）を
            //ID昇順（id_order_by_ASC）で上書きする
            Quotations updateQuotation = new Quotations(
                    "sortTitle",
                    "authorNothing!",
                    "timeNothing!",
                    "id_order_by_ASC",
                    "checkNothing!");

            //更新するインスタンスを紐づけるため、更新前のIdを取得し、setする
            updateQuotation.setId(quotations.getId());

            Log.d(TAG,"バックグラウンドでswitchの条件値更新："+updateQuotation.toString());
            //古いインスタンスを新しいインスタンスで更新する
            mQuotationRepository.updateQuotationTask(updateQuotation);

        }

    }
}