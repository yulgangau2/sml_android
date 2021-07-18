package com.eatyhero.customer.base;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.cardview.widget.CardView;
import android.view.View;

/**
 * Created by admin on 24-01-2018.
 */

public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<CardView> {

    private int height;

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, CardView child, int layoutDirection) {
        height = child.getHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       CardView child, @NonNull
                                               View directTargetChild, @NonNull View target,
                                       int axes, int type)
    {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull CardView child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed,
                               @ViewCompat.NestedScrollType int type)
    {
        if (dyConsumed > 0) {
            slideDown(child);
        } else if (dyConsumed < 0) {
            slideUp(child);
        }
    }

    private void slideUp(CardView child) {
        child.clearAnimation();
        child.animate().translationY(0).setDuration(100);
    }

    private void slideDown(CardView child) {
        child.clearAnimation();
        child.animate().translationY(height).setDuration(100);
    }
}
