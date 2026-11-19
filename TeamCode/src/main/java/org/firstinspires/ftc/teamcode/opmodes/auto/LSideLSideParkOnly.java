package org.firstinspires.ftc.teamcode.opmodes.auto;

import static com.pedropathing.api.Paths.*;
import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.general.Constants;

@Autonomous(name = "LSideLSide", group = "LSideParkOnly")
public class LSideLSideParkOnly extends LinearOpMode {
    //this autonamous is for teammates who Only Park
//We Start on left side and park on left Side
    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(61.3666, 132.8635, 90);
    private final Pose point1 = poseFactory.of(33.1836, 33.8097, 270);
    private final Pose point1Control1 = poseFactory.of(29.3521, 134.8541, 0);
    private final Pose point2 = poseFactory.of(55.5446, 14.1321, 270);
    private final Pose point3 = poseFactory.of(9.57, 14.13, 0);
    private final Pose point4 = poseFactory.of(9.5705, 8.7655, 270);
    private final Pose point5 = poseFactory.of(37.298, 118.6024, 270);
    private final Pose point6 = poseFactory.of(12.7905, 119.4968, 270);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                follow(follower, path2()),
                follow(follower, path3()),
                follow(follower, path4()),
                follow(follower, path5()),
                follow(follower, path6())
        );
    }

    @Override
    public void runOpMode() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        waitForStart();
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }
    }

    public Path path1() {
        return Paths.curve(start, point1Control1, point1).constant(point1);
    }

    public Path path2() {
        return Paths.line(point1, point2).constant(point2);
    }

    public Path path3() {
        return Paths.line(point2, point3).constant(point3);
    }

    public Path path4() {
        return Paths.line(point3, point4).constant(point4);
    }

    public Path path5() {
        return Paths.line(point4, point5).constant(point5);
    }

    public Path path6() {
        return Paths.line(point5, point6).constant(point6);
    }
}
