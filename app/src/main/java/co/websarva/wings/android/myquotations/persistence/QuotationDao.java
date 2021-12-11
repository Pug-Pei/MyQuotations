package co.websarva.wings.android.myquotations.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import co.websarva.wings.android.myquotations.quotations.Quotations;

@Dao
public interface QuotationDao {

    @Query("SELECT * FROM quotations")
    LiveData<List<Quotations>> getLiveDataQuotations();

    @Query("SELECT * FROM quotations")
    List<Quotations> getQuotations();


    //ViewModel上で並び替えの為の識別変数を取得するため、対象のレコードを取得
    @Query("SELECT * FROM quotations WHERE title = 'sortTitle' ")
    LiveData<List<Quotations>> getSortTitleListLive();

    //並び替えの為の識別変数を上書きする為に、対象のレコードを取得
    @Query("SELECT * FROM quotations WHERE title = 'sortTitle' ")
    Quotations getSortTitleList();


    //ViewModel上で、名言の並び替えの為の識別変数を取得するため、対象のレコードを取得
    @Query("SELECT * FROM quotations WHERE title = 'sortQuotation'")
    LiveData<List<Quotations>> getSortQuotationLive();

    //並び替えの為の識別変数を上書きする為に、対象のレコードを取得
    @Query("SELECT * FROM quotations WHERE title = 'sortQuotation' ")
    Quotations getSortQuotation();

    @Insert
    long[] insertQuotation(Quotations...quotations);

    @Delete
    int delete(Quotations...quotations);

    @Update
    int updateQuotations(Quotations...quotations);


}
