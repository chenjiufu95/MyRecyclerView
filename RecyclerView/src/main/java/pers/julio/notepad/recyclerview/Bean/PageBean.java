package pers.julio.notepad.recyclerview.Bean;

/**
 * ClassName:  PageBean
 * Description: TODO
 * Author;  julio_chan  2020/5/5 14:43
 */
public class PageBean {
    private int mTotal = 0;    //总数据条数
    private int mPageIndex = 0;    //当前是第几页
    private int mPageSize = 0;   //一页显示几条数据
    private int mCurCount = 0;    //当前已经显示了几条数据
    private int mDelayed = 0;    //加载延迟


    public int getTotal() {
        return mTotal;
    }
    public PageBean setTotal(int mTotal) {
        this.mTotal = mTotal;
        return this;
    }

    public int getPageIndex() {
        return mPageIndex;
    }
    public PageBean setPageIndex(int mPageIndex) {
        this.mPageIndex = mPageIndex;
        return this;
    }

    public int getPageSize() {
        return mPageSize;
    }
    public PageBean setPageSize(int mPageSize) {
        this.mPageSize = mPageSize;
        return this;
    }

    public int getCurCount() {
        return mCurCount;
    }
    public PageBean setCurCount(int mCurrentCount) {
        this.mCurCount = mCurrentCount;
        return this;
    }

    public int getDelayed() {
        return mDelayed;
    }
    public PageBean setDelayed(int mDelayed) {
        this.mDelayed = mDelayed;
        return this;
    }

    PageBean addIndex() {
        mPageIndex++;
        return this;
    }
}
