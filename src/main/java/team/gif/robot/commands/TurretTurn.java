package team.gif.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import team.gif.robot.Constants;
import team.gif.robot.Robot;

public class TurretTurn extends Command {
private final PIDController alignPID;
private double offset;
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
            if (Robot.limelight.getXOffset() !=0){
                offset = Robot.limelight.getXOffset();
            }
            if (Robot.turret.isAtStop()){
                System.out.println("at 30 stop");
                if (targetingAngularVelocity < 0){
                    Robot.turret.turn(-targetingAngularVelocity*(1500*2));
                }
                else {
                    Robot.turret.turn(0);
                }
            }
            if (Robot.turret.isAt0Stop()){
                System.out.println("at zero");
                if (targetingAngularVelocity > 0){
                    Robot.turret.turn(-targetingAngularVelocity*(1500*2));
                }
                else {
                    Robot.turret.turn(0);
                }}
            if (!Robot.turret.isAt0Stop() && !Robot.turret.isAtStop()){
                Robot.turret.turn(-targetingAngularVelocity * 2000);
                System.out.println("wkjaiuytgxdrhfjgkulh.g,fmjgfc,v.guyfd,xrmfgcj,yftzmct,");
            }
            if (!Robot.limelight.hasTarget()&& !Robot.turret.isAtStop() && !Robot.turret.isAt0Stop()){
                double TAV2 = alignPID.calculate(offset, 0);
                TAV2 *= -1;
                Robot.turret.turn(-TAV2*1500);
            }

            // targetanularvelocity is a very low number that is why we want a larget number even for voltage
//            System.out.println("x- offset"+Robot.limelight.getXOffset());
//            System.out.println("voltage");
//            System.out.println("TAV"+targetingAngularVelocity);
//            Robot.limelight.getDistance();
//            System.out.println("distance"+Robot.limelight.getDistance());
            //System.out.println(Robot.limelight.getXOffset());
        }
        else {
            System.out.println("in manual");
            double speed = Robot.oi.aux.getRightX();
            Robot.turret.PerTurn(speed*.35);
        }
        Robot.manualMode = Math.abs(Robot.oi.aux.getRightX()) > .05;

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
