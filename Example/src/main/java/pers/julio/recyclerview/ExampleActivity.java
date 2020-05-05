package pers.julio.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pers.julio.notepad.recyclerview.Base.BaseViewHolder;
import pers.julio.notepad.recyclerview.Base.MyBaseAdapter;
import pers.julio.notepad.recyclerview.Base.MyBaseDecoration;
import pers.julio.notepad.recyclerview.Bean.BaseItemBean;
import pers.julio.notepad.recyclerview.Bean.DividerBean;
import pers.julio.notepad.recyclerview.MyConfigurableDividerLookup;
import pers.julio.notepad.recyclerview.MySimpleDividerLookup;
import pers.julio.notepad.recyclerview.Utils.ColorUtil;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ExampleActivity extends AppCompatActivity {
    private boolean mDecorationFlag = false;
    private boolean mIsGridLayout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView(this);
        initTabLayout();
    }
    private TabLayout mTabLayout;
    private void initTabLayout() {
        mTabLayout = findViewById(R.id.tab_shortcuts);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabTextColors(Color.GRAY, Color.BLUE);
        mTabLayout.setSelectedTabIndicatorColor(Color.BLUE);
        mTabLayout.setSelectedTabIndicatorHeight(5);
        //ViewCompat.setElevation(mTabLayout, 10);
        //mTabLayout.setupWithViewPager(mContentVp);

       /* int tab_size = TAB_NAMES.length;
        for (int i = 0; i < tab_size; i++) {
            TabLayout.Tab itemTab = mTabLayout.newTab();
            itemTab.setTag(i);
            itemTab.setText(TAB_NAMES[i]);
            //View view = LayoutInflater.from(activity).inflate(R.layout.tab_item_text, null);
            //itemTab.setCustomView(view);
            mTabLayout.addTab(itemTab);
            if (i == mTabIndex) itemTab.select();
        }*/
        mTabLayout.getTabAt(2).select();
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = mTabLayout.getSelectedTabPosition();
                switch (index){
                    case 0:  // 设置分割线
                        SetDecoration();
                        break;
                    case 1:  // 设置 布局风格
                        SwitchLayoutStyle();
                        break;
                    case 2:   // 数据刷新
                        refreshDatas(LoadDatas());
                        break;
                    case 3:   // 切换到多选状态
                        mIsMultiselect = true;
                        setDatas(LoadDatas(), mIsMultiselect);
                        break;
                    case 4:   // 切换到单选状态
                        mIsMultiselect = false;
                        setDatas(LoadDatas(), mIsMultiselect);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /*** 设置不同的 分割线*/
    private void SetDecoration() {
        MyBaseDecoration decoration = new MyBaseDecoration();
        int pedding = ColorUtil.dp2px(ExampleActivity.this,5);
        mDecorationFlag = !mDecorationFlag;
        if (mDecorationFlag) {
            if (mDividers == null) { mDividers = new ArrayList<>(); }
            getDividers(mDatas);
            decoration.setDividerLookup(new MyConfigurableDividerLookup(mDividers));
        } else {
            int color = ExampleActivity.this.getResources().getColor(R.color.myDecoration_light);
            decoration.setDividerLookup(new MySimpleDividerLookup(1, color, pedding, pedding));
        }
        setItemDecoration(decoration);
    }
    /*** 切换 列表和宫格显示（需改变 A: LayoutManager, B: Layout）*/
    private void SwitchLayoutStyle() {
        if (mRecyclerView == null) return;
        mIsGridLayout = !mIsGridLayout;
        SetGridLayoutManager(ExampleActivity.this,mIsGridLayout,3, null);
        mAdapter = new MyAdapter(ExampleActivity.this, mIsGridLayout ? R.layout.base_item_grid : R.layout.base_item_list, mDatas, false);
        mRecyclerView.setAdapter(mAdapter);
    }


    private RecyclerView mRecyclerView;
    private ArrayList<BaseItemBean> mDatas;
    private MyBaseAdapter<BaseItemBean> mAdapter;
    private void initRecyclerView(Context context) {
        mRecyclerView = findViewById(R.id.super_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mDatas = LoadDatas();
        mAdapter = new MyAdapter(context, R.layout.base_item, mDatas, false);
        /*mAdapter = new MyBaseAdapter<BaseItemBean>(context, R.layout.base_item, mDatas, false) {
            @Override
            public void bindViewHolder(final BaseViewHolder holder, final BaseItemBean itemBean, final int pos) {
                holder.setImageView(R.id.base_item_logo, itemBean.Logo);
                holder.setTextView(R.id.base_item_text, itemBean.Text);
                holder.setTextView(R.id.base_item_key, itemBean.Key);
                holder.setTextView(R.id.base_item_value, itemBean.Value);
                holder.setVisibility(R.id.base_item_state, itemBean.ShowState ? View.VISIBLE : View.INVISIBLE);
                if(itemBean.ShowState){ holder.setViewSelected(R.id.base_item_state, itemBean.Selected); }
                holder.setClickListener(R.id.base_item_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HandleSelectionEvents(holder, itemBean, pos);
                        if(mListener!=null) {  mListener.onItemClick(holder,itemBean,pos); }
                    }
                });
            }
        };*/
        mRecyclerView.setAdapter(mAdapter);
    }

    /**  刷新数据*/
    public void refreshDatas(ArrayList<BaseItemBean> datas) {
        if (mDividers != null) { getDividers(datas); }
        mAdapter.refreshDatas(datas);
    }
    private ArrayList<BaseItemBean> LoadDatas() {
        ArrayList<BaseItemBean> datas = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i < 21; i++) {
            datas.add(BaseItemBean.builder(this).setId(i).setText("Name_" + i).setKey("Key_" + i).setValue("Value_" + i).setLogo(R.drawable.ic_test).setShowState(true).setSelected(random.nextInt(10)<5).build());
        }
        return datas;
    }

    /** 主动 设置 分割线
     * @param decoration  null 时 不显示分割线
     */
    public void setItemDecoration(MyBaseDecoration decoration) {
        if(mRecyclerView==null) return;
        //mRecyclerView.invalidateItemDecorations();
        if (decoration != null) {
            mRecyclerView.addItemDecoration(decoration);
        }
    }

    private ArrayList<DividerBean> mDividers;
    private  void getDividers(ArrayList<BaseItemBean> datas) {
        if(datas == null || datas.size() == 0) { mDividers.clear();return; }
        //int dividerColor = getResources().getColor(R.color.myDecoration);
        int padding = ColorUtil.dp2px(this, 5);
        int itemSize = datas.size();
        for (int i = 0; i < itemSize; i++) {
            int flag = i % 3;
            if (flag == 1) {
                mDividers.add(new DividerBean(ColorUtil.dp2px(this, 5), Color.GREEN, padding, padding));
            }else if (flag == 2) {
                mDividers.add(new DividerBean(ColorUtil.dp2px(this,5), Color.BLUE, padding, padding));
            } else {
                mDividers.add(new DividerBean(ColorUtil.dp2px(this,5), Color.RED, padding, padding));
            }
        }
    }
    /** 更换 布局管理器 分配规则
     * @param span            指定 列数
     * @param spanSizeLookup  指定 Span 分配规则
     */
    private GridLayoutManager mGridLayoutManager;
    private boolean SetGridLayoutManager(Context context, final boolean isStyleGrid, final int span, GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        if(mGridLayoutManager == null)  {
            mGridLayoutManager = new GridLayoutManager(context, span);
        }else {
            mGridLayoutManager.setSpanCount(span);
        }
        if(spanSizeLookup == null){
            spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) { return isStyleGrid ? 1 : span; }
            };
        }
        mGridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        if(mRecyclerView != null){ mRecyclerView.setLayoutManager(mGridLayoutManager); }
        return mRecyclerView != null;
    }

    private class MyAdapter extends MyBaseAdapter<BaseItemBean> {
        public MyAdapter(Context context, int layout, ArrayList<BaseItemBean> datas, boolean useDataAddress) {
            super(context,layout, datas, useDataAddress);
        }
        @Override
        public void bindViewHolder(final BaseViewHolder holder, final BaseItemBean itemBean, final int pos) {
            holder.setImageView(R.id.base_item_logo, itemBean.Logo);
            holder.setTextView(R.id.base_item_text, itemBean.Text);
            holder.setTextView(R.id.base_item_key, itemBean.Key);
            holder.setTextView(R.id.base_item_value, itemBean.Value);
            holder.setVisibility(R.id.base_item_state, itemBean.ShowState ? View.VISIBLE : View.INVISIBLE);
            if(itemBean.ShowState){ holder.setViewSelected(R.id.base_item_state, itemBean.Selected); }
            holder.setClickListener(R.id.base_item_root, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HandleSelectionEvents(holder, itemBean, pos);
                    if(mListener!=null) {  mListener.onItemClick(holder,itemBean,pos); }
                }
            });
        }
    }

    private Listener mListener = null;
    public void setListener(Listener listener) { mListener = listener; }
    public interface Listener {
        void onItemClick(BaseViewHolder holder, BaseItemBean itemBean, int pos);
        void onViewClick(BaseViewHolder holder, BaseItemBean itemBean, final int pos, int viewId);
        //void onConfirm(View view, boolean flag);
    }

    //////////////////////////  以下是处理 列表 单选/多选 封装的逻辑代码 //////////////////////////
    public HashMap<Object,Boolean> getMultiSelectMap(){ return mMultiSelectMap; }
    public ArrayList<BaseItemBean> getNewAddList() { return mNewAddList; }
    public ArrayList<BaseItemBean> getNewRemoveList() { return mNewRemoveList; }

    // 以下对多选时 保存的数据
    private ArrayList<BaseItemBean> mNewAddList;    //  针对 已选项 外 的条目 标记为新增状态；
    private ArrayList<BaseItemBean> mNewRemoveList; //  针对 已选项 中 的条目 标记为移除状态；
    private ArrayList<BaseItemBean> mInitSelList = new ArrayList<>();   // 加载前 已选择的条目
    private HashMap<Object,Boolean> mMultiSelectMap = new HashMap<>();

    private int curIndex = -1;  // 单选状态 记录当前选择的条目编号  默认 为都没选中
    private boolean mIsMultiselect = false;

    /** 有单选/多选 需求 时 数据的更新
     * @param datas
     * @param isMultiselect
     */
    public void setDatas(ArrayList<BaseItemBean> datas, boolean isMultiselect){
        if(mDatas == null) mDatas = new ArrayList<>();
        mDatas.clear();
        mDatas.addAll(datas);
        mIsMultiselect = isMultiselect;
        if (mIsMultiselect) {
            buildMultiSelectMap();
        }
        refreshDatas(mDatas);
    }
    public void buildMultiSelectMap() {
        if (mDatas != null && mDatas.size() > 0) {
            mMultiSelectMap.clear();
            int size = mDatas.size();
            for (int i = 0; i < size; i++) {
                BaseItemBean itemBean = mDatas.get(i);
                if(itemBean!=null) {
                    mMultiSelectMap.put(itemBean.Id, itemBean.Selected);
                    if (itemBean.Selected) { mInitSelList.add(itemBean); }
                }
            }
            mNewAddList = new ArrayList<>();
            mNewRemoveList = new ArrayList<>();
        }
    }

    /** 处理 单选/多选 的数据收集 以及 条目状态的局部 更新逻辑
     * @param itemBean
     * @param holder
     * @param pos
     */
    private void HandleSelectionEvents(BaseViewHolder holder, BaseItemBean itemBean, int pos) {
        if(mIsMultiselect){
            itemBean.Selected = !itemBean.Selected;
            if(itemBean.ShowState){ holder.setViewSelected(R.id.base_item_state, itemBean.Selected); }

            // 如果处于多选状态，则 通常需要 记录 增加/和删除的条目，以便最后 统一处理；
            mMultiSelectMap.put(itemBean.Id, itemBean.Selected);
            if(itemBean.Selected){   // 状态: 未选择-->选择
                if(mInitSelList.contains(itemBean)) {  // 移除 存在于 mNewRemoveList 中的标记
                    mNewRemoveList.remove(itemBean);
                }else {
                    mNewAddList.add(itemBean);  // 增加到 NewAddList 中，新增 选中状态
                }
            }else { // 状态: 选择-->未选择
                if(mInitSelList.contains(itemBean)){ // 增加到 mNewRemoveList 中的标记
                    mNewRemoveList.add(itemBean);
                }else {
                    mNewAddList.remove(itemBean);  // 移除 存在于 mNewAddList 中的标记
                }
            }
        }else {   // 如果处于单选状态，则 要作互斥处理；
            if (curIndex != pos) {
                // 取消 上次条目
                if (curIndex != -1 && curIndex < mAdapter.getDatas().size()) {
                    BaseItemBean lastItemBean = mAdapter.getItemData(curIndex);
                    lastItemBean.Selected = false;

                    BaseViewHolder lastHolder = (BaseViewHolder) mRecyclerView.findViewHolderForAdapterPosition(curIndex);
                    if (lastHolder != null && itemBean.ShowState) {
                        lastHolder.setViewSelected(R.id.base_item_state, false);
                    }
                }
                // 更改选中 当前条目
                itemBean.Selected = true;
                if (itemBean.ShowState) {
                    holder.setViewSelected(R.id.base_item_state, true);
                }
                curIndex = pos;
            }
        }
    }


}