package com.example.appcontrolepoids.alertdialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
 
//Classe concernant l'interface des pop-up d'alertes
public class AlerteView extends View {

    public AlerteView(Context context, AttributeSet attributs) {
        super(context, attributs);

        setupPaint();
        rect = new RectF();
        displayMetrics = getDisplayMetrics();
    }
    
    private Paint drawPaint;
    private final RectF rect;
    private final DisplayMetrics displayMetrics;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rect.right = this.getWidth();
        rect.top = 0;
        rect.left = 0;
        rect.bottom = this.getHeight();
        canvas.drawPath(shapePath(), drawPaint);
    }

    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(Color.WHITE);
        drawPaint.setAntiAlias(true);
    }

    private int dp2px(int dp){
        return (int) (dp * displayMetrics.density + 0.5f);
    }

    private Path shapePath(){
        Path path = new Path();
        path.setFillType(Path.FillType.WINDING);
        path.addRoundRect(rect, 16, 16, Path.Direction.CW);
        path.addCircle((this.getWidth())/2f, 0, dp2px(64), Path.Direction.CCW);
        return  path;
    }

    private DisplayMetrics getDisplayMetrics(){
        return Resources.getSystem().getDisplayMetrics();
    }
}