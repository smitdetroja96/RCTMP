package com.example.rctmp;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewDetails extends AppCompatActivity {

    TextView tv_author,tv_title,tv_material_type,tv_publisher,tv_description;
    TextView tv_subjects,tv_callnumber,tv_quantity;
    BooksClass book_viewed = new BooksClass();
    HistoryBooksClass book_now = new HistoryBooksClass();
    String url = "";
    WebView webView;
    WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        boolean whattofetch = (boolean) getIntent().getBooleanExtra("isMain",false);
        if(whattofetch == true){
            book_viewed = (BooksClass) getIntent().getSerializableExtra("BookDetails");
        }
        else{
            book_now = (HistoryBooksClass) getIntent().getSerializableExtra("BookDetails");
        }

        tv_author = findViewById(R.id.tv_author);
        tv_title = findViewById(R.id.tv_title);
        tv_material_type = findViewById(R.id.tv_material_type);
        tv_publisher = findViewById(R.id.tv_publisher);
        tv_description = findViewById(R.id.tv_description);
        tv_callnumber = findViewById(R.id.tv_callnumber);
        tv_subjects = findViewById(R.id.tv_subject);
        tv_quantity = findViewById(R.id.tv_quantity);
        webView = findViewById(R.id.wv_add_to_fav);


        if(whattofetch) {
            String temp;
            String empty = "empty@2019";
            if(!book_viewed.getAuthors().equals(empty)){
                temp = "Authors: " + book_viewed.getAuthors();
                tv_author.setText(temp);
            }
            else{
                tv_author.setVisibility(View.GONE);
            }
            tv_title.setText(book_viewed.getName());
            if(!book_viewed.getMaterialType().equals(empty)){
                temp = "Type: " + book_viewed.getMaterialType();
                tv_material_type.setText(temp);
            }
            else{
                tv_material_type.setVisibility(View.GONE);
            }
            if(!book_viewed.getPublisher().equals(empty)){
                temp = "Publisher: " + book_viewed.getPublisher();
                tv_publisher.setText(temp);
            }
            else{
                tv_publisher.setVisibility(View.GONE);
            }
            if(!book_viewed.getCallnumber().equals(empty)){
                temp = "Call Number: " + book_viewed.getCallnumber();
                tv_callnumber.setText(temp);
            }
            else
                tv_callnumber.setVisibility(View.GONE);
            if(!book_viewed.getSubjects().equals(empty)){
                temp = "Subjects: " + book_viewed.getSubjects();
                tv_subjects.setText(temp);
            }
            else{
                tv_subjects.setVisibility(View.GONE);
            }
            temp = "Quantity: " + book_viewed.getQuantity();
            tv_quantity.setText(temp);
            if(!book_viewed.getDescription().equals(empty)){
                temp = "Description: " + book_viewed.getDescription();
                tv_description.setText(temp);
            }
            else{
                tv_description.setVisibility(View.GONE);
            }
            url = book_viewed.getUrl();
        }
        else{
            url = "https://opac.daiict.ac.in/cgi-bin/koha/opac-detail.pl?biblionumber=" + book_now.getMybook().getBiblionumber();
        }
    }

    public void onClickUrl(View view) {
        Uri uri = Uri.parse(url);
        Intent i1 = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(i1);
    }

    public void onClickFavorites(View view) {

        webView.setWebViewClient(new WebViewClient(){
            boolean page_load_error = false;
            boolean done = false;

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                page_load_error = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(page_load_error)
                    Toast.makeText(getApplicationContext(), "Failed to Connect!", Toast.LENGTH_SHORT).show();
                else if(done==false){
                    done = true;
                    webView.evaluateJavascript("document.getElementById('shelfnumber').length;", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            Log.d("leng",s);
                            if(s.equals("0")){
                                webView.evaluateJavascript("document.getElementById('newvirtualshelf').value='rc_app_list';", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String s) {
                                        webView.evaluateJavascript("document.getElementById('category').value='1'", new ValueCallback<String>() {
                                            @Override
                                            public void onReceiveValue(String s) {
                                                webView.evaluateJavascript("document.getElementsByClassName('btn')[1].click();", new ValueCallback<String>() {
                                                    @Override
                                                    public void onReceiveValue(String s) {
                                                        Toast.makeText(ViewDetails.this, "Successfully Added to the List", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                            else{
//                                String script = "(function(){var len = document.getElementById('shelfnumber'); var tosearch = 'ggmu'; var lab = 'Public lists'; for(var i=0 ; i<len.options.length ; i++){if(len.options[i].text==tosearch && len.options[i].parentNode.label==lab){len.options[i].selected=true; return 1;}} return 0;})();";
//                                Log.d("hello:why2",script);
                                webView.evaluateJavascript("(function(){var len = document.getElementById('shelfnumber'); var tosearch = 'rc_app_list'; var lab = 'Private lists'; for(var i=0 ; i<len.options.length ; i++){if(len.options[i].text==tosearch && len.options[i].parentNode.label==lab){len.options[i].selected=true; return 1;}} return 0;})();", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String s) {
//                                        Log.d("hello:why3",s);
                                        if(s.equals("1")){
                                            webView.evaluateJavascript("document.getElementsByClassName('btn')[0].click();", new ValueCallback<String>() {
                                                @Override
                                                public void onReceiveValue(String s) {
                                                    Toast.makeText(ViewDetails.this, "Successfully Added to the List", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        else{
                                            webView.evaluateJavascript("document.getElementById('newvirtualshelf').value='rc_app_list';", new ValueCallback<String>() {
                                                @Override
                                                public void onReceiveValue(String s) {
                                                    webView.evaluateJavascript("document.getElementById('category').value='1'", new ValueCallback<String>() {
                                                        @Override
                                                        public void onReceiveValue(String s) {
                                                            webView.evaluateJavascript("document.getElementsByClassName('btn')[1].click();", new ValueCallback<String>() {
                                                                @Override
                                                                public void onReceiveValue(String s) {
                                                                    Toast.makeText(ViewDetails.this, "Successfully Added to the List", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
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
        int bib = book_viewed.getBiblionumber();
        String url = "https://opac.daiict.ac.in/cgi-bin/koha/opac-addbybiblionumber.pl?biblionumber=" + bib;
        webView.loadUrl(url);
    }

    public void onClickShare(View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "RC APP");

        BooksClass share_book = book_viewed;
        String empty = "empty@2019";
        String share_string = "RC APP" + "\n\n\n";

        share_string = share_string + share_book.getName() + "\n";
        if(!share_book.getAuthors().equals(empty))
            share_string = share_string + "Authors : " + share_book.getAuthors() + "\n";
        if(!share_book.getSubjects().equals(empty))
            share_string = share_string + "Subjects : " + share_book.getSubjects() + "\n";
        if(!share_book.getPublisher().equals(empty))
            share_string = share_string + "Published By : " + share_book.getPublisher() + "\n";
        if(!share_book.getCallnumber().equals(empty))
            share_string = share_string + "Call Number : " + share_book.getCallnumber() + "\n";
        share_string = share_string + "For more visit: " + share_book.getUrl();

        i.putExtra(Intent.EXTRA_TEXT, share_string);
        startActivity(Intent.createChooser(i, "Happy Reading..."));
    }
}
