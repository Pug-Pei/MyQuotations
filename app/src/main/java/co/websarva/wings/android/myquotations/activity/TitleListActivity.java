package co.websarva.wings.android.myquotations.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import co.websarva.wings.android.myquotations.R;
import co.websarva.wings.android.myquotations.adapters.TitleRecyclerAdapter;
import co.websarva.wings.android.myquotations.persistence.QuotationRepository;
import co.websarva.wings.android.myquotations.persistence.QuotationViewModel;
import co.websarva.wings.android.myquotations.quotations.Quotations;
import co.websarva.wings.android.myquotations.util.ItemDecorator;

public class TitleListActivity extends AppCompatActivity implements
        TitleRecyclerAdapter.OnTitleListener,
        FloatingActionButton.OnClickListener
{

    private static final String TAG ="TitleListActivity";

    //Ui components
    private RecyclerView mRecyclerView;
    private Toolbar toolbar;

    //変数
    private String mBeforeUpdateTitle, mBeforeUpdateAuthorName,mAfterUpdateTitle, mAfterUpdateAuthorName;
    private String mSortVal;
    private boolean mIsDeleteMode;
    private QuotationRepository mQuotationRepository;
    private QuotationViewModel mQuotationViewModel;
    private Quotations mUpdateQuotationInitial,mUpdateQuotationFinal,mAfterUpdateQuotation;
    private ArrayList<Quotations> arrayListAllQuotation = new ArrayList<>();
    private ArrayList<Quotations> arrayListTitles = new ArrayList<>();
    private TitleRecyclerAdapter mListRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_recyclerview);
        Log.d(TAG, "onCreate発動");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        mIsDeleteMode = false;

        mQuotationRepository = new QuotationRepository(this);

        mRecyclerView = findViewById(R.id.title_recyclerview_id);

        //リスナー設定(bottomNavigation)
        findViewById(R.id.navigation_add_title).setOnClickListener(this);
        findViewById(R.id.navigation_title_sort).setOnClickListener(this);
        findViewById(R.id.navigation_title_delete_mode).setOnClickListener(this);
        findViewById(R.id.navigation_description).setOnClickListener(this);

        //RecyclerViewの設定

        initTitleRecyclerView();

        mQuotationViewModel = new ViewModelProvider(this).get(QuotationViewModel.class);
        mQuotationViewModel.getIdOrderByAscQuotations().observe(this, quotations -> {
            Log.d(TAG,"getIdOrderByAscQuotations()発動");
            recyclerViewUpdate(quotations);
        });
    }

    private void initTitleRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        ItemDecorator itemDecorator = new ItemDecorator(15);
        mRecyclerView.addItemDecoration(itemDecorator);

        mListRecyclerAdapter = new TitleRecyclerAdapter(arrayListTitles, this,mIsDeleteMode);
        mRecyclerView.setAdapter(mListRecyclerAdapter);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

    }

    private void deleteModeColorChange(){
        mListRecyclerAdapter = new TitleRecyclerAdapter(arrayListTitles, this,mIsDeleteMode);
        mRecyclerView.setAdapter(mListRecyclerAdapter);
        if (mIsDeleteMode) {
            Log.d(TAG,"削除モードON");
            toolbar.setBackground(new ColorDrawable(getResources().getColor(R.color.deep_red)));
        } else {
            Log.d(TAG,"削除モードOFF");
            toolbar.setBackground(new ColorDrawable(getResources().getColor(R.color.deep_blue)));
        }
    }


        //RecyclerViewの表示を更新
        public void recyclerViewUpdate(List<Quotations> quotations){
            if(arrayListAllQuotation.size() > 0){
                Log.d(TAG, "arrayListTitle.clear発動");
                arrayListAllQuotation.clear();
                arrayListTitles.clear();
            }
            if(arrayListAllQuotation != null){
                Log.d(TAG, "arrayListTitle.addAll発動");
                arrayListAllQuotation.addAll(quotations);
                for(int i = 0; i<arrayListAllQuotation.size(); i++){
                    arrayListAllQuotation.get(i).toString();

                }

                if(getIntent().hasExtra("update_Quotation_Initial")){
                    mUpdateQuotationInitial = getIntent().getParcelableExtra("update_Quotation_Initial");
                    mBeforeUpdateTitle = mUpdateQuotationInitial.getTitle();
                    mBeforeUpdateAuthorName = mUpdateQuotationInitial.getAuthorName();

                    //更新後のインスタンスを受け取り、TitleとAuthorNameを取り出す。
                    mUpdateQuotationFinal = getIntent().getParcelableExtra("update_Quotation_Final");
                    mAfterUpdateTitle = mUpdateQuotationFinal.getTitle();
                    mAfterUpdateAuthorName = mUpdateQuotationFinal.getAuthorName();

                    //更新したインスタンスの更新前のタイトル、著者名と、データベース中の全てのインスタンスの、タイトル、著者名を比較。
                    //タイトル、著者名の両方が更新前のものと一致していれば、そのインスタンスのタイトル、著者名も更新する
                    for(int i = 0; i< arrayListAllQuotation.size(); i++) {
                        mAfterUpdateQuotation = arrayListAllQuotation.get(i);
                        if(mBeforeUpdateTitle.equals(mAfterUpdateQuotation.getTitle())
                                &&
                                mBeforeUpdateAuthorName.equals(mAfterUpdateQuotation.getAuthorName()))
                        {
                            mAfterUpdateQuotation.setTitle(mAfterUpdateTitle);
                            mAfterUpdateQuotation.setAuthorName(mAfterUpdateAuthorName);
                            updateQuotation();
                        }
                    }
                }

                for(int i = 0; i< arrayListAllQuotation.size(); i++){

                    //ArrayList内のQuotationインスタンスを取得
                    Quotations quotation = arrayListAllQuotation.get(i);

                    //取得したインスタンスのContentを取得
                    String quotationContent = quotation.getContent();
                    if(quotationContent.equals("title_instance")){
                        arrayListTitles.add(quotation);
                    }
                }
            }

            //ArrayList内のインスタンスを並び替える
            mQuotationViewModel = new ViewModelProvider(this).get(QuotationViewModel.class);
            mQuotationViewModel.getSortVal().observe(this, mSortVarQuotation -> {

                List<Quotations> listSortQuotation = new ArrayList();
                listSortQuotation.addAll(mSortVarQuotation);

                //並び替えの識別インスタンスから、識別用の文字列を取り出す
                //listSortQuotationの中身は常に１つなのでget(0)でよい
                if(listSortQuotation.isEmpty()){
                    mSortVal = "id_order_by_ASC";
                }else{
                    mSortVal  = listSortQuotation.get(0).getContent();
                }

                switch (mSortVal){
                    case "id_order_by_ASC":
                        Collections.sort(arrayListTitles, new Comparator<Quotations>() {
                            @Override
                            public int compare(Quotations quotations1, Quotations quotations2) {
                                Log.d(TAG,"id_order_by_ASCのCollections.sort発動");
                                return Integer.compare(quotations1.getId(), quotations2.getId());
                            }
                        });
                        break;

                    case "id_order_by_DESC":
                        Collections.sort(arrayListTitles, new Comparator<Quotations>(){
                            @Override
                            public int compare(Quotations quotations1, Quotations quotations2) {
                                Log.d(TAG,"id_order_by_DESCのCollections.sort発動");
                                return Integer.compare(quotations2.getId(),quotations1.getId());
                            }
                        });
                        break;

                    case "title_order_by_ASC":
                        Collections.sort(arrayListTitles, new Comparator<Quotations>() {
                            @Override
                            public int compare(Quotations quotations1, Quotations quotations2) {
                                Collator japaneseCollation = Collator.getInstance(Locale.JAPANESE);
                                return japaneseCollation.compare(quotations1.getTitle(),quotations2.getTitle());
                            }
                        });
                        break;

                    case "title_order_by_DESC":
                        Collections.sort(arrayListTitles, new Comparator<Quotations>() {
                            @Override
                            public int compare(Quotations quotations1, Quotations quotations2) {
                                Collator japaneseCollation = Collator.getInstance(Locale.JAPANESE);
                                return japaneseCollation.compare(quotations2.getTitle(),quotations1.getTitle());
                            }
                        });
                        break;

                    case "author_order_by_ASC":
                        Collections.sort(arrayListTitles, new Comparator<Quotations>() {
                            @Override
                            public int compare(Quotations quotations1, Quotations quotations2) {
                                Collator japaneseCollation = Collator.getInstance(Locale.JAPANESE);
                                return japaneseCollation.compare(quotations1.getAuthorName(),quotations2.getAuthorName());
                            }
                        });
                        break;

                    case "author_order_by_DESC":
                        Collections.sort(arrayListTitles, new Comparator<Quotations>() {
                            @Override
                            public int compare(Quotations quotations1, Quotations quotations2) {
                                Collator japaneseCollation = Collator.getInstance(Locale.JAPANESE);
                                return japaneseCollation.compare(quotations2.getAuthorName(),quotations1.getAuthorName());
                            }
                        });
                        break;
                }
                mListRecyclerAdapter.notifyDataSetChanged();
            });
        }


    @Override
    public void onTitleClick(int position) {
        Log.d(TAG,"onTitleClick:"+ position);
        if(!mIsDeleteMode){
            Intent intent = new Intent(this, QuotationListActivity.class);
            intent.putExtra("selected_title", arrayListTitles.get(position));
            intent.putParcelableArrayListExtra("title_list",arrayListTitles);
            Log.d(TAG,"タップしたタイトル："+arrayListTitles.get(position).getTitle());
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }else{

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.navigation_add_title:{
                Intent intent = new Intent(this, QuotationCreateActivity.class);
                intent.putParcelableArrayListExtra("createNewTitle", arrayListTitles);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            }

            case R.id.navigation_title_sort:{
                Intent intent = new Intent(this,SortTitleActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
                }

            case R.id.navigation_title_delete_mode:{
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

            case R.id.navigation_description:{
                Intent intent = new Intent(this,DescriptionActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            }
        }
    }


    private void updateQuotation(){
        mQuotationRepository.updateQuotationTask(mAfterUpdateQuotation); }


    public void deleteQuotation(Quotations quotations){
        //削除するホルダーのタイトルと著者名を取得
        Log.d(TAG,"削除するインスタンスの中身"+quotations.toString());
        String mDeleteTitle = quotations.getTitle();
        String mDeleteAuthorName = quotations.getAuthorName();

        //右スワイプで削除するホルダーと、タイトルと著者名が同じインスタンスは削除する
        for(int i = 0; i< arrayListAllQuotation.size(); i++){
            if( mDeleteTitle.equals(arrayListAllQuotation.get(i).getTitle())
                &&
                mDeleteAuthorName.equals(arrayListAllQuotation.get(i).getAuthorName())
              )
               {
                   Log.d(TAG,"スワイプしたホルダーと、タイトルと著者名が同じなので削除"+arrayListAllQuotation.get(i).toString());
                   mQuotationRepository.deleteQuotationTask(arrayListAllQuotation.get(i));
            }else{

            }
        }
        //スワイプしたホルダーを削除し、データを更新する
        mListRecyclerAdapter.notifyDataSetChanged();
    }


    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return mIsDeleteMode;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Log.d(TAG,"右スワイプされたので削除します");
            deleteQuotation(arrayListTitles.get(viewHolder.getAdapterPosition()));
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);

            switch (actionState){
                case ItemTouchHelper.ACTION_STATE_SWIPE:
            }
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
    protected void onResume() {
        Log.i(TAG,"onResume();called");
        super.onResume();
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