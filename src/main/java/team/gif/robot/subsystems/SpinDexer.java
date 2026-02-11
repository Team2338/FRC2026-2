// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class SpinDexer extends SubsystemBase {
    /** Creates a new ExampleSubsystem. */
    public TalonSRX spinDexer;
    public SpinDexer() {
        spinDexer = new TalonSRX(RobotMap.SPIN_DEXER_MOTOR_ID);
        spinDexer.configFactoryDefault();
        spinDexer.setNeutralMode(NeutralMode.Brake);
    }
    public void spin(double percent) {
        spinDexer.set(TalonSRXControlMode.PercentOutput, percent);


    }}
