package com.example.tripremainder;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;

import com.bubbles.src.main.java.com.siddharthks.bubbles.FloatingBubbleConfig;
import com.bubbles.src.main.java.com.siddharthks.bubbles.FloatingBubbleService;

import androidx.core.content.ContextCompat;

public class SimpleService extends FloatingBubbleService {
   // @Override
//    protected FloatingBubbleConfig getConfig() {
//        Context context = getApplicationContext();
//
//        return new FloatingBubbleConfig.Builder()
//                .bubbleIcon(ContextCompat.getDrawable(context, R.drawable.web_icon))
//                .removeBubbleIcon(ContextCompat.getDrawable(context, R.drawable.close_default_icon))
//                .bubbleIconDp(54)
//                .expandableView(getInflater().inflate(R.layout.activity_home, null))
//                .removeBubbleIconDp(54)
//                .paddingDp(4)
//                .borderRadiusDp(0)
//                .physicsEnabled(true)
//                .expandableColor(Color.WHITE)
//                .triangleColor(0xFF215A64)
//                .gravity(Gravity.LEFT)
//                .build();
//    }
}

