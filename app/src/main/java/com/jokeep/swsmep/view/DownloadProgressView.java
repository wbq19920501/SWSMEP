package com.jokeep.swsmep.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jokeep.swsmep.R;

public class DownloadProgressView extends View{

	// 画实心圆的画笔
		private Paint mCirclePaint;
		// 画圆环的画笔
		private Paint mRingPaint;
		// 画圆环补环的画笔
		private Paint mAntiRingPaint;
		// 画百分比字体的画笔
		private Paint mPercentPaint;
		// 画字体的画笔
		private Paint mTextPaint;
		// 圆形颜色
		private int mCircleColor;
		// 圆环颜色
		private int mRingColor;
		// 半径
		private float mRadius;
		// 圆环半径
		private float mRingRadius;
		// 圆环宽度
		private float mStrokeWidth;
		// 圆心x坐标
		private int mXCenter;
		// 圆心y坐标
		private int mYCenter;
		// 字的长度
		private float mTxtWidth;
		// 字的高度
		private float mTxtHeight;
		// 总进度
		private int mTotalProgress = 100;
		// 当前进度
		private int mProgress;

		private String text = "正在下载";
	
	public DownloadProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获取自定义的属性
		initAttrs(context, attrs);
		initVariable();
	}
	
	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.TasksCompletedView, 0, 0);
		mRadius = typeArray.getDimension(R.styleable.TasksCompletedView_radius, 80);
		mStrokeWidth = typeArray.getDimension(R.styleable.TasksCompletedView_strokeWidth, 10);
		mCircleColor = typeArray.getColor(R.styleable.TasksCompletedView_circleColor, 0xFFFFFFFF);
		mRingColor = typeArray.getColor(R.styleable.TasksCompletedView_ringColor, Color.rgb(78, 179, 229));
		
		mRingRadius = mRadius + mStrokeWidth / 2;
	}

	private void initVariable() {
		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(mCircleColor);
		mCirclePaint.setStyle(Paint.Style.FILL);
		
		mRingPaint = new Paint();
		mRingPaint.setAntiAlias(true);
		mRingPaint.setColor(mRingColor);
		mRingPaint.setStyle(Paint.Style.STROKE);
		mRingPaint.setStrokeWidth(mStrokeWidth);
		
		mAntiRingPaint = new Paint();
		mAntiRingPaint.setAntiAlias(true);
		mAntiRingPaint.setColor(0x80ffffff);
		mAntiRingPaint.setStyle(Paint.Style.STROKE);
		mAntiRingPaint.setStrokeWidth(mStrokeWidth);
		
		mPercentPaint = new Paint();
		mPercentPaint.setAntiAlias(true);
		mPercentPaint.setStyle(Paint.Style.FILL);
		mPercentPaint.setARGB(255, 255, 255, 255);
		mPercentPaint.setTextSize(mRadius / 2);
		
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setStyle(Paint.Style.FILL);
		mTextPaint.setARGB(255, 255, 255, 255);
		mTextPaint.setTextSize(mRadius / 3);
		
		
		FontMetrics fm = mTextPaint.getFontMetrics();
		mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		mXCenter = getWidth() / 2;
		mYCenter = getHeight() / 2;
		
//		canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);
		
		if (mProgress > 0 ) {
			RectF oval = new RectF();
			oval.left = (mXCenter - mRingRadius);
			oval.top = (mYCenter - mRingRadius);
			oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
			oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
//			canvas.drawCircle(mXCenter, mYCenter, mRadius + mStrokeWidth / 2, mAntiRingPaint);
			canvas.drawArc(oval, -90, ((float)mProgress / mTotalProgress) * 360, false, mRingPaint); //
			canvas.drawArc(oval, ((float)mProgress / mTotalProgress) * 360 - 90, 360 - ((float)mProgress / mTotalProgress) * 360, false, mAntiRingPaint); //
//			canvas.drawCircle(mXCenter, mYCenter, mRadius + mStrokeWidth / 2, mAntiRingPaint);
			String txt = mProgress + "%";
			mTxtWidth = mPercentPaint.measureText(txt, 0, txt.length());
			canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mPercentPaint);
			
			mTxtWidth = mTextPaint.measureText(text, 0, text.length());
			canvas.drawText(text+"....", mXCenter - mTxtWidth / 2, mYCenter + mRingRadius + mTxtHeight, mTextPaint);
		}
	}
	
	public void setProgress(int progress) {
		mProgress = progress;
//		invalidate();
		postInvalidate();
	}

	public void setText(String text){
		this.text = text;
	}

}
