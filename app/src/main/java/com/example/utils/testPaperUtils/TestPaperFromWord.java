package com.example.utils.testPaperUtils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.RawRes;

import com.example.beans.Question;
import com.example.myapplication.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类负责把 word 转换为 testpaper
 */
public class TestPaperFromWord {

    private String testPaper; // 存储所有从word中读取出来的字符
    private String testPaperName;
    private String writing;
    private String listening;
    private String reading;
    private String translation;
    private List<String> listeningAndReadingAnswer;

    // 一套试卷的完整结构
    private TestPaperInfo testPaperInfo;//存储testpaper的基本信息
    private WritingFormWord writingFormWord;
    private ListeningFormWord listeningFormWord;
    private ReadingFormWord readingFormWord;
    private TranslationFormWord translationFormWord;

    // 这个List用来存储题目字符串
    private List<String> questions = new ArrayList<>();
    private List<Question> listeningQuestion = new ArrayList<>();// 1-25题

    private List<String> optionalWords = new ArrayList<>();        // 26-35
    private List<Question> quicklyReadQuestion = new ArrayList<>(); // 36-45
    private List<Question> readCarefullyQuestion = new ArrayList<>(); // 46-55

    public TestPaperFromWord(Context context, @RawRes int rawResId){
        // 1、从word中取出string
        WordUtils wordUtils = new WordUtils(context);
        testPaper = wordUtils.readWord(rawResId);
        // 2、把string进行截取，分为四个部分:写作、听力、阅读、翻译
        testPaperName = testPaper.substring(0,testPaper.indexOf("Part I"));
        writing = testPaper.substring(testPaper.indexOf("Part I"),testPaper.indexOf("Part Ⅱ"));
        listening = testPaper.substring(testPaper.indexOf("Part Ⅱ"),testPaper.indexOf("Part III"));
        reading = testPaper.substring(testPaper.indexOf("Part III"),testPaper.indexOf("Part IV"));
        translation = testPaper.substring(testPaper.indexOf("Part IV"),testPaper.length()-1);

        // 3、构建TestPaper
        // 3.1 先把题目，统一提取出来
        getCet4Questions(testPaper);
        createQuestion();
        // 3.2 构造每种题型
        testPaperInfo = new TestPaperInfo(testPaperName);
        writingFormWord = new WritingFormWord(writing);
        listeningFormWord = new ListeningFormWord(listening);
        readingFormWord = new ReadingFormWord(reading);
        translationFormWord = new TranslationFormWord(translation);
    }

    /**
     * 试卷信息类：
     * 通过 参数 ：testPaperName 构造，
     */
    public class TestPaperInfo {
        private String testPaperName;//试卷名称
        private String year;//年份
        private String month;//月份
        private String index;//第几套

        public TestPaperInfo(String testPaperName){
            this.testPaperName = testPaperName;
            year = testPaperName.substring(0,testPaperName.indexOf("年"));
            month = testPaperName.substring(testPaperName.indexOf("年")+1,testPaperName.indexOf("月"));
            index = testPaperName.substring(testPaperName.indexOf("第")+1,testPaperName.indexOf("套"));
        }

        public String getTestPaperName() {
            return testPaperName;
        }

        public String getYear() {
            return year;
        }

        public String getMonth() {
            return month;
        }

        public String getIndex() {
            return index;
        }
    }

    /**
     * 作文
     */
    public class WritingFormWord {
        private String question;    //作文题目

        public WritingFormWord(String writing){
            question = writing.substring(writing.indexOf("Directions"),writing.length()-1);
        }

        public String getQuestion() {
            return question;
        }
    }

    public class ListeningFormWord implements Serializable {

        List<Question> listeningQuestionList;

        public ListeningFormWord(String listening){

            listeningQuestionList = listeningQuestion;
        }

        public List<Question> getListeningQuestionList() {
            return listeningQuestionList;
        }
    }

    public class ReadingFormWord implements Serializable {

        private String chooseWordReading;//选词填空的string
        private String quicklyReading;//快速阅读的String
        private String carefulReading;//仔细阅读的String

        public ChooseWordBean chooseWordBean;
        public QuicklyReadingBean quicklyReadingBean;
        public CarefulReadingBean carefulReadingBean;

        public ReadingFormWord(String reading){
            // 1、初始化所需数据
            chooseWordReading = reading.substring(reading.indexOf("Section A"),reading.indexOf("Section B"));
            quicklyReading = reading.substring(reading.indexOf("Section B"),reading.indexOf("Section C"));
            carefulReading = reading.substring(reading.indexOf("Section C"),reading.length()-1);

            // 2、
            chooseWordBean = new ChooseWordBean(chooseWordReading);
            quicklyReadingBean = new QuicklyReadingBean(quicklyReading);
            carefulReadingBean = new CarefulReadingBean(carefulReading);
        }

        public class ChooseWordBean {

            private String title;// 选词填空的文章
            private List<String> optionalWord;

            public ChooseWordBean(String chooseWord){
                title = chooseWord.substring(chooseWord.indexOf("You may not use any of the words in the bank more than once.")+
                        "You may not use any of the words in the bank more than once.".length(),chooseWord.indexOf("A)"));

                optionalWord = optionalWords;
            }

            public String getTitle() {
                return title;
            }

            public List<String> getOptionalWords() {
                return optionalWords;
            }
        }

        public class QuicklyReadingBean {

            private String title;//快速阅读的文章

            private List<Question> quicklyReadQuestion1;

            public QuicklyReadingBean(String quicklyReading){
                String identifying = "Answer Sheet 2."; //截取标识
                title = quicklyReading.substring(quicklyReading.indexOf(identifying)+identifying.length(),
                        quicklyReading.indexOf("36."));

                quicklyReadQuestion1 = quicklyReadQuestion;
            }

            public String getTitle() {
                return title;
            }

            public List<Question> getQuicklyReadQuestion1() {
                return quicklyReadQuestion1;
            }
        }

        public class CarefulReadingBean {

            String passageOne;//仔细阅读-第一篇
            String passageTwo;

            List<Question> readCarefullyQuestion1;

            public CarefulReadingBean(String carefulReading){
                passageOne = carefulReading.substring(carefulReading.indexOf("Passage One"),
                        carefulReading.indexOf("46."));
                passageTwo = carefulReading.substring(carefulReading.indexOf("Passage Two"),
                        carefulReading.indexOf("51."));

                readCarefullyQuestion1 = readCarefullyQuestion;
            }

            public String getPassageOne() {
                return passageOne;
            }

            public String getPassageTwo() {
                return passageTwo;
            }

            public List<Question> getReadCarefullyQuestion1() {
                return readCarefullyQuestion1;
            }
        }

        public ChooseWordBean getChooseWordBean() {
            return chooseWordBean;
        }

        public QuicklyReadingBean getQuicklyReadingBean() {
            return quicklyReadingBean;
        }

        public CarefulReadingBean getCarefulReadingBean() {
            return carefulReadingBean;
        }
    }

    public class TranslationFormWord {
        private String question;    //翻译题目

        public TranslationFormWord(String translation){
            String identifying = "Answer Sheet 2.";//截取的标识
            question = translation.substring(translation.indexOf(identifying)+identifying.length(),
                    translation.length()-1);
        }

        public String getQuestion() {
            return question;
        }
    }

    /**
     * 把word -》 string 后，从 string -》 中把问题取出,放入 list：questions
     */
    public void getCet4Questions(String testPaper){

        String test_paper = testPaper;

        for (int i = 1; i < 56; i++){//一共有55个题目

            int questionIndex = i;//题目序号，从 1 开始
            String stringStart = questionIndex+".";
            String stringEnd = (questionIndex+1)+".";
            String content = "";//用于存储截取出来的题目

  //          Log.v("sssssssssss",stringStart+"   "+stringEnd);

            // 把题目序号遍历出来，既然要遍历出一个string，首先要判断一下是否存在该string
            // 主要是因为 选词填空 部分，题目的序号并不是 1 + "." 形式
            if (test_paper.contains( stringStart ) && test_paper.contains( stringEnd )){
                int indexStart = test_paper.indexOf(stringStart);
                int indexEnd = test_paper.indexOf(stringEnd);
                content = test_paper.substring(indexStart,indexEnd);
            }
            if (stringStart.equals(25+".")){//特殊处理，因为不存在 25.    注：这里使用  (stringStart == 36+".") 会判断为false，但是 (content == "") 会为true，为什么？
                content = test_paper.substring(test_paper.indexOf(25+"."),test_paper.indexOf("Part III"));
            }

            // 特殊处理，不知道为什么，怎么取不到呢？
            // 这里有个坑，在word中的 . 也是有区别的，都是英文，但有个.识别不出来，可能是全角和半角的原因
            if (stringStart.equals(36+".")){ }

            if (stringStart.equals(55+".")){//最后一个题目要特殊处理，因为没有 56.
                content = test_paper.substring(test_paper.indexOf(55+"."),test_paper.indexOf("Part IV"));
            }

            if (content == ""){
                content = questionIndex+"";
            }

            // 拿到内容以后，还要剔除杂质，因为有几个会含有这些string：
            // Questions 3and 4 are based on the news report you have just heard.
            // Section B
            // Section C
            // Part III
            // Passage Two
            if (content.contains("Questions")){
                content = content.substring(0,content.indexOf("Questions"));
            }
            if (content.contains("Section B")){
                content = content.substring(0,content.indexOf("Section B"));
            }
            if (content.contains("Section C")){
                content = content.substring(0,content.indexOf("Section C"));
            }
            if (content.contains("Part III")){
                content = content.substring(0,content.indexOf("Part III"));
            }
            if (content.contains("Passage Two")){
                content = content.substring(0,content.indexOf("Passage Two"));
            }

            questions.add(content);

        }

        // 选词填空提取
        String content = ""; //选词填空部分
        content = test_paper.substring(test_paper.indexOf("Part III"),test_paper.indexOf( "Directions: In this section, you are going to read a passage with ten statements attached to it." ));
        // 选词填空的实例化就在这完成了
        String title = content.substring(content.indexOf("You may not use any of the words in the bank more than once.")+
                "You may not use any of the words in the bank more than once.".length(),content.indexOf("A)")); // 选词填空文章
        String optionalWord = content.substring(content.indexOf("A)"),content.indexOf("Section B"));
 //       LogUtils.longlog("String:optionalWord",optionalWord);
        String letters[] = {"A","B","C","D",
                "E", "F","G","H",
                "I","J", "K","L",
                "M","N","O"};
        for (int i = 0; i < 15; i++){
            String option = "";
            if ( i < 14){
                option = optionalWord.substring(optionalWord.indexOf( letters[i]+")" ),optionalWord.indexOf( letters[i+1]+")" ));
            }
            if (i == 14){
                option = optionalWord.substring(optionalWord.indexOf( letters[i]+")" ),optionalWord.length()-1);
            }
            optionalWords.add(option);
        }
 //       Question question = new Question(title,optionalWords);
 //       insertIntoSqliteChooseWord(question);

 //       LogUtils.longlog("List:questionsALL",questions.toString());
        Log.v("List:questions size",""+questions.size());

    }

    // 实例化问题
    public void createQuestion(){

        String title;//题目
        String choiceA,choiceB,choiceC,choiceD;// 选项

        for (int i = 0; i < 55; i++){

            // 取出每个题目的选项和题目
            String choices = questions.get(i);

            // 把题目实例化，然后创建一个list《Question》

            // i < 25 为听力题
            if (i < 25){
                title = choices.substring(0,choices.indexOf("A)"));
                choiceA = choices.substring(choices.indexOf("A)"),choices.indexOf("B)"));
                choiceB = choices.substring(choices.indexOf("B)"),choices.indexOf("C)"));
                choiceC = choices.substring(choices.indexOf("C)"),choices.indexOf("D)"));
                choiceD = choices.substring(choices.indexOf("D)"),choices.length()-1);

                Question question = new Question(title,choiceA,choiceB,choiceC,choiceD);
                listeningQuestion.add(question);

                continue;
            }
            if (i >= 25 && i <= 34){ //   26 <= i <= 35  为选词填空
                // 不需要处理了，上面已经把这部分做好了  readWord中
                // 错误记录：这里使用了    break;  它是直接跳出当前的for循环，导致后面的数据没有初始化

                continue;   //跳过本次，继续迭代
            }
            if (i > 34 && i < 45){// 快速阅读
                title = choices.substring(0,choices.length()-1);
                Question question = new Question(title);
                quicklyReadQuestion.add(question);
    //            Log.v("listquestions22222",quicklyReadQuestion.size()+"");

                continue;
            }
            else {// 为仔细阅读

                title = choices.substring(0,choices.indexOf("A)"));
                choiceA = choices.substring(choices.indexOf("A)"),choices.indexOf("B)"));
                choiceB = choices.substring(choices.indexOf("B)"),choices.indexOf("C)"));
                choiceC = choices.substring(choices.indexOf("C)"),choices.indexOf("D)"));
                choiceD = choices.substring(choices.indexOf("D)"),choices.length()-1);

                Question question = new Question(title,choiceA,choiceB,choiceC,choiceD);
                readCarefullyQuestion.add(question);
            }

        }

    }

    // 最终提供给外部的 有这  五个
    public TestPaperInfo getTestPaperInfo() {
        return testPaperInfo;
    }

    public WritingFormWord getWritingFormWord() {
        return writingFormWord;
    }

    public ListeningFormWord getListeningFormWord() {
        return listeningFormWord;
    }

    public ReadingFormWord getReadingFormWord() {
        return readingFormWord;
    }

    public TranslationFormWord getTranslationFormWord() {
        return translationFormWord;
    }

    public List<String> getListeningAndReadingAnswer() {
        return listeningAndReadingAnswer;
    }

    public void setListeningAndReadingAnswer(List<String> listeningAndReadingAnswer) {
        this.listeningAndReadingAnswer = listeningAndReadingAnswer;
    }
}
