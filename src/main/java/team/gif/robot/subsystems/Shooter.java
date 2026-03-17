// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class Shooter extends SubsystemBase {
    /** Creates a new ExampleSubsystem. */
    public SparkMax shooter;
    public SparkClosedLoopController neoPID;
    public RelativeEncoder shooterEncoder;
    public SparkMaxConfig shooterConfig;
    double Kp =0.00125;
        //0.00025;
    double Ki = 0;
    double Kd = 0;
    public Shooter() {
        shooter = new SparkMax(RobotMap.SHOOT_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        neoPID = shooter.getClosedLoopController();
        shooterEncoder = shooter.getEncoder();
        shooterConfig = new SparkMaxConfig();

        shooterConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(Kp, Ki, Kd);

        shooterConfig.idleMode(SparkMaxConfig.IdleMode.kBrake); //or replace kBrake with kCoast
        shooterConfig.inverted(true); //true or false
        //turretConfig.softLimit.forwardSoftLimit(.5);
        //turretConfig.softLimit.forwardSoftLimitEnabled(true);
//        turretConfig.softLimit.reverseSoftLimit(-.5);
//        turretConfig.softLimit.reverseSoftLimitEnabled(true);

        shooter.configure(shooterConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    }


    public void turn(double voltage) {
        shooter.setVoltage(voltage);
    }

    public void setRPM(double rpm) {
        neoPID.setSetpoint(rpm, SparkBase.ControlType.kVelocity);
    }

    public double getRPM(){
        return
        shooterEncoder.getVelocity();
    }
}
