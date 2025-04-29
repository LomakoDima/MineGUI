package ru.dimalab.minegui.utils.math.interpolation.types;

import ru.dimalab.minegui.utils.math.interpolation.Interpolator;

public class EaseOutInterpolator  implements Interpolator {
    @Override
    public float interpolate(float start, float end, float progress) {
        float t = 1 - progress;
        return start + (end - start) * (1 - t * t * t);
    }
}
