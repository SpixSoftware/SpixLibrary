package com.spixsoftware.spixlibrary.tools;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.spixsoftware.spixlibrary.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Use it to, for example save async task instance and restore it when screen is rotating
 */
public class ObjectsInstanceRestorer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private ObjectSaverFragment objectSavedFragment = new ObjectSaverFragment();
	private final FragmentManager fragmentManager;
	private final String tag;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ObjectsInstanceRestorer(FragmentManager fragmentManager, String tag) {
		this.fragmentManager = fragmentManager;
		this.tag = tag;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void restore() {
		ObjectSaverFragment fragment = (ObjectSaverFragment) fragmentManager.findFragmentByTag(tag);
		if (fragment == null) {
			return;
		}
		fragmentManager.beginTransaction().remove(fragment).commit();

		objectSavedFragment = fragment;
	}

	public void putObject(String key, Object objectToSave) {
		objectSavedFragment.putObject(key, objectToSave);
	}

	public void save() {
		objectSavedFragment.show(fragmentManager, tag);
	}

	public Object getObject(String key) {
		return objectSavedFragment.getObject(key);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
    public static class ObjectSaverFragment extends DialogFragment {
        // ===========================================================
        // Constants
        // ===========================================================

        // ===========================================================
        // Fields
        // ===========================================================

        private Map objectsMap = new HashMap<String, Object>();

        // ===========================================================
        // Constructors
        // ===========================================================

        // ===========================================================
        // Getter & Setter
        // ===========================================================

        // ===========================================================
        // Methods for/from SuperClass/Interfaces
        // ===========================================================
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final RelativeLayout root = new RelativeLayout(getActivity());
            root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            final Dialog dialog = new Dialog(getActivity(), R.style.InvisibleDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(root);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            return dialog;
        }

        // ===========================================================
        // Methods
        // ===========================================================

        public void putObject(String key, Object value) {
            objectsMap.put(key, value);
        }

        public Object getObject(String key) {
            return objectsMap.get(key);
        }

        public void clearObjectContainer() {
            objectsMap.clear();
        }
        // ===========================================================
        // Inner and Anonymous Classes
        // ===========================================================

    }


}
