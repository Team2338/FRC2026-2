package team.gif.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.Collector;

public class AgitateColector extends Command {
    private double counter;
    public AgitateColector() {
        super();
        //addRequirements(Robot.climber); // uncomment
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() { counter=0;
    }

    // Called every time the scheduler runs (~20ms) while the command is scheduled
    @Override
    public void execute() {
        Robot.collector.turn(Constants.Commands.COLLECTOR_SPEED);
        if(counter%Constants.Commands.AGITATE_CYCLE_1==0 && counter%Constants.Commands.AGITATE_CYCLE_2!=0){
            Robot.collectorPivot.turn(Constants.Commands.COLLECTOR_PIVOT_SPEED_UP);
        }
        if(counter%Constants.Commands.AGITATE_CYCLE_2==0){
            Robot.collectorPivot.turn(Constants.Commands.COLLECTOR_PIVOT_SPEED_DOWN);
        }
        counter++;

    }

    // Return true when the command should end, false if it should continue. Runs every ~20ms.
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called when the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.collectorPivot.turn(0);
        Robot.collector.turn(0);
    }
}
