package team.gif.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import team.gif.robot.Robot;

public class SpinerSpinBAckwads extends Command {

    public SpinerSpinBAckwads() {
        super();
        addRequirements(Robot.neoSpinDexer); // uncomment
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs (~20ms) while the command is scheduled
    @Override
    public void execute() {
        System.out.println("asldflskadjflksjdfl");
        Robot.neoSpinDexer.setRPM(1000);
        Robot.indexerWheels.turn(-.3);
    }

    // Return true when the command should end, false if it should continue. Runs every ~20ms.
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called when the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.neoSpinDexer.turn(0);
        Robot.indexerWheels.turn(0);

    }
}
