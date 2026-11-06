// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.RobotMap;

/**
 * Tank-style drivetrain for the outreach robot.
 *
 * <p>This is the competition drivetrain stripped down to what a demo needs:
 * two motors per side, encoder + pigeon odometry for the dashboard, and an
 * arcade drive method that is intentionally slow and smooth.
 *
 * <p>All 2026 game logic (hub aiming, pass zones, PathPlanner, Limelight pose
 * fusion) was removed on purpose. Limelight pose fusion is wrong here because
 * the camera now lives on the rotating turret, so its pose relative to the
 * robot changes every time the turret moves.
 */
public class DriveTrain extends SubsystemBase {

    /** Real effective track width in meters. Keep unless the chassis changes. */
    private static final double TRACK_WIDTH_METERS = 0.597;

    /**
     * Converts SparkMax motor rotations into meters traveled.
     *
     * <p>Same value the competition code used: (1 / gear ratio) * wheel circumference.
     */
    private static final double DRIVE_ENCODER_POSITION_CONVERSION_FACTOR_METERS =
            (1.0 / 10.71) * 0.47879;

    /**
     * Starting pose for odometry.
     *
     * <p>The outreach robot does not use field coordinates, so the origin is fine.
     * It only exists so the pose estimator has a valid starting point.
     */
    private static final Pose2d INITIAL_POSE = new Pose2d();

    private final SparkMax leftFrontNEO;
    private final SparkMax leftBackNEO;
    private final SparkMax rightFrontNEO;
    private final SparkMax rightBackNEO;

    private final DifferentialDrive drive;

    private final DifferentialDriveKinematics kinematics =
            new DifferentialDriveKinematics(TRACK_WIDTH_METERS);

    private final DifferentialDrivePoseEstimator poseEstimator;

    /**
     * Ramp limiters. They convert a step in joystick input into a gradual change,
     * which is the difference between "fun demo" and "robot wheelies at a child".
     */
    private final SlewRateLimiter linearSlew =
            new SlewRateLimiter(Constants.Drive.LINEAR_SLEW_RATE);
    private final SlewRateLimiter turnSlew =
            new SlewRateLimiter(Constants.Drive.TURN_SLEW_RATE);

    /** Publishes the estimated pose to NetworkTables for AdvantageScope. */
    private final StructPublisher<Pose2d> publisher = NetworkTableInstance.getDefault()
            .getStructTopic("ROBOT POSITION", Pose2d.struct)
            .publish();

    public DriveTrain() {
        leftFrontNEO = new SparkMax(RobotMap.LEFT_FRONT_NEO, SparkLowLevel.MotorType.kBrushless);
        leftBackNEO = new SparkMax(RobotMap.LEFT_BACK_NEO, SparkLowLevel.MotorType.kBrushless);
        rightFrontNEO = new SparkMax(RobotMap.RIGHT_FRONT_NEO, SparkLowLevel.MotorType.kBrushless);
        rightBackNEO = new SparkMax(RobotMap.RIGHT_BACK_NEO, SparkLowLevel.MotorType.kBrushless);

        SparkMaxConfig configLeftFront = new SparkMaxConfig();
        SparkMaxConfig configLeftBack = new SparkMaxConfig();
        SparkMaxConfig configRightFront = new SparkMaxConfig();
        SparkMaxConfig configRightBack = new SparkMaxConfig();

        // Brake mode keeps the robot from rolling away while a kid is not touching the sticks.
        configLeftFront.idleMode(SparkMaxConfig.IdleMode.kBrake);
        configLeftBack.idleMode(SparkMaxConfig.IdleMode.kBrake).follow(leftFrontNEO);
        configRightFront.idleMode(SparkMaxConfig.IdleMode.kBrake);
        configRightBack.idleMode(SparkMaxConfig.IdleMode.kBrake).follow(rightFrontNEO);

        // Convert encoder units (motor rotations) into meters so odometry is usable.
        configLeftFront.encoder.positionConversionFactor(DRIVE_ENCODER_POSITION_CONVERSION_FACTOR_METERS);
        configLeftBack.encoder.positionConversionFactor(DRIVE_ENCODER_POSITION_CONVERSION_FACTOR_METERS);
        configRightFront.encoder.positionConversionFactor(DRIVE_ENCODER_POSITION_CONVERSION_FACTOR_METERS);
        configRightBack.encoder.positionConversionFactor(DRIVE_ENCODER_POSITION_CONVERSION_FACTOR_METERS);

        configLeftFront.encoder.velocityConversionFactor(DRIVE_ENCODER_POSITION_CONVERSION_FACTOR_METERS / 60);
        configLeftBack.encoder.velocityConversionFactor(DRIVE_ENCODER_POSITION_CONVERSION_FACTOR_METERS / 60);
        configRightFront.encoder.velocityConversionFactor(DRIVE_ENCODER_POSITION_CONVERSION_FACTOR_METERS / 60);
        configRightBack.encoder.velocityConversionFactor(DRIVE_ENCODER_POSITION_CONVERSION_FACTOR_METERS / 60);

        // Closed-loop gains are not used for outreach driving, but they are harmless
        // and keep the motors consistent with how the robot was tuned.
        configLeftFront.closedLoop.p(0.08).i(0).d(0).outputRange(-1, 1);
        configLeftBack.closedLoop.p(0.08).i(0).d(0).outputRange(-1, 1);
        configRightFront.closedLoop.p(0.08).i(0).d(0).outputRange(-1, 1);
        configRightBack.closedLoop.p(0.08).i(0).d(0).outputRange(-1, 1);

        leftFrontNEO.configure(configLeftFront, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        leftBackNEO.configure(configLeftBack, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        rightFrontNEO.configure(configRightFront, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        rightBackNEO.configure(configRightBack, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        drive = new DifferentialDrive(leftFrontNEO, rightFrontNEO);

        // Motor safety stops the motors if a command stops feeding the drivetrain.
        // Outreach robots spend a lot of time with no one at the controls, so leave it on.
        drive.setSafetyEnabled(true);

        poseEstimator = new DifferentialDrivePoseEstimator(
                kinematics,
                Robot.pigeon.getRotation2d(),
                getLeftDistanceMeters(),
                getRightDistanceMeters(),
                INITIAL_POSE);
    }

    @Override
    public void periodic() {
        // Odometry only: pigeon heading + average wheel distance. No vision.
        poseEstimator.update(
                Robot.pigeon.getRotation2d(),
                getLeftDistanceMeters(),
                getRightDistanceMeters());

        publisher.set(poseEstimator.getEstimatedPosition());
    }

    /**
     * Drives the robot with arcade controls.
     *
     * <p>Inputs are deadbanded, clamped, and slew limited so a child cannot command
     * a large step change in speed or turn rate.
     *
     * @param speed    forward/back, -1 to 1, positive is forward
     * @param rotation left/right, -1 to 1, positive is right
     */
    public void driveArcade(double speed, double rotation) {
        speed = MathUtil.applyDeadband(speed, Constants.Joystick.DEADBAND);
        rotation = MathUtil.applyDeadband(rotation, Constants.Joystick.DEADBAND);

        speed = MathUtil.clamp(speed, -Constants.Drive.MAX_LINEAR_OUTPUT, Constants.Drive.MAX_LINEAR_OUTPUT);
        rotation = MathUtil.clamp(rotation, -Constants.Drive.MAX_TURN_OUTPUT, Constants.Drive.MAX_TURN_OUTPUT);

        // squareInputs = false: the inputs are already shaped by the slew limiters,
        // and squaring would make small stick movements feel dead.
        drive.arcadeDrive(linearSlew.calculate(speed), turnSlew.calculate(rotation), false);
    }

    /** Clears the ramp limiters so the next command starts from zero. */
    public void resetSlewLimiters() {
        linearSlew.reset(0);
        turnSlew.reset(0);
    }

    /** Stops the drivetrain immediately and clears the ramp limiters. */
    public void stop() {
        drive.stopMotor();
        resetSlewLimiters();
    }

    /** Estimated robot pose from wheel encoders + pigeon. */
    public Pose2d getPose() {
        return poseEstimator.getEstimatedPosition();
    }

    /** Resets odometry to a known pose. */
    public void resetPose(Pose2d pose) {
        poseEstimator.resetPosition(
                Robot.pigeon.getRotation2d(),
                getLeftDistanceMeters(),
                getRightDistanceMeters(),
                pose);
    }

    /** Average of the two left wheel distances, in meters. */
    private double getLeftDistanceMeters() {
        return (leftFrontNEO.getEncoder().getPosition()
                + leftBackNEO.getEncoder().getPosition()) / 2.0;
    }

    /** Average of the two right wheel distances, in meters, negated to match the left side. */
    private double getRightDistanceMeters() {
        return -((rightFrontNEO.getEncoder().getPosition()
                + rightBackNEO.getEncoder().getPosition()) / 2.0);
    }

    /** Left wheel velocity for the dashboard, in meters per second. */
    public double getVelocityLeft() {
        return leftFrontNEO.getEncoder().getVelocity();
    }

    /** Right wheel velocity for the dashboard, in meters per second. */
    public double getVelocityRight() {
        return rightFrontNEO.getEncoder().getVelocity();
    }
}
