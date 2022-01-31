package com.example.cameraov

import android.content.Context
import android.graphics.*
import android.view.View

class ImageView(context: Context) : View(context) {
    private val paint = Paint()
    private val startX = 450F
    private val startY = 120F
    private val stopX = 290F * 3 + (startX)
    private val stopY = 160F * 3 + (startY)
    
    override fun onDraw(canvas: Canvas) {
        paintText("SUPER CAR", canvas)
        paintWheel(canvas)
        carBody(canvas)
        headlight(canvas)
        doorHandle(canvas)
        rosette(canvas)
        windows(canvas)
    }
    
    private fun paintText(text: String, canvas: Canvas) {
        canvas.drawARGB(80, 102, 204, 255)
        paint.color = Color.RED
        paint.textSize = 60F
        canvas.drawText(text, 630F, 100F, paint)
    }
    
    private fun paintWheel(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 7F
        paint.color = Color.BLACK
        canvas.drawCircle(startX + (60 * 3), stopY, 60F, paint)
        canvas.drawCircle(startX + (240 * 3), stopY, 60F, paint)
        paint.color = Color.GRAY
        canvas.drawCircle(startX + (60 * 3), stopY, 30F, paint)
        canvas.drawCircle(startX + (240 * 3), stopY, 30F, paint)
    }
    
    private fun carBody(canvas: Canvas) {
        paint.strokeWidth = 7F
        paint.color = Color.RED
        canvas.drawLine(startX, stopY , startX + (30 * 3), stopY, paint)
        canvas.drawLine(startX + (90 * 3), stopY, startX + (210 * 3), stopY, paint)
        canvas.drawLine(startX + (270 * 3), stopY, stopX, stopY, paint)
        canvas.drawLine(startX, startY, startX, stopY, paint)
        canvas.drawLine(startX, startY, startX + (170 * 3), startY, paint)
        canvas.drawLine(startX + (170 * 3), startY, startX + (210 * 3), startY + (70 * 3), paint)
        canvas.drawLine(startX + (210 * 3), startY + (70 * 3), startX + (290 * 3), startY + (70 * 3), paint)
        canvas.drawLine(startX + (290 * 3), startY + (70 * 3), stopX, stopY, paint)
        //Arc
        canvas.drawArc(RectF(startX + (60 * 3) - 87, stopY - 87 , startX + (60 * 3) + 87, stopY + 87 ), 180F, 180F, false, paint)
        canvas.drawArc(RectF(startX + (240 * 3) - 87, stopY - 87 , startX + (240 * 3) + 87, stopY + 87 ), 180F, 180F, false, paint)
    }
    
    private fun headlight(canvas: Canvas) {
        paint.color = Color.YELLOW
        canvas.drawArc(RectF(stopX - 20, startY + (85 * 3) - 20, stopX + 20, startY + (85 * 3) + 20), 270F, 180F, false, paint)
    }
    
    private fun doorHandle(canvas: Canvas) {
        paint.color = Color.RED
        canvas.drawRect(RectF(startX + (100 * 3), startY + (80 * 3), startX + (120 * 3), startY + (90 * 3)), paint)
    }
    
    private fun rosette(canvas: Canvas) {
        paint.color = Color.GREEN
        canvas.drawCircle(startX + (30 * 3), startY + (90 * 3), 30F, paint)
        paint.strokeWidth = 10F
        paint.color = Color.BLACK
        canvas.drawPoint(startX + (25 * 3), startY + (90 * 3), paint)
        canvas.drawPoint(startX + (35 * 3), startY + (90 * 3), paint)
    }
    
    private fun windows(canvas: Canvas) {
        paint.color = Color.BLUE
        canvas.drawRect(RectF(startX + (20 * 3), startY + (10 * 3), startX + (80 * 3), startY + (70 * 3)), paint)
        canvas.drawLine(startX + (100 * 3), startY + (10 * 3), startX + (100 * 3), startY + (70 * 3), paint)
        canvas.drawLine(startX + (100 * 3), startY + (70 * 3), startX + (200 * 3), startY + (70 * 3), paint)
        canvas.drawLine(startX + (100 * 3), startY + (10 * 3), startX + (160 * 3), startY + (10 * 3), paint)
        canvas.drawLine(startX + (160 * 3), startY + (10 * 3), startX + (200 * 3), startY + (70 * 3), paint)
    }
}
