package co.websarva.wings.android.myquotations.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import co.websarva.wings.android.myquotations.R;
import co.websarva.wings.android.myquotations.adapters.QuotationRecyclerAdapter;
import co.websarva.wings.android.myquotations.persistence.QuotationRepository;
import co.websarva.wings.android.myquotations.persistence.QuotationViewModel;
import co.websarva.wings.android.myquotations.quotations.Quotations;
import co.websarva.wings.android.myquotations.util.DuplicateAlertDialogFragment;
import co.websarva.wings.android.myquotations.util.ItemDecorator;
import co.websarva.wings.android.myquotations.util.Utility;

public class QuotationListActivity extends AppCompatActivity implements
        QuotationRecyclerAdapter.OnQuotationListener,
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener {

    private static final String TAG ="QuotationListActivity";

    private static final int EDIT_MODE_ENABLED = 1;

    //Ui??????
    private RecyclerView mRecyclerView;
    private Toolbar toolbar;
    private TextView mViewTitle;
    private EditText mEditTitle;
    private TextView mViewAuthor;
    private EditText mEditAuthor;
    private RelativeLayout mCheckContainer,mBackArrowContainer;
    private LinearLayout mLinearLayoutToolBar;
    private ImageButton mBackArrow;
    private Button mSave;
    private BottomNavigationItemView mBottomNavigationAddQuotation;
    private BottomNavigationItemView mBottomNavigationSort;
    private BottomNavigationItemView mBottomNavigationDelete;
    private BottomNavigationItemView mBottomNavigationDescription;

    //??????
    private String mTappedTitle;
    private String mSortVal;
    private boolean mIsNewTitle,mIsUpdate;
    private boolean mIsTitleDuplicate;
    private boolean mIsDeleteMode;
    private Quotations mQuotationInitial;
    private Quotations mQuotationFinal;
    private Quotations mFromIntentQuotation;
    private QuotationRepository mQuotationRepository;
    private QuotationViewModel mQuotationViewModel;
    private ArrayList<Quotations> arrayListQuotation = new ArrayList<>();
    private List<Quotations> arrayListAllQuotation = new ArrayList<>();
    private List<String> forDuplicateCheckTitleList = new ArrayList<>();
    private QuotationRecyclerAdapter mQuotationRecyclerAdapter;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate??????");
    setContentView(R.layout.activity_quotations_recyclerview);

    //????????????????????????????????????????????????
    mViewTitle = findViewById(R.id.quotation_text_title);
    mEditTitle = findViewById(R.id.quotation_edit_title);
    mViewAuthor = findViewById(R.id.quotation_text_authorname);
    mEditAuthor = findViewById(R.id.quotation_edit_authorname);


    //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    mCheckContainer = findViewById(R.id.button_container);
    mSave = findViewById(R.id.toolbar_save_button);
    mBackArrowContainer = findViewById(R.id.back_arrow_container);
    mBackArrow = findViewById(R.id.toolbar_back_arrow);
    mLinearLayoutToolBar = findViewById(R.id.layout_quotation_toolbar);
    mBottomNavigationAddQuotation = findViewById(R.id.navigation_add_quotation);
    mBottomNavigationSort = findViewById(R.id.navigation_quotation_sort);
    mBottomNavigationDelete = findViewById(R.id.navigation_quotation_delete_mode);
    mBottomNavigationDescription = findViewById(R.id.navigation_quotation_description);

    //???????????????
    toolbar = findViewById(R.id.toolbar);
    toolbar.setTitle(R.string.app_name);
    toolbar.setTitleTextColor(Color.WHITE);
    setSupportActionBar(toolbar);

    //???????????????
    mQuotationRepository = new QuotationRepository(this);

    //ViewModel
    mQuotationViewModel = new ViewModelProvider(this).get(QuotationViewModel .class);
        mQuotationViewModel.getIdOrderByAscQuotations().observe(this, quotations ->{
            QuotationListRecyclerviewUpdate(quotations);
        } );

        setListeners();

        getTitleIntent();

        //???????????????????????????????????????????????????????????????View Mode???
        setQuotationProperties();
        disableEditMode();

        //RecyclerView
        mRecyclerView = findViewById(R.id.quotations_recyclerview_id);
        initQuotationRecyclerView();

    }


    private void setListeners(){
        mGestureDetector = new GestureDetector(this, this);
        mSave.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mViewTitle.setOnClickListener(this);
        mViewAuthor.setOnClickListener(this);
        mBottomNavigationAddQuotation.setOnClickListener(this);
        mBottomNavigationSort.setOnClickListener(this);
        mBottomNavigationDelete.setOnClickListener(this);
        mBottomNavigationDescription.setOnClickListener(this);
        mEditTitle.addTextChangedListener(new QuotationListActivity.GenericTextWatcherSecond(mEditTitle));
        mEditAuthor.addTextChangedListener(new QuotationListActivity.GenericTextWatcherSecond(mEditAuthor));
    }

    private void getTitleIntent() {
        mQuotationInitial = new Quotations();
        //???????????????RecyclerView?????????????????????????????????????????????????????????????????????????????????????????????mQuotationInitial???????????????
        mFromIntentQuotation = getIntent().getParcelableExtra("selected_title");
        mQuotationInitial.setTitle(mFromIntentQuotation.getTitle());
        mQuotationInitial.setAuthorName(mFromIntentQuotation.getAuthorName());
        mQuotationInitial.setContent(mFromIntentQuotation.getContent());
        mQuotationInitial.setId(mFromIntentQuotation.getId());

    }

    private void setQuotationProperties(){
        mViewTitle.setText(mQuotationInitial.getTitle());
        mEditTitle.setText(mQuotationInitial.getTitle());
        mViewAuthor.setText(mQuotationInitial.getAuthorName());
        mEditAuthor.setText(mQuotationInitial.getAuthorName());
    }


    //????????????????????? + ????????????????????????????????????????????????
    // ??????Check???????????????????????????????????????????????????
    private void disableEditMode(){

        mCheckContainer.setVisibility(View.GONE);
        mSave.setVisibility(View.GONE);

        mBackArrowContainer.setVisibility(View.VISIBLE);
        mBackArrow.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);
        mViewAuthor.setVisibility(View.VISIBLE);
        mEditAuthor.setVisibility(View.GONE);

        String textTitle = mEditTitle.getText().toString();
        String textAuthor = mEditAuthor.getText().toString();
        String timeStamp = Utility.getCurrentTimeStamp();

        //???????????????????????????????????????????????????
        textTitle = textTitle.replace("\n","");
        textTitle = textTitle.replace(" ","");
        textAuthor = textAuthor.replace("\n","");
        textAuthor = textAuthor.replace(" ","");

        if(textTitle.length()>0||textAuthor.length()>0){
            mQuotationFinal = new Quotations();//8.22??????
            mQuotationFinal.setTitle(mEditTitle.getText().toString());
            mQuotationFinal.setAuthorName(mEditAuthor.getText().toString());
            mQuotationFinal.setTimeStamp(timeStamp);

            // mQuotationInitial??????title_instance???????????????????????????
            // content??????title_instance?????????????????????????????????????????????????????????????????????????????????????????????set????????????
            if(!mQuotationFinal.getTitle().equals(mQuotationInitial.getTitle())
                    ||!mQuotationFinal.getAuthorName().equals(mQuotationInitial.getAuthorName())){
                        if(mQuotationFinal.getTitle().isEmpty()){
                            mQuotationFinal.setTitle(mQuotationInitial.getTitle());
                        }
                        if(mQuotationFinal.getAuthorName().isEmpty()){
                            mQuotationFinal.setAuthorName(mQuotationInitial.getAuthorName());
                        }

                titleDuplicateCheck();

                if(!mIsTitleDuplicate){
                    Log.d(TAG,"saveChanges()??????");
                    saveChanges();
                }else {
                    enableTitleEditMode();
                    duplicateTitleAlert();
                }
            }
        }
    }

    private void initQuotationRecyclerView(){
        Log.d(TAG, "initQuotationRecyclerView??????");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        ItemDecorator itemDecorator = new ItemDecorator(15);
        mRecyclerView.addItemDecoration(itemDecorator);
        mQuotationRecyclerAdapter = new QuotationRecyclerAdapter(arrayListQuotation,this,mIsDeleteMode);
        mRecyclerView.setAdapter(mQuotationRecyclerAdapter);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
    }


    private void deleteModeColorChange(){
        mQuotationRecyclerAdapter = new QuotationRecyclerAdapter(arrayListQuotation,this,mIsDeleteMode);
        mRecyclerView.setAdapter(mQuotationRecyclerAdapter);

        if (mIsDeleteMode) {
            Log.d(TAG,"???????????????ON");
            toolbar.setBackground(new ColorDrawable(getResources().getColor(R.color.deep_red)));
            mLinearLayoutToolBar.setBackground(new ColorDrawable(getResources().getColor(R.color.deep_red)));

        } else {
            Log.d(TAG,"???????????????OFF");
            toolbar.setBackground(new ColorDrawable(getResources().getColor(R.color.deep_blue)));
            mLinearLayoutToolBar.setBackground(new ColorDrawable(getResources().getColor(R.color.deep_blue)));
        }
    }

    public void QuotationListRecyclerviewUpdate(List<Quotations> quotations){
        if(arrayListAllQuotation.size() > 0){
            Log.d(TAG, "arrayListQuotation.clear??????");
            arrayListAllQuotation.clear();
            arrayListQuotation.clear();
        }

        if(arrayListAllQuotation != null){

            arrayListAllQuotation.addAll(quotations);

            Log.d(TAG, "arrayListQuotation.addAll??????");

            mFromIntentQuotation = getIntent().getParcelableExtra("selected_title");
            mTappedTitle = mFromIntentQuotation.getTitle();

            for(int i = 0; i< arrayListAllQuotation.size(); i++){

                //ArrayList??????Quotation???????????????????????????
                Quotations quotation = arrayListAllQuotation.get(i);

                //??????????????????????????????????????????????????????
                String quotationTitle = quotation.getTitle();
                Log.d(TAG,"??????????????????????????????" + mTappedTitle);
                Log.d(TAG,"?????????"+quotation.toString());

                //??????????????????????????????Title?????????????????????Content???"title_instance"?????????????????????????????????arrayListSelectTitles????????????
                if(quotationTitle.equals(mTappedTitle)&&!quotation.getContent().equals("title_instance")){
                    arrayListQuotation.add(quotation);
                }else{
                    forDuplicateCheckTitleList.add(quotationTitle);
                }
            }
        }

        //ArrayList??????????????????????????????????????????
        mQuotationViewModel = new ViewModelProvider(this).get(QuotationViewModel.class);
        mQuotationViewModel.getSortQuotationList().observe(this, mSortQuotationList -> {

            List<Quotations> listSortQuotation = new ArrayList();
            listSortQuotation.addAll(mSortQuotationList);

            //????????????????????????????????????????????????????????????????????????????????????
            //listSortQuotation?????????????????????????????????get(0)?????????
            if(listSortQuotation.isEmpty()){
                mSortVal = "Quotation_order_by_ASC";
            }else{
                mSortVal  = listSortQuotation.get(0).getContent();
            }

            switch (mSortVal){

                case "Quotation_id_order_by_ASC":
                    Collections.sort(arrayListQuotation, new Comparator<Quotations>() {
                        @Override
                        public int compare(Quotations quotations1, Quotations quotations2) {
                            Log.d(TAG,"Quotation_id_order_by_ASC???Collections.sort??????");
                            return Integer.compare(quotations1.getId(),quotations2.getId());
                        }
                    });
                    break;

                case "Quotation_id_order_by_DESC":
                    Collections.sort(arrayListQuotation, new Comparator<Quotations>() {
                        @Override
                        public int compare(Quotations quotations1, Quotations quotations2) {
                            Log.d(TAG,"Quotation_id_order_by_DESC???Collections.sort??????");
                            return Integer.compare(quotations2.getId(),quotations1.getId());
                        }
                    });
                    break;


                case "Quotation_order_by_ASC":
                    Collections.sort(arrayListQuotation, new Comparator<Quotations>() {
                        @Override
                        public int compare(Quotations quotations1, Quotations quotations2) {
                            Log.d(TAG,"Quotation_order_by_ASC???Collections.sort??????");
                            Collator japaneseCollation = Collator.getInstance(Locale.JAPANESE);
                            return japaneseCollation.compare(quotations1.getContent(),quotations2.getContent());
                        }
                    });
                    break;

                case "Quotation_order_by_DESC":
                    Collections.sort(arrayListQuotation, new Comparator<Quotations>(){
                        @Override
                        public int compare(Quotations quotations1, Quotations quotations2) {
                            Log.d(TAG,"Quotation_order_by_DESC???Collections.sort??????");
                            Collator japaneseCollation = Collator.getInstance(Locale.JAPANESE);
                            return japaneseCollation.compare(quotations2.getContent(),quotations1.getContent());
                        }
                    });
                    break;
            }
            mQuotationRecyclerAdapter.notifyDataSetChanged();
        });
    }


    //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
    private void enableAllEditMode(){
        mSave.setVisibility(View.VISIBLE);
        mBackArrowContainer.setVisibility(View.GONE);
        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);
        mViewAuthor.setVisibility(View.GONE);
        mEditAuthor.setVisibility(View.VISIBLE);

    }

    //??????????????????????????????????????????????????????????????????????????????????????????
    private void enableTitleEditMode(){

        mCheckContainer.setVisibility(View.VISIBLE);
        mSave.setVisibility(View.VISIBLE);

        mBackArrowContainer.setVisibility(View.GONE);
        mBackArrow.setVisibility(View.GONE);

        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        mViewAuthor.setVisibility(View.VISIBLE);
        mEditAuthor.setVisibility(View.GONE);

    }

    //????????????????????????????????????????????????????????????????????????????????????
    private void enableAuthorEditMode(){

        mCheckContainer.setVisibility(View.VISIBLE);
        mSave.setVisibility(View.VISIBLE);

        mBackArrowContainer.setVisibility(View.GONE);
        mBackArrow.setVisibility(View.GONE);

        mViewAuthor.setVisibility(View.GONE);
        mEditAuthor.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

    }



    private void saveChanges(){
        Log.d(TAG,"saveChanges()??????");
        if(mIsNewTitle){
            Log.d(TAG,"saveNewQuotation()");
            //???????????????????????????saveChanges()????????? mIsNewQuotation = false;????????????
            //??????????????????????????????????????????????????????????????????????????????????????????mIsNewQuotation = true??????????????????????????????????????????????????????????????????????????????
            mIsNewTitle = false;
            saveNewQuotation();
        }else {
                mIsUpdate = true;
                Log.d(TAG,"updateQuotation()");
                updateQuotation();
        }
    }



    private void updateQuotation(){
        mQuotationRepository.updateQuotationTask(mQuotationFinal); }

    private void saveNewQuotation(){
        mQuotationRepository.insertQuotationTask(mQuotationFinal); }

    private void deleteQuotation(Quotations quotations){
        arrayListAllQuotation.remove(quotations);
        mQuotationRecyclerAdapter.notifyDataSetChanged();
        mQuotationRepository.deleteQuotationTask(quotations);
    }



    private void titleDuplicateCheck() {
        if(getIntent().hasExtra("selected_title")){
            for (int i = 0; i < forDuplicateCheckTitleList.size(); i++) {
                if (
                        (forDuplicateCheckTitleList.get(i).equals(mQuotationFinal.getTitle()))
                                &&
                                (!mQuotationFinal.getTitle().equals(mQuotationInitial.getTitle()))
                ) {
                    mIsTitleDuplicate = true;
                    break;
                }else{
                    mIsTitleDuplicate = false;
                }
            }
        }
    }


    public void duplicateTitleAlert(){
        DuplicateAlertDialogFragment dialogFragment = new DuplicateAlertDialogFragment();
        dialogFragment.show(getSupportFragmentManager(),"DuplicateAlertDialogFragment");
    }


    @Override
    public void onQuotationClick(View view, int position) {
        if(!mIsDeleteMode){
            disableEditMode();
            Intent intent = new Intent(this, QuotationContentActivity.class);
            intent.putExtra("selected_quotation", arrayListQuotation.get(position));
            intent.putParcelableArrayListExtra("selected_quotation_arrayList", arrayListQuotation);
            Log.d(TAG, "arrayListSelectTitles.get(position):" + arrayListQuotation.get(position).toString());
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }else{

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_arrow:
                Intent intent = new Intent(this,TitleListActivity.class);
                //????????????????????????intent?????????????????????????????????
                if (mIsUpdate){
                    intent.putExtra("update_Quotation_Initial", mQuotationInitial);
                    intent.putExtra("update_Quotation_Final", mQuotationFinal);
                }
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;

            case R.id.toolbar_save_button:
                disableEditMode();
                break;

            case R.id.quotation_text_title:
                Log.d(TAG, "???????????????????????????????????????");
                enableTitleEditMode();
                mEditTitle.requestFocus();
                mEditTitle.setSelection(mEditTitle.length());
                break;

            case R.id.quotation_text_authorname:
                Log.d(TAG, "????????????????????????????????????");
                enableAuthorEditMode();
                mEditAuthor.requestFocus();
                mEditAuthor.setSelection(mEditAuthor.length());
                break;

            case R.id.navigation_add_quotation:
                Log.d(TAG, "??????????????????????????????");
                Intent mAddQuotationIntent = new Intent(this, QuotationCreateActivity.class);
                mAddQuotationIntent.putExtra("create_quotation", mQuotationFinal);
                startActivity(mAddQuotationIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;

            case R.id.navigation_quotation_sort:
                Log.d(TAG, "????????????????????????????????????????????????");
                Intent mQuotationSortIntent = new Intent(this, SortQuotationActivity.class);
                startActivity(mQuotationSortIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;


            case R.id.navigation_quotation_delete_mode:{
                Log.d(TAG,"??????????????????????????????????????????");
                PopupMenu mPopMenu = new PopupMenu(this,view);
                mPopMenu.getMenuInflater().inflate(R.menu.bottom_popup_menu,mPopMenu.getMenu());
                mPopMenu.show();

                mPopMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.popup_menu_delete_mode_on:
                                mIsDeleteMode = true;
                                deleteModeColorChange();
                                break;

                            case R.id.popup_menu_delete_mode_off:
                                mIsDeleteMode = false;
                                deleteModeColorChange();
                                break;
                        }
                        return false;
                    }
                });
                break;
            }

            case R.id.navigation_quotation_description:
                Log.d(TAG, "????????????????????????????????????");
                Intent mDescriptionIntent = new Intent(this,DescriptionActivity.class);
                startActivity(mDescriptionIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
        }
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
        Log.d(TAG,"onDoubleTap: double tapped!");
        enableAllEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void onBackPressed() {
            Log.d(TAG, "super.onBackPressed??????");
            super.onBackPressed();
    }


    private class GenericTextWatcherSecond implements TextWatcher {

        private View view;
        public GenericTextWatcherSecond(View view) {
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


    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return true;
        }

                @Override
                public boolean isItemViewSwipeEnabled() {
                    return mIsDeleteMode;
                }

                @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteQuotation(arrayListQuotation.get(viewHolder.getAdapterPosition()));
        }
    };


    @Override
    protected void onStart() {
        Log.i(TAG,"onStart();called");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG,"onRestart();called");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.i(TAG,"onPause();called");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG,"onStop();called");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG,"onDestroy();called");
        super.onDestroy();
    }
}
