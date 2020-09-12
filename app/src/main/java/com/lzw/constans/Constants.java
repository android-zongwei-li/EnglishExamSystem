package com.lzw.constans;

/**
 * 保存一些App中用到的常量。比如
 * 版本信息url、新版本apk url 等。。。
 *
 * @author Li Zongwei
 * @date 2020/9/9
 **/
public class Constants {

    /**
     * 保存需要访问的Url。
     */
    public static final class Url{
        /**
         * 获取App最新版本信息，是一个 json 文件，格式如下：
         * {
         *     "title":"新版本来啦",
         *     "content":"1. 新增应用内更新\n2.应用优化",
         *     "url":"https://english-exam-system-resources.oss-cn-beijing.aliyuncs.com/app_updater/app-release.apk",
         *     "md5":"9e9963e02b7351a144c9d3643fb9dbf5",
         *     "versionCode":"450"
         * }
         */
        public static final String appUpdaterJsonUrl = "https://english-exam-system-resources.oss-cn-beijing.aliyuncs.com/app_updater/app_updater_json.json";
    }

}
