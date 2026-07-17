// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.subsystems;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.Robot;

public class TurretActTurnCalc extends SubsystemBase {
    /** Creates a new ExampleSubsystem. */
    private final static InterpolatingDoubleTreeMap distanceMap = new InterpolatingDoubleTreeMap();

    static {

        distanceMap.put(-360.0,-25.238);
        distanceMap.put(-180.0,-12.619);
        //distanceMap.put(-142.0,-15.857);
        distanceMap.put(-56.25, 0.0);
        distanceMap.put(0.0, (-4.762));
        distanceMap.put(180.0,(-17.381));
        distanceMap.put(360.0,-34.762);
       /* distanceMap.put(-360.0,-27.0);
        distanceMap.put(-180.0,-20.0);
        distanceMap.put(-142.0,-15.857);
        distanceMap.put(-56.25, 0.0);
        distanceMap.put(0.0, (-4.762));
        distanceMap.put(180.0,(-20.0));
        distanceMap.put(360.0,-27.0);*/



    }
    public static double turnPlace() {
    return distanceMap.get(Robot.driveTrain.turnAmount());
}


}


