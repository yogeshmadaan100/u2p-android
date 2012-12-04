package com.u2p.ui.component;

import com.u2p.ui.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

public class LoginDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		builder.setTitle(R.string.loginDialogTitle);
		
		builder.setView(inflater.inflate(R.layout.login_dialog, null))
			.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// Que hacer cuando haga login
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					LoginDialogFragment.this.getDialog().cancel();
				}
			});
		return builder.create();
	}

}
