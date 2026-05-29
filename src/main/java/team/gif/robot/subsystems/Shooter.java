// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.RobotMap;

public class Shooter extends SubsystemBase {
   /** public SparkMax shooter;
    public SparkClosedLoopController neoPID;
    public RelativeEncoder shooterEncoder;
    public SparkMaxConfig shooterConfig; **/

    public TalonFX shooterMotorOne;
    public TalonFX shooterMotorTwo;
    public TalonFXConfiguration configurationOne;
    public TalonFXConfiguration configurationTwo;
    public VelocityVoltage velocityVoltage;
    double Kp =0.00125;
        //0.00025;
    double Ki = 0;
    double Kd = 0;


    public Shooter() {
        shooterMotorOne = new TalonFX(RobotMap.SHOOT_MOTOR_ID_ONE);
        shooterMotorTwo = new TalonFX(RobotMap.SHOOT_MOTOR_ID_TWO);

        configurationOne = new TalonFXConfiguration();
        configurationOne.Slot0.kP = Kp;
        configurationOne.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        configurationOne.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        configurationTwo = new TalonFXConfiguration();
        configurationTwo.Slot0.kP = Kp;
        configurationTwo.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        configurationTwo.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        velocityVoltage = new VelocityVoltage(0).withSlot(0);

/**
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
 **/

    }


    public void turn(double voltage) {
        shooterMotorOne.setVoltage(voltage);
        shooterMotorTwo.setVoltage(voltage);
    }

    public void setRPM(double rpm) {
        shooterMotorOne.setControl(velocityVoltage.withVelocity(rpm/60));
        shooterMotorTwo.setControl(velocityVoltage.withVelocity(rpm/60));
    }

    public double getRPMOne(){
        return shooterMotorOne.getVelocity().getValueAsDouble() * 60;
    }

    public double getRPMTwo(){
        return shooterMotorTwo.getVelocity().getValueAsDouble() * 60;
    }
}
