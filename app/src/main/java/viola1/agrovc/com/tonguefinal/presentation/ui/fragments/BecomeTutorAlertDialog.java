package viola1.agrovc.com.tonguefinal.presentation.ui.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import viola1.agrovc.com.tonguefinal.R;

public class BecomeTutorAlertDialog extends DialogFragment {
    public static BecomeTutorAlertDialog newInstance(String title, String message) {
        BecomeTutorAlertDialog frag = new BecomeTutorAlertDialog();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int action = getArguments().getInt("action");
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");

        return new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //((ScheduleMeetingFragment)getActivity()).doPositiveClick();
                                getActivity().finish();
                                dismiss();
                            }
                        }
                )
                /*.setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((FragmentAlertDialog)getActivity()).doNegativeClick();
                            }
                        }
                )*/
                .create();
    }
}
