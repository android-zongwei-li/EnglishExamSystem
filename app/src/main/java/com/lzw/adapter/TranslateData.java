/**
 * 
 */
package com.lzw.adapter;

import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.WebExplain;

import java.io.Serializable;
import java.util.List;

/**
 * @author lukun
 * 
 */
public class TranslateData implements Serializable {

	private Long createTime;

	private Translate translate;

	public TranslateData(Long createTime, Translate translate) {
		super();
		this.createTime = createTime;
		this.translate = translate;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Translate getTranslate() {
		return translate;
	}

	public void setTranslate(Translate translate) {
		this.translate = translate;
	}

	public String means() {
		return listStr(translate.getExplains());
	}
	
	public String translates() {
		return listStr(translate.getTranslations());
	}
	
	public String getQuery(){
		return translate.getQuery();
	}
	
	public String webMeans() {
		StringBuilder sb = new StringBuilder();

		List<WebExplain> explains = translate.getWebExplains();

		if (explains != null) {
			for (WebExplain s : explains) {
				sb.append(s.getKey()).append(":").append(listStr(s.getMeans())).append("\n");
			}
		}

		return sb.toString();
	}
	
	private String listStr(List<String> list) {
		StringBuilder sb = new StringBuilder();

		if (list != null) {
			for (String s : list) {
				sb.append(s).append("\n");
			}
		}

		return sb.toString();
	}


}
