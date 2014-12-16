package com.spixsoftware.spixlibrary.tools;

import android.database.DataSetObserver;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * Animates hiding and displaying the view when scrolling. call {@link #onScroll(android.widget.AbsListView, int, int, int)} in OnScrollListener or setOnScroolListener this class. also
 * {@link #show()} when adapter has no items
 */
public class ListBarHider implements OnScrollListener {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int BAR_POSITION_TOP = 1;
	public static final int BAR_POSITION_BOTTOM = 2;

	public static final int ANIMATION_SPEED = 300;
	// ===========================================================
	// Fields
	// ===========================================================

	private View view;
	private int barPosition;
	private boolean isHiden;
	private boolean isAnimationStarted;
	private int lastFirstVisibleItem = 0;
	private boolean isDataObserverRegistered;

	private boolean isLocked;

	private final AnimationListener onHideAnimationEndListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			isAnimationStarted = true;
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			isAnimationStarted = false;
			view.setVisibility(View.GONE);
		}
	};
	private final AnimationListener onShowAnimationEndListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			isAnimationStarted = true;
			view.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			isAnimationStarted = false;
		}
	};

	// ===========================================================
	// Constructors
	// ===========================================================

	public ListBarHider(View view, int barPosition) {
		this.view = view;
		this.barPosition = barPosition;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public void setView(View view) {
		this.view = view;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public void onScroll(final AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (isLocked) {
			return;
		}
		final int currentFirstVisibleItem = view.getFirstVisiblePosition();
		if (firstVisibleItem == 0) {
			showAggressive();
		} else if (currentFirstVisibleItem > lastFirstVisibleItem) {
			hide();
		} else if (currentFirstVisibleItem < lastFirstVisibleItem) {
			show();
		}
		lastFirstVisibleItem = currentFirstVisibleItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void show() {
		if (isHiden && !isAnimationStarted) {
			startShowAnimation();
			isHiden = false;
		}
	}

	public void showAggressive() {
		if (isHiden) {
			startShowAnimation();
			isHiden = false;
		}
	}

	public void hide() {
		if (!isHiden && !isAnimationStarted) {
			startHideAnimation();
			isHiden = true;
		}
	}

	public void hideAggressive() {
		if (!isHiden) {
			startHideAnimation();
			isHiden = true;
		}
	}

	private void registerDataObserver(final AbsListView absListView) {
		if (absListView.getAdapter() != null && !isDataObserverRegistered) {
			absListView.getAdapter().registerDataSetObserver(new DataSetObserver() {
				@Override
				public void onChanged() {
					super.onChanged();
					if (absListView.getAdapter().getCount() == 0) {
						show();
					}
				}
			});
			isDataObserverRegistered = true;
		}
	}

	private void startShowAnimation() {
		if (barPosition == BAR_POSITION_TOP) {
			apiearFromTopToBottom(view);
		} else {
			apiearFromBottomToTop(view);
		}
	}

	private void startHideAnimation() {
		if (barPosition == BAR_POSITION_TOP) {
			dismisFromBottomToTop(view);
		} else {
			dismisFromTopToBottom(view);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	private void apiearFromTopToBottom(View view) {
		TranslateAnimation animate = new TranslateAnimation(0, 0, -view.getHeight(), 0);
		animate.setDuration(ANIMATION_SPEED);
		animate.setAnimationListener(onShowAnimationEndListener);
		view.startAnimation(animate);
	}

	private void apiearFromBottomToTop(View view) {
		TranslateAnimation animate = new TranslateAnimation(0, 0, view.getHeight(), 0);
		animate.setDuration(ANIMATION_SPEED);
		animate.setAnimationListener(onShowAnimationEndListener);
		view.startAnimation(animate);

	}

	private void dismisFromBottomToTop(View view) {
		TranslateAnimation animate = new TranslateAnimation(0, 0, 0, -view.getHeight());
		animate.setDuration(ANIMATION_SPEED);
		animate.setAnimationListener(onHideAnimationEndListener);
		view.startAnimation(animate);
	}

	private void dismisFromTopToBottom(View view) {
		TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight());
		animate.setDuration(ANIMATION_SPEED);
		animate.setAnimationListener(onHideAnimationEndListener);
		view.startAnimation(animate);
	}

}
