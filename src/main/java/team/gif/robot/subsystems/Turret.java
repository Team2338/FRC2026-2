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
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.units.measure.Velocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.w3c.dom.ls.LSOutput;
import team.gif.lib.LimelightHelpers;
import team.gif.robot.RobotMap;

public class Turret extends SubsystemBase {
    /** Creates a new ExampleSubsystem. */
    /*
    Need to add correct PID + Feedforward loop into code
    This could be done by a SysID function
     */
    public SparkMax turret;
    public SparkClosedLoopController neoPID;
    public RelativeEncoder turretEncoder;
    public SparkMaxConfig turretConfig;
    double Kp = 2;
        //0.00025;
    double Ki = 0;
    double Kd = 0;
    public double offset = 0;
    public Turret() {
        turret = new SparkMax(RobotMap.TURRET_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        neoPID = turret.getClosedLoopController();
        turretEncoder = turret.getEncoder();
        turretConfig = new SparkMaxConfig();
        turretConfig.smartCurrentLimit(3,5,300);
        turretConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(Kp, Ki, Kd)
                .allowedClosedLoopError(.01,ClosedLoopSlot.kSlot0);
        // double check if this gets set in motor controller or is run on rio

        turretConfig.idleMode(SparkMaxConfig.IdleMode.kBrake); //or replace kBrake with kCoast
        turretConfig.inverted(true); //true or false
        // Redo soft limit testing as turret is now an odd 360 deg
        turretConfig.softLimit.forwardSoftLimit(0.5);
        turretConfig.softLimit.forwardSoftLimitEnabled(true);
        turretConfig.softLimit.reverseSoftLimit(-30.5);
        turretConfig.softLimit.reverseSoftLimitEnabled(true);

        turret.configure(turretConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);


    }
    public boolean isAtStop(){
        return turretEncoder.getPosition() <= -30.5;
    }
    public boolean isAt0Stop(){
        return turretEncoder.getPosition() >= -0.5;
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

    public void turnToPoint(double point){neoPID.setSetpoint(point, SparkBase.ControlType.kPosition, ClosedLoopSlot.kSlot0);}
    public boolean turingP(){return neoPID.isAtSetpoint();}
    public double getPos(){return turretEncoder.getPosition();}
    public void setpos(){turretEncoder.setPosition(0);}



}
