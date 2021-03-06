package co.websarva.wings.android.myquotations.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import co.websarva.wings.android.myquotations.R;
import co.websarva.wings.android.myquotations.persistence.QuotationRepository;
import co.websarva.wings.android.myquotations.quotations.Quotations;
import co.websarva.wings.android.myquotations.util.Utility;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class QuotationContentActivity extends AppCompatActivity implements View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener
{

    private static final String TAG = "QuotationsContent";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;

    //ui
    private EditText mEditContentText;
    private RelativeLayout mButtonContainer,mBackArrowContainer;
    private ImageButton mBackArrow;
    private Button mSave;

    //var
    private boolean mIsNewTitle;
    private boolean mIsNewQuotation;
    private boolean mIsQuotationUpdate;
    private int mMODE;
    private Quotations mQuotationInitial;
    private Quotations mQuotationFinal;
    private QuotationRepository mQuotationRepository;
    private GestureDetector mGestureDetector;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_content);

        mQuotationRepository = new QuotationRepository(this);
        mIsQuotationUpdate = false;

        mEditContentText = findViewById(R.id.quotation_content);
        mButtonContainer = findViewById(R.id.button_container_nontext);
        mBackArrowContainer = findViewById(R.id.back_arrow_container_nontext);
        mBackArrow = findViewById(R.id.toolbar_back_arrow_nontext);
        mSave = findViewById(R.id.toolbar_save_button);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

        mAdView = new AdView(this);

        mAdView.setAdSize(AdSize.BANNER);

        mAdView.setAdUnitId("ca-app-pub-9182262619189802/4193312796");

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView = findViewById(R.id.adViewBottom);
        mAdView.loadAd(adRequest);


        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


        Toolbar toolbar = findViewById(R.id.quotation_toolbar);
        setSupportActionBar(toolbar);

        setListeners();

        if (getIncomingIntent()) {
            //??????????????????????????????Edit Mode???
            if(mIsNewQuotation){
                setNewQuotationProperties();
                enableContentEditMode();
                return;
            }else if(mIsNewTitle){
            setNewTitleProperties();
            enableAllEditMode();
            }
        } else {
            //?????????????????????View Mode???
            setQuotationProperties();
            disableEditMode();
        }


    }

    private void setListeners(){
        mEditContentText.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, this);
        mBackArrow.setOnClickListener(this);
        mSave.setOnClickListener(this);
    }

    private boolean getIncomingIntent() {
        //?????????????????????????????????????????????????????????
        if (getIntent().hasExtra("selected_quotation")) {
            mQuotationInitial = getIntent().getParcelableExtra("selected_quotation");
            mQuotationFinal = new Quotations();
            mQuotationFinal.setTitle(mQuotationInitial.getTitle());
            mQuotationFinal.setAuthorName(mQuotationInitial.getAuthorName());
            mQuotationFinal.setContent(mQuotationInitial.getContent());
            mQuotationFinal.setTimeStamp(mQuotationInitial.getTimeStamp());
            mQuotationFinal.setId(mQuotationInitial.getId());
            mQuotationFinal.setCheckBox(mQuotationInitial.getCheckBox());
            mMODE = EDIT_MODE_DISABLED;
            mIsNewQuotation = false;
            return false;
        }
        //????????????????????????????????????
        else if (getIntent().hasExtra("createNewTitle")) {
            mMODE = EDIT_MODE_ENABLED;
            mIsNewTitle = true;
            return true;
        }else
            //??????????????????????????????
            Log.d(TAG,"?????????????????????");
        mQuotationInitial = getIntent().getParcelableExtra("create_quotation");
        mQuotationInitial.setContent("title_instance");
        mEditContentText.setText(mQuotationInitial.getContent());

        mMODE = EDIT_MODE_DISABLED;
        mIsNewQuotation = true;
        return true;
    }


    private void saveChanges(){
        Log.d(TAG,"saveChanges()??????");
        if(mIsNewTitle){
            Log.d(TAG,"???????????????????????????");
            //???????????????????????????saveChanges()????????? mIsNewQuotation = false;????????????
            //??????????????????????????????????????????????????????????????????????????????????????????????????????mIsNewQuotation = true??????????????????????????????????????????????????????????????????????????????
            mIsNewTitle = false;
            saveNewQuotation();
        }else if(mIsNewQuotation){

            Log.d(TAG,"?????????????????????");
            mIsNewQuotation = false;
            saveNewQuotation();
        } else{

            Log.d(TAG,"???????????????");
            updateQuotation();
            mIsQuotationUpdate = true;
        }
    }


    private void updateQuotation(){
        mQuotationRepository.updateQuotationTask(mQuotationFinal);
    }

    private void saveNewQuotation(){
        mQuotationRepository.insertQuotationTask(mQuotationFinal);
    }


    private void enableContentInteraction(){
        mEditContentText.setKeyListener(new EditText(this).getKeyListener());
        mEditContentText.setFocusable(true);
        mEditContentText.setFocusableInTouchMode(true);
        mEditContentText.setCursorVisible(true);
        mEditContentText.requestFocus();
    }

    //????????????????????????????????????????????????????????????+????????????????????????????????????
    private void enableContentEditMode(){
        mBackArrowContainer.setVisibility(View.GONE);
        mButtonContainer.setVisibility(View.VISIBLE);
        mEditContentText.setVisibility(View.VISIBLE);

        Log.d(TAG,"enableContentEditMode()??????");

        mMODE = EDIT_MODE_ENABLED;
    }

    //?????????????????????????????????+?????????????????????????????????????????????
    private void enableAllEditMode(){
        mBackArrowContainer.setVisibility(View.GONE);
        mButtonContainer.setVisibility(View.VISIBLE);
        mEditContentText.setVisibility(View.GONE);

        mMODE = EDIT_MODE_ENABLED;
    }

    private void disableContentInteraction(){
        mEditContentText.setKeyListener(null);
        mEditContentText.setFocusable(false);
        mEditContentText.setFocusableInTouchMode(false);
        mEditContentText.setCursorVisible(false);
        mEditContentText.clearFocus();
    }

    private void disableEditMode(){
        mBackArrowContainer.setVisibility(View.VISIBLE);
        mButtonContainer.setVisibility(View.GONE);

        disableContentInteraction();

        mMODE = EDIT_MODE_DISABLED;

        //???????????????????????????????????????????????????????????????????????????????????????

        String textContent =  mEditContentText.getText().toString();
        //????????????????????????????????????????????????????????????

        textContent = textContent.replace("\n","");
        textContent = textContent.replace(" ","");
        if(textContent.length()>0){

            //?????????????????????????????????????????????EditText???????????????Content??????????????????
            if(mIsNewTitle){
                mQuotationFinal.setContent("title_instance");
            }else{
                mQuotationFinal.setContent(mEditContentText.getText().toString());
            }
            String timeStamp = Utility.getCurrentTimeStamp();
            mQuotationFinal.setTimeStamp(timeStamp);

            if(!mQuotationFinal.getContent().equals(mQuotationInitial.getContent())){

                if(mQuotationFinal.getContent().isEmpty()){
                    mQuotationFinal.setContent("title_instance");
                }
                Log.d(TAG,"disableEditMode()??????");

                saveChanges();
            }
        }
    }

    //??????????????????????????????????????????
    private  void setNewTitleProperties() {
        mQuotationInitial = new Quotations();
        mQuotationInitial.setContent("title_instance");
    }

    private  void setNewQuotationProperties() {
        mQuotationInitial = getIntent().getParcelableExtra("create_quotation");
        mQuotationInitial.setContent("title_instance");
    }

    //?????????????????????????????????????????????????????????
    private void setQuotationProperties(){
        Log.d(TAG,"setQuotationProperties()"+mQuotationInitial.toString());
        mEditContentText.setText(mQuotationInitial.getContent());
    }


    //OnGestureListener???????????????????????????????????????
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG,"on Single tapped!");
        enableContentInteraction();
        enableContentEditMode();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    //OnDoubleTapListener???????????????????????????????????????
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    //OnClickListener???????????????????????????????????????
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_arrow_nontext: {

                if(mIsQuotationUpdate){
                    Log.d(TAG,"getWorkManagerInfo()??????");
                    mQuotationRepository.getWorkManagerInfo();
                }

                //???????????????????????????????????????????????????
                if(getIntent().hasExtra("selected_quotation")){
                    Quotations mFromNotificationInstance = getIntent().getParcelableExtra("selected_quotation");
                    Intent intent = new Intent(this,QuotationListActivity.class);
                    intent.putExtra("selected_title",mFromNotificationInstance);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                //????????????????????????????????????????????????????????????
                }else{
                    finish();
                }
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            }
            case R.id.toolbar_save_button: {
                disableEditMode();
                break;
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (mMODE == EDIT_MODE_ENABLED) {
            onClick(mSave);
            Log.d(TAG, "mCheck clicked");
        } else {
            Log.d(TAG, "super.onBackPressed fires!");
            super.onBackPressed();
        }
    }

}