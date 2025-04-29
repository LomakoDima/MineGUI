package ru.dimalab.minegui.utils.math.interpolation.types;

import ru.dimalab.minegui.utils.math.interpolation.Interpolator;

public class ExponentialInterpolator implements Interpolator {
    @Override
    public float interpolate(float start, float end, float progress) {
        return start + (end - start) * ((float) Math.pow(2, 10 * (progress - 1)));
    }
}

