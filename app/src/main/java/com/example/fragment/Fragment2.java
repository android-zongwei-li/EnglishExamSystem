package com.example.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.VideoModule.VideoActivity;
import com.example.activity.activity_in_fragment1.WordsActivity;
import com.example.activity.activity_in_fragment1.WordsBookActivity;
import com.example.base.OneSentenceOneDayList;
import com.example.myapplication.R;
import com.example.utils.GlideImageLoader;
import com.example.utils.HttpUtil;
import com.example.utils.LogUtils;
import com.example.utils.ToastUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Fragment2 extends Fragment {
    private static final String TXAPIKEY = "651e9ecffdcd16dba451ea0b129b5dca";

    private ImageView mIVOneSentenceOneDay;

    private View rootView;

    //每日一句，图片地址
    String imgUrl;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
     //       Bitmap bitmap = (Bitmap) msg.obj;
            //对bitmap进行缩放,现在缩放并为达到预期，并且加入缩放以后，明显卡顿
            //  oom 异常
            /*Bitmap newBitmap = setImgSize(bitmap,
                    mIVOneSentenceOneDay.getHeight(),mIVOneSentenceOneDay.getWidth());*/
     //       mIVOneSentenceOneDay.setImageBitmap(bitmap);


            //方式二
            GlideImageLoader imageLoader = new GlideImageLoader();
            imageLoader.displayImage(getActivity(),imgUrl,mIVOneSentenceOneDay);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (rootView == null){

            rootView = inflater.inflate(R.layout.fragment_2,null);

            //每日一句
            mIVOneSentenceOneDay = rootView.findViewById(R.id.iv_one_sentence_one_day);
            HttpUtil.sendOkHttpRequest("http://api.tianapi.com/txapi/everyday/index?key="+TXAPIKEY,
                    new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            LogUtils.e("天行每日一句数据请求","失败");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response)
                                throws IOException {
                            String json = response.body().string();
                            Gson gson = new Gson();
                            OneSentenceOneDayList list = gson.fromJson(json, OneSentenceOneDayList.class);
                            imgUrl = list.newslist.get(0).imgurl;

                            Message msg = handler.obtainMessage();
                            handler.sendMessage(msg);

                            //方式一
                /*Bitmap bitmap = getUrlImg(imgUrl);
                bitmap.recycle();
                msg.obj = bitmap;
                */


                        }
                    });

            //单词搜索
            final EditText etSearchWord = rootView.findViewById(R.id.ed_search_word);
            etSearchWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    //以下方法防止两次发送请求
                    if (actionId == EditorInfo.IME_ACTION_SEND ||
                            (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        switch (event.getAction()){
                            case KeyEvent.ACTION_UP:
                                String word = etSearchWord.getText().toString();
                                LogUtils.i("输入的单词",word);
                                if (word.trim().length() != 0){
                                    Intent intent = new Intent(getActivity(), WordsActivity.class);
                                    intent.putExtra(WordsActivity.WORD,word);
                                    startActivity(intent);
                                }else {
                                    ToastUtils.show(getActivity(),R.string.input_tips);
                                }
                                return true;
                            default:
                                return true;
                        }
                    }
                    return false;
                }
            });

            /*Button button_video = rootView.findViewById(R.id.btn_video);
            button_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getContext(), VideoActivity.class);
                    startActivity(intent);
                }
            });*/

            //高考词汇书
            LinearLayout ll_ceee_word_book = rootView.findViewById(R.id.ll_ceee_word_book);
            ll_ceee_word_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), WordsBookActivity.class);
                    intent.putExtra(WordsBookActivity.TYPE,WordsBookActivity.CEEE_BOOK);
                    startActivity(intent);
                }
            });

            //四级词汇书
            LinearLayout ll_cet4_word_book = rootView.findViewById(R.id.ll_cet4_word_book);
            ll_cet4_word_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), WordsBookActivity.class);
                    intent.putExtra(WordsBookActivity.TYPE,WordsBookActivity.CET4_BOOK);
                    startActivity(new Intent(getContext(), WordsBookActivity.class));
                }
            });

        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (rootView != null){
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

    private Bitmap getUrlImg(String url){
        Bitmap bitmap = null;
        try {
            URL imgUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

            bitmap.recycle();
            is.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //这个图片缩放还未达到预期目标
    private Bitmap setImgSize(Bitmap bitmap, int newWidth, int newHeight){
        //获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //设置缩放比例
        float scaleWidth;
        float scaleHeight;
        if (width > newWidth && height > newHeight){//错误(1)解决
            scaleWidth = width / newWidth;
            scaleHeight = height / newHeight;
        }else {
            scaleWidth = newWidth / width;
            scaleHeight = newHeight / height;
        }
        //取得想要缩放的Matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        //得到新的图片
        // 这里会出现错误(1)：width and height must be > 0
        Bitmap newBitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        return newBitmap;
    }

}
