// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.units.measure.Velocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class Turret extends SubsystemBase {
    /** Creates a new ExampleSubsystem. */
    public TalonSRX turret;
    public Turret() {
        turret = new TalonSRX(RobotMap.TURRET_MOTOR_ID);
        turret.configFactoryDefault();
        turret.setNeutralMode(NeutralMode.Brake);
    }
    public void turn(double Velocity) {
        turret.set(TalonSRXControlMode.Velocity, Velocity);


    }}
