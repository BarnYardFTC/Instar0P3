package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.general.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

@TeleOp(name = "PollenFollowingTest", group = "test")
public class PollenFollowingTest extends CommandOpMode {
    BarnRobot robot = BarnRobot.getInstance();
    @Override
    public void initialize() {
        robot.init(this);
        robot.intake.setDefaultCommand(robot.intake.enableContiniousliCommand());
        robot.drive.setDefaultCommand(robot.drive.followPollenCommand());

//        TeleopTemplate.apply(this, false);
    }

    @Override
    public void run() {
        super.run();
        robot.periodic();
    }

    @Override
    public void end() {
        super.end();
//        TeleopTemplate.end();
    }
}
