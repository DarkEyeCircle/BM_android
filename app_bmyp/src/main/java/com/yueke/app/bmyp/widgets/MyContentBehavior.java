package com.yueke.app.bmyp.widgets;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;

import com.yueke.app.bmyp.R;

import java.util.List;

public class MyContentBehavior extends AppBarLayout.ScrollingViewBehavior {

    private static final String TAG = "MyContentBehavior";

    private View mToolbar1;
    private View mToolbar2;
    private AppBarLayout mBarLayout;
    private View mDependency = null;
    private int height = -1;
    private float lastV;

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (height <= 0) {
            height = dependency.getMeasuredHeight();
        }
        float v = (height - Math.abs(dependency.getTop())) * 1.0f / height;
        dependency.setAlpha(v);
        if (mToolbar1 != null && mToolbar2 != null) {
            if (v <= 0) {
                mToolbar1.setVisibility(View.VISIBLE);
                mToolbar2.setVisibility(View.GONE);
                mToolbar1.setAlpha(1);
                mToolbar2.setAlpha(0);
            } else if (v < 0.98f) {
                if (v > 0.5f) {
                    mToolbar1.setVisibility(View.GONE);
                    mToolbar2.setVisibility(View.VISIBLE);
                    mToolbar2.setAlpha(v * 0.5f);
                } else if (v < 0.5f) {
                    mToolbar1.setVisibility(View.VISIBLE);
                    mToolbar2.setVisibility(View.GONE);
                    mToolbar1.setAlpha(1.0f - v);
                }
            } else {
                mToolbar1.setVisibility(View.GONE);
                mToolbar2.setVisibility(View.VISIBLE);
                mToolbar1.setAlpha(0);
                mToolbar2.setAlpha(1);
            }
        }
        lastV = v;
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        if (mBarLayout == null) {
            View rootView = parent.getRootView();
            mToolbar1 = rootView.findViewById(R.id.title_1);
            mToolbar2 = rootView.findViewById(R.id.title_2);
            mBarLayout = findFirstDependency(parent.getDependencies(child));
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        Log.d(TAG, "onStopNestedScroll");
    }

    public void stop() {
        if (lastV < 1f && lastV > 0.55f) {
            //打开
            mBarLayout.setExpanded(true, true);
        } else if (lastV > 0f && lastV <= 0.55f) {
            //关闭
            mBarLayout.setExpanded(false, true);
        }
    }

    /**
     * 查找AppBarLayout
     *
     * @param views
     * @return
     */
    private AppBarLayout findFirstDependency(List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (view instanceof AppBarLayout) {
                return (AppBarLayout) view;
            }
        }
        return null;
    }
}
