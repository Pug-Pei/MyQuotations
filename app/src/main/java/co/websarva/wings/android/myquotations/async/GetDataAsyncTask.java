package co.websarva.wings.android.myquotations.async;

import android.os.AsyncTask;
import android.util.Log;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import co.websarva.wings.android.myquotations.persistence.QuotationDao;
import co.websarva.wings.android.myquotations.quotations.Quotations;
import co.websarva.wings.android.myquotations.util.MyApplication;
import co.websarva.wings.android.myquotations.util.NotificationUnit;
import co.websarva.wings.android.myquotations.util.QuotationNotifyWorker;

public class GetDataAsyncTask extends AsyncTask<Quotations, Void, List<Quotations>> {

    private static final String TAG ="GetDataAsyncTask";
    private QuotationDao mQuotationDao;
    private ArrayList<Quotations> mArrayListAllQuotation = new ArrayList<>();
    public static ArrayList<Quotations> mArrayListNotification = new ArrayList<>();
    private MyApplication myApplication = new MyApplication();

    public GetDataAsyncTask(QuotationDao dao)
    { mQuotationDao = dao;}


    @Override
    protected List<Quotations> doInBackground(Quotations... quotations) {
        return mQuotationDao.getQuotations();
    }

    @Override
    protected void onPostExecute(List<Quotations> quotations) {

        mArrayListAllQuotation.addAll(quotations);
        if(!mArrayListNotification.isEmpty()){
            mArrayListNotification.clear();
        }
        for(int i = 0; i<mArrayListAllQuotation.size(); i++){
            if(mArrayListAllQuotation.get(i).getCheckBox().equals("check_on")){
                mArrayListNotification.add(mArrayListAllQuotation.get(i));
            }
        }

        //WorkManagerの実行周期の定義
        PeriodicWorkRequest mNotificationWorkRequest =
                new PeriodicWorkRequest.Builder(QuotationNotifyWorker.class,
                        16, TimeUnit.MINUTES,
                        15,TimeUnit.MINUTES)
                        .build();

        WorkManager.getInstance(myApplication).enqueueUniquePeriodicWork(
              "randomNotification", ExistingPeriodicWorkPolicy.REPLACE,mNotificationWorkRequest
        );
    }
}
