package team.gif.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import team.gif.robot.Constants;
import team.gif.robot.Robot;

/**
 * Shoot command, bound to the right bumper.
 *
 * <p>Timing is fixed and simple:
 * <ol>
 *   <li>Shooter and indexer start immediately.</li>
 *   <li>After {@link Constants#SHOOT_FEED_DELAY_SECONDS}, the spindexer starts
 *       feeding fuel into the shooter.</li>
 *   <li>Releasing the button stops everything.</li>
 * </ol>
 *
 * <p>The delay is intentional rather than RPM-based: it gives the flywheel a
 * consistent head start, and it behaves the same every time for demos.
 */
public class Shoot extends Command {

    /** Counts time since the right bumper was pressed. */
    private final Timer spinUpTimer = new Timer();

    public Shoot() {
        super();
        // These are all the mechanisms this command owns while the button is held.
        addRequirements(Robot.shooter, Robot.indexerWheels, Robot.neoSpinDexer);
    }

    @Override
    public void initialize() {
        spinUpTimer.reset();
        spinUpTimer.start();
    }

    @Override
    public void execute() {
        // Shooter and indexer run for the whole press.
        Robot.shooter.setRPM(Constants.SHOOTER_RPM);
        Robot.indexerWheels.turn(Constants.INDEX_WHEELS_SPEED_PER);

        // Spindexer only joins in after the fixed head-start delay.
        if (spinUpTimer.hasElapsed(Constants.SHOOT_FEED_DELAY_SECONDS)) {
            Robot.neoSpinDexer.setRPM(Constants.SPINDEXER_RPM);
        } else {
            Robot.neoSpinDexer.turn(0);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        spinUpTimer.stop();
        Robot.shooter.stopMotors();
        Robot.indexerWheels.turn(0);
        Robot.neoSpinDexer.turn(0);
    }
}
