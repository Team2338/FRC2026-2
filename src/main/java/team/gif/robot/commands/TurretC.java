package team.gif.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import team.gif.robot.Robot;

public class TurretC extends Command {
    Rotation2d blah;
    Rotation2d inc = Rotation2d.fromDegrees(2);

    public TurretC() {
        super();
        addRequirements(Robot.turret); // uncomment
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs (~20ms) while the command is scheduled
    @Override
    public void execute() {
       blah = Robot.pigeon.getRotation2d().plus(inc) ;
       Robot.pigeon.resetPigeonPosition(blah.getDegrees());

    }

    // Return true when the command should end, false if it should continue. Runs every ~20ms.
    @Override
    public boolean isFinished() {
        return true;
    }

    // Called when the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }
}
