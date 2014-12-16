package com.spixsoftware.spixlibrary.tools;

import android.view.View;
import android.view.ViewGroup;

/**
 * Use adapter for any View group as you using adapter for ListView
 */
public abstract class ViewGroupAdapter {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private final ViewGroup parentView;

    // ===========================================================
    // Constructors
    // ===========================================================

    public ViewGroupAdapter(ViewGroup parentView) {
        this.parentView = parentView;
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    public abstract int getCount();

    public abstract View getView(ViewGroup parent, int position);

    public abstract Object getItem(int position);

    // ===========================================================
    // Methods
    // ===========================================================

    public void notifyDataSetChanged() {
        draw();
    }

    private void draw() {
        parentView.removeAllViews();
        for (int position = 0; position < getCount(); position++) {
            parentView.addView(getView(parentView, position));
        }
    }
}

// ===========================================================
// Inner and Anonymous Classes
// ===========================================================
