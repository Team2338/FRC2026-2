package team.gif.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import team.gif.robot.Robot;
// to be removed later
public class TurnPointTest extends Command {

    public TurnPointTest() {
        super();
        addRequirements(Robot.turret); // uncomment
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {

    }

    // Called every time the scheduler runs (~20ms) while the command is scheduled
    @Override
    public void execute() {
        System.out.println("ahhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        Robot.turret.turnToPoint(-1);
        System.out.println(Robot.turret.turingP());
    }

    // Return true when the command should end, false if it should continue. Runs every ~20ms.
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called when the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}
}
