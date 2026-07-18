package team.gif.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.TurretActTurnCalc;

public class turnetPosTurn extends Command {
    public double turn =0;

    public turnetPosTurn() {
        super();
        //addRequirements(Robot.climber);
        // uncomment
        addRequirements(Robot.turret);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {


    }

    // Called every time the scheduler runs (~20ms) while the command is scheduled
    @Override
    public void execute() {
        //offset
//        System.out.println("-------\n"+ TurretActTurnCalc.turnPlace()+"\n---------------------");
        //Robot.turret.turnToPoint(TurretActTurnCalc.turnPlace()); #important
        if(Robot.driveTrain.turnAmount()>4.262){ // ofset orriganl 4.762 /// 4.262 // was 5.062
             turn = Robot.driveTrain.turnAmount() - 30;
            System.out.println("turn +"+ turn +"bleh");
        }
        if (Robot.driveTrain.turnAmount()<=4.262){
            turn = Robot.driveTrain.turnAmount() ;
        }
//        else{
//            turn = Robot.driveTrain.turnAmount();
//        }\
        if(turn <= 4.262 && turn > -25.738){ // 5.062 is offset becuase turret zero is not robot zzero, 30 - offset
            turn -= 4.262;
            Robot.turret.turnToPoint(turn);
            System.out.println("||||||\n"+turn+"\n|||||");
            System.out.println(Robot.driveTrain.turnAmount());
        }
       System.out.println("-------\n"+Robot.driveTrain.turnAmount()+"\n----------");


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
