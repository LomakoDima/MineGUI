package ru.dimalab.minegui.utils.math.interpolation.types;

import ru.dimalab.minegui.utils.math.interpolation.Interpolator;

public class QuadraticInterpolator implements Interpolator {
    @Override
    public float interpolate(float start, float end, float progress) {
        return start + (end - start) * (progress * progress);
    }
}
