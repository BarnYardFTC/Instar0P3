package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.general.BarnRobot;

@TeleOp(name = "Main Teleop", group = "main")
public class MainTeleop extends CommandOpMode {

    private final PoseFactory poseFactory = PoseFactory.degrees();
    private final Pose START_POSE = poseFactory.of(9.8461262553802,130.98139347202294, -90);

    @Override
    public void initialize() {
        TeleopTemplate.apply(this);
    }

    @Override
    public void preRun() {
        BarnRobot.getInstance().drive.follower.setPose(START_POSE);
    }

    @Override
    public void run() {
        super.run();
        TeleopTemplate.periodic();
    }

    @Override
    public void end() {
        super.end();
        TeleopTemplate.end();
    }
}
