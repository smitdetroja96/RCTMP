package com.example.rctmp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewDetails extends AppCompatActivity {

    ArrayList<BooksClass> books;
    TextView tv_author,tv_title,tv_material_type,tv_publisher,tv_description;
    TextView tv_subjects,tv_callnumber,tv_quantity;
    BooksClass book_viewed = new BooksClass();
    HistoryBooksClass book_now = new HistoryBooksClass();
    String url = "";
    WebView webView, webView1;
    Boolean loadNextPage = false, third_if = false;
    WebSettings webSettings, webSettings1;
    TextView add_fav;
    boolean remove_t;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        books = new ArrayList<>();
        readBookData();

        boolean whattofetch = (boolean) getIntent().getBooleanExtra("isMain",false);
        remove_t = (boolean) getIntent().getBooleanExtra("showRemove",false);


        if(whattofetch == true){
            book_viewed = (BooksClass) getIntent().getSerializableExtra("BookDetails");
        }
        else{
            book_now = (HistoryBooksClass) getIntent().getSerializableExtra("BookDetails");
        }

        add_fav = findViewById(R.id.tv_add_to_fav);
        tv_author = findViewById(R.id.tv_author);
        tv_title = findViewById(R.id.tv_title);
        tv_material_type = findViewById(R.id.tv_material_type);
        tv_publisher = findViewById(R.id.tv_publisher);
        tv_description = findViewById(R.id.tv_description);
        tv_callnumber = findViewById(R.id.tv_callnumber);
        tv_subjects = findViewById(R.id.tv_subject);
        tv_quantity = findViewById(R.id.tv_quantity);
        webView = findViewById(R.id.wv_add_to_fav);
        webView1 = findViewById(R.id.wv_update_quantity);

        if(remove_t){
            add_fav.setText("Remove from Fav.");
        }

        int bibnumber;

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
            else {
                tv_callnumber.setVisibility(View.GONE);
            }
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
            bibnumber = book_viewed.getBiblionumber();
        }
        else{
            bibnumber = book_now.getMybook().getBiblionumber();
            url = "https://opac.daiict.ac.in/cgi-bin/koha/opac-detail.pl?biblionumber=" + book_now.getMybook().getBiblionumber();
        }

        // getting index in books array
        int i, indexOfCurrentBib;
        for(i=0 ; i<books.size() ; i++){
            if(books.get(i).getBiblionumber() == bibnumber){
                indexOfCurrentBib = i;
                break;
            }
        }

        // getting the current quantity

//        AlertDialog.Builder builder = new AlertDialog.Builder(ViewDetails.this);
//        builder.setCancelable(false);
//        builder.setView(R.layout.progress_dialogue_layout_1);
//
//        final AlertDialog dialog = builder.create();
//
//        Window window1 = dialog.getWindow();
//        if (window1 != null) {
//            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//            layoutParams.copyFrom(dialog.getWindow().getAttributes());
//            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
//            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
//            dialog.getWindow().setAttributes(layoutParams);
//        }
//
//        dialog.show();
//
//        webView1.setWebViewClient(new WebViewClient() {
//            boolean page_load_error = false;
//
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                page_load_error = true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                if (page_load_error) {
//
//                    dialog.dismiss();
//                    Toast.makeText(getApplicationContext(), "Failed to Connect!", Toast.LENGTH_SHORT).show();
//
//                }
//                else {
//                    //Toast.makeText(SignInActivity.this, "Reached Here!", Toast.LENGTH_SHORT).show();
//                    String script= "(function(){\n" +
//                            " return document.getElementsByClassName('status').length; \n"+
//                            "})();";
//                    webView1.evaluateJavascript(script, new ValueCallback<String>() {
//                        @Override
//                        public void onReceiveValue(String value) {
//                            Log.e("hhhhhhhhhhhhhhhhhh",value);
//                            dialog.dismiss();
//                        }
//                    });
//                }
//
//
//                //startActivity(new Intent(MainActivity.this, ChangedLayoutActivity.class));
//            }
//        });
//
//        webSettings1 = webView.getSettings();
//        webSettings1.setJavaScriptEnabled(true);
//        webSettings1.setAllowContentAccess(true);
//        webSettings1.setDomStorageEnabled(true);
//        webSettings1.setDatabaseEnabled(true);
//        webView1.loadUrl(url);
    }

    public void onClickUrl(View view) {
        Uri uri = Uri.parse(url);
        Intent i1 = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(i1);
    }

    public void onClickFavorites(View view) {

        if(!remove_t) {
            webView.setWebViewClient(new WebViewClient() {
                boolean page_load_error = false;
                boolean done = false;

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    page_load_error = true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (page_load_error)
                        Toast.makeText(getApplicationContext(), "Failed to Connect!", Toast.LENGTH_SHORT).show();
                    else if (done == false) {
                        done = true;
                        webView.evaluateJavascript("document.getElementById('shelfnumber').length;", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                                Log.d("leng", s);
                                if (s.equals("0")) {
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
                                                            remove_t = true;
                                                            add_fav.setText("Remove from Fav.");
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                } else {
//                                String script = "(function(){var len = document.getElementById('shelfnumber'); var tosearch = 'ggmu'; var lab = 'Public lists'; for(var i=0 ; i<len.options.length ; i++){if(len.options[i].text==tosearch && len.options[i].parentNode.label==lab){len.options[i].selected=true; return 1;}} return 0;})();";
//                                Log.d("hello:why2",script);
                                    webView.evaluateJavascript("(function(){var len = document.getElementById('shelfnumber'); var tosearch = 'rc_app_list'; var lab = 'Private lists'; for(var i=0 ; i<len.options.length ; i++){if(len.options[i].text==tosearch && len.options[i].parentNode.label==lab){len.options[i].selected=true; return 1;}} return 0;})();", new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String s) {
//                                        Log.d("hello:why3",s);
                                            if (s.equals("1")) {
                                                webView.evaluateJavascript("document.getElementsByClassName('btn')[0].click();", new ValueCallback<String>() {
                                                    @Override
                                                    public void onReceiveValue(String s) {
                                                        Toast.makeText(ViewDetails.this, "Successfully Added to the List", Toast.LENGTH_SHORT).show();
                                                        remove_t = true;
                                                        add_fav.setText("Remove from Fav.");
                                                    }
                                                });
                                            } else {
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
                                                                        remove_t = true;
                                                                        add_fav.setText("Remove from Fav.");
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
        else{
            webView.setWebViewClient(new WebViewClient(){
                boolean page_load_error = false;
                boolean done = false;
                boolean secondif = false;

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    page_load_error = true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if(page_load_error){
                        Toast.makeText(ViewDetails.this, "Failed to Connect !", Toast.LENGTH_SHORT).show();
                    }
                    else if(!done && !loadNextPage){
                        done = true;
                        webView.evaluateJavascript("(function(){return (document.getElementsByClassName('table')).length;})();", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                                Log.d("hello1:",s);
                                if(s.equals("0") || s==null){

                                }
                                else{
                                    String script= "(function(){\n" +
                                            "\tvar x = document.getElementsByClassName('table')[0].rows.length;\n" +
                                            "  var temp = document.getElementsByClassName('table')[0];\n" +
                                            "  var search = 'rc_app_list';\n" +
                                            "  for(var i=1 ; i<x ; i++){\n" +
                                            "  \tif(temp.rows[i].cells[0].innerText == search){\n" +
                                            "    \treturn i;\n" +
                                            "    }\n" +
                                            "  }\n" +
                                            "  return 0;\n" +
                                            "})();";
                                    webView.evaluateJavascript(script, new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String s) {
                                            Log.d("hello3:",s);
                                            if(s.equals("0")){
                                            }
                                            else{
                                                String newscript = "(document.getElementsByClassName('table')[0].rows["+s+"].cells[0]).firstChild.click();";
                                                webView.evaluateJavascript(newscript, new ValueCallback<String>() {
                                                    @Override
                                                    public void onReceiveValue(String s) {
                                                        //Toast.makeText(ShowFavouritesActivity.this, "Opening lists...", Toast.LENGTH_SHORT).show();
                                                        loadNextPage = true;
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                    else if(loadNextPage && !secondif){
                        secondif = true;
                        webView.evaluateJavascript("document.querySelector('div.maincontent > h2').textContent;", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
//                            Toast.makeText(ShowActivity.this, s, Toast.LENGTH_SHORT).show();
                                Log.d("fffff1",s);
                                webView.evaluateJavascript("(document.getElementsByClassName('table')).length;", new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String s) {
//                                    if(s==null)
                                        if(s.equals("0")){
                                        }
                                        else{
                                            webView.evaluateJavascript("", new ValueCallback<String>() {
                                                @Override
                                                public void onReceiveValue(String s) {
                                                    String bibnum = book_viewed.getBiblionumber()+"";
                                                    String cururl = webView.getUrl();
                                                    int idx = cururl.indexOf("number=");
                                                    cururl = cururl.substring(idx+7);
                                                    String newurl = "https://opac.daiict.ac.in/cgi-bin/koha/opac-shelves.pl?op=remove_biblios&shelfnumber="+cururl+"&biblionumber="+bibnum;
                                                    Log.e("newrr",cururl);
                                                    Log.e("newur",newurl);
                                                    Log.e("bib",bibnum);
                                                    webView.loadUrl(newurl);
                                                    Toast.makeText(ViewDetails.this, "Book removed from favourites successfully", Toast.LENGTH_SHORT).show();
                                                    add_fav.setText("Add to Favourites");
                                                    remove_t = false;
                                                }
                                            });
                                        }
                                    }
                                });
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
            webView.loadUrl("https://opac.daiict.ac.in/cgi-bin/koha/opac-shelves.pl?op=list&category=1");
        }
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK,new Intent());
        finish();
    }

    private void readBookData()
    {
        SharedPreferences sharedpreferences = getSharedPreferences("allBooks", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getApplicationContext(),"allBooks",0);

        int numberOfAllBooks = sharedpreferences.getInt("numberOfBooks",0);

//        Toast.makeText(this, "" + numberOfAllBooks, Toast.LENGTH_SHORT).show();
        if(books.size() > 0)
            books.clear();

        for(int i=0;i<numberOfAllBooks;i++)
        {
            books.add(complexPreferences.getObject("Books"+Integer.toString(i),BooksClass.class));
        }

//        Toast.makeText(this, "" + books.size() + " hii", Toast.LENGTH_SHORT).show();
    }
}
