package at.htlgkr.steamgameapp;

import at.htlgkr.steam.ReportType;

public class ReportTypeSpinnerItem {

    ReportType rT;
    String displayText;

    public ReportTypeSpinnerItem(ReportType type, String displayText) {
        this.rT = type;
        this.displayText = displayText;
    }

    public ReportType getType() {
        return this.rT;
    }

    public String getDisplayText() {
        return this.displayText;
    }

    @Override
    public String toString() {
        return displayText;
    }
}