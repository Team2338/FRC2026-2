package team.gif.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import team.gif.robot.Constants;
import team.gif.robot.Robot;

/**
 * Default turret command: keep the selected AprilTag centered in the turret camera.
 *
 * <p>The Limelight 2+ is mounted on the turret itself, so "aim at the tag" is just
 * "drive the horizontal offset (tx) to zero". No field pose, no lookup tables.
 *
 * <p>Safety behavior:
 * <ul>
 *   <li>No target: stop and hold position.</li>
 *   <li>At a mechanical stop: refuse to push further into it.</li>
 *   <li>Always capped at a modest motor percent.</li>
 * </ul>
 */
public class AimTurret extends Command {

    private final PIDController pid = new PIDController(
            Constants.Vision.AIM_KP,
            Constants.Vision.AIM_KI,
            Constants.Vision.AIM_KD);

    public AimTurret() {
        addRequirements(Robot.turret);
        pid.setTolerance(Constants.Vision.AIM_TOLERANCE_DEG);
    }

    @Override
    public void initialize() {
        pid.reset();
    }

    @Override
    public void execute() {
        // Do not move the turret while the robot is disabled (defaults still run then).
        if (DriverStation.isDisabled()) {
            Robot.turret.PerTurn(0);
            return;
        }

        // Without a tag there is nothing to aim at. Hold still instead of hunting.
        if (!Robot.limelight.hasTargetForId(Constants.Vision.TARGET_TAG_ID)) {
            Robot.turret.PerTurn(0);
            return;
        }

        double error = Robot.limelight.getXOffset() - Constants.Vision.AIM_OFFSET_DEG;
        double output = pid.calculate(error, 0.0);

        output = MathUtil.clamp(output, -Constants.Vision.AIM_MAX_OUTPUT, Constants.Vision.AIM_MAX_OUTPUT);

        // If the turret turns away from the tag, flip AIM_DIRECTION to -1.
        output *= Constants.Vision.AIM_DIRECTION;

        // Never drive farther into a mechanical stop. The two stops are the ends of
        // the turret's travel; pushing into them stalls the motor and heats it up.
        if (Robot.turret.isAtStop() && output < 0) {
            output = 0;
        }
        if (Robot.turret.isAt0Stop() && output > 0) {
            output = 0;
        }

        Robot.turret.PerTurn(output);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        Robot.turret.PerTurn(0);
    }
}
