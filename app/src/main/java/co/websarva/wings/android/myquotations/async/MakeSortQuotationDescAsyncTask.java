package co.websarva.wings.android.myquotations.async;

import android.os.AsyncTask;

import co.websarva.wings.android.myquotations.persistence.QuotationDao;
import co.websarva.wings.android.myquotations.persistence.QuotationRepository;
import co.websarva.wings.android.myquotations.quotations.Quotations;
import co.websarva.wings.android.myquotations.util.MyApplication;

public class MakeSortQuotationDescAsyncTask extends AsyncTask<Quotations,Void,Quotations> {

    private final static String TAG = "MakeSortQuotaDescAsyTask";
    private MyApplication myApplication = new MyApplication();
    private QuotationDao mQuotationDao;
    private QuotationRepository mQuotationRepository = new QuotationRepository(myApplication);

    public MakeSortQuotationDescAsyncTask(QuotationDao dao) { mQuotationDao = dao;}

    @Override
    protected Quotations doInBackground(Quotations... quotations) {
        return mQuotationDao.getSortQuotation();
    }

    @Override
    protected void onPostExecute(Quotations quotations) {
        if(quotations == null){
            Quotations mInsertQuotation = new Quotations(
                    "sortQuotation",
                    "authorNothing!",
                    "timeNothing!",
                    "Quotation_order_by_DESC",
                    "checkNothing!"
            );
            mQuotationRepository.insertQuotationTask(mInsertQuotation);

        }else{
            Quotations mUpdateQuotation = new Quotations(
                    "sortQuotation",
                    "authorNothing!",
                    "timeNothing!",
                    "Quotation_order_by_DESC",
                    "checkNothing!"
            );

            mUpdateQuotation.setId(quotations.getId());
            mQuotationRepository.updateQuotationTask(mUpdateQuotation);
        }
    }
}
