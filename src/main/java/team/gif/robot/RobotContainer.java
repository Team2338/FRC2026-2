// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package team.gif.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

/**
 * Robot-wide setup: autonomous selection and (empty) trigger bindings.
 *
 * <p>This robot has no autonomous modes, and all driver buttons live in {@link OI}.
 * The class is kept because {@link Robot} expects it and because it is the natural
 * place to add bindings later.
 */
public class RobotContainer {

    public RobotContainer() {
        // Configure the trigger bindings.
        configureBindings();
    }

    /**
     * Trigger -> command mappings.
     *
     * <p>Intentionally empty: {@link OI} owns every binding on this robot.
     */
    private void configureBindings() {
    }

    /**
     * Autonomous command.
     *
     * <p>Outreach robot: there are no autonomous modes, so this returns a command
     * that does nothing and finishes immediately.
     */
    public Command getAutonomousCommand() {
        return Commands.none();
    }
}
