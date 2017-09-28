package com.klcxkj.rs.bo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.klcxkj.rs.R;

import java.util.List;

public class CityAdapter extends BaseAdapter implements SectionIndexer{

	private Context mContext;
	private List<CityInfoBo> mCitys;

	public CityAdapter(Context context) {
		super();
		this.mContext = context;
	}

	public void setContacts(List<CityInfoBo> citys) {
		this.mCitys = citys;
	}

	public List<CityInfoBo> getContacts() {
		return mCitys;
	}
	
	public int getCount() {
		return mCitys == null ? 0 : mCitys.size();
	}

	public Object getItem(int position) {
		return mCitys == null ? null : mCitys.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Hodler viewHolder = null;
		if (convertView == null) {
			viewHolder = new Hodler();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_view_item_city, null);
			viewHolder.tvLetter = (TextView) convertView
					.findViewById(R.id.tv_letter);
			viewHolder.contact = (TextView) convertView.findViewById(R.id.contact);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (Hodler) convertView.getTag();
		}

	        int section = getSectionForPosition(position);  
	          
	        //
	        if(position == getPositionForSection(section)){  
	            viewHolder.tvLetter.setVisibility(View.VISIBLE);  
	            viewHolder.tvLetter.setText(mCitys.get(position).getCityPy().toUpperCase()
						.substring(0, 1));  
	        }else{  
	            viewHolder.tvLetter.setVisibility(View.GONE);  
	        }  

	        viewHolder.contact.setText(mCitys.get(position).getCityName());

		return convertView;
	}

	private class Hodler {
		TextView tvLetter, contact;
	}

 /** 
     */  
    public int getSectionForPosition(int position) {  
        return mCitys.get(position).getCityPy().charAt(0);  
    }  
  
    public int getPositionForSection(int section) {  
    	if (mCitys != null) {
	        for (int i = 0; i < getCount(); i++) {  
	            String sortStr =mCitys.get(i).getCityPy();  
	            char firstChar = sortStr.charAt(0);  
	            if (firstChar == section) {  
	                return i;  
	            }  
	        }  
    	}
        return -1;  
    }  
      
    public Object[] getSections() {  
        return null;  
    } 

}
