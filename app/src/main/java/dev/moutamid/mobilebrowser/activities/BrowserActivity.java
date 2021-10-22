package dev.moutamid.mobilebrowser.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.util.ArrayList;

import dev.moutamid.mobilebrowser.R;
import dev.moutamid.mobilebrowser.databinding.ActivityBrowserBinding;
import dev.moutamid.mobilebrowser.models.TabsModel;
import dev.moutamid.mobilebrowser.utils.Constants;
import dev.moutamid.mobilebrowser.utils.Helper;
import dev.moutamid.mobilebrowser.utils.Utils;
import im.delight.android.webview.AdvancedWebView;

public class BrowserActivity extends AppCompatActivity implements AdvancedWebView.Listener {
    private static final String TAG = "BrowserActivity";
    private Context context = BrowserActivity.this;

    private AdvancedWebView webView;

    private ActivityBrowserBinding b;
    String selectedSearchEngine = Constants.GOOGLE;
    boolean caching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityBrowserBinding.inflate(getLayoutInflater());
        View view = b.getRoot();
        setContentView(view);

        Utils.changeStatusBarColor(this, R.color.black);

        webView = (AdvancedWebView) view.findViewById(R.id.webview);
        webView.setListener(this, this);
        webView.setMixedContentAllowed(false);
        String searchQuery = getIntent().getStringExtra(Constants.SEARCH_QUERY);
        selectedSearchEngine = getIntent().getStringExtra(Constants.SEARCH_ENGINE);

        caching = Helper.enableSettingsOfWebView(caching, webView);

        //IF USER SELECTED ONE OF THE FEW BUTTONS OF MAIN
        if (selectedSearchEngine.equals("")) {
            selectedSearchEngine = Constants.GOOGLE;
            webView.loadUrl(searchQuery, caching);

        } else {
            webView.loadUrl(selectedSearchEngine + searchQuery, caching);
        }

        addRemoveEditTextListener();

        addSearchViewEditTextListener();

        addSearchQuesryBtnListener();

        addMenuBtnListener();

        Helper.addProgressBarToWebView(webView, b.progressBarBrowser);
    }

    private void addRemoveEditTextListener() {
        b.removedEditTextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.searViewEdittextBrowser.setText("");
                b.searViewEdittextBrowser.requestFocus();
            }
        });

    }

    private void addSearchViewEditTextListener() {
        b.searViewEdittextBrowser.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String newQuery = b.searViewEdittextBrowser.getText().toString();

                    if (newQuery.isEmpty()) {
                        return false;
                    }

                    webView.loadUrl(selectedSearchEngine + newQuery, caching);

                }
                return false;
            }
        });

    }

    boolean isDeskTop = false;

    private void addSearchQuesryBtnListener() {
        b.searchQueryBtnBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newQuery = b.searViewEdittextBrowser.getText().toString();

                if (newQuery.isEmpty()) {
                    return;
                }

                webView.loadUrl(selectedSearchEngine + newQuery, caching);

            }
        });

    }

    private void addMenuBtnListener() {
        b.menuBtnBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final CharSequence[] items = {
                        "Share Url",
                        "Toggle Desktop mode",
                        "Open in other browser",
                        "Settings"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {

                        switch (position) {
                            case 0:
                                shareCurrentUrl();
                                break;
                            case 1:
                                isDeskTop = !isDeskTop;
                                webView.setDesktopMode(isDeskTop);
                                break;
                            case 2:
                                if (AdvancedWebView.Browsers.hasAlternative(context)) {
                                    AdvancedWebView.Browsers.openUrl(
                                            BrowserActivity.this, currentUrl);
                                }
                                break;
                            case 3:
                                startActivity(new Intent(context, SettingsActivity.class));
                                break;
                        }

                    }
                });

                dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void shareCurrentUrl() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Url");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, currentUrl);
        startActivity(Intent.createChooser(intent, "Share using"));
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        Log.d(TAG, "onResume: ");
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        webView.onPause();
        // ...
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        webView.onDestroy();
        // ...
        super.onDestroy();

        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "onActivityResult: ");
        webView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!webView.onBackPressed()) {
            Log.d(TAG, "onBackPressed: webview");
            return;
        }
        // ...
        Log.d(TAG, "onBackPressed: ");
        super.onBackPressed();
    }

    String currentUrl = "null";

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        Log.d(TAG, "onPageStarted: url: " + url);
        b.searViewEdittextBrowser.setText(url);
        currentUrl = url;
    }

    String currentIndex = Utils.getRandomNmbr(99999999);

    @Override
    public void onPageFinished(String url) {
        Log.d(TAG, "onPageFinished: " + url);

        // GETTING BITMAP FROM WEB VIEW
        webView.setDrawingCacheEnabled(true);
        Bitmap bmap = webView.getDrawingCache();

        // STORING THAT BITMAP INTO PREFERENCES
        Utils.store(currentIndex, Helper.BitMapToString(bmap));

        // GETTING TABS ARRAYLIST
        ArrayList<TabsModel> userTabsList = Utils.getArrayList(Constants.USER_TABS, TabsModel.class);

        int count = 0;
        // IF ARRAY LIST ALREADY HAS THAT ITEM THEN REPLACE
        for (int i = 0; i <= userTabsList.size() - 1; i++) {
            if (userTabsList.get(i).getBitmapName().equals(currentIndex)) {
                userTabsList.remove(i);
                userTabsList.add(i, new TabsModel(currentUrl, currentIndex));

                count++;
            }
        }

        if (count == 0) {
            userTabsList.add(new TabsModel(currentUrl, currentIndex));
        }

        // STORE THAT ARRAY LIST INTO PREFERENCES
        Utils.store(Constants.USER_TABS, userTabsList);

        // DISABLING CANVAS DRAWING
        webView.setDrawingCacheEnabled(false);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        Log.d(TAG, "onPageError: description:" + description);
        Log.d(TAG, "onPageError: failingUrl: " + failingUrl);
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
    }

    @Override
    public void onExternalPageRequest(String url) {
        Log.d(TAG, "onExternalPageRequest: url: " + url);
    }
}