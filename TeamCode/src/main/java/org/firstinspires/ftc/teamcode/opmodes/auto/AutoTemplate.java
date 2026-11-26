package org.firstinspires.ftc.teamcode.opmodes.auto;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

public class AutoTemplate {

    private static final PoseFactory poseFactory = PoseFactory.degrees();

    private static final Pose start = poseFactory.of(22.7218, 118.7921, 270);
    private static final Pose path1 = poseFactory.of(46.5306, 22.7835, 270);
    private static final Pose path1Control1 = poseFactory.of(16, 20, 0);

    public static Path goCurve() {
        return Paths.curve(start, path1Control1, path1).constant(path1);
    }

    public static Pose getStartPose() {
        return start;
    }
}
