package co.websarva.wings.android.myquotations.persistence;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import co.websarva.wings.android.myquotations.async.DeleteAsyncTask;
import co.websarva.wings.android.myquotations.async.GetDataAsyncTask;
import co.websarva.wings.android.myquotations.async.MakeSortAuthorAscAsyncTask;
import co.websarva.wings.android.myquotations.async.MakeSortAuthorDescAsyncTask;
import co.websarva.wings.android.myquotations.async.MakeSortIdAscAsyncTask;
import co.websarva.wings.android.myquotations.async.InsertAsyncTask;
//import co.websarva.wings.android.myquotations.async.TitleOrderByDescAsyncTask;
import co.websarva.wings.android.myquotations.async.MakeSortQuotationAscAsyncTask;
import co.websarva.wings.android.myquotations.async.MakeSortQuotationDescAsyncTask;
import co.websarva.wings.android.myquotations.async.MakeSortQuotationIdAscAsyncTask;
import co.websarva.wings.android.myquotations.async.MakeSortQuotationIdDescAsyncTask;
import co.websarva.wings.android.myquotations.async.MakeSortTitleAscAsyncTask;
import co.websarva.wings.android.myquotations.async.MakeSortTitleDescAsyncTask;
import co.websarva.wings.android.myquotations.async.UpdateAsyncTask;
import co.websarva.wings.android.myquotations.async.MakeSortIdDescAsyncTask;
import co.websarva.wings.android.myquotations.quotations.Quotations;

public class QuotationRepository {
    private static final String TAG ="QuotationRepository";

    private QuotationDatabase mQuotationDatabase;

    public QuotationRepository(Context context) {
        mQuotationDatabase =QuotationDatabase.getInstance(context);}

    //作成日時昇順でデータ取得（idが小さいものが上に来る。）
    public LiveData<List<Quotations>>idOrderByAscTask(){
        Log.d(TAG,"idOrderByAscTask()発動");
        return mQuotationDatabase.getQuotationDao().getLiveDataQuotations();
    }

    //名言昇順のため,データベースにcontentが"○○_order_by_ASC(DESC)"のList＜Quotation＞データを作成する。作成済みのときは上書きする
    //ID昇順
    public void getSortIdAscTask(){
        Log.d(TAG,"getSortIdAscTask()発動");
        new MakeSortIdAscAsyncTask(mQuotationDatabase.getQuotationDao()).execute();
    }

    //ID降順
    public void getSortIdDescTask(){
        Log.d(TAG,"getSortIdDescTask()発動");
        new MakeSortIdDescAsyncTask(mQuotationDatabase.getQuotationDao()).execute();
    }

    //タイトル昇順
    public void getSortTitleAscTask(){
        Log.d(TAG,"getSortTitleAscQuotation()発動");
        new MakeSortTitleAscAsyncTask(mQuotationDatabase.getQuotationDao()).execute();
    }

    //タイトル降順
    public void getSortTitleDescTask(){
        Log.d(TAG,"getSortTitleDescQuotation()発動");
        new MakeSortTitleDescAsyncTask(mQuotationDatabase.getQuotationDao()).execute();
    }

    //著者名昇順
    public void getSortAuthorAscTask(){
        Log.d(TAG,"getSortAuthorAscTask()発動");
        new MakeSortAuthorAscAsyncTask(mQuotationDatabase.getQuotationDao()).execute();
    }

    //著者名降順
    public void getSortAuthorDescTask(){
        Log.d(TAG,"getSortAuthorDescTask()発動");
        new MakeSortAuthorDescAsyncTask(mQuotationDatabase.getQuotationDao()).execute();
    }

    //名言昇順
    public void getSortQuotationAscTask(){
        Log.d(TAG,"getSortQuotationAscTask()発動");
        new MakeSortQuotationAscAsyncTask(mQuotationDatabase.getQuotationDao()).execute();
    }

    //名言降順
    public void getSortQuotationDescTask(){
        Log.d(TAG,"getSortQuotationDescTask()発動");
        new MakeSortQuotationDescAsyncTask(mQuotationDatabase.getQuotationDao()).execute();
    }

    //名言ID昇順
    public void getSortQuotationIdAscTask(){
        Log.d(TAG,"getSortQuotationIdAscTask()発動");
        new MakeSortQuotationIdAscAsyncTask(mQuotationDatabase.getQuotationDao()).execute();
    }

    //名言ID降順
    public void getSortQuotationIdDescTask(){
        Log.d(TAG,"getSortQuotationIdAscTask()発動");
        new MakeSortQuotationIdDescAsyncTask(mQuotationDatabase.getQuotationDao()).execute();
    }



    //タイトルリスト並び替えの識別変数をもったリストを取得
    public LiveData<List<Quotations>> getSortValQuotation(){
        return mQuotationDatabase.getQuotationDao().getSortTitleListLive();
    }

    //名言リスト並び替えの識別変数をもったリストを取得
    public LiveData<List<Quotations>> getSortQuotationList(){
        return mQuotationDatabase.getQuotationDao().getSortQuotationLive();
    }

    //WorkManager使用時のデータ取得
    public void getWorkManagerInfo(){
        Log.d(TAG,"getAllQuotations()発動");
        new GetDataAsyncTask(mQuotationDatabase.getQuotationDao()).execute();
    }

    //データ挿入
    public void insertQuotationTask(Quotations quotations){
        Log.d(TAG,"insertQuotationTask()発動");
        new InsertAsyncTask(mQuotationDatabase.getQuotationDao()).execute(quotations);
    }

    //データ更新
    public void updateQuotationTask(Quotations quotations){
        Log.d(TAG,"updateQuotationTask()発動");
        new UpdateAsyncTask(mQuotationDatabase.getQuotationDao()).execute(quotations);
    }

    //データ削除
    public void deleteQuotationTask(Quotations quotations){
        Log.d(TAG,"削除するインスタンス："+quotations.toString());
        new DeleteAsyncTask(mQuotationDatabase.getQuotationDao()).execute(quotations);
    }
}
