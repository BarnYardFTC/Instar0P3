package org.firstinspires.ftc.teamcode.opmodes.teleop;


import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.photon.PhotonCore;

import org.firstinspires.ftc.teamcode.general.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

import java.util.ArrayList;

public class TeleopTemplate {

    private static ArrayList<String> binds = new ArrayList<>();
    private static final BarnRobot robot = BarnRobot.getInstance();

    public static void apply(OpMode opMode, Drivetrain.DriveMode driveMode) {
        PhotonCore.enable();
        robot.init(opMode);
        robot.drive.setDriveMode(driveMode);
//        robot.shooter.setDefaultCommand(robot.shooter.operateShooterDependsOnDist());
        robot.drive.setDefaultCommand(robot.drive.fieldCentricCommand());
        // Binds
        triggerBind(GamepadKeys.Trigger.RIGHT_TRIGGER, "Intake", robot.intake.enableCommand(), robot.intake.disableCommand());
        triggerBind(GamepadKeys.Trigger.RIGHT_TRIGGER, "Transfer", robot.transfer.enableCommand(), robot.transfer.disableCommand());
        holdBind(GamepadKeys.Button.LEFT_STICK_BUTTON, "Slow Mode", robot.drive.setSlowModeCommand(), robot.drive.setFastModeCommand());
        toggleBind(GamepadKeys.Button.DPAD_UP, "Gate",
                new ParallelCommandGroup(robot.backGate.openCommand(), robot.frontGate.openCommand()),
                new ParallelCommandGroup(robot.backGate.closeCommand(), robot.frontGate.closeCommand())
        );
//        keyBind(GamepadKeys.Button.A, "Shooter speed: Low", robot);
    }

    public static void toggleBind(GamepadKeys.Button button, String description, Command command1, Command command2) {
        robot.gamepadEx1.getGamepadButton(button)
                .toggleWhenPressed(
                        command1,
                        command2
                );
        binds.add(button.toString() + ": " + description);
    }

    public static void keyBind(GamepadKeys.Button button, String description, Command command1) {
        robot.gamepadEx1.getGamepadButton(button)
                .whenPressed(
                        command1
                );
        binds.add(button.toString() + ": " + description);
    }

    public static void holdBind(GamepadKeys.Button button, String description, Command command1, Command command2) {
        robot.gamepadEx1.getGamepadButton(button)
                .whenHeld(
                        command1
                )
                .whenReleased(
                        command2
                );
        binds.add(button.toString() + ": " + description);
    }

    public static void triggerBind(GamepadKeys.Trigger trigger, String description, Command command, Command offCommand) {
        new Trigger(() -> robot.gamepadEx1.getTrigger(trigger) > 0.5)
                .whenActive(
                        command
                )
                .whenInactive(
                        offCommand
                );
        binds.add(trigger.toString() + ": " + description);
    }

    public static void periodic(){
        binds.forEach(robot.telemetry::addLine);
        robot.telemetry.addData("TX", robot.limelight.getTx());
        robot.telemetry.addData("TY", robot.limelight.getTy());
        robot.telemetry.addData("TA", robot.limelight.getTa());

        robot.telemetry.addData("Robot's following:", (robot.limelight.currentPipeline == 1) ? "pollen" : "red nectars");


        robot.telemetry.addData("Target Robot Space:", robot.limelight.getTargetRobotSpace());
//        robot.telemetry.addData("Is data: ", robot.limelight.isData);
//        robot.telemetry.addData("Is valid: ", robot.limelight.isValid);

//        robot.telemetry.addData("Target Distance:", BarnRobot.getInstance().limelight.getDistance());
//        robot.telemetry.addData("targetToField",robot.drive.targetToField(robot.drive.follower.pose(), robot.limelight.getTargetRobotSpace()));
        robot.periodic();
    }

    public static void end() {
        binds.clear();
    }
}