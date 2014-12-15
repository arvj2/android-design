package com.jvra.demos.content;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.jvra.demos.R;

/**
 * Created by Jansel R. Abreu (Vanwolf) on 12/15/2014.
 */
public class ClipboardSample extends Activity {

    private ClipboardManager mClipBoard;

    private Spinner spinner;
    TextView mimeTypes;
    TextView dataText;

    CharSequence styledText;

    String plainText;
    String htmlText;
    String htmlPlainText;

    ClipboardManager.OnPrimaryClipChangedListener mPrimaryClipChangedListener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @Override
        public void onPrimaryClipChanged() {
            updateClipData(true);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.clipboard);
        mClipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        TextView tv;
        styledText = getString(R.string.styled_text);
        tv = (TextView) findViewById(R.id.styled_text);
        tv.setText(styledText);

        plainText = styledText.toString();
        tv = (TextView) findViewById(R.id.plain_text);
        tv.setText(plainText);

        htmlText = "<b>Link:</b> <a href=\"http://www.android.com\">Android</a>";
        htmlPlainText = "Link: http://www.android.com";
        tv = (TextView) findViewById(R.id.html_text);
        tv.setText(htmlText);

        spinner = (Spinner) findViewById(R.id.clip_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.clip_data_types, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateClipData(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mimeTypes = (TextView) findViewById(R.id.clip_mime_types);
        dataText = (TextView) findViewById(R.id.clip_text);

        mClipBoard.addPrimaryClipChangedListener(mPrimaryClipChangedListener);
        updateClipData(true);

    }

    @Override
    protected void onDestroy() {
        mClipBoard.removePrimaryClipChangedListener(mPrimaryClipChangedListener);
    }


    public void pasteStyledText(View button) {
        mClipBoard.setPrimaryClip(ClipData.newPlainText("Styled Text", styledText));
    }


    public void pastePlainText(View button) {
        mClipBoard.setPrimaryClip(ClipData.newPlainText("Styled Text", plainText));
    }

    public void pasteHtmlText(View button) {
        mClipBoard.setPrimaryClip(ClipData.newHtmlText("HTML Text", htmlPlainText, htmlText));
    }

    public void pasteIntent(View button) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.android.com/"));
        mClipBoard.setPrimaryClip(ClipData.newIntent("View Intent", intent));
    }


    public void pasteUri(View button) {
        mClipBoard.setPrimaryClip(ClipData.newRawUri("URI", Uri.parse("http://www.android.com/")));
    }


    private void updateClipData(boolean updateType) {
        ClipData clip = mClipBoard.getPrimaryClip();
        String[] lMimeTypes = null != clip ? clip.getDescription().filterMimeTypes("*/*") : null;

        if (null != lMimeTypes) {
            mimeTypes.setText("");
            for (int i = 0; i < lMimeTypes.length; ++i) {
                if (0 < i) {
                    mimeTypes.append("\n");
                }
                mimeTypes.append(lMimeTypes[i]);
            }
        } else {
            mimeTypes.setText("NULL");
        }

        if (updateType) {
            if (clip != null) {
                ClipData.Item item = clip.getItemAt(0);
                if (item.getHtmlText() != null) {
                    spinner.setSelection(2);
                } else if (item.getText() != null) {
                    spinner.setSelection(1);
                } else if (item.getIntent() != null) {
                    spinner.setSelection(3);
                } else if (item.getUri() != null) {
                    spinner.setSelection(4);
                } else {
                    spinner.setSelection(0);
                }
            } else {
                spinner.setSelection(0);
            }
        }

        if (clip != null) {
            ClipData.Item item = clip.getItemAt(0);
            switch (spinner.getSelectedItemPosition()) {
                case 0:
                    dataText.setText("(No data)");
                    break;
                case 1:
                    dataText.setText(item.getText());
                    break;
                case 2:
                    dataText.setText(item.getHtmlText());
                    break;
                case 3:
                    dataText.setText(item.getIntent().toUri(0));
                    break;
                case 4:
                    dataText.setText(item.getUri().toString());
                    break;
                case 5:
                    dataText.setText(item.coerceToText(this));
                    break;
                case 6:
                    dataText.setText(item.coerceToStyledText(this));
                    break;
                case 7:
                    dataText.setText(item.coerceToHtmlText(this));
                    break;
                default:
                    dataText.setText("Unknown option: " + spinner.getSelectedItemPosition());
                    break;
            }
        } else {
            dataText.setText("(NULL clip)");
        }
        dataText.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
