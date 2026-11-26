package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.general.Constants;

@TeleOp(name = "Pinpoint Test", group = "test")
public class PPTest extends CommandOpMode {
    private Follower follower;

    @Override
    public void initialize() {
        follower = Constants.create(hardwareMap);
    }

    @Override
    public void run() {
        super.run();

        follower.update();

        telemetry.addData("x", follower.pose().x());
        telemetry.addData("y", follower.pose().y());
        telemetry.addData("heading", follower.pose().heading());
        telemetry.update();
    }
}
