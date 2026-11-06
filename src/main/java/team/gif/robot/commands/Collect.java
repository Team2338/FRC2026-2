package team.gif.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import team.gif.robot.Constants;
import team.gif.robot.Robot;

/**
 * Forward-only collect command, bound to the left bumper.
 *
 * <p>Runs the collector only. The indexer is a separate mechanism and is driven
 * by {@link Shoot}, not by collecting.
 *
 * <p>There is deliberately no reverse: young drivers should not be able to launch
 * fuel back out of the robot.
 */
public class Collect extends Command {

    public Collect() {
        super();
        addRequirements(Robot.collector);
    }

    @Override
    public void execute() {
        Robot.collector.turn(Constants.COLLECTOR_IN_PERCENT);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Robot.collector.turn(0);
    }
}
