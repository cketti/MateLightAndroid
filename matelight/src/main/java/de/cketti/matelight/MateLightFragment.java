package de.cketti.matelight;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MateLightFragment extends Fragment {
    public static final String COLOR_PREFIX = "\033[35m";

    private TextView mTextView;
    private Button mSendButton;
    private TextView mStatusText;
    private Button mCancelButton;
    private SendMessageTask mSendMessageTask;
    private Context mContext;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextView = (TextView) view.findViewById(R.id.text);
        mStatusText = (TextView) view.findViewById(R.id.status_text);
        mSendButton = (Button) view.findViewById(R.id.send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        mCancelButton = (Button) view.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelSendMessage();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getApplicationContext();
    }

    private void sendMessage() {
        String text = mTextView.getText().toString();
        mSendMessageTask = new SendMessageTask();
        mSendMessageTask.execute(text);
    }

    private void cancelSendMessage() {
        try {
            mSendMessageTask.cancelSend();
        } catch (Exception e) {
            // ignore
        }
    }


    class SendMessageTask extends AsyncTask<String, Void, String> {
        private volatile MateLight mMateLight;


        public void cancelSend() {
            try {
                cancel(false);
                mMateLight.cancel();
            } catch (Exception e) {
                // ignore
            }
            mSendButton.setEnabled(true);
            mCancelButton.setEnabled(false);
            mStatusText.setText(mContext.getString(R.string.status_cancelled));
        }

        @Override
        protected void onPreExecute() {
            mSendButton.setEnabled(false);
            mCancelButton.setEnabled(true);
            mStatusText.setText(mContext.getString(R.string.status_trying_to_send));
        }

        @Override
        protected String doInBackground(String... params) {
            String text = params[0];
            try {
                mMateLight = new MateLight();
                mMateLight.sendMessage(COLOR_PREFIX + text);

                return null;
            } catch (IOException e) {
                e.printStackTrace();
                String message = e.getMessage();
                return (message != null) ? message : e.getClass().toString();
            }
        }

        @Override
        protected void onPostExecute(String errorMessage) {
            mSendButton.setEnabled(true);
            mCancelButton.setEnabled(false);

            if (errorMessage == null) {
                mStatusText.setText(mContext.getString(R.string.status_ok));
            } else {
                mStatusText.setText(mContext.getString(R.string.status_error, errorMessage));
            }
        }
    }
}
