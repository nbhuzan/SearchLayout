package com.pomelo.searchlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huzan on 2016/11/25 11:54.
 */

public class SearchLayout extends ViewGroup {

    public SearchLayout(Context context) {
        this(context, null);
    }

    public SearchLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private List<Integer> listLineHeight = new ArrayList<>();

    private List<List<View>> listView = new ArrayList<>();

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        listLineHeight.clear();
        listView.clear();

        int width = getWidth();

        int heightLine = 0;
        int widthLine = 0;

        int count = getChildCount();
        Log.e("TAG","屏幕宽"+getWidth());
        List<View> listLineView = new ArrayList<>();
        for (int j = 0; j < count ; j++) {
            View child = getChildAt(j);

            Log.e("TAG","行宽"+widthLine);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();
            if(widthLine+childWidth+lp.topMargin+lp.bottomMargin>width-getPaddingLeft()-getPaddingRight()) {
                listLineHeight.add(heightLine);
                listView.add(listLineView);
                listLineView = new ArrayList<>();
                listLineView.add(child);
                heightLine =childHeight+lp.topMargin+lp.bottomMargin;
                widthLine = childWidth+lp.leftMargin+lp.rightMargin;
            }else{
                widthLine+=childWidth+lp.leftMargin+lp.rightMargin;
                heightLine = Math.max(childHeight+lp.topMargin+lp.bottomMargin,heightLine);
                listLineView.add(child);
            }
            if(j==count-1){
                listLineHeight.add(heightLine);
                listView.add(listLineView);
            }
        }
        int left = getPaddingLeft();
        int top = getPaddingTop();

        for (int j = 0; j < listView.size(); j++) {
            listLineView = listView.get(j);
            heightLine = listLineHeight.get(j);
            for (int k = 0; k <listLineView.size() ; k++) {
                View child = listLineView.get(k);
                if(child.getVisibility()==GONE){
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int cl = left+lp.leftMargin;
                int ct = top+lp.topMargin;
                int cr = cl+child.getMeasuredWidth();
                int cb = ct+child.getMeasuredHeight();
                child.layout(cl,ct,cr,cb);
                left +=lp.leftMargin+child.getMeasuredWidth()+lp.rightMargin;
            }
            top+=heightLine;
            left = getPaddingLeft();
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        int widthLine = 0;
        int heightLine = 0;

        int count = getChildCount();

        for (int i = 0; i <count ; i++) {
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
            int childHeight = child.getMeasuredHeight()+lp.bottomMargin+lp.topMargin;

            if(childWidth+widthLine>widthSize-getPaddingRight()-getPaddingLeft()){
                width = Math.max(width,widthLine);
                widthLine = childWidth;
                height += heightLine;
            }else{
                width+=childWidth;
                heightLine = Math.max(heightLine,childHeight);
            }

            if(i==count-1){
                height +=heightLine;
            }

        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width, heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
