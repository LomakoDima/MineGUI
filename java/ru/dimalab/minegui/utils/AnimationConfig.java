package ru.dimalab.minegui.utils;

import ru.dimalab.minegui.utils.math.interpolation.InterpolationType;

public class AnimationConfig {
    private final AnimationType animationType;
    private final InterpolationType interpolationType;
    private final int speed;

    public AnimationConfig(AnimationType animationType, InterpolationType interpolationType, int speed) {
        this.animationType = animationType;
        this.interpolationType = interpolationType;
        this.speed = speed;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public InterpolationType getInterpolationType() {
        return interpolationType;
    }

    public int getSpeed() {
        return speed;
    }
}
