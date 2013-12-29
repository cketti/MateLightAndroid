package de.cketti.matelight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;


public class AboutFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_about, null);

        TextView about = (TextView) view.findViewById(R.id.about_text);
        about.setText(Html.fromHtml(getString(R.string.about_text)));
        about.setMovementMethod(LinkMovementMethod.getInstance());

        return new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.about_dialog_title))
                .setPositiveButton(R.string.about_dialog_ok, null)
                .setView(view)
                .create();
    }
}
