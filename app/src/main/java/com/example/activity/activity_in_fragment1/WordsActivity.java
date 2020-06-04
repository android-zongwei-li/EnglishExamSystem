package com.example.activity.activity_in_fragment1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.activity.base.BaseAppCompatActivity;
import com.example.myapplication.R;
import com.example.utils.LogUtils;
import com.example.utils.ToastUtils;
import com.example.utils.TransApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class WordsActivity extends BaseAppCompatActivity {

    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String APP_ID = "20200325000404873";
    private static final String SECURITY_KEY = "3RzXL88TA1D71SYgC7a0";

    public static final String WORD = "word";   //要查询的单词

    public boolean toZh = true;   //默认为英译中

    private Button btnSelectWord;

    private String queryWord = "高度600米";

    private String result = "查询失败";

    private TextView tvSearchedWord;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            tvSearchedWord.setText(result);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);
        tvSearchedWord = findViewById(R.id.tv_searched_word);//显示翻译结果
        final AutoCompleteTextView tv = findViewById(R.id.actvWord);

        Intent intent = getIntent();
        queryWord = intent.getStringExtra(WORD);

        query(queryWord);

        btnSelectWord = findViewById(R.id.btnSelectWord);
        btnSelectWord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(WordsActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(tv.getWindowToken(),0);
                String word = tv.getText().toString();
                if (word.trim().length() == 0){
                    ToastUtils.show(getString(R.string.input_tips));
                }else {
                    query(word);
                }
            }
        });

    }

    private void query(String queryWord){

        toZh = checkChar(queryWord.charAt(0));
        if (toZh){
            trans(queryWord,"auto","zh");
        }else {
            trans(queryWord,"auto","en");
        }

    }

    //英文占1byte，非英文（可认为是中文）占2byte，根据这个特性来判断字符
    public static boolean checkChar(char ch) {
        if ((ch + "").getBytes().length == 1) {
            return true;//英文
        } else {
            return false;//中文
        }
    }

    /**
     * 翻译
     * @param from  源语言
     * @param to    目标语言
     */
    private void trans(final String queryWord, final String from, final String to){
        // Android 4.0 之后不能在主线程中请求HTTP请求
        // 放在主线程会出现：android.os.NetworkOnMainThreadException
        //参考：https://blog.csdn.net/qq_29477223/article/details/81027716
        new Thread(new Runnable(){
            @Override
            public void run() {
                TransApi api = new TransApi(APP_ID, SECURITY_KEY);
                //例：{"from":"en","to":"zh","trans_result":[{"src":"apple","dst":"\u82f9\u679c"}]}
                String json = api.getTransResult(queryWord, from, to);   //为整个json
                if (json == null){
                    LogUtils.v("数据获取结果","失败");
                    mHandler.sendEmptyMessage(0);
                    return;
                }
                LogUtils.i("翻译",json);
                try {
                    json = URLDecoder.decode(json,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                    result = jsonObject.getJSONArray("trans_result")
                                       .getJSONObject(0)
                                       .getString("dst");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mHandler.sendEmptyMessage(0);
                LogUtils.i("查询结果",result);

            }
        }).start();

    }

}
