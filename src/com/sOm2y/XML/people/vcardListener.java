package com.sOm2y.XML.people;

import java.util.List;

import com.sOm2y.RssFeed.RssItem;

import ezvcard.VCard;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

public class vcardListener implements OnItemLongClickListener {
	List<VCard> stuffVcard;
	// Calling activity reference
	Activity activity1;
	
	public vcardListener(List<VCard> stuff, Activity anActivity) {
		stuffVcard = stuff;
		activity1  = anActivity;
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?>parent, View view, int pos, long id) {
		// TODO Auto-generated method stub
		Intent i = new Intent(Intent.ACTION_VIEW);
		//i.setData(Uri.parse(listItems.get(pos).getLink()));
		
		i.setData(Uri.parse(stuffVcard.get(pos).getSourceDisplayText().getGroup()));
		
		
		activity1.startActivity(i);
		return false;
	}

}
