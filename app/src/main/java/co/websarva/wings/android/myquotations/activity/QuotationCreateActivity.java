package co.websarva.wings.android.myquotations.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.websarva.wings.android.myquotations.R;
import co.websarva.wings.android.myquotations.persistence.QuotationRepository;
import co.websarva.wings.android.myquotations.quotations.Quotations;
import co.websarva.wings.android.myquotations.util.DuplicateAlertDialogFragment;
import co.websarva.wings.android.myquotations.util.Utility;

public class QuotationCreateActivity extends AppCompatActivity implements View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener {

    private static final String TAG = "QuotationCreateActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;

    //ui
    private TextView mViewTitle;
    private EditText mEditTitle;
    private TextView mViewAuthor;
    private EditText mEditAuthor;
    private EditText mEditContentText;
    private RelativeLayout mButtonContainer, mBackArrowContainer;
    private ImageButton mBackArrow;
    private Button mSave;
    private View mRhombusMaterial;

    //var
    private boolean mIsNewTitle;
    private boolean mIsNewQuotation;
    private boolean mIsTitleDuplicate;
    private int mMODE;
    private Quotations mQuotationInitial;
    private Quotations mQuotationFinal;
    private QuotationRepository mQuotationRepository;
    private GestureDetector mGestureDetector;
    private ArrayList<Quotations> arrayListTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_create);

        mViewTitle = findViewById(R.id.quotation_text_title);
        mEditTitle = findViewById(R.id.quotation_edit_title);
        mViewAuthor = findViewById(R.id.quotation_text_authorname);
        mEditAuthor = findViewById(R.id.quotation_edit_authorname);
        mEditContentText = findViewById(R.id.quotation_content);

        mButtonContainer = findViewById(R.id.button_container);
        mBackArrowContainer = findViewById(R.id.back_arrow_container);
        mSave = findViewById(R.id.toolbar_save_button);
        mBackArrow = findViewById(R.id.toolbar_back_arrow);
        mRhombusMaterial = findViewById(R.id.view_rhombus_material);

        mQuotationRepository = new QuotationRepository(this);

        Toolbar toolbar = findViewById(R.id.quotation_toolbar);
        setSupportActionBar(toolbar);

        setListeners();

        if (getIncomingIntent()) {
            //新しい名言集を作成
            if (mIsNewQuotation) {
                setNewQuotationProperties();
                enableContentEditMode();
            } else if (mIsNewTitle) {
                setNewTitleProperties();
                enableAllEditMode();
            }
        }
    }

    //リスナを設定
    private void setListeners() {
        mBackArrow.setOnClickListener(this);
        mSave.setOnClickListener(this);
        mViewTitle.setOnClickListener(this);
        mViewAuthor.setOnClickListener(this);
        mEditContentText.setOnTouchListener(this);
        mEditTitle.addTextChangedListener(new GenericTextWatcherFirst(mEditTitle));
        mEditAuthor.addTextChangedListener(new GenericTextWatcherFirst(mEditAuthor));
        mGestureDetector = new GestureDetector(this, this);
    }

    private void saveChanges() {
        Log.d(TAG, "saveChanges()発動");
        if (mIsNewTitle) {
            Log.d(TAG, "saveNew!");
            //新規作成時の初回のsaveChanges()以降は mIsNewQuotation = false;とする。
            //理由：タイトルなどを変更して✓ボタンのタップを繰り返すと、mIsNewQuotation = trueのままなので、新しいリストがそのたびにできてしまう。
            mIsNewTitle = false;
            saveNewQuotation();

        } else if (mIsNewQuotation) {
            Log.d(TAG, "saveNewQuotation()発動");
            mIsNewQuotation = false;
                saveNewQuotation();

        } else {
            //新規作成画面で、複数回保存ボタンを押した場合のための必要
            Log.d(TAG, "updateQuotation()発動");
            updateQuotation();
        }
    }

    private void updateQuotation() {
        Log.d(TAG,"更新するインスタンス情報"+mQuotationFinal.toString());
        mQuotationRepository.updateQuotationTask(mQuotationFinal);
    }

    private void saveNewQuotation() {
        mQuotationRepository.insertQuotationTask(mQuotationFinal);
    }


    private boolean getIncomingIntent() {
        //タイトルの新規作成の場合
        if (getIntent().hasExtra("createNewTitle")) {
            mMODE = EDIT_MODE_ENABLED;
            mIsNewTitle = true;
            return true;

        } else
            //名言の新規作成の場合
            Log.d(TAG, "名言を新規作成");
        mMODE = EDIT_MODE_DISABLED;
        mIsNewQuotation = true;
        return true;
    }

    private void enableContentInteraction() {

        mEditContentText.setKeyListener(new EditText(this).getKeyListener());
        mEditContentText.setFocusable(true);
        mEditContentText.setFocusableInTouchMode(true);
        mEditContentText.setCursorVisible(true);
        mEditContentText.requestFocus();

    }

    //新規にタイトルを作成した場合
    public void enableAllEditMode() {
        Log.d(TAG,"enableAllEditMode()発動");
        mBackArrowContainer.setVisibility(View.GONE);
        mButtonContainer.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        mViewAuthor.setVisibility(View.GONE);
        mEditAuthor.setVisibility(View.VISIBLE);

        mEditContentText.setVisibility(View.GONE);

        mMODE = EDIT_MODE_ENABLED;
    }


    private void enableTitleEditMode() {

        Log.d(TAG,"QuotationCreateActivity　enableTitleEditMode()発動");

        mBackArrowContainer.setVisibility(View.GONE);
        mButtonContainer.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        mViewAuthor.setVisibility(View.VISIBLE);
        mEditAuthor.setVisibility(View.GONE);
        mMODE = EDIT_MODE_ENABLED;

    }


    private void enableAuthorEditMode() {

        mBackArrowContainer.setVisibility(View.GONE);
        mButtonContainer.setVisibility(View.VISIBLE);

        mViewAuthor.setVisibility(View.GONE);
        mEditAuthor.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

        mMODE = EDIT_MODE_ENABLED;
    }


    //既存のタイトルで中で、新規の名言を作成した場合
    private void enableContentEditMode() {

        mBackArrowContainer.setVisibility(View.GONE);
        mButtonContainer.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

        mViewAuthor.setVisibility(View.VISIBLE);
        mEditAuthor.setVisibility(View.GONE);

        mEditContentText.setVisibility(View.VISIBLE);

        Log.d(TAG, "enableContentEditMode()発動");

        mMODE = EDIT_MODE_ENABLED;
    }


    private void disableContentInteraction() {

        mEditContentText.setKeyListener(null);
        mEditContentText.setFocusable(false);
        mEditContentText.setFocusableInTouchMode(false);
        mEditContentText.setCursorVisible(false);
        mEditContentText.clearFocus();

    }

    private void disableEditMode() {

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

        mViewAuthor.setVisibility(View.VISIBLE);
        mEditAuthor.setVisibility(View.GONE);

        mBackArrowContainer.setVisibility(View.VISIBLE);
        mButtonContainer.setVisibility(View.GONE);
        disableContentInteraction();

        //内容を変更していれば保存、何も変更していないなら保存しない
        String textTitle = mEditTitle.getText().toString();
        String textAuthor = mEditAuthor.getText().toString();
        String textContent = mEditContentText.getText().toString();
        //改行、スペースは変更なし（空白）とみなす
        textTitle = textTitle.replace("\n", "");
        textTitle = textTitle.replace(" ", "");
        textAuthor = textAuthor.replace("\n", "");
        textAuthor = textAuthor.replace(" ", "");
        textContent = textContent.replace("\n", "");
        textContent = textContent.replace(" ", "");


        mQuotationFinal = new Quotations();
        mQuotationFinal.setTitle(mEditTitle.getText().toString());
        mQuotationFinal.setAuthorName(mEditAuthor.getText().toString());
        String timeStamp = Utility.getCurrentTimeStamp();
        mQuotationFinal.setTimeStamp("作成日 " + timeStamp);
        mQuotationFinal.setCheckBox("check_off");
        Log.d(TAG,"QuotationCreateActivityにてcheck_offになりました");

        //以下を判定する
        //・タイトルの新規作成か
        // ・mEditTitle内の文字が0文字より多いか
        // ・更新前後の入力内容が異なるか
        //タイトルの新規作成の場合Contentを　”title_instance”　とする
        if (mIsNewTitle
            &&
            textTitle.length() > 0
            &&
            !mQuotationFinal.getTitle().equals(mQuotationInitial.getTitle())
            ) {

            arrayListTitles = getIntent().getParcelableArrayListExtra("createNewTitle");

            //タイトルが既存のものと重複しているかチェック
            titleDuplicateCheck();

            //タイトルが既存のものと重複していない場合
            if(!mIsTitleDuplicate)
            {
            mQuotationInitial.setContent("title_instance");
            mQuotationFinal.setContent(mQuotationInitial.getContent());
            saveChanges();

            //タイトルが既存のものと重複している場合
            }else if(mIsTitleDuplicate)
            {
                //既存タイトルの為作成不可を通知し、タイトル編集状態に戻る
                Log.d(TAG, "タイトルが既存のものと重複しています" );
                duplicateTitleAlert();
                enableTitleEditMode();
                mEditTitle.requestFocus();
                mEditTitle.setSelection(mEditTitle.length());
            }

        } else if (mIsNewQuotation
                   &&
                   textContent.length() > 0)
        {
            mQuotationFinal.setContent(mEditContentText.getText().toString());
            saveChanges();
        }else{

        }
    }



    private void titleDuplicateCheck() {
        arrayListTitles = getIntent().getParcelableArrayListExtra("createNewTitle");
        for (int i = 0; i < arrayListTitles.size(); i++) {
            if (arrayListTitles.get(i).getTitle().equals(mQuotationFinal.getTitle())) {
                mIsTitleDuplicate = true;
                break;
            }else{
                mIsTitleDuplicate = false;
            }
        }
    }



    private void duplicateTitleAlert(){
        DuplicateAlertDialogFragment dialogFragment = new DuplicateAlertDialogFragment();
        dialogFragment.show(getSupportFragmentManager(),"DuplicateAlertDialogFragment");
    }



    //新規にタイトルを作成する場合
    private  void setNewTitleProperties() {

        mViewTitle.setText("タイトル");
        mEditTitle.setText("タイトル");
        mViewAuthor.setText("著者名");
        mEditAuthor.setText("著者名");
        mQuotationInitial = new Quotations();
        mQuotationInitial.setTitle("タイトル");
        mQuotationInitial.setAuthorName("著者名");
        mRhombusMaterial.setVisibility(View.GONE);

    }

    private  void setNewQuotationProperties() {

        mQuotationInitial = getIntent().getParcelableExtra("create_quotation");
        mViewTitle.setText(mQuotationInitial.getTitle());
        mEditTitle.setText(mQuotationInitial.getTitle());
        mViewAuthor.setText(mQuotationInitial.getAuthorName());
        mEditAuthor.setText(mQuotationInitial.getAuthorName());
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
            case R.id.toolbar_back_arrow: {
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            }
            case R.id.toolbar_save_button: {
                Log.d(TAG, "保存ボタンがタップされました");
                disableEditMode();
                break;
            }
            case R.id.quotation_text_title: {
                Log.d(TAG, "タイトルがタップされました");

                if(mIsNewTitle) {
                    enableTitleEditMode();
                    mEditTitle.requestFocus();
                    mEditTitle.setSelection(mEditTitle.length());
                }else{
                   String mToastText = getString(R.string.edit_forbidden_toast);
                    Toast toast = Toast.makeText(QuotationCreateActivity.this,mToastText,Toast.LENGTH_LONG);
                    toast.show();
                }
                break;

            }
            case R.id.quotation_text_authorname: {
                Log.d(TAG, "著者名がタップされました");
                if(mIsNewTitle) {
                    enableAuthorEditMode();
                    mEditAuthor.requestFocus();
                    mEditAuthor.setSelection(mEditAuthor.length());
                }else{
                    Toast.makeText(QuotationCreateActivity.this,"名言新規作成時は編集できません",Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mMODE == EDIT_MODE_ENABLED) {
            onClick(mSave);
            Log.d(TAG, "mSaveButtonがタップされました");
        } else {
            Log.d(TAG, "super.onBackPressed発動");
            super.onBackPressed();
        }
    }


   private class GenericTextWatcherFirst implements TextWatcher {

        private View view;
        public GenericTextWatcherFirst(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()){
                case R.id.quotation_edit_title:
                    mViewTitle.setText(text);
                    break;
                case R.id.quotation_edit_authorname:
                    mViewAuthor.setText(text);
                    break;
            }
        }
    }
}