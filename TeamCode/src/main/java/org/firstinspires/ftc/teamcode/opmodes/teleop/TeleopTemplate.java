package org.firstinspires.ftc.teamcode.opmodes.teleop;


import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.photon.PhotonCore;

import org.firstinspires.ftc.teamcode.general.BarnRobot;

import java.util.ArrayList;

public class TeleopTemplate {

    private static ArrayList<String> binds = new ArrayList<>();
    private static final BarnRobot robot = BarnRobot.getInstance();

    public static void apply(OpMode opMode) {
        PhotonCore.enable();
        robot.init(opMode);
//        robot.shooter.setDefaultCommand(robot.shooter.operateShooterDependsOnDist());
        robot.drive.setDefaultCommand(robot.drive.followPollenCommand());

        // Binds
        toggleBind(GamepadKeys.Button.B, "Change speed", robot.drive.setSlowModeCommand(),  robot.drive.setFastModeCommand());
        toggleBind(GamepadKeys.Button.A, "Transfer", robot.transfer.setPassCommand(), robot.transfer.setCollectCommand());
        toggleBind(GamepadKeys.Button.Y, "Shooter", robot.shooter.turnOff(), robot.shooter.operateShooterDependsOnDist());
        toggleBind(GamepadKeys.Button.X, "Intake", robot.intake.disableCommand(), robot.intake.enableCommand());
        //toggleBind(GamepadKeys.Button.DPAD_DOWN, "Go to", robot.drive.goToCommand(new Pose(47, 47, 90)), robot.drive.goToCommand(new Pose(47, 47, 90)));
        //toggleBind(GamepadKeys.Button.DPAD_UP, "Turn to", robot.drive.turnToGoal(), robot.drive.turnToGoal());
    }

    public static void toggleBind(GamepadKeys.Button button, String description, Command command1, Command command2) {
        robot.gamepadEx1.getGamepadButton(button)
                .toggleWhenPressed(
                        command1,
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


        robot.telemetry.addData("Target Robot Space:", robot.limelight.getTargetRobotSpace());
        robot.telemetry.addData("Is data: ", robot.limelight.isData);
        robot.telemetry.addData("Is valid: ", robot.limelight.isValid);

//        robot.telemetry.addData("Target Distance:", BarnRobot.getInstance().limelight.getDistance());
//        robot.telemetry.addData("targetToField",robot.drive.targetToField(robot.drive.follower.pose(), robot.limelight.getTargetRobotSpace()));
        robot.periodic();
    }

    public static void end() {
        binds.clear();
    }
}




