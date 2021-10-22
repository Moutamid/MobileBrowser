package dev.moutamid.mobilebrowser.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;

import im.delight.android.webview.AdvancedWebView;

public class Helper {

    public static Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("HELP", "BitMapToString: temp: "+temp);
        return temp;
    }

    public static boolean enableSettingsOfWebView(boolean caching, AdvancedWebView webView) {

        webView.getSettings().setBuiltInZoomControls(Utils.getBoolean(Constants.ZOOM_SETTINGS, true));

        caching = Utils.getBoolean(Constants.CACHE_SETTINGS, true);

//        // disable third-party cookies only
        webView.setThirdPartyCookiesEnabled(Utils.getBoolean(Constants.THIRD_PARTY_COOKIE_SETTINGS, true));

//           or disable cookies in general
        webView.setCookiesEnabled(Utils.getBoolean(Constants.COOKIE_SETTINGS, true));

        webView.getSettings().setJavaScriptEnabled(true);

        return caching;
    }

    public static void addProgressBarToWebView(AdvancedWebView webView, ProgressBar progressBarBrowser) {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);

                if (progress < 100 && progressBarBrowser.getVisibility() == ProgressBar.GONE) {
                    progressBarBrowser.setVisibility(ProgressBar.VISIBLE);
                }

                progressBarBrowser.setProgress(progress);

                if (progress == 100) {
                    progressBarBrowser.setVisibility(ProgressBar.GONE);
                }
            }
        });
    }
}
