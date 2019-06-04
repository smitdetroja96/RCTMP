package com.example.rctmp;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

class CustomJavaScriptInterface {
    Context mContext;
    public CustomJavaScriptInterface(Context c) {mContext = c;}
    @JavascriptInterface
    public void userCheck(final String val)
    {
        Log.d("returnVal",val);
        MainActivity.loginState = !(val.compareTo("false")==0);
    }

    @JavascriptInterface
    public void printId(final String val)
    {
        Log.d("returnVal",val);
        Toast.makeText(mContext,val+"yes",Toast.LENGTH_SHORT).show();
    }

}
