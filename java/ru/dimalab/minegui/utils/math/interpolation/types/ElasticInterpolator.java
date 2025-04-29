package ru.dimalab.minegui.utils.math.interpolation.types;

import ru.dimalab.minegui.utils.math.interpolation.Interpolator;

public class ElasticInterpolator implements Interpolator {
    @Override
    public float interpolate(float start, float end, float progress) {
        if (progress == 0) return start;
        if (progress == 1) return end;
        float t = progress - 1;
        return end + (end - start) * ((float) Math.pow(2, 10 * t) * (float) Math.sin((t * 40 - 3) * Math.PI / 6));
    }
}
