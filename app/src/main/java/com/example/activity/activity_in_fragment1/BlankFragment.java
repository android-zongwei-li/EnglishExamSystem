package com.example.activity.activity_in_fragment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.activity.activity_in_fragment1.reading.ReadingExamActivity;
import com.example.myapplication.R;
import com.example.utils.testPaperUtils.TestPaperFactory;
import com.example.utils.testPaperUtils.TestPaperFromWord;

import java.util.ArrayList;
import java.util.List;

/**
 *  这个类为  ReadingActivity 提供Fragment，里面包含一个ListView
 */
public class BlankFragment extends Fragment {

    // 用来标识是哪个页面：依次为  选词填空、快速阅读、仔细阅读
    private int index;

    ListView listView;  //试卷信息
    TextView tv_test_paper_name;    // 试卷名称


    List<TestPaperFromWord.TestPaperInfo> allTestPaperInfo = new ArrayList<>();   // 存储所有试卷-信息


    public BlankFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(int position) {
        BlankFragment fragment = new BlankFragment();
        fragment.index = position;
        return fragment;
    }

    /**
     * 获取需要的所有数据
     */
    private void initData(){
        TestPaperFactory testPaperFactory = TestPaperFactory.getInstance();
        allTestPaperInfo = testPaperFactory.getAllTestPaperInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initData();

        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.lv_question);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return allTestPaperInfo.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup viewGroup) {
                View view;
                if(convertView == null ) {
                    LayoutInflater inflater = BlankFragment.this.getLayoutInflater();
                    view = inflater.inflate(R.layout.examination_paper_information_item,null);
                }
                else {
                    view = convertView;
                }

                final TestPaperFromWord.TestPaperInfo info = allTestPaperInfo.get(position);

                tv_test_paper_name = view.findViewById(R.id.tv_item_test_paper_name);
                tv_test_paper_name.setText(info.getTestPaperName().trim());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ReadingExamActivity.class);
                        intent.putExtra(ReadingExamActivity.TEST_PAPER_INDEX,position);//告诉下一个页面是第几套试卷
                        intent.putExtra(ReadingExamActivity.QUESTION_TYPE,index);//是什么题型

                        startActivity(intent);
                    }
                });

                return view;
            }
        });
    }

}
