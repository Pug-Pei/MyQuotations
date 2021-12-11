package co.websarva.wings.android.myquotations.async;

import android.os.AsyncTask;

import co.websarva.wings.android.myquotations.persistence.QuotationDao;
import co.websarva.wings.android.myquotations.quotations.Quotations;

public class DeleteAsyncTask extends AsyncTask<Quotations, Void, Void> {

    private QuotationDao mQuotationDao;

    public DeleteAsyncTask(QuotationDao dao)
    { mQuotationDao = dao;}

    @Override
    protected Void doInBackground(Quotations...quotations) {
        mQuotationDao.delete(quotations);
        return null;
    }
}
