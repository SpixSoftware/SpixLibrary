package com.spixsoftware.spixlibrary.tools;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class EditViewChangesListener implements OnEditorActionListener, OnFocusChangeListener, TextWatcher {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private EditText editText;

	private OnTextChanged onTextChanged;

	// ===========================================================
	// Constructors
	// ===========================================================
	public EditViewChangesListener(EditText editText, String text) {
		this.editText = editText;
		this.editText.setText(text);
		editText.setOnEditorActionListener(this);
		editText.setOnFocusChangeListener(this);
		editText.addTextChangedListener(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public void setOnTextChanged(OnTextChanged onTextChanged) {
		this.onTextChanged = onTextChanged;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE || (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
			InputMethodManager inputManager = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			onTextChanged(textView);
			return true;
		}
		return false;
	}

	@Override
	public void onFocusChange(View textView, boolean focused) {
		onTextChanged(editText);
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		onTextChanged(editText);
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void onTextChanged(TextView textView) {
		if (onTextChanged != null) {
			onTextChanged.textChanged(textView, textView.getText().toString());
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface OnTextChanged {
		void textChanged(TextView textView, String text);
	}

}
