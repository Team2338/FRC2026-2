// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot;

/**
 * Robot-wide constants.
 *
 * <p>Everything here is a plain value with no logic. Keeping the numbers in one place
 * means the outreach robot can be re-tuned (slower, faster, different RPM) without
 * hunting through commands.
 */
public final class Constants {

    /** Default debounce time for triggers, in seconds. */
    public static final double DEBOUNCE_DEFAULT = 0.020;

    // ---------------------------------------------------------------------
    // Shooter / collector speeds for outreach
    // ---------------------------------------------------------------------

    /**
     * Fixed shooter speed for the 2-3 meter demo distance, in RPM.
     *
     * <p>This is deliberately NOT distance based. The Limelight 2+ sits on the turret,
     * so its geometry changes as the turret moves; a fixed RPM is far more repeatable
     * for demos. Starting point comes from the old 2.8-3.0 m distance-map entries.
     * Tune on the real robot.
     */
    public static final double SHOOTER_RPM = 3000.0;

    /** How far the shooter RPM may be from {@link #SHOOTER_RPM} and still count as "ready". */
    public static final double SHOOTER_RPM_TOLERANCE = 200.0;

    /**
     * Spindexer speed while feeding, in RPM.
     *
     * <p>Negative matches the old {@code SpinerSpin} direction, which is the direction
     * that actually moves note/fuel into the shooter.
     *
     * <p>Reduced from -2000 to cut battery draw during long demos. Raise it back up
     * if fuel starts jamming on the way into the shooter.
     */
    public static final double SPINDEXER_RPM = -1200.0;

    /** Collector motor output while collecting. Negative is "pull in" on this robot. */
    public static final double COLLECTOR_IN_PERCENT = -0.5;

    /**
     * Indexer wheel output while feeding.
     *
     * <p>Collecting does not use this; the collector is a separate mechanism.
     * Reduced from 0.5 to cut battery draw.
     */
    public static final double INDEX_WHEELS_SPEED_PER = 0.3;

    /**
     * Seconds the shooter and indexer run before the spindexer starts feeding.
     *
     * <p>Gives the flywheel a consistent head start before fuel is pushed in.
     */
    public static final double SHOOT_FEED_DELAY_SECONDS = 1.0;

    // ---------------------------------------------------------------------
    // Drivetrain
    // ---------------------------------------------------------------------

    /** Limits and acceleration rates that make the robot safe for young drivers. */
    public static final class Drive {
        /** Maximum forward/back throttle, as a fraction of full output. */
        public static final double MAX_LINEAR_OUTPUT = 0.35;

        /** Maximum turn throttle, as a fraction of full output. */
        public static final double MAX_TURN_OUTPUT = 0.40;

        /** Maximum change in forward output per second (acceleration limit). */
        public static final double LINEAR_SLEW_RATE = 0.70;

        /** Maximum change in turn output per second (acceleration limit). */
        public static final double TURN_SLEW_RATE = 1.20;
    }

    // ---------------------------------------------------------------------
    // Vision (single Limelight 2+ on the turret)
    // ---------------------------------------------------------------------

    /** Everything the turret aim loop needs to find and center one AprilTag. */
    public static final class Vision {
        /** NetworkTables name of the Limelight. Must match the name in the Limelight web UI. */
        public static final String LIMELIGHT_NAME = "limelight-turret";

        /** Limelight pipeline that is configured for AprilTags. */
        public static final int PIPELINE = 0;

        /** Tag to track. -1 means "any single AprilTag". */
        public static final int TARGET_TAG_ID = -1;

        /** Proportional gain for the turret aim loop. */
        public static final double AIM_KP = 0.02;

        /** Integral gain. Left at zero: the turret does not need to remove steady-state error. */
        public static final double AIM_KI = 0.0;

        /** Derivative gain, used to damp oscillation from the ~7 FPS Limelight 2+. */
        public static final double AIM_KD = 0.001;

        /** Largest motor percent the aim loop may command. Keeps the turret from slamming. */
        public static final double AIM_MAX_OUTPUT = 0.35;

        /** Aim error, in degrees, that counts as "centered". */
        public static final double AIM_TOLERANCE_DEG = 1.5;

        /** Trim, in degrees, if the camera is not perfectly aligned with the shooter. */
        public static final double AIM_OFFSET_DEG = 0.0;

        /** Flip to -1 if the turret turns away from the tag. */
        public static final int AIM_DIRECTION = 1;
    }

    /** Controller input handling. */
    public static final class Joystick {
        /** Stick values smaller than this are treated as zero. */
        public static final double DEADBAND = 0.1;
    }
}
