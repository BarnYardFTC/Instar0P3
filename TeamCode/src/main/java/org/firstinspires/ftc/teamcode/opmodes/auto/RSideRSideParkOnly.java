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
//this autonamous is for teammates who Only Park
//We Start on right side and park on right Side
@Autonomous(name = "RSide", group = "RSideParkOnly")
public class RSideRSideParkOnly extends LinearOpMode {

    private Follower follower;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(56, 8.5367, 90);
    private final Pose point1 = poseFactory.of(9.5705, 23.2554, 270);
    private final Pose point2 = poseFactory.of(8.676, 8.9444, 270);
    private final Pose point3 = poseFactory.of(65.0256, 127.7257, 270);
    private final Pose point3Control1 = poseFactory.of(25.237, 111.6653, 0);
    private final Pose point4 = poseFactory.of(9.9283, 94.6315, 270);
    private final Pose point4Control1 = poseFactory.of(34.5279, 90.1474, 0);

    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                follow(follower, path2()),
                follow(follower, path3()),
                follow(follower, path4())
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
        return Paths.line(start, point1).constant(point1);
    }

    public Path path2() {
        return Paths.line(point1, point2).constant(point2);
    }

    public Path path3() {
        return Paths.curve(point2, point3Control1, point3).constant(point3);
    }

    public Path path4() {
        return Paths.curve(point3, point4Control1, point4).constant(point4);
    }
}

