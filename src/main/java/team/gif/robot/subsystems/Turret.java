// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.*;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.units.measure.Velocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.w3c.dom.ls.LSOutput;
import team.gif.robot.RobotMap;

public class Turret extends SubsystemBase {
    /** Creates a new ExampleSubsystem. */
    public SparkMax turret;
    public SparkClosedLoopController neoPID;
    public RelativeEncoder turretEncoder;
    public SparkMaxConfig turretConfig;
    double Kp =0;
        //0.00025;
    double Ki = 0;
    double Kd = 0;
    public Turret() {
        turret = new SparkMax(RobotMap.TURRET_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        neoPID = turret.getClosedLoopController();
        turretEncoder = turret.getEncoder();
        turretConfig = new SparkMaxConfig();

        turretConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(Kp, Ki, Kd);

        turretConfig.idleMode(SparkMaxConfig.IdleMode.kBrake); //or replace kBrake with kCoast
        turretConfig.inverted(true); //true or false
        //turretConfig.softLimit.forwardSoftLimit(.5);
        //turretConfig.softLimit.forwardSoftLimitEnabled(true);
//        turretConfig.softLimit.reverseSoftLimit(-.5);
//        turretConfig.softLimit.reverseSoftLimitEnabled(true);

        turret.configure(turretConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    }


    public void turn(double voltage) {
        turret.setVoltage(voltage);
    }
    public void PerTurn(double percent){
        turret.set(percent);
    }

    public void setRPM(double rpm) {
        neoPID.setSetpoint(rpm, SparkBase.ControlType.kVelocity);
    }

}
