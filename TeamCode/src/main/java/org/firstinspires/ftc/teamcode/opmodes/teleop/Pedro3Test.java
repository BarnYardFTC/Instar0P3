package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import org.firstinspires.ftc.teamcode.general.BarnRobot;

@TeleOp(name = "Pedro3 Test", group = "test")
public class Pedro3Test extends CommandOpMode {
    BarnRobot robot;

    Pose defPose;

    @Override
    public void initialize() {
        robot = BarnRobot.getInstance();
        robot.init(this);
        robot.drive.setDefaultCommand(robot.drive.fieldCentricCommand());
        defPose = robot.drive.follower.pose();
    }

    @Override
    public void end() {
        robot.drive.follower.setPose(defPose);
    }
}
