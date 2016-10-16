package app.com.thetechnocafe.eventos.Dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gurleensethi on 16/10/16.
 */

public class LoadingDialog extends android.support.v4.app.DialogFragment {
    private static final String LOADING_MESSAGE_TAG = "loadingmessage";

    public static LoadingDialog getInstance(String loadingMessage) {
        LoadingDialog dialog = new LoadingDialog();

        //Set arguments to fragment
        Bundle args = new Bundle();
        args.putString(LOADING_MESSAGE_TAG, loadingMessage);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());

        //Set dialog properties
        dialog.setMessage(getArguments().getString(LOADING_MESSAGE_TAG));


        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
