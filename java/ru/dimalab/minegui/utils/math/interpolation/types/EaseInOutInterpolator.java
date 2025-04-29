package ru.dimalab.minegui.utils.math.interpolation.types;

import ru.dimalab.minegui.utils.math.interpolation.Interpolator;

public class EaseInOutInterpolator  implements Interpolator {
    @Override
    public float interpolate(float start, float end, float progress) {
        float t = progress * 2;
        if (t < 1) {
            return start + (end - start)/2 * t * t * t;
        }
        t -= 2;
        return start + (end - start)/2 * (t * t * t + 2);
    }
}
