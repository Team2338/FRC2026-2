// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.units.measure.Velocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class Turret extends SubsystemBase {
    /** Creates a new ExampleSubsystem. */
    public SparkMax turret;
    public SparkClosedLoopController neoPID;
    public RelativeEncoder turretEncoder;
    public SparkMaxConfig turretConfig;
    double Kp =0.0005;
    double Ki =0;
    double Kd = 0;
    public Turret() {
        turret = new SparkMax(RobotMap.TURRET_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        neoPID = turret.getClosedLoopController();
        turretEncoder = turret.getEncoder();
        turretConfig = new SparkMaxConfig();

        turretConfig.closedLoop.pid(Kp, Ki, Kd);


        turretConfig.idleMode(SparkMaxConfig.IdleMode.kBrake); //or replace kBrake with kCoast
        turretConfig.inverted(true); //true or false
    }
    public void turn(double voltage) {
        turret.setVoltage(voltage);


    }}
