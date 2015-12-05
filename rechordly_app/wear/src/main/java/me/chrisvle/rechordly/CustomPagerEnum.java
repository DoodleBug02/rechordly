package me.chrisvle.rechordly;

public enum CustomPagerEnum {

    DONE(R.string.Done, R.layout.activity_volume) ; //dummy for now
////    LYRIC(R.string.Lyric, R.layout.view_blue),
////    CROP(R.string.Lyric, R.layout.view_orange);
////    FILTER();
////    ECHO();

    private int mTitleResId;
    private int mLayoutResId;

    CustomPagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}