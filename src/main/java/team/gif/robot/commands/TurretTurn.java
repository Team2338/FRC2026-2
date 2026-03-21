package team.gif.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
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
        if (!Robot.manualMode) {
            double targetingAngularVelocity = alignPID.calculate(Robot.limelight.getXOffset(), 0);
            targetingAngularVelocity *= -1;
            Robot.turret.turn(targetingAngularVelocity * 800);
            System.out.println(Robot.limelight.getXOffset());
            System.out.println("voltage");
            System.out.println(targetingAngularVelocity);
            Robot.limelight.getDistance();
            System.out.println(Robot.limelight.getDistance());
            System.out.println(Robot.limelight.getXOffset());
        }
        else {
            double speed = Robot.oi.driver.getRightX();
            Robot.turret.PerTurn(speed*.05);
        }
        Robot.manualMode = Math.abs(Robot.oi.driver.getRightX()) > .05;

//        if (Robot.limelight.getXOffset()<.5){
//            Robot.shooter.setRPM(Constants.SHOOTER_RPM);
//            if(Robot.shooter.getRPM()>2500){
//                Robot.neoSpinDexer.turn(Constants.SPINNER_SPEED_PER);
//                Robot.indexerWheels.turn(Constants.INDEX_WHEELS_SPEED_PER);
//            }
//        }


    }

    // Return true when the command should end, false if it should continue. Runs every ~20ms.
    @Override
    public boolean isFinished() {
            return false;
    }
//hello world
    // Called when the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Robot.turret.turn(0);
    }


}
