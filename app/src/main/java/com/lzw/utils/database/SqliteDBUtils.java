package com.lzw.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.lzw.beans.MySqliteManager;
import com.lzw.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

/**
 * 这个类就是实现从assets目录读取数据库文件然后写入SDcard中,
 * 如果在SDcard中存在，就打开数据库，不存在就从assets目录下复制过去
 * 为什么需要这个类？见https://blog.csdn.net/u010800530/article/details/40192279
 */
public class SqliteDBUtils {
    //数据库存储路径
    private String filePath = "data/data/com.lzw.englishExamSystem/english_exam_system_sqlite.db";
    //数据库存放的文件夹 data/data/com.lzw.englishExamSystem 下面
    private String pathStr = "data/data/com.lzw.englishExamSystem";

    SQLiteDatabase database;

    public SQLiteDatabase openDatabase(Context context) {
        System.out.println("filePath:" + filePath);
        File jhPath = new File(filePath);
        //查看数据库文件是否存在
        if (jhPath.exists()) {
            Log.i("test", "存在数据库");
            //存在则直接返回打开的数据库
            return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
        } else {
            //不存在先创建文件夹
            File path = new File(pathStr);
            Log.i("test", "pathStr=" + path);
            if (path.mkdir()) {
                LogUtils.i("test", "创建成功");
            } else {
                LogUtils.e("test", "创建失败");
            }
            try {
                //得到资源
                AssetManager am = context.getAssets();
                //得到数据库的输入流
                InputStream is = am.open("english_exam_system_sqlite.db");
                Log.i("test", is + "");
                //用输出流写到SDcard上面
                FileOutputStream fos = new FileOutputStream(jhPath);
                Log.i("test", "fos=" + fos);
                Log.i("test", "jhPath=" + jhPath);
                //创建byte数组  用于1KB写一次
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    LogUtils.v("test", "得到");
                    fos.write(buffer, 0, count);
                }
                //最后关闭就可以了
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            //如果没有这个数据库  我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了
            return openDatabase(context);
        }
    }

    /**
     * 往数据库中存入 对象
     * 1.把对象序列化
     * 2.把对象存入数据库
     *
     * @param db
     * @param object
     * @param sql
     */
    public void saveData(SQLiteDatabase db, Object object, String sql) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            byte data[] = arrayOutputStream.toByteArray();
            objectOutputStream.close();
            arrayOutputStream.close();
            db.execSQL(sql, new Object[]{data});
            db.close();// 这句话可以省略，交给外部来关闭
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void saveDataByGson(SQLiteDatabase db, Object object, String sql) {
        Gson gson = new Gson();
        ContentValues values = new ContentValues();
        values.put("Question", gson.toJson(object).getBytes());
        // insert or update the DB
        String whereClause = "QuestionID = ?";
        String whereArgs[] = {"cet4_2016_06_01_reding_choose_word"};
        db.update(MySqliteManager.TABLE_READING_CHOOSE_WORD, values, whereClause, whereArgs);
    }

    public void copyDB() {
        File dbFile = new File(Environment.getDataDirectory().getAbsolutePath() + "/data/" + MySqliteManager.DATABASE_ENGLISH_EXAM_SYSTEM_DB);
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(dbFile);
            fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + MySqliteManager.DATABASE_ENGLISH_EXAM_SYSTEM_DB);
            int len = 0;
            byte[] buffer = new byte[2048];
            while (-1 != (len = fis.read(buffer))) {
                fos.write(buffer, 0, len);
            }
            fos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) fos.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
