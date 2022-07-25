package com.example.drawshape

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        //First image
        val pointBottom = Point(530, 600)
        val pointLeftTop = Point(0, 0)
        val pointLeftBottom = Point(0, 425)
        val pointRightTop = Point(width, 0)
        val pointRightBottom = Point(width, 425)

        //Second image
        val pointBottom1 = Point(500, 650)
        val pointBottom2 = Point(500, 670)

        val pointLeftTop1 = Point(0, 445)
        val pointLeftBottom1 = Point(0, 465)

        val pointRightTop1 = Point(width, 445)
        val pointRightBottom1 = Point(width, 465)


        val wallPaint = Paint()
        wallPaint.color = getColor(R.color.aaaa)
        wallPaint.style = Paint.Style.FILL

        val path = Path()
        path.moveTo(pointLeftTop.x.toFloat(), pointLeftTop.y.toFloat())
        path.moveTo(pointLeftBottom.x.toFloat(), pointLeftBottom.y.toFloat())
        path.moveTo(pointRightTop.x.toFloat(), pointRightTop.y.toFloat())
        path.moveTo(pointRightBottom.x.toFloat(), pointRightBottom.y.toFloat())
        path.moveTo(pointBottom.x.toFloat(), pointBottom.y.toFloat())

        //Move to point of image2
//        path.moveTo(pointLeftTop1.x.toFloat(), pointLeftTop1.y.toFloat())
//        path.moveTo(pointLeftBottom1.x.toFloat(), pointLeftBottom1.y.toFloat())
//        path.moveTo(pointRightTop1.x.toFloat(), pointRightTop1.y.toFloat())
//        path.moveTo(pointRightBottom1.x.toFloat(), pointRightBottom1.y.toFloat())
//        path.moveTo(pointBottom1.x.toFloat(), pointBottom1.y.toFloat())
//        path.moveTo(pointBottom2.x.toFloat(), pointBottom2.y.toFloat())

        path.reset()

        path.fillType = Path.FillType.EVEN_ODD
        path.lineTo(pointBottom.x.toFloat(), pointBottom.y.toFloat())
        path.lineTo(pointLeftBottom.x.toFloat(), pointLeftBottom.y.toFloat())
        path.lineTo(pointLeftTop.x.toFloat(), pointLeftTop.y.toFloat())
        path.lineTo(pointRightTop.x.toFloat(), pointRightTop.y.toFloat())
        path.lineTo(pointRightBottom.x.toFloat(), pointRightBottom.y.toFloat())
        path.lineTo(pointBottom.x.toFloat(), pointBottom.y.toFloat())


        //Line for draw image2
//        path.lineTo(pointBottom2.x.toFloat(), pointBottom2.y.toFloat())
//        path.lineTo(pointLeftBottom1.x.toFloat(), pointLeftBottom1.y.toFloat())
//        path.lineTo(pointLeftTop1.x.toFloat(), pointLeftTop1.y.toFloat())
//        path.lineTo(pointBottom1.x.toFloat(), pointBottom1.y.toFloat())
//        path.lineTo(pointRightTop1.x.toFloat(), pointRightTop1.y.toFloat())
//        path.lineTo(pointRightBottom1.x.toFloat(), pointRightBottom1.y.toFloat())
//        path.lineTo(pointBottom2.x.toFloat(), pointBottom2.y.toFloat())

        path.close()
        canvas.drawPath(path, wallPaint)

        // set bitmap as background to ImageView
        val imageV = findViewById<ImageView>(R.id.imageV)
        imageV.background = BitmapDrawable(resources, bitmap)

    }
}