
package com.example.rctmp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FinesActivity extends AppCompatActivity {

    TextView textViewForNoFines;
    WebView webView;
    WebSettings webSettings;
    boolean try_once = false;

    ArrayList<String> dates = new ArrayList<>();
    ArrayList<String> fine_amounts = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();
    ArrayList<String> amounts_outstanding = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fines);

        webView = findViewById(R.id.wv_fines);
        textViewForNoFines = findViewById(R.id.tv_no_fines);

        try_once = true;
        webView.setWebViewClient(new WebViewClient() {
            boolean page_load_error = false;

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                page_load_error = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (page_load_error)
                    Toast.makeText(getApplicationContext(), "Failed to Connect!", Toast.LENGTH_SHORT).show();
                else {
                    if (!try_once) return;
                    try_once = false;

                    webView.evaluateJavascript("(function(){var x = document.getElementsByTagName('td');" +
                            " var i; var sr; for(i=1;i<x.length;i++) { sr = sr + x[i].innerHTML.trim() + '|||||';} return sr;})();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            if (value != null) {
                                Log.d("RETURNED", value);
                                int l = value.length();
                                int i = 10;
                                ArrayList<String> fine_details = new ArrayList<>();
                                StringBuilder parsed = new StringBuilder();
                                while (i + 5 < l) {
                                    if (value.substring(i, i + 5).compareTo("|||||") == 0) {
                                        i = i + 5;
                                        fine_details.add(parsed.toString());
                                        parsed = new StringBuilder();
                                    } else {
                                        parsed.append(value.charAt(i));
                                        i++;
                                    }
                                }
                                for (i = 0; i < fine_details.size(); i = i + 4) {
                                    //Log.d(Integer.toString(i), fine_details.get(i));
                                    if (fine_details.get(i + 1).charAt(0) != 'P') {
                                        dates.add(fine_details.get(i));
                                        fine_amounts.add(fine_details.get(i + 2));
                                        amounts_outstanding.add(fine_details.get(i + 3));
                                        int j = 0;
                                        String des = fine_details.get(i + 1);
                                        StringBuilder description = new StringBuilder();
                                        while (des.charAt(j) != ',') j++;
                                        j = j + 2;
                                        while (des.charAt(j) != '/' && des.charAt(j) != '\n' && des.charAt(j) != '\\') {
                                            description.append(des.charAt(j));
                                            j++;
                                        }
                                        j = description.lastIndexOf(" ");
                                        descriptions.add(description.toString().substring(0, j));
                                    }
                                }
                                if(dates.size()==0){
                                    textViewForNoFines.setVisibility(View.VISIBLE);
                                }
                                else {

                                    webView.evaluateJavascript("(function(){var x = document.getElementsByClassName('sum'); return x[1].innerHTML;}())", new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String value) {

                                            value = value.substring(1, value.length() - 1);
                                            StringBuilder content = new StringBuilder();
                                            content.append("<!DOCTYPE html> <html> <head> <style> table, th, td {  border: 2px solid black;  border-collapse: collapse;} </style> </head> ");
                                            content.append("<body> <table style=\"width:100%\"> <thread> <tr> <th>Date</th> <th>Description</th> <th>Fine Amount</th> <th>Amount Outstanding</th> </tr> </thread>");

                                            content.append("<tfoot> <tr> <th colspan='3'> Total due</th> <td>" + value + "</td> </tr> </tfoot> <tbody>");
                                            for (int i = 0; i < dates.size(); i++) {
                                                content.append("<tr>");
                                                content.append("<td>" + dates.get(i) + "</td>");
                                                content.append("<td>" + descriptions.get(i) + "</td>");
                                                content.append("<td>" + fine_amounts.get(i) + "</td>");
                                                content.append("<td>" + amounts_outstanding.get(i) + "</td>");
                                                content.append("</tr>");
                                            }

                                            content.append("</tbody> </table> </body> </html>");
                                            webView.loadUrl("about:blank");
                                            webView.loadData(content.toString(), "text/html", "UTF-8");
                                            webView.setVisibility(View.VISIBLE);


                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webView.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-account.pl");

    }
}
