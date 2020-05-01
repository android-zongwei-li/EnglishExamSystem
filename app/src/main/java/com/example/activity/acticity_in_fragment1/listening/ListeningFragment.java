package com.example.activity.acticity_in_fragment1.listening;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.utils.testPaperUtils.TestPaperFactory;
import com.example.utils.testPaperUtils.TestPaperFromWord;

import java.util.ArrayList;
import java.util.List;

/**
 *  这个类为  ListeningActivity 提供Fragment，里面包含一个ListView
 */
public class ListeningFragment extends Fragment {

    // 用来标识是哪个页面：依次为  短篇新闻、情景对话、听力文章
    private int index;

    List<TestPaperFromWord.TestPaperInfo> allTestPaperInfo = new ArrayList<>();   // 存储所有试卷-信息

    public ListeningFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(int position) {
        ListeningFragment fragment = new ListeningFragment();
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

        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        ListView listView = view.findViewById(R.id.lv_question);
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
                /**对ListView的优化，convertView为空时，创建一个新视图；
                 * convertView不为空时，代表它是滚出,
                 * 放入Recycler中的视图,若需要用到其他layout，
                 * 则用inflate(),同一视图，用findViewBy()
                 * **/
                if(convertView == null ) {
                    LayoutInflater inflater = ListeningFragment.this.getLayoutInflater();
                    view = inflater.inflate(R.layout.examination_paper_information_item,null);
                    //view = View.inflate(getBaseContext(),R.layout.item,null);
                } else {
                    view = convertView;
                }

                final TestPaperFromWord.TestPaperInfo testPaper = allTestPaperInfo.get(position);

                TextView tv_test_paper_name = view.findViewById(R.id.tv_item_test_paper_name);
                tv_test_paper_name.setText(testPaper.getTestPaperName());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),ListeningExamActivity.class);
                        intent.putExtra(ListeningExamActivity.TESTPAPER_INDEX,position);//告诉下一个页面是第几套试卷
                        intent.putExtra(ListeningExamActivity.QUESTION_TYPE,index);//是什么题型

                        startActivity(intent);
                    }
                });

                return view;
            }
        });

        return view;
    }

}