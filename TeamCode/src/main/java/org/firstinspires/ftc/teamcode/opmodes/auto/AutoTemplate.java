package org.firstinspires.ftc.teamcode.opmodes.auto;

import static com.pedropathing.api.Paths.*;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.paths.Path;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

public class AutoTemplate {
    final static double FIELD_SIZE = 141.5;

    enum Alliance {
        RED,
        BLUE
    }

//    static final Pose START_POSE = new Pose(8.83106169296987,132.80850968436155, Math.toRadians(90));
//    static final Pose COLLECT_POSE = new Pose(8.83106169296987, 15.812051649928243, Math.toRadians(90));
//    static final Pose SHOOT_POSE = new Pose(47.291965566714495,108.44404591104735,Math.toRadians(-90));

    static final PoseFactory poseFactory = PoseFactory.degrees();
    static final Pose START_POSE = poseFactory.of(8.83106169296987,132.80850968436155, 90);
    static final Pose COLLECT_POSE = poseFactory.of(8.83106169296987, 15.812051649928243, 90);
    static final Pose SHOOT_POSE = poseFactory.of(47.291965566714495,108.44404591104735,-90);
    static final Pose TEST_PSOE = poseFactory.of(29.811334289813484, 36.406025824964125, 90);

    static Path goCollect, goShoot;


    static Pose transform(Pose pose, Alliance alliance) {
        if (alliance == Alliance.BLUE) {
            return pose;
        }

        return new Pose(
                FIELD_SIZE - pose.x(),
                FIELD_SIZE - pose.y(),
                pose.heading() + Math.PI
        );
    }

    static void pathBuilder(Alliance alliance) {
        Pose start = transform(START_POSE, alliance);
        Pose collect = transform(COLLECT_POSE, alliance);
        Pose shoot = transform(SHOOT_POSE, alliance);

        goCollect = line(start, collect).linear(start, collect);
        goShoot = line(collect, shoot).linear(collect, shoot);
    }

    static Path goTestpose(){
        return line(START_POSE, TEST_PSOE).linear(START_POSE, TEST_PSOE);
    }
}
