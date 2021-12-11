package co.websarva.wings.android.myquotations.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import co.websarva.wings.android.myquotations.R;

public class DescriptionUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_update);

        findViewById(R.id.description_update_backArrow1).setOnClickListener(this);
        findViewById(R.id.description_update_backArrow2).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.description_update_backArrow1:
            case R.id.description_update_backArrow2:
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
        }
    }
}