package com.resc.remgauge;

/**
 * Created by rob on 7/19/15.
 */
public class GaugeBuilderParams {

    String unitTitle = "";
    String upperTitle = "";
    String lowerTitle = "";
    int scaleMin = 0;
    int scaleMax = 100;
    int notches = 100;
    int incrementsPerLargeNotch = 10;
    int incrementsPerSmallNotch = 1;
    int centerVal = 50;

    //dgView = (DigitalView) view.findViewById(R.id.DigitalView);
    GaugeBuilderParams(String unitTitle, String upperTitle, String lowerTitle, int scaleMin, int scaleMax, int notches, int incrementsPerLargeNotch, int incrementsPerSmallNotch, int centerVal ) {
        unitTitle = unitTitle;
        upperTitle = upperTitle;
        lowerTitle = lowerTitle;
        scaleMin = scaleMin;
        scaleMax = scaleMax;
        notches = notches;
        incrementsPerLargeNotch = incrementsPerLargeNotch;
        incrementsPerSmallNotch = incrementsPerSmallNotch;
        centerVal = centerVal;
    }
    GaugeBuilderParams() {

    }
    GaugeBuilderParams(String upperTitle, int min, int max ) {
        upperTitle = upperTitle;
        scaleMin = min;
        scaleMax = max;
        notches = max - min;
        incrementsPerLargeNotch = notches / 10;
        incrementsPerSmallNotch = 1;
        centerVal = (max-min) / 2;
    }
}
