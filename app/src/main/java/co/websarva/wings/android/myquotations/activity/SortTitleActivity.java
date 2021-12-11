package co.websarva.wings.android.myquotations.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import co.websarva.wings.android.myquotations.R;
import co.websarva.wings.android.myquotations.persistence.QuotationRepository;
import co.websarva.wings.android.myquotations.util.MyApplication;

public class SortTitleActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = " SortTitleActivity";
    private MyApplication myApplication = new MyApplication();
    QuotationRepository mQuotationRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_title);

        mQuotationRepository  = new QuotationRepository(myApplication);

        findViewById(R.id.back_arrow).setOnClickListener(this);

        findViewById(R.id.sort_id_ASC).setOnClickListener(this);
        findViewById(R.id.sort_id_DESC).setOnClickListener(this);
        findViewById(R.id.sort_title_ASC).setOnClickListener(this);
        findViewById(R.id.sort_title_DESC).setOnClickListener(this);
        findViewById(R.id.sort_Author_ASC).setOnClickListener(this);
        findViewById(R.id.sort_Author_DESC).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_arrow:
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;

            case R.id.sort_id_ASC:
                mQuotationRepository.getSortIdAscTask();
                Log.d(TAG,"ID昇順がタップされました");
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;

            case R.id.sort_id_DESC:
                Log.d(TAG,"ID降順がタップされました");
                mQuotationRepository.getSortIdDescTask();
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;

            case R.id.sort_title_ASC:
                Log.d(TAG,"タイトル昇順がタップされました");
                mQuotationRepository.getSortTitleAscTask();
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;

            case R.id.sort_title_DESC:
                Log.d(TAG,"タイトル降順がタップされました");
                mQuotationRepository.getSortTitleDescTask();
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;

            case R.id.sort_Author_ASC:
                Log.d(TAG,"著者名昇順がタップされました");
                mQuotationRepository.getSortAuthorAscTask();
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;

            case R.id.sort_Author_DESC:
                Log.d(TAG,"著者名降順がタップされました");
                mQuotationRepository.getSortAuthorDescTask();
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;

        }
    }
}