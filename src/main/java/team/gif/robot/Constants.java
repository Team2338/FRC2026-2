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
    public static final double TURRET_P = .000035;//.00003 good
            //0.02;
    public static final double TURRET_I = 0.0000;//0.00003;//.000015
    public static final double TURRET_D = 0;
    public static final double SINER_SPPED_VOLT = 2;
    public static final double SPINNER_SPEED_PER = .5;
    public static final double INDEX_WHEELS_SPEED_PER = .5;
    public static final double SHOOTER_RPM = 3800; //4000

    // DriveTrain constants
    public static final class DriveTrain {
        public static final double TRACK_WIDTH_METERS = 0.597;
        public static final double DRIVE_GEAR_RATIO = 10.71;
        public static final double DRIVE_WHEEL_DIAMETER_METERS = 0.47879;
        public static final double DRIVE_ENCODER_POSITION_CONVERSION_FACTOR_METERS = (1.0 / 10.71) * 0.47879;
        public static final double DRIVE_MASS_KG = 56.7;
        public static final double DRIVE_MOI_KG_M2 = 20.21;
        public static final double WHEEL_RADIUS_METERS = 0.0762;
        public static final double WHEEL_MASS_KG = 5;
        public static final double WHEEL_MOI_KG_M2 = 1;
        public static final int DRIVE_MOTORS_PER_MODULE = 4;
        public static final double MODULE_GEAR_RATIO = 38;
        public static final double INITIAL_POSE_X_METERS = 16.541 - 3.56;
        public static final double INITIAL_POSE_Y_METERS = 8.07 - 4.09;
        public static final double VISION_X_STD_DEV_METERS = 0.7;
        public static final double VISION_Y_STD_DEV_METERS = 0.7;
        public static final double VISION_THETA_STD_DEV_RADIANS = 9999999.0;
        public static final double MAX_VISION_POSE_JUMP_METERS = 20;
        public static final double DRIVE_PID_P = 0.08;
        public static final double PATHPLANNER_P = 0.02;
        public static final double FIELD_Y_DIVIDER_METERS = Units.inchesToMeters(158.84);
        public static final double TURN_MULTIPLIER = 1.0 / 12.0;
        public static final double SHOOTING_SPEED_REDUCTION = 0.5;
        public static final String LIMELIGHT_NAME = "limelight-side";
    }

    // Shooter constants
    public static final class Shooter {
        public static final double SHOOTER_KS = 0.12278;
        public static final double SHOOTER_KV = 0.11522;
        public static final double SHOOTER_KA = 0.0078728;
        public static final double RPS_TO_RPM = 60;
    }

    // Turret constants
    public static final class Turret {
        public static final double TURRET_PID_P = 2;
        public static final int TURRET_CURRENT_LIMIT_STALL = 7;
        public static final int TURRET_CURRENT_LIMIT_FREE = 7;
        public static final double TURRET_CURRENT_LIMIT_LIMIT_TIME_MS = 300;
        public static final double TURRET_ALLOWED_CLOSED_LOOP_ERROR = 0.01;
        public static final double TURRET_SOFT_LIMIT_FORWARD = 0.5;
        public static final double TURRET_SOFT_LIMIT_REVERSE = -30.5;
        public static final double TURRET_STOP_POSITION = -30.5;
        public static final double TURRET_ZERO_STOP_POSITION = -0.5;
        public static final double TURRET_MANUAL_TURN_SPEED_MULTIPLIER = 0.35;
        public static final double TURRET_VOLTAGE_MULTIPLIER_NORMAL = 2000;
        public static final double TURRET_VOLTAGE_MULTIPLIER_LIMITED = 1500;
        public static final double TURRET_VOLTAGE_MULTIPLIER_LIMITED_DOUBLE = 3000;
        public static final double GYRO_ADJUSTMENT_DEGREES = 2;
        public static final double TURRET_OFFSET_DEGREES = 4.262;
        public static final double TURRET_WRAP_THRESHOLD = 4.262;
        public static final double TURRET_MIN_RANGE = -25.738;
        public static final double TURRET_WRAP_OFFSET = 30;
    }

    // Motor subsystem PID constants
    public static final class MotorPID {
        public static final double COLLECTOR_PID_P = 0.00025;
        public static final double COLLECTOR_PIVOT_PID_P = 0.0005;
        public static final double SPIN_DEXER_PID_P = 0.000125;
        public static final double INDEXER_PID_P = 0.000;
    }

    // Command constants
    public static final class Commands {
        public static final double SPIN_DEXER_REVERSE_RPM = -2000;
        public static final double SPIN_DEXER_FORWARD_RPM = 1000;
        public static final double INDEXER_WHEELS_REVERSE_SPEED = -0.3;
        public static final double SHOOT_TEST_RPM = 4400;
        public static final double AGITATE_CYCLE_1 = 25;
        public static final double AGITATE_CYCLE_2 = 40;
        public static final double COLLECTOR_SPEED = -0.75;
        public static final double COLLECTOR_PIVOT_SPEED_UP = 0.2;
        public static final double COLLECTOR_PIVOT_SPEED_DOWN = -0.2;
        public static final double SHOOT_TIMEOUT_SECONDS = 0.25;
        public static final double SHOOT_TEST_TIMEOUT_SECONDS = 0.25;
        public static final double MANUAL_MODE_THRESHOLD = 0.05;
    }

    // OI constants
    public static final class OI {
        public static final double DRIVE_SPEED_MULTIPLIER = 0.85;
        public static final double GYRO_RESET_0 = 0;
        public static final double GYRO_RESET_90 = 90;
        public static final double GYRO_RESET_180 = 180;
        public static final double GYRO_RESET_270 = 270;
    }

    // TurretTurnCalc constants
    public static final class TurretTurnCalc {
        public static final double MEASUREMENT_OFFSET = 0.88;
    }

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
        public static final double FIELD_WIDTH_INCHES = 651.22;
        public static final double HUB_X_INCHES_BLUE = 182.11;
        public static final double HUB_Y_INCHES = 158.84;
        public static final double DEPOT_X_INCHES = 32;
        public static final double DEPOT_Y_INCHES = 212;
        public static final double OUTPOST_Y_INCHES = 105;
        public static final double OFFSET_X_INCHES = 36;

        public static final Translation2d HUB_BLUE_TRANSLATION = new Translation2d(Units.inchesToMeters(HUB_X_INCHES_BLUE), Units.inchesToMeters(HUB_Y_INCHES));
        public static final Translation2d HUB_RED_TRANSLATION = new Translation2d(Units.inchesToMeters(FIELD_WIDTH_INCHES - HUB_X_INCHES_BLUE), Units.inchesToMeters(HUB_Y_INCHES));
        public static final Translation2d PASS_RED_DEPOT = new Translation2d(Units.inchesToMeters(FIELD_WIDTH_INCHES - DEPOT_X_INCHES), Units.inchesToMeters(DEPOT_Y_INCHES - OFFSET_X_INCHES));
        public static final Translation2d PASS_RED_OUTPOST = new Translation2d(Units.inchesToMeters(FIELD_WIDTH_INCHES - DEPOT_X_INCHES), Units.inchesToMeters(OUTPOST_Y_INCHES - OFFSET_X_INCHES));
        public static final Translation2d PASS_BLUE_DEPOT = new Translation2d(Units.inchesToMeters(DEPOT_X_INCHES), Units.inchesToMeters(DEPOT_Y_INCHES - OFFSET_X_INCHES));
        public static final Translation2d PASS_BLUE_OUTPOST = new Translation2d(Units.inchesToMeters(DEPOT_X_INCHES), Units.inchesToMeters(OUTPOST_Y_INCHES - OFFSET_X_INCHES));
}
    public static final class Vision {
        public static final String LIMELIGHT_NAME = "limelight-front";
        public static final int PIGEON_ID = 9; // change to your real CAN ID
    }
    public static final class config{}
}