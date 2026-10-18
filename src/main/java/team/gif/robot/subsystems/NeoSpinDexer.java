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
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;

public class NeoSpinDexer extends SubsystemBase {

    public SparkMax spinner;
    public SparkClosedLoopController neoPID;
    public RelativeEncoder spinnerEncoder;
    public SparkMaxConfig spinnerConfig;
    double kP = Constants.MotorPID.SPIN_DEXER_PID_P;
        //0.00025;
    double kI = 0;
    double kD = 0;

    public NeoSpinDexer() {
        spinner = new SparkMax(RobotMap.SPIN_DEXER_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        neoPID = spinner.getClosedLoopController();
        spinnerEncoder = spinner.getEncoder();
        spinnerConfig = new SparkMaxConfig();

        spinnerConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(kP, kI, kD);

        spinnerConfig.idleMode(SparkMaxConfig.IdleMode.kBrake); //or replace kBrake with kCoast
        spinnerConfig.inverted(true); //true or false
        //turretConfig.softLimit.forwardSoftLimit(.5);
        //turretConfig.softLimit.forwardSoftLimitEnabled(true);
//        turretConfig.softLimit.reverseSoftLimit(-.5);
//        turretConfig.softLimit.reverseSoftLimitEnabled(true);

        spinner.configure(spinnerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }


    public void turn(double percent) {
        spinner.set(percent);
    }

    public void setRPM(double rpm) {
        neoPID.setSetpoint(rpm, SparkBase.ControlType.kVelocity);
    }

}
