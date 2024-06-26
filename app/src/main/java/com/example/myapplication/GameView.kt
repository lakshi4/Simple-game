package com.example.myapplication
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import java.lang.Math.max
import java.lang.Math.random

class GameView (var c :Context,var gameTask: GameTask) :View(c)
{
    private var myPaint:Paint? =null
    private var speed=1
    private var time=0
    private var score =0
    private var myGirlPosition =0
    private val otherRocks = ArrayList<HashMap<String,Any>>()

    var viewWidth=0
    var viewHeight = 0
    init {
        myPaint= Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight= this.measuredHeight

        if(time % 700< 10 +speed){
            val map= HashMap<String,Any>()
            map["lane"] =(0..2).random()
            map["startTime"]=time
            otherRocks.add(map)
        }
        time=time + 10 + speed
        val girlWidth = viewWidth /3
        val girlHeight= girlWidth + 10
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.girl,null)

        d.setBounds(
            myGirlPosition * viewWidth /3 + viewWidth / 15 + 25,
            viewHeight-2 - girlHeight,
            myGirlPosition *  viewWidth /3 +viewWidth /15 +girlWidth - 25,
            viewHeight -2

        )
        d.draw(canvas!!)
        myPaint!!.color = Color.GREEN
        var highScore =0

        for (i in otherRocks.indices){
            try {
                val rockWidth = viewWidth /4
                val rockHeight= rockWidth + 10
                val rockX =otherRocks[i]["lane"]as Int * viewWidth/3 + viewWidth/ 15
                var rockY =time- otherRocks[i]["startTime"]as Int
                val d2 = resources.getDrawable(R.drawable.rock,null)

                d2.setBounds(
                    rockX + 25 , rockY - rockHeight, rockX+rockWidth-25,rockY
                )
                d2.draw(canvas)
                if(otherRocks[i]["lane"] as Int == myGirlPosition){
                    if(rockY > viewHeight -2 -rockHeight
                        && rockY < viewHeight -2){
                            gameTask.closeGame(score)
                    }
                }
                if (rockY >viewHeight + rockHeight){
                    otherRocks.removeAt(i)
                    score++
                    speed=1 + Math.abs(score/8)
                    if (score>highScore){
                        highScore = score
                    }
                }
            }
            catch(e:Exception){
                e.printStackTrace()
            }
        }

        myPaint!!.color =Color.BLACK
        myPaint!!.textSize =60f
        canvas.drawText("Score: $score",80f,100f,myPaint!!)
        canvas.drawText("Speed: $speed",380f,100f,myPaint!!)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN ->{
                val  x1 = event.x
                if(x1 < viewWidth /2){
                    if(myGirlPosition> 0){
                        myGirlPosition --
                    }

                }
                if(x1 > viewWidth /2 ){
                    if(myGirlPosition<2){
                        myGirlPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP->{}
        }
        return true
    }

}