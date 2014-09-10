package com.doublechen.peoplecountfortravel.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doublechen.peoplecountfortravel.R;
import com.doublechen.peoplecountfortravel.model.People;
import com.doublechen.peoplecountfortravel.utils.DisplayUtils;

public class MainActivity extends Activity {
	/* 数据 */
	private ArrayList<People> mPeopleList;

	private int mSelectMode = SELECT_MODE_DEFAULT;

	// 记录一下当前处于选中状态的View
	private ArrayList<View> mSelectViews = new ArrayList<View>();

	/* 常量 */
	private static final String DATA_FILE_NAME = "people.json"; // 存成员数据的文件
	private static final int ITEM_COUNT_FOR_ONE_LINE = 2; // 每行的条数
	private static final int ITEM_GAP = 15; // 条目间的空格，PX

	private static final int SELECT_MODE_DEFAULT = 0;
	private static final int SELECT_MODE_FIRST = 1;
	private static final int SELECT_MODE_SELECT = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// initialize data
		initData();
		// initialize views
		initView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 将选中的条目清除选中状态
			if (mSelectViews.size() > 0) {
				for (View view : mSelectViews) {
					ImageView foreground = (ImageView) view.findViewById(R.id.foreground);
					foreground.setVisibility(View.GONE);
				}
				// 一些清理工作
				mSelectViews.clear();
				mSelectMode = SELECT_MODE_DEFAULT;
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initData() {
		// 读取数据文件
		StringBuilder sb = new StringBuilder();
		BufferedReader in = null;
		try {
			InputStream json = getAssets().open(DATA_FILE_NAME);
			in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
			String line;
			// 循环读取
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// 从数据文件中读出的JSON字符串
		String dataString = sb.toString();
		if (TextUtils.isEmpty(dataString)) {
			return;
		}
		dataString = dataString.replace("\\\\n", "\\n"); // 替换换行多出的转移字符

		// 解析JSON
		try {
			JSONObject json = new JSONObject(dataString);

			JSONArray array = json.optJSONArray("people");
			if (array == null)
				return;
			// 成员总数
			int peopleSize = array.length();
			mPeopleList = new ArrayList<People>(peopleSize);

			for (int i = 0; i < peopleSize; i++) {
				JSONObject peopleJson = array.optJSONObject(i);
				if (peopleJson == null)
					continue;
				// 获取成员数据
				People people = new People();
				people.chineseName = peopleJson.optString("cName");
				people.englishName = peopleJson.optString("eName");
				people.sex = peopleJson.optInt("sex");
				people.phone = peopleJson.optString("phone");
				// 加入成员列表
				mPeopleList.add(people);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		/* 动态添加人员条目 */
		LinearLayout container = (LinearLayout) findViewById(R.id.container);
		// 计算每一条的宽度
		int itemWidth = (DisplayUtils.getScreenWidthInPx(this) - DisplayUtils.dp2px(ITEM_GAP, this.getResources()) * 3)
				/ ITEM_COUNT_FOR_ONE_LINE;

		int lineCount = 0;

		do {
			LinearLayout linearLayout = new LinearLayout(this);
			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
			// 构造每一行
			for (int i = 0; i < ITEM_COUNT_FOR_ONE_LINE; i++) {
				final int currentItemId = i + lineCount * ITEM_COUNT_FOR_ONE_LINE;
				// 满足条件，构造性的条目
				if (currentItemId < mPeopleList.size()) {
					LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
					final RelativeLayout itemView = (RelativeLayout) inflater
							.inflate(R.layout.people_info_layout, null);
					// 设置宽高
					LayoutParams layoutParams = new LayoutParams(itemWidth, 2 * itemWidth / 5);
					if (i > 0) {
						// 左右间隔
						layoutParams.leftMargin = DisplayUtils.dp2px(ITEM_GAP, this.getResources());
					}
					itemView.setLayoutParams(layoutParams);

					/* 填充UI */
					// 根据性别设置不同的背景
					RelativeLayout peopleInfoRl = (RelativeLayout) itemView
							.findViewById(R.id.info_container);
					peopleInfoRl
							.setBackgroundResource(mPeopleList.get(currentItemId).sex == 1 ? R.drawable.male_card_background
									: R.drawable.female_card_background);
					// 姓名
					TextView chineseName = (TextView) itemView.findViewById(R.id.chinese_name);
					chineseName.setText(mPeopleList.get(currentItemId).chineseName);
					TextView englishName = (TextView) itemView.findViewById(R.id.english_name);
					englishName.setText(mPeopleList.get(currentItemId).englishName);
					// 用于遮盖的前景层
					final ImageView foreground = (ImageView) itemView.findViewById(R.id.foreground);
					// 拨号
					ImageView phoneCall = (ImageView) itemView.findViewById(R.id.phone_call);
					phoneCall.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (mSelectMode == SELECT_MODE_DEFAULT) {
								Uri telUri = Uri.parse("tel:" + mPeopleList.get(currentItemId).phone);
								Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
								MainActivity.this.startActivity(intent);
							} else {
								if (foreground.getVisibility() == View.GONE) {
									foreground.setVisibility(View.VISIBLE);

									mSelectViews.add(itemView);
								} else {
									foreground.setVisibility(View.GONE);

									mSelectViews.remove(itemView);
									if (mSelectViews.size() == 0) {
										mSelectMode = SELECT_MODE_DEFAULT;
									}
								}
							}
						}
					});

					// 添加条目的事件监听
					itemView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (mSelectMode == SELECT_MODE_FIRST) {
								mSelectMode = SELECT_MODE_SELECT;
							} else if (mSelectMode == SELECT_MODE_SELECT) {
								if (foreground.getVisibility() == View.GONE) {
									foreground.setVisibility(View.VISIBLE);

									mSelectViews.add(itemView);
								} else {
									foreground.setVisibility(View.GONE);

									mSelectViews.remove(itemView);
									if (mSelectViews.size() == 0) {
										mSelectMode = SELECT_MODE_DEFAULT;
									}
								}
							}
						}
					});
					itemView.setOnLongClickListener(new OnLongClickListener() {
						@Override
						public boolean onLongClick(View arg0) {
							if (mSelectMode == SELECT_MODE_DEFAULT) {
								mSelectMode = SELECT_MODE_FIRST;
								foreground.setVisibility(View.VISIBLE);

								mSelectViews.add(itemView);
							}
							return false;
						}
					});

					// 水平方向加上一个条目
					linearLayout.addView(itemView);
				}
			}
			// 上下间隔
			LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layoutParams.bottomMargin = DisplayUtils.dp2px(ITEM_GAP, this.getResources());
			linearLayout.setLayoutParams(layoutParams);
			// 垂直方向加上一排条目
			container.addView(linearLayout);
			lineCount++;
		} while (lineCount * ITEM_COUNT_FOR_ONE_LINE < mPeopleList.size());
	}
}
