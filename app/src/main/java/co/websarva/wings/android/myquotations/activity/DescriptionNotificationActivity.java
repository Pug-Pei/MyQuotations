package co.websarva.wings.android.myquotations.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import co.websarva.wings.android.myquotations.R;

public class DescriptionNotificationActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "DescriptionNotifiAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_notification);

        findViewById(R.id.description_notification_backArrow_1).setOnClickListener(this);
        findViewById(R.id.description_notification_backArrow_2).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.description_notification_backArrow_1:
            case R.id.description_notification_backArrow_2:
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
        }
    }
}