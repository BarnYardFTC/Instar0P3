package org.firstinspires.ftc.teamcode.opmodes.auto;

import static org.firstinspires.ftc.teamcode.opmodes.auto.AutoTemplate.*;


import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.pedropathing.ivy.CommandBuilder;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.general.BarnRobot;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous(name="Auto test", group="Test")
public class TestAuto extends CommandOpMode {
    BarnRobot robot;
    Follower follower;

    @Override
    public void initialize() {
        robot = BarnRobot.getInstance();
        robot.init(this);
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        AutoTemplate.pathBuilder(Alliance.BLUE);
        follower.setPose(START_POSE);
//        robot.shooter.operateShooter();
//        robot.intake.enableCommand();
        Scheduler.schedule(autoRoutine());
    }

    @Override
    public void run() {
        robot.periodic();
        follower.update();
        super.run();
    }

    @Override
    public void end() {
        super.end();
//        Drivetrain.setPassPose(robot.drive.follower.getPose());
    }

    private CommandBuilder autoRoutine() {
        return sequential(
                follow(follower, goTestpose())
        );
    }

}
