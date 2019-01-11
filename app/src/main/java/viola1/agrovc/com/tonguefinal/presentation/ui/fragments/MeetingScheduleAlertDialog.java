package viola1.agrovc.com.tonguefinal.presentation.ui.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import viola1.agrovc.com.tonguefinal.R;

public class MeetingScheduleAlertDialog extends DialogFragment {
    public static MeetingScheduleAlertDialog newInstance(int action, String title, String message) {
        MeetingScheduleAlertDialog frag = new MeetingScheduleAlertDialog();
        //1 - confirm meeting
        //2 - reject/delete meeting
        //3 - scheduled meeting confirmation
        //4 - update meeting schedule

        Bundle args = new Bundle();
        args.putInt("action", action);
        args.putString("title", title);
        args.putString("message", message);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int action = getArguments().getInt("action");
        String title = null;
        String message = null;
        if (action == 3) {
            title = getArguments().getString("title");
            message = getArguments().getString("message");

        } else if (action == 1){
            title = getArguments().getString("title");
            message = getArguments().getString("message");

        }else if (action == 2){
            title = getArguments().getString("title");
            message = getArguments().getString("message");

        }else if (action == 4){
            title = getArguments().getString("title");
            message = getArguments().getString("message");
        }

        return new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //((ScheduleMeetingFragment)getActivity()).doPositiveClick();
                                if (action == 3) {
                                    Fragment prev = getFragmentManager().findFragmentByTag("schedule_meeting_fragment_dialog");
                                    if (prev != null) {
                                        DialogFragment dialogFragment = (DialogFragment) prev;
                                        dialogFragment.dismiss();
                                    }
                                } else if (action == 2) {
                                    dismiss();
                                    /*Fragment prev = getFragmentManager().findFragmentById(R.id.tutorship_requests_fragment_container);
                                    if (prev != null) {
                                        DialogFragment dialogFragment = (DialogFragment) prev;
                                        dialogFragment.dismiss();
                                    }*/
                                } else if (action == 4) {
                                    Fragment prev = getFragmentManager().findFragmentByTag("update_meeting_fragment_dialog");
                                    if (prev != null) {
                                        DialogFragment dialogFragment = (DialogFragment) prev;
                                        dialogFragment.dismiss();
                                    }
                                }

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
