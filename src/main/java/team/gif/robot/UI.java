package team.gif.robot;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import team.gif.robot.commands.ZeroEncoder;

/**
 * Dashboard output for the outreach robot.
 *
 * <p>Two audiences:
 * <ul>
 *   <li>Drivers: tag lock, aim error, shooter ready.</li>
 *   <li>Mentors: turret zero and pigeon reset, kept off the controller on purpose.</li>
 * </ul>
 */
public class UI {

    public UI() {
        ShuffleboardTab tab = Shuffleboard.getTab("Outreach");

        tab.addDouble("Turret Position", Robot.turret::getPos);
        tab.addBoolean("Turret At Stop", Robot.turret::isAtStop);
        tab.addBoolean("Turret At Zero", Robot.turret::isAt0Stop);
        tab.addDouble("Pigeon Heading", Robot.pigeon::get360Heading);

        // Mentor-only setup buttons. ignoringDisable(true) lets them be used in the pit
        // with the robot disabled, which is when you actually zero mechanisms.
        SmartDashboard.putData("Zero Turret", new ZeroEncoder().ignoringDisable(true));
        SmartDashboard.putData("Reset Pigeon 0",
                new InstantCommand(() -> Robot.pigeon.resetPigeonPosition(0)).ignoringDisable(true));
    }

    /** Called every robot loop from {@link Robot#robotPeriodic()}. */
    public void update() {
        // Vision / aiming
        SmartDashboard.putString("Limelight Name", Constants.Vision.LIMELIGHT_NAME);
        SmartDashboard.putBoolean("Tag Locked",
                Robot.limelight.hasTargetForId(Constants.Vision.TARGET_TAG_ID));
        SmartDashboard.putNumber("Tag ID", Robot.limelight.getTargetID());
        SmartDashboard.putNumber("Aim Error", Robot.limelight.getXOffset());

        // Shooter
        SmartDashboard.putNumber("Shooter RPM", Robot.shooter.getRPM());
        SmartDashboard.putBoolean("Shooter Ready", Robot.shooter.isReady());

        // Drivetrain
        SmartDashboard.putNumber("HEADING", Robot.pigeon.getRotation2d().getDegrees());
        SmartDashboard.putNumber("left motor", Robot.driveTrain.getVelocityLeft());
        SmartDashboard.putNumber("right motor", Robot.driveTrain.getVelocityRight());
    }
}
