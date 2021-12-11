package co.websarva.wings.android.myquotations.persistence;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import co.websarva.wings.android.myquotations.quotations.Quotations;

public class QuotationViewModel extends AndroidViewModel {

    private static final String TAG ="QuotationViewModel";

    private QuotationRepository mRepository;
    private final LiveData<List<Quotations>> mIdOrderByAscQuotations;
    private final LiveData<List<Quotations>> mSortValQuotation;
    private final LiveData<List<Quotations>> mSortQuotationList;

    public QuotationViewModel(@NonNull Application application) {
        super(application);
        mRepository = new QuotationRepository(application);
        mIdOrderByAscQuotations = mRepository.idOrderByAscTask();
        mSortValQuotation = mRepository.getSortValQuotation();
        mSortQuotationList = mRepository.getSortQuotationList();
    }


    //作成日時昇順でデータ取得（idが小さいものが上に来る。）
    public LiveData<List<Quotations>> getIdOrderByAscQuotations(){
        Log.d(TAG,"LiveData<List<Quotations>>getAllQuotations()発動");
        return mIdOrderByAscQuotations;
    }


    public LiveData<List<Quotations>>getSortVal()
    {
        return mSortValQuotation;
    }

    public LiveData<List<Quotations>>getSortQuotationList()
    {
        return mSortQuotationList;
    }

}
