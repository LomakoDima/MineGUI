package ru.dimalab.minegui.utils.math.interpolation;

public interface Interpolator {
    float interpolate(float start, float end, float progress);
}
