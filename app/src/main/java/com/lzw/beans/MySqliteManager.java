package com.lzw.beans;

/**
 * 这个类用来提供一些 sqlite 中一些使用到的常量，和sql语句
 *
 */
public class MySqliteManager {

    // 数据库名
    public static final String DATABASE_ENGLISH_EXAM_SYSTEM_DB = "english_exam_system_sqlite_db";

    // 表
    public static final String TABLE_CET4_WORDS = "cet4_words";
    public static final String TABLE_LISTENING_QUESTIONS = "listening_questions";
    public static final String TABLE_READING_CHOOSE_WORD = "reading_choose_word";
    public static final String TABLE_TRANSLATION = "translation";
    public static final String TABLE_WRITING = "writing";

    // 字段，用到的时候把它加到这里来，就不一次性加了

    // sql 语句

    // writing 表字段
    public static final class Writing{
        public static final String QUESTIONID = "QuestionID";
        public static final String QUESTIONType = "QuestionType";
        public static final String SUBJECT = "Subject";
        public static final String YEAR = "Year";
        public static final String MONTH = "Month";
        public static final String INDEX = "Index";
        public static final String QUESTION = "Question";
        public static final String ANSWER = "Answer";
    }

    // writing 表字段
    public static final class Translation{
        public static final String QUESTIONID = "QuestionID";
        public static final String QUESTIONType = "QuestionType";
        public static final String SUBJECT = "Subject";
        public static final String YEAR = "Year";
        public static final String MONTH = "Month";
        public static final String INDEX = "Index";
        public static final String QUESTION = "Question";
        public static final String ANSWER = "Answer";
    }



}
