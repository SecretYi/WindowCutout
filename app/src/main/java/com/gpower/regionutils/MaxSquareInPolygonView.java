package com.gpower.regionutils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Secret
 * @since 2018/9/29
 */
public class MaxSquareInPolygonView extends View {

    private Path mPath;
    private Paint mPaint;
    private Region mBonusRegion;
    private Region mTotalRegion;
    private List<PointF> mFloats;
    private Rect mSquareRect;
    private PointF mGravityPointF;
    private Paint mTextPaint;

    public MaxSquareInPolygonView(Context context) {
        this(context, null);
    }
    public MaxSquareInPolygonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxSquareInPolygonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPath = new Path();
        mFloats = new ArrayList<>();
        PointF pointF = new PointF(80*4,60*4);
        PointF pointF1 = new PointF(160*4,200*4);
        PointF pointF2 = new PointF(320*4,260*4);
        PointF pointF3 = new PointF(380*4,300*4);
        PointF pointF4 = new PointF(400*4,310*4);
        mFloats.add(pointF);
        mFloats.add(pointF1);
        mFloats.add(pointF2);
        mFloats.add(pointF3);
        mFloats.add(pointF4);
        mPath.moveTo(pointF.x,pointF.y);
        mPath.lineTo(pointF1.x,pointF1.y);
        mPath.lineTo(pointF2.x,pointF2.y);
        mPath.lineTo(pointF3.x,pointF3.y);
        mPath.lineTo(pointF4.x,pointF4.y);
        mPath.close();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2f);
        mTotalRegion = new Region();
        mTotalRegion.set(0,0,2000,2000);
        mBonusRegion = new Region();
        mBonusRegion.setPath(mPath,mTotalRegion);
        mTextPaint = new Paint();
        mTextPaint.setTextSize(500f);
        getMaxSquareInPolygon();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(null != mPath){
            mPaint.setColor(Color.BLACK);
            canvas.drawPath(mPath,mPaint);
        }
        if(null != mSquareRect){
            mPaint.setColor(Color.RED);
            canvas.drawRect(mSquareRect,mPaint);
            canvas.drawText("88",mSquareRect.left,mSquareRect.bottom,mTextPaint);
        }
    }

    private void getMaxSquareInPolygon(){
        mGravityPointF = getGravityPointOfPath(mFloats);
        float x = mGravityPointF.x;
        float y = mGravityPointF.y;
        int width = 1000;
        int left = Math.round(x - width);
        int top = Math.round(y - width);
        int right = Math.round(x + width);
        int bottom = Math.round(y + width);
        while (!mBonusRegion.contains(left,top)
                || !mBonusRegion.contains(right,bottom)
                || !mBonusRegion.contains(right,top)
                || !mBonusRegion.contains(left,bottom)){
            --width;
            left = Math.round(x - width);
            top = Math.round(y - width);
            right = Math.round(x + width);
            bottom = Math.round(y + width);
        }
        mSquareRect = new Rect(left,top,right,bottom);

        float textWidth = mTextPaint.measureText("88");
        while (textWidth > mSquareRect.width()){
            mTextPaint.setTextSize(mTextPaint.getTextSize() / 2);
            textWidth = mTextPaint.measureText("88");
        }

        invalidate();
    }

     /**
     * 获取不规则多边形重心点
     *
     * @param mPoints
     * @return
     */
    public  PointF getGravityPointOfPath(List<PointF> mPoints) {
        //多边形面积
        float area = 0f;
        // 重心的x、y
        float gx = 0f, gy = 0f;
        for (int i = 1; i <= mPoints.size(); i++) {
            float iLat = mPoints.get(i % mPoints.size()).x;
            float iLng = mPoints.get(i % mPoints.size()).y;
            float nextLat = mPoints.get(i - 1).x;
            float nextLng = mPoints.get(i - 1).y;
            float temp = (iLat * nextLng - iLng * nextLat) / 2f;
            area += temp;
            gx += temp * (iLat + nextLat) / 3.0;
            gy += temp * (iLng + nextLng) / 3.0;
        }
        gx = gx / area;
        gy = gy / area;
        return new PointF(gx, gy);
    }
}
