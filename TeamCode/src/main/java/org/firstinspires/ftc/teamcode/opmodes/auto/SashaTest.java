package org.firstinspires.ftc.teamcode.opmodes.auto;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.command.ParallelRaceGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;
import org.firstinspires.ftc.teamcode.general.BarnRobot;

@Autonomous(name="Sasha Test", group="Test")
public class SashaTest extends CommandOpMode {

    private final PoseFactory poseFactory = PoseFactory.degrees();
    private final Pose START_POSE = poseFactory.of(9.8461262553802,130.98139347202294, -90);
    private final Pose SHOOT_POSE = poseFactory.of(47.067431850789106,24.02223816355811,-90);

    private final Pose CTRL_1 = poseFactory.of(40.081061692969875, 53.08895265423242, 0);
    private final Pose CTRL_2 = poseFactory.of(2.148493543758967, 1.2539454806312789, 0);
    private final Pose CTRL_3  = poseFactory.of(39.307747489239595, 24.307030129124826, 0);

    private Path goShoot = curve(START_POSE, CTRL_1, CTRL_2, CTRL_3, SHOOT_POSE).linear(START_POSE, SHOOT_POSE);
    private Path goPark = line(SHOOT_POSE, START_POSE).constant(Math.toRadians(90));

    private BarnRobot robot;

    @Override
    public void initialize() {
        robot = BarnRobot.getInstance();
        robot.init(this);
        robot.shooter.setDefaultCommand(robot.shooter.operateShooter());
    }
    @Override
    public void preRun() {
        robot.drive.follower.setPose(START_POSE);
        schedule(routine());
    }

    @Override
    public void run() {
        super.run();
        robot.periodic();
    }

    private Command routine() {
        return new SequentialCommandGroup(
                robot.intake.enableCommand(),
                new FollowPathCommand(robot.drive.follower, goShoot),
                robot.transfer.setPassCommand(),
                robot.intake.disableCommand(),
                new WaitCommand(1000),
                new FollowPathCommand(robot.drive.follower, goPark)
        );
    }
}
