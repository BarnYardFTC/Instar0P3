package org.firstinspires.ftc.teamcode.opmodes.teleop;


import com.bylazar.gamepad.Gamepad;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.photon.PhotonCore;

import org.firstinspires.ftc.teamcode.general.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import java.util.ArrayList;

public class TeleopTemplate {

    private static ArrayList<String> binds = new ArrayList<>();
    private static final BarnRobot robot = BarnRobot.getInstance();

    public static void apply(OpMode opMode, Drivetrain.DriveMode driveMode) {
        PhotonCore.enable();
        robot.init(opMode);
        robot.drive.setDriveMode(driveMode);
        robot.frontShooter.setDefaultCommand(robot.frontShooter.operateShooter());
        robot.backShooter.setDefaultCommand(robot.backShooter.operateShooter());
        robot.drive.setDefaultCommand(robot.drive.fieldCentricCommand());
        // Binds
        triggerBind(GamepadKeys.Trigger.RIGHT_TRIGGER, "Intake", robot.intake.enableCommand(), robot.intake.disableCommand());
        triggerBind(GamepadKeys.Trigger.RIGHT_TRIGGER, "Transfer", robot.transfer.enableCommand(), robot.transfer.disableCommand());
        holdBind(GamepadKeys.Button.LEFT_STICK_BUTTON, "Slow Mode", robot.drive.setSlowModeCommand(), robot.drive.setFastModeCommand());
        toggleBind(GamepadKeys.Button.DPAD_UP, "Gate",
                new ParallelCommandGroup(robot.backGate.openCommand(), robot.frontGate.openCommand()),
                new ParallelCommandGroup(robot.backGate.closeCommand(), robot.frontGate.closeCommand())
        );
        keyBind(GamepadKeys.Button.A, "Shooter speed: Low",
                new ParallelCommandGroup(robot.frontShooter.setCustomRPM(Shooter.CLOSE_RPM), robot.backShooter.setCustomRPM(Shooter.CLOSE_RPM))
        );
        keyBind(GamepadKeys.Button.B, "Shooter speed: Mid",
                new ParallelCommandGroup(robot.frontShooter.setCustomRPM(Shooter.MID_RPM), robot.backShooter.setCustomRPM(Shooter.MID_RPM))
        );
        keyBind(GamepadKeys.Button.Y, "Shooter speed: Fast",
                new ParallelCommandGroup(robot.frontShooter.setCustomRPM(Shooter.FAR_RPM), robot.backShooter.setCustomRPM(Shooter.FAR_RPM))
        );
        keyBind(GamepadKeys.Button.X, "Stop Shooter",
                new ParallelCommandGroup(robot.frontShooter.turnOff(), robot.backShooter.turnOff())
        );
        keyBind(GamepadKeys.Button.RIGHT_BUMPER, "Turn Turret Right",
                new ParallelCommandGroup(robot.frontTurret.adjustAngleCommand(10))
        );
        keyBind(GamepadKeys.Button.LEFT_BUMPER, "Turn Turret Left",
            new ParallelCommandGroup(robot.frontTurret.adjustAngleCommand(-10))
        );
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

        robot.telemetry.addData("Speed modifier: ", robot.drive.getDriveSpeedMode());

        robot.telemetry.addLine("________ SHOOTER ________");
        robot.telemetry.addData("Front shooter speed: ", robot.frontShooter.getRPM());
        robot.telemetry.addData("Back shooter speed: ", robot.backShooter.getRPM());

        robot.telemetry.addLine("________ TURRET ________");
        robot.telemetry.addData("Front turret angle: ", robot.frontTurret.getAngle());
        robot.telemetry.addData("Back turret angle: ", robot.backTurret.getAngle());

        robot.telemetry.addLine("________ GATE ________");
        robot.telemetry.addData("Front gate position: ", robot.frontGate.getGatePosition());
        robot.telemetry.addData("Back gate position: ", robot.backGate.getGatePosition());

        robot.periodic();
    }

    public static void end() {
        binds.clear();
    }
}