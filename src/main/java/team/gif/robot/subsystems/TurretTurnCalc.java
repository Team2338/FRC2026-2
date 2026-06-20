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


        distanceMap.put((1.2 + measurementOffset), 2500.0);
        distanceMap.put((1.75 + measurementOffset), 2750.0);
        distanceMap.put((2.15 + measurementOffset), 3000.0);
        distanceMap.put((3.16 + measurementOffset), 3400.0);
        distanceMap.put((3.66 + measurementOffset), 3800.0);
        distanceMap.put((4.3 + measurementOffset), 4000.0);

    }
    public static double getShotRPM() {
    return distanceMap.get(Robot.driveTrain.distance());
}


}


