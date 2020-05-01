package com.example.beans;

import android.util.Log;

import androidx.core.view.NestedScrollingChild;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 试卷管理类（单例）
 * 1、在此类解析json数据，并将试卷类的实例化
 * 2、把相关的数据提供给app使用
 */
public class TestPaperManager {

    // 创建 SingleObject 的一个对象
    private static TestPaperManager testPaperManager = new TestPaperManager();

    // 让构造函数为 private，这样该类就不会被实例化
    private TestPaperManager(){

    }

    // 获取唯一可用的对象
    public static TestPaperManager getInstance(){
        return testPaperManager;
    }


    // 试卷列表
    List<TestPaper> allTestPaper = new ArrayList<>();
    List<TestPaper.ListeningBean> allListening = new ArrayList<>();
    List<TestPaper.ReadingBean> allReading = new ArrayList<>();
    List<TestPaper.TransBean> allTrans = new ArrayList<>();
    List<TestPaper.WritingBean> allWriting = new ArrayList<>();
    //听力
    List<List<TestPaper.ListeningBean.NewsBean>> allNewsBeans = new ArrayList<>();
    List<List<TestPaper.ListeningBean.ConversationBean>> allConversationBeans = new ArrayList<>();
    List<List<TestPaper.ListeningBean.PassageBean>> allPassagBeans = new ArrayList<>();
    // 阅读
    List<TestPaper.ReadingBean.SectionABean> allSectionABeans = new ArrayList<>();
    List<TestPaper.ReadingBean.SectionBBean> allSectionBBeans = new ArrayList<>();
    List<List<TestPaper.ReadingBean.SectionCBean>> allSectionCBeans = new ArrayList<>();


    // 试卷类，包括四个部分：听力、阅读、翻译、写作，这四个类的实例化也在此类完成
    TestPaper testPaper;

    // 听力类，包括三部分：新闻、对话、文章
    TestPaper.ListeningBean listeningBean;
    List<TestPaper.ListeningBean.NewsBean> newsBeans;
    List<TestPaper.ListeningBean.NewsBean.QuestionsBean> newsQuestionsBeans;

    List<TestPaper.ListeningBean.ConversationBean> conversationBeans;
    List<TestPaper.ListeningBean.ConversationBean.QuestionsBeanX> questionsBeanXES;

    List<TestPaper.ListeningBean.PassageBean> passageBeans;
    List<TestPaper.ListeningBean.PassageBean.QuestionsBeanXX> questionsBeanXXES;

    // 阅读类,包括三个部分：选词填空(SectionA)、快速阅读(SectionB)、仔细阅读(SectionC)
    TestPaper.ReadingBean readingBean;
    TestPaper.ReadingBean.SectionABean sectionABean;

    TestPaper.ReadingBean.SectionBBean sectionBBean;

    List<TestPaper.ReadingBean.SectionCBean> sectionCBeans;
    List<TestPaper.ReadingBean.SectionCBean.QuestionsBeanXXX> questionsBeanXXXList;

    // 翻译类
    TestPaper.TransBean transBean;

    // 写作类
    TestPaper.WritingBean writingBean;

    /**
     * 创建TestPaper
     * @param jsonObject
     * @throws JSONException
     */
    public void addTestPaper(JSONObject jsonObject) throws JSONException {

        String id = jsonObject.getString("id");
        String name = jsonObject.getString("name");
        JSONObject jsonListening = jsonObject.getJSONObject("listening");
        JSONObject jsonReading = jsonObject.getJSONObject("reading");
        JSONObject jsonTrans = jsonObject.getJSONObject("trans");
        JSONObject jsonWriting = jsonObject.getJSONObject("writing");

        testPaper = new TestPaper();

        testPaper.setId(id);
        testPaper.setName(name);
        testPaper.setListening(initListening(jsonListening));
        testPaper.setReading(initReading(jsonReading));
        testPaper.setTrans(initTrans(jsonTrans));
        testPaper.setWriting(initWriting(jsonWriting));


        allTestPaper.add(testPaper);
        allListening.add(testPaper.getListening());
        allReading.add(testPaper.getReading());
        allTrans.add(testPaper.getTrans());
        allWriting.add(testPaper.getWriting());

    }

    // 初始化听力类
    private TestPaper.ListeningBean initListening(JSONObject jsonListening) throws JSONException {
        listeningBean = new TestPaper.ListeningBean();

        listeningBean.setId(jsonListening.getString("id"));
        listeningBean.setName(jsonListening.getString("name"));


        JSONArray jsonNewsBean = jsonListening.getJSONArray("news");
        JSONArray jsonConversationsBean = jsonListening.getJSONArray("conversation");
        JSONArray jsonPassages = jsonListening.getJSONArray("passage");

        listeningBean.setNews(initListeningNews(jsonNewsBean));

        listeningBean.setConversation(initListeningConversation(jsonConversationsBean));
        listeningBean.setPassage(initListeningPassage(jsonPassages));

        return listeningBean;
    }
    private List<TestPaper.ListeningBean.NewsBean> initListeningNews(JSONArray jsonNewsBean) throws JSONException {
        newsBeans = new ArrayList<>();
        allNewsBeans.add(newsBeans);

        for (int i = 0; i < jsonNewsBean.length(); i++){
            newsBeans.add(new TestPaper.ListeningBean.NewsBean());
            JSONObject jsonNew = jsonNewsBean.getJSONObject(i);
            newsBeans.get(i).setName(jsonNew.getString("name"));
            newsBeans.get(i).setText(jsonNew.getString("text"));

            JSONArray questions = jsonNew.getJSONArray("questions");
            newsQuestionsBeans = new ArrayList<>();
            for (int j = 0; j < questions.length(); j++){
                newsQuestionsBeans.add(new TestPaper.ListeningBean.NewsBean.QuestionsBean());
                JSONObject jsonQuestion = questions.getJSONObject(j);
                newsQuestionsBeans.get(j).setName(jsonQuestion.getString("name"));
                newsQuestionsBeans.get(j).setTitle(jsonQuestion.getString("title"));

                JSONArray jsonAnswers = jsonQuestion.getJSONArray("answers");
                List<String> answers = new ArrayList<>();
                for (int k = 0; k < jsonAnswers.length(); k++){
                    answers.add(jsonAnswers.getString(k));
                }
                newsQuestionsBeans.get(j).setAnswers(answers);
            }
            newsBeans.get(i).setQuestions(newsQuestionsBeans);
        }

        return newsBeans;
    }
    private List<TestPaper.ListeningBean.ConversationBean> initListeningConversation(JSONArray jsonConversationsBean) throws JSONException {
        conversationBeans = new ArrayList<>();
        allConversationBeans.add(conversationBeans);
        for (int i = 0; i < jsonConversationsBean.length(); i++){
            conversationBeans.add(new TestPaper.ListeningBean.ConversationBean());
            JSONObject jsonConversation = jsonConversationsBean.getJSONObject(i);
            conversationBeans.get(i).setName(jsonConversation.getString("name"));
            conversationBeans.get(i).setText(jsonConversation.getString("text"));

            JSONArray questions = jsonConversation.getJSONArray("questions");
            questionsBeanXES = new ArrayList<>();
            for (int j = 0; j < questions.length(); j++){
                questionsBeanXES.add(new TestPaper.ListeningBean.ConversationBean.QuestionsBeanX());
                JSONObject jsonQuestion = questions.getJSONObject(j);
                questionsBeanXES.get(j).setName(jsonQuestion.getString("name"));
                questionsBeanXES.get(j).setTitle(jsonQuestion.getString("title"));

                JSONArray jsonAnswers = jsonQuestion.getJSONArray("answers");
                List<String> answers = new ArrayList<>();
                for (int k = 0; k < jsonAnswers.length(); k++){
                    answers.add(jsonAnswers.getString(k));
                }
                questionsBeanXES.get(j).setAnswers(answers);
            }
            conversationBeans.get(i).setQuestions(questionsBeanXES);
        }

        return conversationBeans;
    }
    private List<TestPaper.ListeningBean.PassageBean> initListeningPassage(JSONArray jsonPassages) throws JSONException {
        passageBeans = new ArrayList<>();
        allPassagBeans.add(passageBeans);
        for (int i = 0; i < jsonPassages.length(); i++){
            passageBeans.add(new TestPaper.ListeningBean.PassageBean());
            passageBeans.add(new TestPaper.ListeningBean.PassageBean());
            JSONObject jsonPassage = jsonPassages.getJSONObject(i);
            passageBeans.get(i).setName(jsonPassage.getString("name"));
            passageBeans.get(i).setText(jsonPassage.getString("text"));

            JSONArray questions = jsonPassage.getJSONArray("questions");
            questionsBeanXXES = new ArrayList<>();
            for (int j = 0; j < questions.length(); j++){
                questionsBeanXXES.add(new TestPaper.ListeningBean.PassageBean.QuestionsBeanXX());
                JSONObject jsonQuestion = questions.getJSONObject(j);
                questionsBeanXXES.get(j).setName(jsonQuestion.getString("name"));
                questionsBeanXXES.get(j).setTitle(jsonQuestion.getString("title"));

                JSONArray jsonAnswers = jsonQuestion.getJSONArray("answers");
                List<String> answers = new ArrayList<>();
                for (int k = 0; k < jsonAnswers.length(); k++){
                    answers.add(jsonAnswers.getString(k));
                }
                questionsBeanXXES.get(j).setAnswers(answers);
            }
            passageBeans.get(i).setQuestions(questionsBeanXXES);
        }

        return passageBeans;
    }

    // 初始化阅读类
    private TestPaper.ReadingBean initReading(JSONObject jsonReading) throws JSONException {
        readingBean = new TestPaper.ReadingBean();
        readingBean.setId(jsonReading.getString("id"));
        readingBean.setName(jsonReading.getString("name"));

        JSONObject jsonReadingSectionA = jsonReading.getJSONObject("section_a");
        JSONObject jsonReadingSectionB = jsonReading.getJSONObject("section_b");
        JSONArray readingSectionC = jsonReading.getJSONArray("section_c");
        readingBean.setSection_a(initReadingSectionA(jsonReadingSectionA));
        readingBean.setSection_b(initReadingSectionB(jsonReadingSectionB));
        readingBean.setSection_c(initReadingSectionC(readingSectionC));

        return readingBean;
    }
    private TestPaper.ReadingBean.SectionABean initReadingSectionA(JSONObject jsonReadingSectionA) throws JSONException {
        sectionABean = new TestPaper.ReadingBean.SectionABean();
        allSectionABeans.add(sectionABean);

        sectionABean.setId(jsonReadingSectionA.getString("id"));
        sectionABean.setText(jsonReadingSectionA.getString("text"));

        JSONArray sectionAWords = jsonReadingSectionA.getJSONArray("words");
        List<String> words = new ArrayList<>();
        for (int i = 0; i < sectionAWords.length(); i++){
            words.add(sectionAWords.getString(i));
        }

        sectionABean.setWords(words);

        return sectionABean;
    }
    private TestPaper.ReadingBean.SectionBBean initReadingSectionB(JSONObject jsonReadingSectionB) throws JSONException {
        sectionBBean = new TestPaper.ReadingBean.SectionBBean();
        allSectionBBeans.add(sectionBBean);

        sectionBBean.setId(jsonReadingSectionB.getString("id"));
        sectionBBean.setTitle(jsonReadingSectionB.getString("title"));
        sectionBBean.setText(jsonReadingSectionB.getString("text"));

        JSONArray sectionBQuestions = jsonReadingSectionB.getJSONArray("questions");
        List<String> questions = new ArrayList<>();
        for (int i = 0; i < sectionBQuestions.length(); i++){
            questions.add(sectionBQuestions.getString(i));
        }
        sectionBBean.setQuestions(questions);

        return sectionBBean;
    }
    private List<TestPaper.ReadingBean.SectionCBean> initReadingSectionC(JSONArray readingSectionC) throws JSONException {
        sectionCBeans = new ArrayList<>(readingSectionC.length());
        allSectionCBeans.add(sectionCBeans);

        for (int i = 0; i < readingSectionC.length(); i++){
            JSONObject jsonSectionCBean = readingSectionC.getJSONObject(i);
            TestPaper.ReadingBean.SectionCBean sectionCBean = new TestPaper.ReadingBean.SectionCBean();
            sectionCBean.setId(jsonSectionCBean.getString("id"));
            sectionCBean.setName(jsonSectionCBean.getString("name"));
            sectionCBean.setText(jsonSectionCBean.getString("text"));

            JSONArray sectionCBeanQuestions = jsonSectionCBean.getJSONArray("questions");
            sectionCBean.setQuestions(initReadingSectionCQuestions(sectionCBeanQuestions));

            sectionCBeans.add(sectionCBean);

        }

        return sectionCBeans;
    }
    private List<TestPaper.ReadingBean.SectionCBean.QuestionsBeanXXX> initReadingSectionCQuestions(JSONArray sectionCBeanQuestions) throws JSONException {
        TestPaper.ReadingBean.SectionCBean.QuestionsBeanXXX questionsBeanXXX = new TestPaper.ReadingBean.SectionCBean.QuestionsBeanXXX();
        questionsBeanXXXList = new ArrayList<>();
        for (int i = 0; i < sectionCBeanQuestions.length(); i++){
            JSONObject jsonQuestion = sectionCBeanQuestions.getJSONObject(i);
            questionsBeanXXX.setName(jsonQuestion.getString("name"));
            questionsBeanXXX.setTitle(jsonQuestion.getString("title"));

            JSONArray jaAnswer = jsonQuestion.getJSONArray("answers");
            List<String> answer = new ArrayList<>();
            for (int j = 0; j < jaAnswer.length(); j++){
                answer.add(jaAnswer.getString(j));
            }

            questionsBeanXXX.setAnswers(answer);

            questionsBeanXXXList.add(questionsBeanXXX);
        }

        return questionsBeanXXXList;
    }

    // 初始化翻译类
    private TestPaper.TransBean initTrans(JSONObject jsonTrans) throws JSONException {
        transBean = new TestPaper.TransBean();

        transBean.setId(jsonTrans.getString("id"));
        transBean.setName(jsonTrans.getString("name"));
        transBean.setTitle(jsonTrans.getString("title"));
        transBean.setText(jsonTrans.getString("text"));

        return transBean;
    }
    // 初始化写作类
    private TestPaper.WritingBean initWriting(JSONObject jsonWriting) throws JSONException {
        writingBean = new TestPaper.WritingBean();

        writingBean.setId(jsonWriting.getString("id"));
        writingBean.setName(jsonWriting.getString("name"));
        writingBean.setTitle(jsonWriting.getString("title"));

        return writingBean;
    }


    // getters
    public List<TestPaper> getAllTestPaper() {
        return allTestPaper;
    }

    public List<TestPaper.ListeningBean> getAllListening() {
        return allListening;
    }

    public List<TestPaper.ReadingBean> getAllReading() {
        return allReading;
    }

    public List<TestPaper.TransBean> getAllTrans() {
        return allTrans;
    }

    public List<TestPaper.WritingBean> getAllWriting() {
        return allWriting;
    }

    public List<List<TestPaper.ListeningBean.NewsBean>> getAllNewsBeans() {
        return allNewsBeans;
    }

    public List<List<TestPaper.ListeningBean.ConversationBean>> getAllConversationBeans() {
        return allConversationBeans;
    }

    public List<List<TestPaper.ListeningBean.PassageBean>> getAllPassagBeans() {
        return allPassagBeans;
    }

    public List<TestPaper.ReadingBean.SectionABean> getAllSectionABeans() {
        return allSectionABeans;
    }

    public List<TestPaper.ReadingBean.SectionBBean> getAllSectionBBeans() {
        return allSectionBBeans;
    }

    public List<List<TestPaper.ReadingBean.SectionCBean>> getAllSectionCBeans() {
        return allSectionCBeans;
    }
}
