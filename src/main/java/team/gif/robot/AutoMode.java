package team.gif.robot;

public enum AutoMode {
    CENTER_TO_DEPOT("Center to Depot"),
    LEFT_SIDE_BACK("Depot Bump Auto"),
    RIGHT_SIDE_BACK("Outpost Bump Auto")
    // Add more auto modes here as needed
    ;

    private final String pathName;

    AutoMode(String pathName) {
        this.pathName = pathName;
    }

    public String getPathName() {
        return pathName;
    }
}
