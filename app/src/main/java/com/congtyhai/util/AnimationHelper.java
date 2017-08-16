package com.congtyhai.util;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.congtyhai.haidms.R;

/**
 * Created by HAI on 8/11/2017.
 */

public class AnimationHelper {


    public Animation animationFadeIn(Context context, Animation.AnimationListener  listener) {
        Animation animFadein = AnimationUtils.loadAnimation(context,
                R.anim.fade_in);

        animFadein.setAnimationListener(listener);

        return animFadein;
    }

}
