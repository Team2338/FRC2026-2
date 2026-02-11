package team.gif.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import team.gif.lib.LimelightHelpers;
import team.gif.robot.Constants;
import team.gif.robot.Robot;

public class TurretTurn extends Command {
private final PIDController alignPID;
    public TurretTurn() {
        super();
        addRequirements(Robot.turret);
        alignPID = new PIDController(Constants.TURRET_P,Constants.TURRET_I,Constants.TURRET_D);

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {

    }

    // Called every time the scheduler runs (~20ms) while the command is scheduled
    @Override
    public void execute() {
        double targetingAngularVelocity = alignPID.calculate(Robot.limelight.getXOffset(),0);
        targetingAngularVelocity *= -1.0;
        Robot.turret.turn(targetingAngularVelocity);
    }

    // Return true when the command should end, false if it should continue. Runs every ~20ms.
    @Override
    public boolean isFinished() {
            return false;
    }

    // Called when the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {

    }
}
