package co.websarva.wings.android.myquotations.async;

import android.os.AsyncTask;

import co.websarva.wings.android.myquotations.persistence.QuotationDao;
import co.websarva.wings.android.myquotations.persistence.QuotationRepository;
import co.websarva.wings.android.myquotations.quotations.Quotations;
import co.websarva.wings.android.myquotations.util.MyApplication;

public class MakeSortQuotationIdAscAsyncTask extends AsyncTask<Quotations,Void,Quotations> {

    private final static String TAG = "MakeSortQuotaAscAsyTask";
    private MyApplication myApplication = new MyApplication();
    private QuotationDao mQuotationDao;
    private QuotationRepository mQuotationRepository = new QuotationRepository(myApplication);

    public MakeSortQuotationIdAscAsyncTask(QuotationDao dao) { mQuotationDao = dao;}

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
                    "Quotation_id_order_by_ASC",
                    "checkNothing!"
            );
            mQuotationRepository.insertQuotationTask(mInsertQuotation);

        }else{
            Quotations mUpdateQuotation = new Quotations(
                    "sortQuotation",
                    "authorNothing!",
                    "timeNothing!",
                    "Quotation_id_order_by_ASC",
                    "checkNothing!"
            );

            mUpdateQuotation.setId(quotations.getId());
            mQuotationRepository.updateQuotationTask(mUpdateQuotation);
        }
    }
}
