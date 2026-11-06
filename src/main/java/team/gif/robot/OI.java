package team.gif.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import team.gif.robot.commands.Collect;
import team.gif.robot.commands.Shoot;

/**
 * Operator interface: one Xbox controller does everything.
 *
 * <p>Binding table for the outreach robot:
 * <pre>
 *   Left stick Y    forward / back
 *   Right stick X   turn
 *   Left bumper     collect (collector only)
 *   Right bumper    shoot (shooter + indexer, spindexer after a 1 s delay)
 * </pre>
 *
 * <p>A second controller can mirror the two bumpers when {@link #USE_AUX_CONTROLLER}
 * is turned on. It is off by default.
 *
 * <p>Turret aiming is automatic and has no button. Collector pivot and reverse
 * collector no longer have bindings at all.
 */
public class OI {

    /** The only controller. Driver station port comes from {@link RobotMap}. */
    public final CommandXboxController driver =
            new CommandXboxController(RobotMap.DRIVER_CONTROLLER_ID);

    /**
     * Optional second controller for a two-operator setup.
     *
     * <p>Off by default: the outreach robot is driven by one controller. Set this to
     * true if a second person should also be able to collect and shoot; the aux
     * bumpers do the same thing as the driver's.
     */
    public static final boolean USE_AUX_CONTROLLER = false;

    /** Only used when {@link #USE_AUX_CONTROLLER} is true. */
    public final CommandXboxController aux =
            new CommandXboxController(RobotMap.AUX_CONTROLLER_ID);

    public final Trigger dLBump = driver.leftBumper();
    public final Trigger dRBump = driver.rightBumper();

    public OI() {
        // No need to flood the console when the single controller is unplugged.
        DriverStation.silenceJoystickConnectionWarning(true);

        // Collect is the left bumper, matching how the robot has always been driven.
        // It runs the collector by itself; the indexer belongs to the shoot sequence.
        dLBump.and(DriverStation::isEnabled).whileTrue(new Collect());

        // Shoot is the right bumper: shooter + indexer start together, then the
        // spindexer joins in after the spin-up delay (see Shoot).
        dRBump.and(DriverStation::isEnabled).whileTrue(new Shoot());

        // Optional second operator. Disabled unless USE_AUX_CONTROLLER is flipped.
        if (USE_AUX_CONTROLLER) {
            aux.leftBumper().and(DriverStation::isEnabled).whileTrue(new Collect());
            aux.rightBumper().and(DriverStation::isEnabled).whileTrue(new Shoot());
        }
    }
}
