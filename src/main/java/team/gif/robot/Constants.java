// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot;


import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final double DEBOUNCE_DEFAULT = 0.020;
    public static final double TURRET_P = .00003;//.00003 good
            //0.02;
    public static final double TURRET_I = 0.00003;//0.00003;//.000015
    public static final double TURRET_D = 0;
    public static final double SINER_SPPED_VOLT = 2;
    public static final double SPINNER_SPEED_PER = .5;
    public static final double INDEX_WHEELS_SPEED_PER = .5;
    public static final double SHOOTER_RPM = 3000;
    public static final class turret{
        public static final double limelightMountAngleDegrees = 27.83;
                //15.73469965;
        public static final double limelightLensHeightInches = 23;
        public static final double goalHeightInches =44.125;
                //17.25;
                //14.125;
    }


    //These constants should be referenced via Robot.swerveDrive.getConstants();


    public static final class Joystick {
        public static final double DEADBAND = 0.1;

    }
    public static final class Field {
        public static final Translation2d HUB_BLUE_TRANSLATION = new Translation2d(Units.inchesToMeters(182.11), Units.inchesToMeters(158.84));
        public static final Translation2d HUB_RED_TRANSLATION = new Translation2d(Units.inchesToMeters((651.22 - 182.11)), Units.inchesToMeters(158.84));
}}