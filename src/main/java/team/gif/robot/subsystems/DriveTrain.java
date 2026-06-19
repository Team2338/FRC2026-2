// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.subsystems;

//import com.pathplanner.lib.auto.AutoBuilder;
//import com.pathplanner.lib.config.RobotConfig;
import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry3d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.Constants;
import team.gif.robot.LimelightHelpers;
import team.gif.robot.Robot;
import team.gif.robot.RobotMap;
import com.revrobotics.spark.SparkLowLevel;


public class DriveTrain extends SubsystemBase {
    private final DifferentialDriveKinematics m_kinematics =
            new DifferentialDriveKinematics(.127);
    private SparkMax leftFrontNEO;
    private SparkMaxConfig configLeftFront;
    private SparkMax leftBackNEO;
    private SparkMaxConfig configLeftBack;
    private SparkMax rightFrontNEO;
    private SparkMaxConfig configRightFront;
    private SparkMax rightBackNEO;
    private SparkMaxConfig configRightBack;
    private DifferentialDrive drive;
    public Pose2d m_pose;
    private DifferentialDrivePoseEstimator m_poseEstimator;


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
        configLeftFront.encoder.positionConversionFactor((1/10.71)*.47879);
        configLeftBack.encoder.positionConversionFactor((1/10.71)*.47879);
        configRightBack.encoder.positionConversionFactor((1/10.71)*.47879);
        configRightFront.encoder.positionConversionFactor((1/10.71)*.47879);



        leftFrontNEO.configure(configLeftFront, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        leftBackNEO.configure(configLeftBack, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        rightFrontNEO.configure(configRightFront, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        rightBackNEO.configure(configRightBack, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);


        configLeftBack.follow(RobotMap.LEFT_FRONT_NEO);
        configRightBack.follow(RobotMap.RIGHT_FRONT_NEO);

        drive = new DifferentialDrive(leftFrontNEO, rightFrontNEO);


        m_poseEstimator =
                new DifferentialDrivePoseEstimator(m_kinematics,Robot.pigeon.getRotation2d(),rightFrontNEO.getEncoder().getPosition(),leftFrontNEO.getEncoder().getPosition(),new Pose2d());

        m_odometry = new DifferentialDriveOdometry(
                Robot.pigeon.getRotation2d(),
                (leftFrontNEO.getEncoder().getPosition() + leftBackNEO.getEncoder().getPosition())/2, -(rightFrontNEO.getEncoder().getPosition()+ rightBackNEO.getEncoder().getPosition())/2,
                new Pose2d(16.541-3.56,8.07-4.09,new Rotation2d())

        );

    }
    DifferentialDriveOdometry m_odometry;
    StructPublisher<Pose2d> publisher = NetworkTableInstance.getDefault()
            .getStructTopic("ROBOT POSITION", Pose2d.struct).publish();


    public void periodic(){
        LimelightHelpers.SetRobotOrientation("limelight-side",Robot.pigeon.get360Heading(),0,0,0,0,0);
        LimelightHelpers.PoseEstimate mt2 = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("limelight-side");
        var gyroAngle = Robot.pigeon.getRotation2d();
        m_pose = m_odometry.update(gyroAngle,-(rightFrontNEO.getEncoder().getPosition()+ rightBackNEO.getEncoder().getPosition())/2,(leftFrontNEO.getEncoder().getPosition() + leftBackNEO.getEncoder().getPosition())/2);
        if(mt2.tagCount > 0){
            m_poseEstimator.setVisionMeasurementStdDevs(VecBuilder.fill(.7,.7,9999999));
            m_poseEstimator.addVisionMeasurement(
                    mt2.pose,
                    mt2.timestampSeconds

            );

            leftFrontNEO.getEncoder().setPosition(0);
            rightFrontNEO.getEncoder().setPosition(0);
            m_pose=m_poseEstimator.getEstimatedPosition();
            m_odometry.resetPosition(gyroAngle,leftFrontNEO.getEncoder().getPosition(),-rightFrontNEO.getEncoder().getPosition(),m_poseEstimator.getEstimatedPosition());
         ;


            }
        publisher.set(m_pose);


    }
    public double distance(){
        double xdistance =  Constants.Field.HUB_RED_TRANSLATION.getX() - Math.abs(m_pose.getX());
        double ydistance = Constants.Field.HUB_RED_TRANSLATION.getY() - Math.abs(m_pose.getY());
        return Math.hypot(xdistance,ydistance);
    }
    public void driveTank(double leftSpeed, double rightSpeed){drive.tankDrive(leftSpeed, rightSpeed);}
    public void driveArcade(double speed, double rotation){drive.arcadeDrive(speed, rotation);}
    public double getVelocityLeft(){
        return leftFrontNEO.getEncoder().getVelocity();
    }
    public double getVelocityRight(){
        return rightFrontNEO.getEncoder().getVelocity();
    }



}

