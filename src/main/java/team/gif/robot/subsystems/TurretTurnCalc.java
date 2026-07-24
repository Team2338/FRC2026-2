// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot.subsystems;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import team.gif.robot.Robot;

public class TurretTurnCalc extends SubsystemBase {
    /** Creates a new ExampleSubsystem. */
    private final static InterpolatingDoubleTreeMap distanceMap = new InterpolatingDoubleTreeMap();

    static {

        double measurementOffset = 0.88;

        distanceMap.put((2.3), 2600.0);
        distanceMap.put((2.4 ), 2600.0);
        distanceMap.put((2.7), 2800.0);
        distanceMap.put((2.8), 2900.0);
        distanceMap.put((3.0), 3000.0);
        distanceMap.put((3.2), 3100.0);
        distanceMap.put((3.5), 3200.0);
        distanceMap.put((3.8), 3300.0);
        distanceMap.put((4.0), 3500.0);
        distanceMap.put((4.3), 3700.0);
        distanceMap.put((4.8), 4000.0);
        distanceMap.put((5.0), 4200.0);
        distanceMap.put((5.4), 4450.0);
        distanceMap.put((7.0),4800.0);

        /*distanceMap.put((2.3), 2700.0);
        distanceMap.put((2.4 ), 2700.0);
        distanceMap.put((2.7), 2900.0);
        distanceMap.put((2.8), 3000.0);
        distanceMap.put((3.0), 3100.0);
        distanceMap.put((3.2), 3200.0);
        distanceMap.put((3.5), 3300.0);
        distanceMap.put((3.8), 3500.0);
        distanceMap.put((4.0), 3700.0);
        distanceMap.put((4.3), 3900.0);
        distanceMap.put((4.8), 4200.0);
        distanceMap.put((5.0), 4400.0);
        distanceMap.put((5.4), 4650.0);
        distanceMap.put((7.0),5000.0);*/
    }
    public static double getShotRPM() {
        //System.out.println(Robot.driveTrain.distance());
        return distanceMap.get(Robot.driveTrain.distance());
}


}


