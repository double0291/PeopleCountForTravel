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
	/* ���� */
	private ArrayList<People> mPeopleList;

	private int mSelectMode = SELECT_MODE_DEFAULT;

	// ��¼һ�µ�ǰ����ѡ��״̬��View
	private ArrayList<View> mSelectViews = new ArrayList<View>();

	/* ���� */
	private static final String DATA_FILE_NAME = "people.json"; // ���Ա���ݵ��ļ�
	private static final int ITEM_COUNT_FOR_ONE_LINE = 2; // ÿ�е�����
	private static final int ITEM_GAP = 15; // ��Ŀ��Ŀո�PX

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
			// ��ѡ�е���Ŀ���ѡ��״̬
			if (mSelectViews.size() > 0) {
				for (View view : mSelectViews) {
					ImageView foreground = (ImageView) view.findViewById(R.id.foreground);
					foreground.setVisibility(View.GONE);
				}
				// һЩ������
				mSelectViews.clear();
				mSelectMode = SELECT_MODE_DEFAULT;
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initData() {
		// ��ȡ�����ļ�
		StringBuilder sb = new StringBuilder();
		BufferedReader in = null;
		try {
			InputStream json = getAssets().open(DATA_FILE_NAME);
			in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
			String line;
			// ѭ����ȡ
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

		// �������ļ��ж�����JSON�ַ���
		String dataString = sb.toString();
		if (TextUtils.isEmpty(dataString)) {
			return;
		}
		dataString = dataString.replace("\\\\n", "\\n"); // �滻���ж����ת���ַ�

		// ����JSON
		try {
			JSONObject json = new JSONObject(dataString);

			JSONArray array = json.optJSONArray("people");
			if (array == null)
				return;
			// ��Ա����
			int peopleSize = array.length();
			mPeopleList = new ArrayList<People>(peopleSize);

			for (int i = 0; i < peopleSize; i++) {
				JSONObject peopleJson = array.optJSONObject(i);
				if (peopleJson == null)
					continue;
				// ��ȡ��Ա����
				People people = new People();
				people.chineseName = peopleJson.optString("cName");
				people.englishName = peopleJson.optString("eName");
				people.sex = peopleJson.optInt("sex");
				people.phone = peopleJson.optString("phone");
				// �����Ա�б�
				mPeopleList.add(people);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		/* ��̬�����Ա��Ŀ */
		LinearLayout container = (LinearLayout) findViewById(R.id.container);
		// ����ÿһ���Ŀ��
		int itemWidth = (DisplayUtils.getScreenWidthInPx(this) - DisplayUtils.dp2px(ITEM_GAP, this.getResources()) * 3)
				/ ITEM_COUNT_FOR_ONE_LINE;

		int lineCount = 0;

		do {
			LinearLayout linearLayout = new LinearLayout(this);
			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
			// ����ÿһ��
			for (int i = 0; i < ITEM_COUNT_FOR_ONE_LINE; i++) {
				final int currentItemId = i + lineCount * ITEM_COUNT_FOR_ONE_LINE;
				// ���������������Ե���Ŀ
				if (currentItemId < mPeopleList.size()) {
					LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
					final RelativeLayout itemView = (RelativeLayout) inflater
							.inflate(R.layout.people_info_layout, null);
					// ���ÿ��
					LayoutParams layoutParams = new LayoutParams(itemWidth, 2 * itemWidth / 5);
					if (i > 0) {
						// ���Ҽ��
						layoutParams.leftMargin = DisplayUtils.dp2px(ITEM_GAP, this.getResources());
					}
					itemView.setLayoutParams(layoutParams);

					/* ���UI */
					// �����Ա����ò�ͬ�ı���
					RelativeLayout peopleInfoRl = (RelativeLayout) itemView
							.findViewById(R.id.info_container);
					peopleInfoRl
							.setBackgroundResource(mPeopleList.get(currentItemId).sex == 1 ? R.drawable.male_card_background
									: R.drawable.female_card_background);
					// ����
					TextView chineseName = (TextView) itemView.findViewById(R.id.chinese_name);
					chineseName.setText(mPeopleList.get(currentItemId).chineseName);
					TextView englishName = (TextView) itemView.findViewById(R.id.english_name);
					englishName.setText(mPeopleList.get(currentItemId).englishName);
					// �����ڸǵ�ǰ����
					final ImageView foreground = (ImageView) itemView.findViewById(R.id.foreground);
					// ����
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

					// �����Ŀ���¼�����
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

					// ˮƽ�������һ����Ŀ
					linearLayout.addView(itemView);
				}
			}
			// ���¼��
			LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layoutParams.bottomMargin = DisplayUtils.dp2px(ITEM_GAP, this.getResources());
			linearLayout.setLayoutParams(layoutParams);
			// ��ֱ�������һ����Ŀ
			container.addView(linearLayout);
			lineCount++;
		} while (lineCount * ITEM_COUNT_FOR_ONE_LINE < mPeopleList.size());
	}
}
