package co.websarva.wings.android.myquotations.async;

import android.os.AsyncTask;

import co.websarva.wings.android.myquotations.persistence.QuotationDao;
import co.websarva.wings.android.myquotations.quotations.Quotations;

public class InsertAsyncTask extends AsyncTask<Quotations,Void,Void> {

    private QuotationDao mQuotationDao;

    public InsertAsyncTask(QuotationDao dao)
    { mQuotationDao = dao;}

    @Override
    protected Void doInBackground(Quotations...quotations) {
        mQuotationDao.insertQuotation(quotations);
        return null;
    }
}
