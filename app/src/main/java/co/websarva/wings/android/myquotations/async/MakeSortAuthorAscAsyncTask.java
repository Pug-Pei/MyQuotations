package co.websarva.wings.android.myquotations.async;

import android.os.AsyncTask;
import android.util.Log;

import co.websarva.wings.android.myquotations.persistence.QuotationDao;
import co.websarva.wings.android.myquotations.persistence.QuotationRepository;
import co.websarva.wings.android.myquotations.quotations.Quotations;
import co.websarva.wings.android.myquotations.util.MyApplication;

public class MakeSortAuthorAscAsyncTask extends AsyncTask<Quotations,Void,Quotations> {

    private static final String TAG = "MakeSortAuthorAscAsyTas";
    private MyApplication myApplication = new MyApplication();
    private QuotationDao mQuotationDao;
    private QuotationRepository mQuotationRepository = new QuotationRepository(myApplication);

    public MakeSortAuthorAscAsyncTask(QuotationDao dao) { mQuotationDao = dao;}


    @Override
    protected Quotations doInBackground(Quotations... quotations) {
        return mQuotationDao.getSortTitleList();
    }

    @Override
    protected void onPostExecute(Quotations quotations) {

        //並び替えボタンをタップした時に、初めて並び替え変数を持ったインスタンスを生成する
        if(quotations == null){
            Quotations mInsertQuotation = new Quotations(
                    "sortTitle",
                    "authorNothing!",
                    "timeNothing!",
                    "author_order_by_ASC",
                    "checkNothing!"
            );
            mQuotationRepository.insertQuotationTask(mInsertQuotation);

        }else {

            //既に並び替えインスタンスが存在しているなら、content（並び替え識別変数）を
            //著者名昇順（author_order_by_ASC）で上書きする
            Quotations updateQuotation = new Quotations(
                    "sortTitle",
                    "authorNothing!",
                    "timeNothing!",
                    "author_order_by_ASC",
                    "checkNothing!");

            //更新するインスタンスを紐づけるため、更新前のidを取得し、setする
            updateQuotation.setId(quotations.getId());

            //古いインスタンスを新しいインスタンスで更新する
            mQuotationRepository.updateQuotationTask(updateQuotation);
        }

    }
}
