// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.subsystems;

//import com.pathplanner.lib.auto.AutoBuilder;
//import com.pathplanner.lib.config.RobotConfig;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;
import com.revrobotics.spark.SparkLowLevel;

import static team.gif.robot.RobotMap.LEFT_FRONT_NEO;

public class DriveTrain extends SubsystemBase {
    private SparkMax leftFrontNEO;
    private SparkMaxConfig configLeftFront;
    private SparkMax leftBackNEO;
    private SparkMaxConfig configLeftBack;
    private SparkMax rightFrontNEO;
    private SparkMaxConfig configRightFront;
    private SparkMax rightBackNEO;
    private SparkMaxConfig configRightBack;
    private DifferentialDrive drive;

    public DriveTrain() {
        leftFrontNEO = new SparkMax(LEFT_FRONT_NEO, SparkLowLevel.MotorType.kBrushless);
        leftBackNEO = new SparkMax(RobotMap.LEFT_BACK_NEO, SparkLowLevel.MotorType.kBrushless);
        rightFrontNEO = new SparkMax(RobotMap.RIGHT_FRONT_NEO, SparkLowLevel.MotorType.kBrushless);
        rightBackNEO = new SparkMax(RobotMap.RIGHT_BACK_NEO, SparkLowLevel.MotorType.kBrushless);

        configLeftFront = new SparkMaxConfig();
        configLeftBack = new SparkMaxConfig();
        configRightFront = new SparkMaxConfig();
        configRightBack = new SparkMaxConfig();

        configLeftFront.idleMode(SparkMaxConfig.IdleMode.kBrake);
        configLeftBack.idleMode(SparkMaxConfig.IdleMode.kBrake).follow(LEFT_FRONT_NEO);
        configRightFront.idleMode(SparkMaxConfig.IdleMode.kBrake);
        configRightBack.idleMode(SparkMaxConfig.IdleMode.kBrake).follow(RobotMap.RIGHT_FRONT_NEO);

        leftFrontNEO.configure(configLeftFront, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        leftBackNEO.configure(configLeftBack, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        rightFrontNEO.configure(configRightFront, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        rightBackNEO.configure(configRightBack, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);


        configLeftBack.follow(RobotMap.LEFT_FRONT_NEO);
        configRightBack.follow(RobotMap.RIGHT_FRONT_NEO);

        drive = new DifferentialDrive(leftFrontNEO, rightFrontNEO);


    }
    public void driveTank(double leftSpeed, double rightSpeed){drive.tankDrive(leftSpeed, rightSpeed);}
    public void driveArcade(double speed, double rotation){drive.arcadeDrive(speed, rotation);}

     {

    }
}

