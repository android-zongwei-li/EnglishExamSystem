package com.lzw.activity.activity_in_fragment1.listening;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.lzw.englishExamSystem.R;
import com.lzw.utils.testPaperUtils.TestPaperFactory;
import com.lzw.utils.testPaperUtils.TestPaperFromWord;

import java.util.ArrayList;
import java.util.List;

/**
 *  这个类为  ListeningActivity 提供Fragment，里面包含一个ListView
 */
public class ListeningFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // 用来标识是哪个页面：依次为  短篇新闻、情景对话、听力文章
    private int index;

    private List<TestPaperFromWord.TestPaperInfo> allTestPaperInfo = new ArrayList<>();   // 存储所有试卷-信息

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

    private LinearLayout llFilterPanel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initData();

        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        //初始化筛选面板
        llFilterPanel = view.findViewById(R.id.ll_filter_panel);
        initSpinner(view);

        initListView(view);

        return view;
    }

    /**
     * 初始化listView
     * @param view
     */
    private void initListView(View view) {
        //listView初始化
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
                if(convertView == null) {
                    LayoutInflater inflater = ListeningFragment.this.getLayoutInflater();
                    view = inflater.inflate(R.layout.examination_paper_information_item,null);
                } else {
                    view = convertView;
                }

                final TestPaperFromWord.TestPaperInfo testPaper = allTestPaperInfo.get(position);

                TextView tv_test_paper_name = view.findViewById(R.id.tv_item_test_paper_name);
                tv_test_paper_name.setText(testPaper.getYear()+"年"+testPaper.getMonth()+"月"+"-第"+testPaper.getIndex()+"套");

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),ListeningExamActivity.class);
                        intent.putExtra(ListeningExamActivity.TEST_PAPER_INDEX,position);//告诉下一个页面是第几套试卷
                        intent.putExtra(ListeningExamActivity.QUESTION_TYPE,index);//是什么题型

                        startActivity(intent);
                    }
                });

                return view;
            }
        });
    }

    private Spinner spinnerSort;
    private Spinner spinnerYear;
    private Spinner spinnerMonth;
    private Spinner spinnerPaperIndex;
    private Spinner spinnerAverageScore;
    private Spinner spinnerPeopleNumber;
    /**
     * 初始化几个Spinner的排序规则
     * @param view
     */
    private void initSpinner(View view) {
        spinnerSort = view.findViewById(R.id.spinner_sort);
        spinnerYear = view.findViewById(R.id.spinner_year);
        spinnerMonth = view.findViewById(R.id.spinner_month);
        spinnerPaperIndex = view.findViewById(R.id.spinner_paper_index);
        spinnerAverageScore = view.findViewById(R.id.spinner_average_score);
        spinnerPeopleNumber = view.findViewById(R.id.spinner_people_number);

        spinnerSort.setOnItemSelectedListener(this);
        spinnerYear.setOnItemSelectedListener(this);
        spinnerMonth.setOnItemSelectedListener(this);
        spinnerPaperIndex.setOnItemSelectedListener(this);
        spinnerAverageScore.setOnItemSelectedListener(this);
        spinnerPeopleNumber.setOnItemSelectedListener(this);
    }

    /**
     * Spinner的事件监听处理
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @SuppressLint("ResourceType")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.spinner_sort:
                //TODO：排序的核心是 allTestPaperInfo 中元素的顺序
                for (int i = 0; i < allTestPaperInfo.size(); i++){

                }
                break;
            case R.id.spinner_year:

                break;
            case R.id.spinner_month:

                break;
            case R.id.spinner_paper_index:

                break;
            case R.id.spinner_average_score:

                break;
            case R.id.spinner_people_number:

                break;
            default:
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * 展示筛选面板
     * 筛选面板：在这里可以设置ListView展示那些item
     * @return  把筛选面板返回出去，用于控制面板的显示/隐藏
     */
    public LinearLayout getFilterPanel(){
        return llFilterPanel;
    }


}
