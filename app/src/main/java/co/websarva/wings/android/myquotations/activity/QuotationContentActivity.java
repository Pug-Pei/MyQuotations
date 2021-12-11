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
            //新しい名言集を作成（Edit Mode）
            if(mIsNewQuotation){
                setNewQuotationProperties();
                enableContentEditMode();
                return;
            }else if(mIsNewTitle){
            setNewTitleProperties();
            enableAllEditMode();
            }
        } else {
            //既存の名言集（View Mode）
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
        //既存の名言をタップして遷移してきた場合
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
        //タイトルの新規作成の場合
        else if (getIntent().hasExtra("createNewTitle")) {
            mMODE = EDIT_MODE_ENABLED;
            mIsNewTitle = true;
            return true;
        }else
            //名言の新規作成の場合
            Log.d(TAG,"名言を新規作成");
        mQuotationInitial = getIntent().getParcelableExtra("create_quotation");
        mQuotationInitial.setContent("title_instance");
        mEditContentText.setText(mQuotationInitial.getContent());

        mMODE = EDIT_MODE_DISABLED;
        mIsNewQuotation = true;
        return true;
    }


    private void saveChanges(){
        Log.d(TAG,"saveChanges()発動");
        if(mIsNewTitle){
            Log.d(TAG,"新規フォルダを保存");
            //新規作成時の初回のsaveChanges()以降は mIsNewQuotation = false;とする。
            //そうしないと、タイトルなどを変更して✓ボタンのクリックを繰り返すと、mIsNewQuotation = trueのままなので、新しいリストがそのたびにできてしまう。
            mIsNewTitle = false;
            saveNewQuotation();
        }else if(mIsNewQuotation){

            Log.d(TAG,"新規名言を保存");
            mIsNewQuotation = false;
            saveNewQuotation();
        } else{

            Log.d(TAG,"名言を更新");
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

    //既存のタイトルで中で、新規の名言を作成（+ボタンをタップ）した場合
    private void enableContentEditMode(){
        mBackArrowContainer.setVisibility(View.GONE);
        mButtonContainer.setVisibility(View.VISIBLE);
        mEditContentText.setVisibility(View.VISIBLE);

        Log.d(TAG,"enableContentEditMode()発動");

        mMODE = EDIT_MODE_ENABLED;
    }

    //新規にタイトルを作成（+ボタンをタップ）を作成した場合
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

        //内容を変更していれば保存、何も変更していないなら保存しない

        String textContent =  mEditContentText.getText().toString();
        //改行、スペースは変更なし（空白）とみなす

        textContent = textContent.replace("\n","");
        textContent = textContent.replace(" ","");
        if(textContent.length()>0){

            //タイトルの新規作成以外の場合はEditText内の文字をContentに設定する。
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
                Log.d(TAG,"disableEditMode()発動");

                saveChanges();
            }
        }
    }

    //新規にタイトルを作成する場合
    private  void setNewTitleProperties() {
        mQuotationInitial = new Quotations();
        mQuotationInitial.setContent("title_instance");
    }

    private  void setNewQuotationProperties() {
        mQuotationInitial = getIntent().getParcelableExtra("create_quotation");
        mQuotationInitial.setContent("title_instance");
    }

    //既存の名言をタップして遷移してきた場合
    private void setQuotationProperties(){
        Log.d(TAG,"setQuotationProperties()"+mQuotationInitial.toString());
        mEditContentText.setText(mQuotationInitial.getContent());
    }


    //OnGestureListenerインターフェースのメソッド
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


    //OnDoubleTapListenerインターフェースのメソッド
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

    //OnClickListenerインターフェースのメソッド
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_arrow_nontext: {

                if(mIsQuotationUpdate){
                    Log.d(TAG,"getWorkManagerInfo()発動");
                    mQuotationRepository.getWorkManagerInfo();
                }

                //通知をタップしてこの画面に来た場合
                if(getIntent().hasExtra("selected_quotation")){
                    Quotations mFromNotificationInstance = getIntent().getParcelableExtra("selected_quotation");
                    Intent intent = new Intent(this,QuotationListActivity.class);
                    intent.putExtra("selected_title",mFromNotificationInstance);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

                //名言リストをタップしてこの画面に来た場合
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