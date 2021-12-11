package co.websarva.wings.android.myquotations.util;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Random;

import co.websarva.wings.android.myquotations.async.GetDataAsyncTask;
import co.websarva.wings.android.myquotations.quotations.Quotations;

public class QuotationNotifyWorker extends Worker {

    private static final String TAG = "QuotationNotifyWorker";
    NotificationUnit mNotificationUnit = new NotificationUnit();
    Context context = getApplicationContext();
    Random random = new Random();

    public QuotationNotifyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters workerParams) {
                super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        randomQuotationNotification();
        return Result.success();
    }


    public void randomQuotationNotification(){
        if(!GetDataAsyncTask.mArrayListNotification.isEmpty()) {
            int mRandomPosition = random.nextInt(GetDataAsyncTask.mArrayListNotification.size());
            Quotations notificationInstance = GetDataAsyncTask.mArrayListNotification.get(mRandomPosition);

            String content = notificationInstance.getContent();
            String title = notificationInstance.getTitle();

            confirmArrayListNotificationContent();
            mNotificationUnit.makeQuotationNotification(content, title, context,notificationInstance);
        }
    }

    //【目的】UI上で✓したものと一致するか確認する
    //【機能】mArrayListNotification内の、各インスタンス（チェックボックスON）のメンバを表示
    private void confirmArrayListNotificationContent(){
        for(int i = 0; i<GetDataAsyncTask.mArrayListNotification.size(); i++){
            Log.d(TAG,"mArrayListNotificationのインスタンスの中身：" + GetDataAsyncTask.mArrayListNotification.get(i).toString());
        }
    }
}
