package team.gif.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import team.gif.robot.Robot;

public class ManualTurn extends Command {

    public ManualTurn() {
        super();
        addRequirements(Robot.turret);
        //addRequirements(Robot.climber); // uncomment
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs (~20ms) while the command is scheduled
    @Override
    public void execute() {
        double speed = Robot.oi.driver.getRightX();
        Robot.turret.PerTurn(speed);
        System.out.println("i am runninng ");
        System.out.println("x"+Robot.oi.driver.getRightX());
        System.out.println(speed+"speed");


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
