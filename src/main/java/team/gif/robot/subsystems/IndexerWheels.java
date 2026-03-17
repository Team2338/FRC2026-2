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

public class IndexerWheels extends SubsystemBase {
    /** Creates a new ExampleSubsystem. */
    public SparkMax indexer;
    public SparkClosedLoopController neoPID;
    public RelativeEncoder indexerEncoder;
    public SparkMaxConfig indexerConfig;
    double Kp =0.00025;
        //0.00025;
    double Ki = 0;
    double Kd = 0;
    public IndexerWheels() {
        indexer = new SparkMax(RobotMap.INDEX_MOTER_ID, SparkLowLevel.MotorType.kBrushless);
        neoPID = indexer.getClosedLoopController();
        indexerEncoder = indexer.getEncoder();
        indexerConfig = new SparkMaxConfig();

        indexerConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(Kp, Ki, Kd);

        indexerConfig.idleMode(SparkMaxConfig.IdleMode.kBrake); //or replace kBrake with kCoast
        indexerConfig.inverted(true); //true or false
        //turretConfig.softLimit.forwardSoftLimit(.5);
        //turretConfig.softLimit.forwardSoftLimitEnabled(true);
//        turretConfig.softLimit.reverseSoftLimit(-.5);
//        turretConfig.softLimit.reverseSoftLimitEnabled(true);

        indexer.configure(indexerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    }


    public void turn(double percent) {
        indexer.set(percent);
    }

    public void setRPM(double rpm) {
        neoPID.setSetpoint(rpm, SparkBase.ControlType.kVelocity);
    }

}
