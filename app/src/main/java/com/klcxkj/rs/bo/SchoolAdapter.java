package com.klcxkj.rs.bo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.klcxkj.rs.R;

import java.util.List;

public class SchoolAdapter extends BaseAdapter implements SectionIndexer {

	private Context mContext;
	private List<SchoolInfoBo> mSchools;//

	public SchoolAdapter(Context context) {
		super();
		this.mContext = context;
	}

	public void setContacts(List<SchoolInfoBo> schools) {
		this.mSchools = schools;
	}

	public List<SchoolInfoBo> getContacts() {
		return mSchools;
	}

	public int getCount() {
		return mSchools == null ? 0 : mSchools.size();
	}

	public Object getItem(int position) {
		return mSchools == null ? null : mSchools.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (mSchools == null || mSchools.size() == 0) {
			return null;
		}
		Hodler viewHolder = null;
		if (convertView == null) {
			viewHolder = new Hodler();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_view_item_city, null);
			viewHolder.tvLetter = (TextView) convertView
					.findViewById(R.id.tv_letter);
			viewHolder.contact = (TextView) convertView
					.findViewById(R.id.contact);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (Hodler) convertView.getTag();
		}

		int section = getSectionForPosition(position);

		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mSchools.get(position).getPriPy()
					.toUpperCase().substring(0, 1));
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}

		viewHolder.contact.setText(mSchools.get(position).getPrjName());

		return convertView;
	}

	private class Hodler {
		TextView tvLetter, contact;
	}

	/** 
     */
	public int getSectionForPosition(int position) {
		if (TextUtils.isEmpty(mSchools.get(position).getPriPy())) {
			return 0;
		} else {
			return mSchools.get(position).getPriPy().charAt(0);
		}

	}

	/** 
     */
	public int getPositionForSection(int section) {
		try {
			if (mSchools != null) {
				for (int i = 0; i < getCount(); i++) {
					String sortStr = mSchools.get(i).getPriPy();
					char firstChar = sortStr.toUpperCase().charAt(0);
					if (firstChar == section) {
						return i;
					}
				}
			}else {
				return -1;
			}
		} catch (Exception e) {
			return -1;
		}
		return -1;
		
		
	}

	public Object[] getSections() {
		return null;
	}

}
