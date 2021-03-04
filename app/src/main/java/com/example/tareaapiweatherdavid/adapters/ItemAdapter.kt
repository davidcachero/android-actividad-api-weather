package com.example.tareaapiweatherdavid.adapters

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent

class ItemAdapter(
    context:Context?, recyclerView:RecyclerView, private val itemClick:OnItemClickListener?
) : RecyclerView.OnItemTouchListener {
    private var touchOn:GestureDetector

    init {
        touchOn = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e:MotionEvent):Boolean {
                return true
            }

            override fun onLongPress(e:MotionEvent) {
                val pressFavorites = recyclerView.findChildViewUnder(e.x, e.y)
                if (pressFavorites != null && itemClick != null) {
                    itemClick.onTouchClick(
                        pressFavorites, recyclerView.getChildAdapterPosition(pressFavorites)
                    )
                }
            }
        })
    }

    override fun onInterceptTouchEvent(view:RecyclerView, e:MotionEvent):Boolean {
        val touchView = view.findChildViewUnder(e.x, e.y)
        if (touchView != null && itemClick != null && touchOn.onTouchEvent(e)) {
            itemClick.onClick(touchView, view.getChildAdapterPosition(touchView))
        }
        return false
    }

    override fun onTouchEvent(view:RecyclerView, motionEvent:MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept:Boolean) {}
    interface OnItemClickListener {
        fun onTouchClick(view:View?, position:Int)
        fun onClick(view:View?, position:Int)
    }
}