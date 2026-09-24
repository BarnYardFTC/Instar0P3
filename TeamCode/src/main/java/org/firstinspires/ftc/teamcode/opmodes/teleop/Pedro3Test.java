package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import org.firstinspires.ftc.teamcode.general.BarnRobot;

@TeleOp(name = "Pedro3 Test", group = "test")
public class Pedro3Test extends CommandOpMode {
    BarnRobot robot;

    @Override
    public void initialize() {
        robot = BarnRobot.getInstance();
        robot.init(this);
        robot.drive.setDefaultCommand(robot.drive.fieldCentricCommand());
    }
}
