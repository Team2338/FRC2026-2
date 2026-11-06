// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import team.gif.robot.commands.AimTurret;
import team.gif.robot.commands.ArcadeDrive;
import team.gif.robot.subsystems.Collector;
import team.gif.robot.subsystems.CollectorPivot;
import team.gif.robot.subsystems.DriveTrain;
import team.gif.robot.subsystems.IndexerWheels;
import team.gif.robot.subsystems.NeoSpinDexer;
import team.gif.robot.subsystems.Shooter;
import team.gif.robot.subsystems.Turret;
import team.gif.robot.subsystems.drivers.Limelight;
import team.gif.robot.subsystems.drivers.Pigeon2_0;

/**
 * Outreach version of the 2026 robot.
 *
 * <p>What changed from the competition robot:
 * <ul>
 *   <li>One Limelight 2+ on the turret; the old side camera is gone.</li>
 *   <li>Turret auto-aims at a single AprilTag; no field-heading math.</li>
 *   <li>One Xbox controller, slower drivetrain, no autonomous modes.</li>
 *   <li>Collector pivot and reverse collector are no longer used by anything.</li>
 * </ul>
 */
public class Robot extends TimedRobot {

    private static Command autonomousCommand;

    public static OI oi;
    public static UI ui;

    public static CollectorPivot collectorPivot;
    public static Collector collector;
    public static IndexerWheels indexerWheels;
    public static NeoSpinDexer neoSpinDexer;
    public static Shooter shooter;
    public static Pigeon2_0 pigeon;
    public static Turret turret;
    public static DriveTrain driveTrain;
    public static Limelight limelight;

    private final CommandScheduler commandScheduler = CommandScheduler.getInstance();
    private RobotContainer robotContainer;

    public Robot() {
        // The camera is now on the turret, so there is exactly one Limelight object.
        limelight = new Limelight(Constants.Vision.LIMELIGHT_NAME);
        limelight.setupAprilTag(Constants.Vision.PIPELINE, Constants.Vision.TARGET_TAG_ID);

        pigeon = new Pigeon2_0(RobotMap.PIGEON_ID);
        turret = new Turret();

        // Kept instantiated even though nothing commands it: the pivot motor is still
        // wired, and leaving the object alive is safer than deleting hardware support.
        collectorPivot = new CollectorPivot();

        neoSpinDexer = new NeoSpinDexer();
        shooter = new Shooter();
        indexerWheels = new IndexerWheels();
        driveTrain = new DriveTrain();
        collector = new Collector();

        // Default commands run whenever nothing else owns the subsystem.
        Robot.driveTrain.setDefaultCommand(new ArcadeDrive());
        Robot.turret.setDefaultCommand(new AimTurret());

        // These should be at or near the bottom: they read the subsystems above.
        robotContainer = new RobotContainer();
        oi = new OI();
        ui = new UI();
    }

    @Override
    public void robotPeriodic() {
        // Runs scheduled commands and every subsystem's periodic().
        commandScheduler.run();
        ui.update();
    }

    @Override
    public void disabledInit() {
        // Never leave a mechanism running while the robot is disabled. A demo robot
        // sits disabled a lot, and a spinning shooter on the field is a hazard.
        driveTrain.stop();
        shooter.stopMotors();
        collector.turn(0);
        indexerWheels.turn(0);
        neoSpinDexer.turn(0);
        turret.PerTurn(0);
    }

    @Override
    public void disabledPeriodic() {}

    @Override
    public void autonomousInit() {
        // There are no autonomous modes. The container returns Commands.none().
        autonomousCommand = robotContainer.getAutonomousCommand();
        if (autonomousCommand != null) {
            commandScheduler.schedule(autonomousCommand);
        }
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void teleopInit() {
        // Cancel anything left over from autonomous, then clear the ramp limiters so
        // teleop starts from a clean, slow state instead of a stale fast one.
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
        driveTrain.resetSlewLimiters();
    }

    @Override
    public void teleopPeriodic() {}

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {}

    @Override
    public void simulationInit() {}

    @Override
    public void simulationPeriodic() {}
}
