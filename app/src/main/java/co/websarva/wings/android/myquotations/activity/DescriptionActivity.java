package co.websarva.wings.android.myquotations.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.cachapa.expandablelayout.ExpandableLayout;

import co.websarva.wings.android.myquotations.R;

public class DescriptionActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "DescriptionActivity";

    private boolean mIsWhatKIndOfAppSelected = false;
    private ExpandableLayout mExAbleWhatKIndOfApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        mExAbleWhatKIndOfApp = findViewById(R.id.expandableLayout_what_kind_of_app);

        findViewById(R.id.what_kind_of_app_titleLayout).setOnClickListener(this);
        findViewById(R.id.description_add_backArrow1).setOnClickListener(this);
        findViewById(R.id.description_add_title).setOnClickListener(this);
        findViewById(R.id.description_update_title).setOnClickListener(this);
        findViewById(R.id.description_delete_title).setOnClickListener(this);
        findViewById(R.id.description_notification_title).setOnClickListener(this);
        findViewById(R.id.description_terms_of_service_titleLayout).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.description_add_backArrow1:
            finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            break;

            case R.id.what_kind_of_app_titleLayout:
                if(mIsWhatKIndOfAppSelected){
                    mExAbleWhatKIndOfApp.collapse();
                }else{
                    mExAbleWhatKIndOfApp.expand();
                }
                mIsWhatKIndOfAppSelected = !mIsWhatKIndOfAppSelected;
                break;

            case R.id.description_add_title:{
                Intent intent = new Intent(this, DescriptionAddActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            }

            case R.id.description_update_title:{
                Intent intent = new Intent(this,DescriptionUpdateActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            }

            case R.id.description_delete_title:{
                Intent intent = new Intent(this,DescriptionDeleteActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            }


            case R.id.description_notification_title:{
                Intent intent = new Intent(this,DescriptionNotificationActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            }

            case R.id.description_terms_of_service_titleLayout:{
                Intent intent = new Intent(this,TermsAndPrivacyActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            }
        }
    }
}