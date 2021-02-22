package de.lunardoggo.studyassistant.ui.animations

import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar

class ProgressBarAnimation(progressBar: ProgressBar) : Animation() {

    private val progressBar : ProgressBar = progressBar;

    private var start : Float = 0.0f;
    private var end : Float = 0.0f;

    public fun setStartEndValue(start : Float, end: Float) {
        this.start = start;
        this.end = end;
    }

    protected override fun applyTransformation(interpolatedTime: Float, transformation: Transformation?) {
        if(this.start != 0.0f) {
            super.applyTransformation(interpolatedTime, transformation)
            val value = (this.start + (this.end - this.start) * interpolatedTime);
            this.progressBar.progress = (this.start + (this.end - this.start) * interpolatedTime).toInt();
        }
    }
}