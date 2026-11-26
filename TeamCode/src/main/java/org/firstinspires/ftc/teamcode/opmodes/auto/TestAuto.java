package org.firstinspires.ftc.teamcode.opmodes.auto;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.general.Constants;

@Autonomous(name="Auto test", group="Test")
public class TestAuto extends LinearOpMode {
    private Follower follower;

    @Override
    public void runOpMode() {
        follower = Constants.create(hardwareMap);

        follower.setPose(AutoTemplate.getStartPose());
        follower.update();

        waitForStart();

        follower.follow(AutoTemplate.goCurve());

        while (opModeIsActive()) {
            follower.update();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());
            telemetry.update();
        }
    }
}
