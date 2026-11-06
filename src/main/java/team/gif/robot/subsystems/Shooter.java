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
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.config.SparkFlexConfig;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;

/**
 * Single NEO Vortex shooter on a SPARK Flex.
 *
 * <p>This is the original single-motor shooter this robot was built with, restored
 * now that the two Kraken flywheel motors are gone. The only real difference from
 * that old version is the controller class: a NEO Vortex needs a SPARK Flex, not a
 * SPARK Max.
 *
 * <p>The old P gain is kept as the starting point. It was tuned on a NEO, so expect
 * to adjust it (or add a kV feedforward) once the Vortex is spinning on the robot.
 */
public class Shooter extends SubsystemBase {

    /** Cap the current so a jam cannot brown out the robot mid-demo. */
    private static final int SMART_CURRENT_LIMIT_AMPS = 40;

    public SparkFlex shooter;
    public SparkClosedLoopController neoPID;
    public RelativeEncoder shooterEncoder;
    public SparkFlexConfig shooterConfig;

    /** Velocity gains carried over from the original single-motor setup. */
    double Kp = 0.00125;
    double Ki = 0;
    double Kd = 0;

    public Shooter() {
        shooter = new SparkFlex(RobotMap.SHOOT_MOTOR_ID, SparkLowLevel.MotorType.kBrushless);
        neoPID = shooter.getClosedLoopController();
        shooterEncoder = shooter.getEncoder();
        shooterConfig = new SparkFlexConfig();

        shooterConfig.smartCurrentLimit(SMART_CURRENT_LIMIT_AMPS);

        shooterConfig.closedLoop
                .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
                .pid(Kp, Ki, Kd);

        shooterConfig.idleMode(SparkFlexConfig.IdleMode.kBrake);
        // Flip if the flywheel spins the wrong way after the motor swap.
        shooterConfig.inverted(true);

        shooter.configure(shooterConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    /** Closed-loop velocity setpoint, in RPM. */
    public void setRPM(double rpm) {
        neoPID.setSetpoint(rpm, SparkBase.ControlType.kVelocity);
    }

    /** Flywheel speed in RPM. The SPARK Flex reports velocity in RPM already. */
    public double getRPM() {
        return shooterEncoder.getVelocity();
    }

    public void stopMotors() {
        shooter.stopMotor();
    }

    /**
     * True when the flywheel is close enough to the fixed outreach speed to feed.
     *
     * <p>Uses the absolute value because the reported sign depends on the motor
     * inversion. This only drives the dashboard "Shooter Ready" light; the feed
     * timing in {@link team.gif.robot.commands.Shoot} is still time based.
     */
    public boolean isReady() {
        return Math.abs(getRPM()) >= Constants.SHOOTER_RPM - Constants.SHOOTER_RPM_TOLERANCE;
    }
}
