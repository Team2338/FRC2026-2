package team.gif.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import team.gif.robot.Robot;

/**
 * Default drivetrain command.
 *
 * <p>Two-stick arcade:
 * <ul>
 *   <li>Left stick Y = forward / back</li>
 *   <li>Right stick X = turn</li>
 * </ul>
 *
 * <p>Xbox controllers report left Y as negative when pushed forward, so it is
 * negated here. Speed shaping (deadband, clamps, acceleration limits) lives in
 * {@link team.gif.robot.subsystems.DriveTrain#driveArcade(double, double)}.
 */
public class ArcadeDrive extends Command {

    public ArcadeDrive() {
        super();
        addRequirements(Robot.driveTrain);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        // Default commands keep running while the robot is disabled, so refuse to
        // drive then. Otherwise a held stick could move the robot on the pit table.
        if (DriverStation.isDisabled()) {
            Robot.driveTrain.stop();
            return;
        }

        // Negated: pushing the left stick forward returns a negative value.
        double speed = -Robot.oi.driver.getLeftY();
        // Right stick: positive is right.
        double rotation = Robot.oi.driver.getRightX();

        Robot.driveTrain.driveArcade(speed, rotation);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        // Always stop when this command releases the drivetrain, so a disabled or
        // interrupted drive command cannot leave the robot rolling.
        Robot.driveTrain.stop();
    }
}
