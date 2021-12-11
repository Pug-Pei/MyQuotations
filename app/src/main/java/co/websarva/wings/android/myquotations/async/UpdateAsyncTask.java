package co.websarva.wings.android.myquotations.async;

import android.os.AsyncTask;

import co.websarva.wings.android.myquotations.persistence.QuotationDao;
import co.websarva.wings.android.myquotations.quotations.Quotations;

public class UpdateAsyncTask extends AsyncTask<Quotations,Void,Void> {

    private QuotationDao mQuotationDao;

    public UpdateAsyncTask(QuotationDao dao)
    { mQuotationDao = dao;}

    @Override
    protected Void doInBackground(Quotations...quotations) {
        mQuotationDao.updateQuotations(quotations);
        return null;
    }
}
