// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.subsystems;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPLTVController;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
// OLD ODOMETRY IMPORT - intentionally no longer used.
// import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
// import edu.wpi.first.math.kinematics.DifferentialDriveOdometry3d;

import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import team.gif.robot.Constants;
import team.gif.robot.LimelightHelpers;
import team.gif.robot.Robot;
import team.gif.robot.RobotContainer;
import team.gif.robot.RobotMap;

public class DriveTrain extends SubsystemBase {
    public boolean shooting = false;
    public boolean scoreing = true; // determines if turret shoots at hub or pass zone

    /*
     * FIXME:
     * This MUST be the real effective drivetrain track width in meters.
     *
     * Your old code used 0.127 meters, which is about 5 inches.
     * That is almost certainly too small for an FRC drivetrain.
     *
     * Do not trust turning pose accuracy until this is measured/tuned.
     */
    private static final double TRACK_WIDTH_METERS = 0.597;

    /*
     * Your old conversion factor:
     *
     *     (1 / 10.71) * .47879
     *
     * Because 10.71 and .47879 are doubles, Java will not do integer division here,
     * but writing 1.0 makes the intent clearer.
     *
     * This should convert SparkMax encoder motor rotations into meters traveled.
     */
    private static final double DRIVE_ENCODER_POSITION_CONVERSION_FACTOR_METERS =
            (1.0 / 10.71) * 0.47879;

    /*
     * Use the same initial pose everywhere.
     *
     * This was the old odometry starting pose:
     *
     *     new Pose2d(16.541 - 3.56, 8.07 - 4.09, new Rotation2d())
     *
     * Keep it here unless your actual autonomous starting pose is different.
     */
    private static final Pose2d INITIAL_POSE =
            new Pose2d(16.541 - 3.56, 8.07 - 4.09, new Rotation2d());

    /*
     * FIXME:
     * Move this into Constants.Vision once the Limelight name is finalized.
     *
     * Your uploaded code used "limelight-side".
     * Earlier code references suggested there may also be "limelight-front" or
     * "limelight-turret" floating around. That mismatch needs to be cleaned up.
     */
    private static final String LIMELIGHT_NAME = "limelight-side";

    /*
     * Vision standard deviations.
     *
     * X/Y are trusted moderately.
     * Heading is basically ignored because MegaTag2 should use the gyro heading,
     * not overwrite the robot's yaw estimate from AprilTags.
     */
    private static final double VISION_X_STD_DEV_METERS = 0.7;
    private static final double VISION_Y_STD_DEV_METERS = 0.7;
    private static final double VISION_THETA_STD_DEV_RADIANS = 9999999.0;

    /*
     * Reject vision poses that are wildly far from the current estimated pose.
     *
     * If your robot starts with a bad initial pose, this may reject useful vision
     * corrections. During initial testing, you can raise this value or temporarily
     * disable this check.
     */
    private static final double MAX_VISION_POSE_JUMP_METERS = 5;

    private final DifferentialDriveKinematics m_kinematics =
            new DifferentialDriveKinematics(TRACK_WIDTH_METERS);

    private SparkMax leftFrontNEO;
    private SparkMaxConfig configLeftFront;

    private SparkMax leftBackNEO;
    private SparkMaxConfig configLeftBack;

    private SparkMax rightFrontNEO;
    private SparkMaxConfig configRightFront;

    private SparkMax rightBackNEO;
    private SparkMaxConfig configRightBack;

    private DifferentialDrive drive;

    public Pose2d m_pose = INITIAL_POSE;

    private boolean RedAllience;
    private Translation2d hub;

    private final DifferentialDrivePoseEstimator m_poseEstimator;
    private Translation2d pos;
    private Translation2d depoShot;
    private Translation2d outpostShot;

    Rotation2d Rots;
    public Rotation2d gyroAngle = Robot.pigeon.getRotation2d();

    /*
     * OLD ODOMETRY FIELD - intentionally commented out.
     *
     * The pose estimator replaces DifferentialDriveOdometry. It still uses gyro
     * and encoder odometry internally, but also allows Limelight vision fusion.
     */
    // private DifferentialDriveOdometry m_odometry;

    private final StructPublisher<Pose2d> publisher = NetworkTableInstance.getDefault()
            .getStructTopic("ROBOT POSITION", Pose2d.struct)
            .publish();

    public DriveTrain() {

        leftFrontNEO = new SparkMax(RobotMap.LEFT_FRONT_NEO, SparkLowLevel.MotorType.kBrushless);
        leftBackNEO = new SparkMax(RobotMap.LEFT_BACK_NEO, SparkLowLevel.MotorType.kBrushless);
        rightFrontNEO = new SparkMax(RobotMap.RIGHT_FRONT_NEO, SparkLowLevel.MotorType.kBrushless);
        rightBackNEO = new SparkMax(RobotMap.RIGHT_BACK_NEO, SparkLowLevel.MotorType.kBrushless);

        configLeftFront = new SparkMaxConfig();
        configLeftBack = new SparkMaxConfig();
        configRightFront = new SparkMaxConfig();
        configRightBack = new SparkMaxConfig();

        configLeftFront.idleMode(SparkMaxConfig.IdleMode.kBrake);
        configLeftBack.idleMode(SparkMaxConfig.IdleMode.kBrake).follow(RobotMap.LEFT_FRONT_NEO);
        configRightFront.idleMode(SparkMaxConfig.IdleMode.kBrake);
        configRightBack.idleMode(SparkMaxConfig.IdleMode.kBrake).follow(RobotMap.RIGHT_FRONT_NEO);

        configLeftFront.encoder.positionConversionFactor(DRIVE_ENCODER_POSITION_CONVERSION_FACTOR_METERS);
        configLeftBack.encoder.positionConversionFactor(DRIVE_ENCODER_POSITION_CONVERSION_FACTOR_METERS);
        configRightFront.encoder.positionConversionFactor(DRIVE_ENCODER_POSITION_CONVERSION_FACTOR_METERS);
        configRightBack.encoder.positionConversionFactor(DRIVE_ENCODER_POSITION_CONVERSION_FACTOR_METERS);

        leftFrontNEO.configure(
                configLeftFront,
                ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters
        );

        leftBackNEO.configure(
                configLeftBack,
                ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters
        );

        rightFrontNEO.configure(
                configRightFront,
                ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters
        );

        rightBackNEO.configure(
                configRightBack,
                ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters
        );

        drive = new DifferentialDrive(leftFrontNEO, rightFrontNEO);

        m_poseEstimator = new DifferentialDrivePoseEstimator(
                m_kinematics,
                Robot.pigeon.getRotation2d(),
                getLeftDistanceMeters(),
                getRightDistanceMeters(),
                INITIAL_POSE
        );

        /*
         * OLD ODOMETRY CONSTRUCTOR - intentionally commented out.
         *
         * m_odometry = new DifferentialDriveOdometry(
         *         Robot.pigeon.getRotation2d(),
         *         getLeftDistanceMeters(),
         *         getRightDistanceMeters(),
         *         INITIAL_POSE
         * );
         */
    }

    @Override
    public void periodic() {
        gyroAngle = Robot.pigeon.getRotation2d();
        RedAllience = SmartDashboard.getBoolean("RED allience", true);
        if(RedAllience){
            hub = Constants.Field.HUB_RED_TRANSLATION;
            depoShot = Constants.Field.PASS_RED_DEOPO;
            outpostShot = Constants.Field.PASS_RED_OUTPOST;
        }
        if(!RedAllience){
            hub = Constants.Field.HUB_BLUE_TRANSLATION;
            outpostShot = Constants.Field.PASS_BLUE_DEOPO;
            depoShot = Constants.Field.PASS_BLUE_OUTPOST;
        }
        if(scoreing){
            pos = hub;
        }
        if(!scoreing){
         if(m_pose.getY() <= Units.inchesToMeters(158.84)){
             pos = outpostShot;
         }
         else{
             pos = depoShot;
         }
        }

        /*
         * Correct pose-estimator update.
         *
         * This replaces:
         *
         *     m_pose = m_odometry.update(...)
         *
         * The estimator must be updated every robot loop using gyro + cumulative
         * left/right wheel distances.
         */
        m_pose = m_poseEstimator.update(
                gyroAngle,
                getLeftDistanceMeters(),
                getRightDistanceMeters()
        );

        // this as its named will add a vision measurement if the limelight can see a tag, but will do nothing if it can't see a tag
        addLimelightVisionMeasurement(gyroAngle);

        // Update the field-relative pose to be published to NetworkTables.
        m_pose = m_poseEstimator.getEstimatedPosition();

        // Publish the pose to NetworkTables for use by other programs (e.g. AdvantageScope).
        publisher.set(m_pose);
    }

    private void addLimelightVisionMeasurement(Rotation2d gyroAngle) {
        //System.out.println("command is running");
        /*
         * MegaTag2 needs the robot's current heading.
         *
         * Limelight's MegaTag2 docs show passing robot yaw through
         * SetRobotOrientation before reading the MegaTag2 pose.
         *
         */
        LimelightHelpers.SetRobotOrientation(
                LIMELIGHT_NAME,
                gyroAngle.getDegrees(),
                0.0, // yawRate this is optional and only used if you have a gyro that can provide yaw rate. If you don't have a gyro that provides yaw rate, just pass 0.0 here.
                0.0, // pitch -> Usually not needed for flat FRC drivetrain localization.
                0.0, // pitchRate -> Usually not needed for flat FRC drivetrain localization.q
                0.0, // roll -> Usually not needed for flat FRC drivetrain localization.
                0.0  // rollRate -> Usually not needed for flat FRC drivetrain localization.
        );

        LimelightHelpers.PoseEstimate mt2 = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(LIMELIGHT_NAME);

        SmartDashboard.putNumber("MT2 tag count", mt2.tagCount);

        if (mt2 == null) {
            return;
        }

        if (mt2.tagCount <= 0) {
           return;
        }

        if (mt2.pose == null) {
            System.out.println("null");
            return;
        }

        /*
         * Optional sanity gate.
         *
         * If the vision pose is very far away from the current estimated pose,
         * ignore it. This prevents one bad tag solve from teleporting the robot.
         */
        double poseDifferenceMeters = mt2.pose.getTranslation()
                .getDistance(m_poseEstimator.getEstimatedPosition().getTranslation());

        /*
         * If the pose difference is greater than the threshold, do not add the
         * vision measurement.
         *
         * During initial testing, you can raise this threshold or disable this
         * check to allow vision measurements to be added even if they are far from
         * the current estimate.
         */
        if (poseDifferenceMeters > MAX_VISION_POSE_JUMP_METERS) {
            SmartDashboard.putNumber("posedistance", poseDifferenceMeters);
            //System.out.println("tooooo longggggg" + poseDifferenceMeters);
            return;
        }

        m_poseEstimator.setVisionMeasurementStdDevs(
                VecBuilder.fill(
                        VISION_X_STD_DEV_METERS,
                        VISION_Y_STD_DEV_METERS,
                        VISION_THETA_STD_DEV_RADIANS
                )
        );

        m_poseEstimator.addVisionMeasurement(
                mt2.pose,
                mt2.timestampSeconds
        );
        //System.out.println("fixing pos");


    }
    private boolean checkRedAlliance() {
        //This state should never happen unless we are not connected.
        //It is set to red because that is what we are set up for in the shop.
        if(DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == DriverStation.Alliance.Red) {
            return true;
        } else return DriverStation.getAlliance().isEmpty() || DriverStation.getAlliance().get() != DriverStation.Alliance.Blue;

    }
    public ChassisSpeeds getRobotRelitiveSpeed(){
        DifferentialDriveWheelSpeeds wheelSpeeds = new DifferentialDriveWheelSpeeds(
                leftFrontNEO.getEncoder().getVelocity(),
                rightFrontNEO.getEncoder().getVelocity()
        );
        return m_kinematics.toChassisSpeeds(wheelSpeeds);
    }
    public void driveRobotRelative(ChassisSpeeds speeds){
        DifferentialDriveWheelSpeeds wheelSpeeds = m_kinematics.toWheelSpeeds(speeds);
        leftFrontNEO.getClosedLoopController().setSetpoint(wheelSpeeds.leftMetersPerSecond, SparkBase.ControlType.kVelocity);
        rightFrontNEO.getClosedLoopController().setSetpoint(wheelSpeeds.rightMetersPerSecond, SparkBase.ControlType.kVelocity);
    }
    public Pose2d getPose() {
        return m_poseEstimator.getEstimatedPosition();
    }

    public void resetPose(Pose2d pose) {
        m_poseEstimator.resetPosition(
                Robot.pigeon.getRotation2d(),
                getLeftDistanceMeters(),
                getRightDistanceMeters(),
                pose
        );

        m_pose = pose;
    }
    private void configPathPlanner(){
        RobotConfig ppConfig;
        try{
            ppConfig = RobotConfig.fromGUISettings();
        }catch (Exception e){
            ModuleConfig moduleConfig = new ModuleConfig(.0762,5,1, DCMotor.getNEO(2), 50, 1);
            ppConfig = new RobotConfig(15, 0,moduleConfig , TRACK_WIDTH_METERS);

        }

        AutoBuilder.configure(
                this::getPose,
                this::resetPose,
                this::getRobotRelitiveSpeed,
                this::driveRobotRelative,
                new PPLTVController(0.02),
                ppConfig,
                this::checkRedAlliance,
                this
        );
    }


    private double getLeftDistanceMeters() {
        return (leftFrontNEO.getEncoder().getPosition()
                + leftBackNEO.getEncoder().getPosition()) / 2.0;
    }

    private double getRightDistanceMeters() {
        /*
         * Your old code negated the right side.
         *
         * Keep this only if right encoder position goes negative when the robot
         * drives forward.
         *
         * Test:
         *   1. Put robot on blocks or push it forward by hand.
         *   2. Print left and right distances.
         *   3. Both values must increase when the robot moves forward.
         *
         * If right distance already increases when driving forward, remove the
         * negative sign below.
         */
        return -((rightFrontNEO.getEncoder().getPosition()
                + rightBackNEO.getEncoder().getPosition()) / 2.0);
    }




    public double distance() {
        double xdistance = pos.getX() - Math.abs(m_pose.getX());
        double ydistance = pos.getY() - Math.abs(m_pose.getY());

        return Math.hypot(xdistance, ydistance);
    }
    public Rotation2d getAngleHub(){
        Translation2d robotTrans = m_pose.getTranslation();
        Translation2d hubTrans = pos;

        Translation2d RobotToHub = hubTrans.minus(robotTrans);
        return RobotToHub.getAngle();
    }
    public Rotation2d headingErrorHub(){
        Rotation2d angleToHub = getAngleHub();
        Rotation2d RobotHeading = gyroAngle;

        double errorRadins = angleToHub.minus(RobotHeading).getRadians();

        return Rotation2d.fromRadians(MathUtil.angleModulus(errorRadins));
    }
    public double turnAmount(){
        double angle = headingErrorHub().getDegrees();
        return ((angle)*0.08333333333333);
//        return angle;
    }



    public void driveTank(double leftSpeed, double rightSpeed) {
        drive.tankDrive(leftSpeed, rightSpeed);
    }

    public void driveArcade(double speed, double rotation) {
        if(shooting){
        drive.arcadeDrive(speed*.5, rotation*.5);
    }
        else{drive.arcadeDrive(speed, rotation);}
    }

    public double getVelocityLeft() {
        return leftFrontNEO.getEncoder().getVelocity();
    }

    public double getVelocityRight() {
        return rightFrontNEO.getEncoder().getVelocity();
    }
}