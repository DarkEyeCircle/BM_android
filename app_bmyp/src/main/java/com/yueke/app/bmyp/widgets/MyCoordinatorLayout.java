package com.yueke.app.bmyp.widgets;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.yueke.app.bmyp.R;

public class MyCoordinatorLayout extends CoordinatorLayout {

    private static String TAG = "MyCoordinatorLayout";

    public MyCoordinatorLayout(Context context) {
        super(context);
    }

    public MyCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int axes, int type) {
        Log.d(TAG, "onStartNestedScroll-MyCoordinatorLayout");
        return super.onStartNestedScroll(child, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(View target, int type) {
        Log.d(TAG, "onStopNestedScroll-MyCoordinatorLayout");
        CoordinatorLayout.LayoutParams params = (LayoutParams) this.findViewById(R.id.dataView).getLayoutParams();
        if (params != null && params.getBehavior() instanceof MyContentBehavior) {
            MyContentBehavior behavior = (MyContentBehavior) params.getBehavior();
            behavior.stop();
        }
    }

    // @Override
//    public void onStopNestedScroll(View target) {
//        /**
//         *
//         * 默认给屏蔽
//         *  final LayoutParams lp = (LayoutParams) view.getLayoutParams();
//         if (!lp.isNestedScrollAccepted()) {
//         continue;
//         }
//         *
//         *  使用 AppBarLayout.ScrollingViewBehavior的onStopNestedScroll可以回调。
//         *
//         */
//        Log.d(TAG, "onStopNestedScroll-MyCoordinatorLayout");
//        super.onStopNestedScroll(target);
//        final int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            final View view = getChildAt(i);
//            final LayoutParams lp = (LayoutParams) view.getLayoutParams();
//            final Behavior viewBehavior = lp.getBehavior();
//            if (viewBehavior != null) {
//                viewBehavior.onStopNestedScroll(this, view, target, ViewCompat.TYPE_TOUCH);
//            }
//        }
//
//    }

}
