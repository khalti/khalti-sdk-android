package khalti.carbonX.shadow;

public interface ShadowView {
    /**
     * Gets the elevation.
     *
     * @return the elevation value.
     */
    float getElevation();

    /**
     * Sets the elevation value. There are useful values of elevation defined in xml as carbon_elevationFlat, carbon_elevationLow, carbon_elevationMedium, carbon_elevationHigh, carbon_elevationMax
     *
     * @param elevation can be from range [0 - 25]
     */
    void setElevation(float elevation);

    float getTranslationZ();

    void setTranslationZ(float translationZ);

    ShadowShape getShadowShape();

    Shadow getShadow();

    void invalidateShadow();
}
