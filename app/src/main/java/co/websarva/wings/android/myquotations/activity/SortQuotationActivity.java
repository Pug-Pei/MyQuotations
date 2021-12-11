package co.websarva.wings.android.myquotations.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import co.websarva.wings.android.myquotations.R;
import co.websarva.wings.android.myquotations.persistence.QuotationRepository;
import co.websarva.wings.android.myquotations.util.MyApplication;

public class SortQuotationActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = " SortQuotationActivity";
    private MyApplication myApplication = new MyApplication();
    QuotationRepository mQuotationRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_quotation);

        mQuotationRepository  = new QuotationRepository(myApplication);

        findViewById(R.id.back_arrow_from_content_order).setOnClickListener(this);
        findViewById(R.id.sort_content_ASC).setOnClickListener(this);
        findViewById(R.id.sort_content_DESC).setOnClickListener(this);
        findViewById(R.id.sort_content_id_ASC).setOnClickListener(this);
        findViewById(R.id.sort_content_id_DESC).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.back_arrow_from_content_order:
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;

            case R.id.sort_content_id_ASC:
                mQuotationRepository.getSortQuotationIdAscTask();
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;

            case R.id.sort_content_id_DESC:
                mQuotationRepository.getSortQuotationIdDescTask();
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;

            case R.id.sort_content_ASC:
                mQuotationRepository.getSortQuotationAscTask();
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;

            case R.id.sort_content_DESC:
                mQuotationRepository.getSortQuotationDescTask();
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;

            }
        }
}