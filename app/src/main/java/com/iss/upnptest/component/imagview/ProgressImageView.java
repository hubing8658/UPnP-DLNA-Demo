package com.iss.upnptest.component.imagview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author hubing
 * @version 1.0.0 2015-5-21
 */

public class ProgressImageView extends ImageView {

	private int prog = 0;
	private int progColor = Color.parseColor("#FF009ACD");
	private int bProgColor = Color.parseColor("#FF838B83");
	private Paint paint;
	private RectF rectF;
	private int strokeWith = 30;
	private boolean showProg = false;
	
	public ProgressImageView(Context context) {
		super(context);
		init();
	}
	
	public ProgressImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public ProgressImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		paint = new Paint();
		rectF = new RectF();
	}
	
	@Override
	public void setImageDrawable(Drawable drawable) {
		showProg = false;
		super.setImageDrawable(drawable);
	}
	
	@Override
	public void setImageResource(int resId) {
		showProg = false;
		super.setImageResource(resId);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (!showProg) {
			return;
		}
		//  图片的宽、高
		int with = getWidth();
		int height = getHeight();
		
		int radius = (Math.min(with, height) / 2 - strokeWith) / 2;
		
		int left = with / 2 - radius;
		int top = height / 2 - radius;
		int right = with - left;
		int bottom = height - top;
		
		rectF.set(left, top, right, bottom);
		
		paint.setColor(Color.GRAY);
		paint.setTextSize(radius);
		paint.setStyle(Style.FILL);
		paint.setTextAlign(Align.CENTER);
		
		FontMetrics fontMetrics = paint.getFontMetrics();
		// 计算文字高度
		float fontHeight = fontMetrics.bottom - fontMetrics.top;
		// 计算文字baseline
		float textBaseY = height - (height - fontHeight) / 2 - fontMetrics.bottom;
		canvas.drawText(String.valueOf(prog), with / 2, textBaseY, paint);
		
		paint.setAntiAlias(true);
		paint.setStrokeWidth(strokeWith);
		paint.setStyle(Style.STROKE);

		
		paint.setColor(bProgColor);
		canvas.drawArc(rectF, -90, 360, false, paint);
		
		paint.setColor(progColor);
		canvas.drawArc(rectF, -90, ((float) (prog / 100.0)) * 360, false, paint);
		
	}
	
	public void setLoadProgress(int progress) {
		this.prog = progress;
		showProg = true;
		invalidate();
	}
	
	public void setLoadProgressForPost(int progress) {
		this.prog = progress;
		showProg = true;
		postInvalidate();
	}
	
}

