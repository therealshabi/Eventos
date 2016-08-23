package app.com.thetechnocafe.eventos;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by gurleensethi on 23/08/16.
 */
public class GuidelinesDialog extends DialogFragment {
    public static GuidelinesDialog getInstance() {
        return new GuidelinesDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog mDialog = new Dialog(getContext());
        mDialog.setCancelable(false);

        //Set up the custom view
        View guidelinesView = LayoutInflater.from(getContext()).inflate(R.layout.guidelines, null);

        String guidelinesArray[] = getResources().getStringArray(R.array.guidelines);
        ListView listView = (ListView) guidelinesView.findViewById(R.id.guidelines_list);
        //Create and set adapter
        Adapter adapter = new Adapter(getContext(), R.layout.guidelines_item, guidelinesArray);
        listView.setAdapter(adapter);

        Button mCloseButton = (Button) guidelinesView.findViewById(R.id.guidelines_button);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.setContentView(guidelinesView);
        return mDialog;
    }

    //Custom adapter for list view
    class Adapter extends ArrayAdapter {
        private String[] array;

        public Adapter(Context context, int resource, String[] array) {
            super(context, resource, array);
            this.array = array;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.guidelines_item, parent, false);
            TextView guideLineText = (TextView) view.findViewById(R.id.guidelines_item_text);
            guideLineText.setText(array[position]);
            return view;
        }
    }
}
