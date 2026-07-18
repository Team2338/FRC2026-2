package team.gif.robot;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.security.PublicKey;

public class UI {

    /**
     *  Widgets (e.g. gyro, text, True/False flags),
     *  buttons (e.g. SmartDashboard.putData("Reset", new ResetHeading()); ),
     *  and Chooser options (e.g. auto mode, auto delay)
     *
     *  Placed in SmartDashboard network table
     *  After dashboard loads for the first time, manually move items from network table onto respective dashboard tab
     *  and save file as "YYYY elastic-layout.json"
     */
    public UI() {
        ShuffleboardTab shuffleboardTab = Shuffleboard.getTab("FRC 2026");
        shuffleboardTab.addDouble("GetPositon", Robot.turret::getPos);
        shuffleboardTab.addBoolean("is at stop", Robot.turret::isAtStop);
        shuffleboardTab.addDouble("pigion heading", Robot.pigeon::get360Heading);
        shuffleboardTab.addBoolean("IS at 0 stop", Robot.turret::isAt0Stop);
//        shuffleboardTab.addDouble("turret rots", Robot.turret::);
        SmartDashboard.putNumber("P", 0);
        SmartDashboard.putBoolean("RED allience", true);
        SmartDashboard.putBoolean("LL3 HasTarget", LimelightHelpers.getTV("Limelight-side"));
        //SmartDashboard.putNumber("distance", Robot.driveTrain.distance());

    }

    /**
     * Widgets which are updated periodically should be placed here
     *
     * Convenient way to format a number is to use putString w/ format:
     *     SmartDashboard.putString("Elevator", String.format("%11.2f", Elevator.getPosition());
     */
    public void update() {
        //Example
        //SmartDashboard.putNumber("Climber Position", Robot.elevator.getPosition())
        SmartDashboard.putNumber("M1 RPM", Robot.shooter.getRPMOne());
        SmartDashboard.putNumber("M2 RPM", Robot.shooter.getRPMTwo());
        SmartDashboard.putNumber("HEADING", Robot.pigeon.get360Heading());
        SmartDashboard.putNumber("right motor", Robot.driveTrain.getVelocityRight());
        SmartDashboard.putNumber("left motor", Robot.driveTrain.getVelocityLeft());
        SmartDashboard.putBoolean("hub aim?", Robot.driveTrain.scoreing);
        SmartDashboard.putNumber("distance",Robot.driveTrain.distance());


    }
}
