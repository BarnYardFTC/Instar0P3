package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.general.BarnRobot;

@TeleOp(name = "TestTeleop", group = "Test")
public class TestTeleop extends CommandOpMode {

    BarnRobot robot = BarnRobot.getInstance();

    @Override
    public void initialize() {
        robot.init(this);
        robot.drive.setDefaultCommand(robot.drive.robotCentricCommand());
    }
}
