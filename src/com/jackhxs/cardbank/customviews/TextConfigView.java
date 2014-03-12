package com.jackhxs.cardbank.customviews;

import com.jackhxs.cardbank.R;
import com.jackhxs.cardbank.R.color;
import com.jackhxs.cardbank.R.layout;
import com.jackhxs.cardbank.R.styleable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextConfigView extends LinearLayout {

	private View textColor;
	private ImageView mText;
	private RelativeLayout layout;
	private RelativeLayout borderLayout;
	private TextView title;
	
	
	public TextConfigView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	
	    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextConfigView, 0, 0);
	    
	    String titleText = a.getString(R.styleable.TextConfigView_titleText);
	    
	    int color = a.getColor(R.styleable.TextConfigView_valueColor, android.R.color.holo_blue_light);
	    
	    int borderColor = a.getColor(R.styleable.TextConfigView_borderColor, R.color.black);
		    
	    a.recycle();
	
	    setOrientation(LinearLayout.HORIZONTAL);
	    setGravity(Gravity.CENTER_VERTICAL);
	
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.color_style_view, this, true);
	
	    
	    layout = (RelativeLayout) getChildAt(0);
	    
	    borderLayout = (RelativeLayout) layout.getChildAt(0);
	    borderLayout.setBackgroundColor(borderColor);
	    
	    textColor = borderLayout.getChildAt(0);
	    textColor.setBackgroundColor(color);
	
	    title = (TextView) layout.getChildAt(1);
	    title.setText(titleText);
	    //title.setTextColor(0xFFFFFF - color);
	}
	
	public void setText(String text){
		title.setText(text);
	}
 	
	public void setColor(int color) {
		textColor.setBackgroundColor(color);
		title.setTextColor(getContrastColor(color));

		title.setAlpha(100);
	}
	
	public TextConfigView(Context context) {
	    this(context, null);
	}
	
	public void setValueColor(int color) {
	    textColor.setBackgroundColor(color);
	}
	
	public void setImageVisible(boolean visible) {
	    mText.setVisibility(visible ? View.VISIBLE : View.GONE);
	}
	
	public static int getContrastColor(int color) {
		  double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
		  return y >= 128 ? Color.BLACK : Color.WHITE;
		}
	
} 