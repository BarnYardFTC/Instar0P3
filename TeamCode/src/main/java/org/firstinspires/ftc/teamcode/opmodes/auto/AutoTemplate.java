package org.firstinspires.ftc.teamcode.opmodes.auto;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

public class AutoTemplate {
    final static double FIELD_SIZE = 141.5;

    enum Alliance {
        RED,
        BLUE
    }

    static final Pose START_POSE = new Pose(8.83106169296987,132.80850968436155, Math.toRadians(90));
    static final Pose COLLECT_POSE = new Pose(8.83106169296987, 15.812051649928243, Math.toRadians(90));
    static final Pose SHOOT_POSE = new Pose(47.291965566714495,108.44404591104735,Math.toRadians(-90));

    static final PoseFactory poseFactory = PoseFactory.degrees();


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
}
