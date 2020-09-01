package com.lzw.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lzw.englishExamSystem.R;

import java.util.List;

public class SwListDialog extends Dialog {

	int layoutRes = R.layout.sw_list_dialog;// 布局文件

	Context context;

	Activity mActivity;

	private ListView operateList;

	private List<String> items;

	private ItemListener itemListener;

	public SwListDialog(Context context, List<String> items) {
		super(context, R.style.customMarketScoresDailogStyle);
		this.context = context;
		mActivity = (Activity) context;
		this.items = items;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(layoutRes);
		this.setCanceledOnTouchOutside(true);
		SwListAdapter adapter = new SwListAdapter(this.getContext(), items);
		operateList = (ListView) findViewById(R.id.operateList);// 得到ListView对象的引用
																// /*为ListView设置Adapter来绑定数据*/
		operateList.setAdapter(adapter);

		operateList.setVerticalScrollBarEnabled(false);

		operateList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				final String title = items.get(position);
				if (itemListener != null) {
					itemListener.click(position, title);
				}
				dismiss();
				return false;
			}
		});

		operateList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				final String title = items.get(position);
				if (itemListener != null) {
					itemListener.click(position, title);
				}
				dismiss();
			}
		});

	}

	public void setItemListener(ItemListener itemListener) {
		this.itemListener = itemListener;
	}

	public static interface ItemListener {
		public void click(int position, String title);
	}

	class SwListAdapter extends BaseAdapter {

		private Context context;

		private List<String> items;

		private LayoutInflater inflater;

		public SwListAdapter(Context context, List<String> items) {
			this.items = items;
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public String getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		@Override
		public int getItemViewType(int position) {
			return 1;
		}

		public View createView(final int position, final ViewGroup parent) {
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			View convertView = null;
			convertView = layoutInflater.inflate(R.layout.sw_list_row, parent,
					false);
			return convertView;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final String news = getItem(position);

			if (convertView == null) {
				convertView = createView(position, parent);
				ViewHolder holder = new ViewHolder();
				if (convertView != null) {
					holder.title = (TextView) convertView
							.findViewById(R.id.title);
					convertView.setTag(holder);
				}
			}

			if (convertView != null) {
				final ViewHolder holder = (ViewHolder) convertView.getTag();

				if (holder.title != null) {
					holder.title.setText(news);
				}

			}

			return convertView;
		}

		class ViewHolder {
			TextView title;
		}
	}
}
