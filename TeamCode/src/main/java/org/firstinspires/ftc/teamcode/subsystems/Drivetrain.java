package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.DeferredCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;
import com.seattlesolvers.solverslib.pedroCommand.TurnToCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.general.BarnRobot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class Drivetrain extends SubsystemBase {
    private final DcMotor leftFront;
    private final DcMotor rightFront;
    private final DcMotor leftBack;
    private final DcMotor rightBack;

    public Follower follower;

    DrivePowers powers;
    private final double SLOW_SPEED = 0.3;
    private final double FAST_SPEED = 1.0;

    private final Pose goalPose = new Pose(47, 130, 0);

    private double speedModifier;

    private boolean pollenInReach = true;

    //TODO: Find min and max tx where we  can intake pollens
    private final double maxTx = 1;
    private final double minTx = 0.5;

    public Set<Pose> targetsHashSet = new LinkedHashSet<>();
    public List<Pose> targetsList = new ArrayList<>(targetsHashSet);


    public Drivetrain() {
        leftFront = BarnRobot.getInstance().hardware.leftFrontDrivetrain;
        rightFront = BarnRobot.getInstance().hardware.rightFrontDrivetrain;
        leftBack = BarnRobot.getInstance().hardware.leftBackDrivetrain;
        rightBack = BarnRobot.getInstance().hardware.rightBackDrivetrain;

        initMotor(DcMotorSimple.Direction.REVERSE, leftFront);
        initMotor(DcMotorSimple.Direction.FORWARD, rightFront);
        initMotor(DcMotorSimple.Direction.REVERSE, leftBack);
        initMotor(DcMotorSimple.Direction.FORWARD, rightBack);

        follower = Constants.create(BarnRobot.getInstance().hardware.hwMap);
        speedModifier = FAST_SPEED;
    }

    private void initMotor(DcMotorSimple.Direction direction, DcMotor motor) {
        motor.setDirection(direction);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private void fieldCentric() { //TODO: write a system that can invert controls without misplacing the robot
        powers = ManualDrive.fieldCentric(
                BarnRobot.getInstance().gamepadEx1.getLeftY() * speedModifier,
                -BarnRobot.getInstance().gamepadEx1.getLeftX() * speedModifier,
                -BarnRobot.getInstance().gamepadEx1.getRightX() * speedModifier * 0.7,
                follower.pose().heading()
        );
        follower.manual(powers);
        follower.update();
    }

    //Converts robot position and target position from limelight view to the target's field position
    public Pose targetToField(Pose robotPose, Pose3D targetPoseRobotSpace){
        Position targetPose = targetPoseRobotSpace.getPosition().toUnit(DistanceUnit.INCH);
        double forward = targetPose.y;
        double left = -targetPose.x;

        double heading = robotPose.heading();
        double cos = Math.cos(heading);
        double sin = Math.sin(heading);

        double fieldX = robotPose.x() + forward * cos - left * sin;
        double fieldY = robotPose.y() + forward * sin + left * cos;

        return new Pose(fieldX, fieldY);
    }

    Pose pollenPose;
    public void followPollen(double distance, double tx) {
        double targetX = follower.pose().x() + distance * Math.cos(follower.pose().heading());
        if (targetX > 72 && (tx < minTx || tx > maxTx)) {
            pollenInReach = false;
        } else {
            pollenInReach = true;
        }

        if(pollenInReach){
            pollenPose = targetToField(follower.pose(), BarnRobot.getInstance().limelight.getTargetRobotSpace());
            goToCommand(pollenPose);
        }
//        powers = new DrivePowers(0, 0, angle); //TODO: determine what to do with angle, add forward input
//        follower.manual(powers);
//        follower.update();
    }

    public boolean getInReach() {
        return pollenInReach;
    }

    public RunCommand fieldCentricCommand() {
        return new RunCommand(this::fieldCentric, this);
    }

    public Command setSlowModeCommand() {
        return new InstantCommand(() -> speedModifier = SLOW_SPEED, this);
    }

    public Command setFastModeCommand() {
        return new InstantCommand(() -> speedModifier = FAST_SPEED, this);
    }

    public Command turnToGoal() {
        return new TurnToCommand(
                follower,
                Math.atan2(
                    goalPose.y() - follower.pose().y(),
                    goalPose.x() - follower.pose().x()
                )
        );
    }

    public Command goToCommand(Pose pose) {
        return new FollowPathCommand(follower, line(follower.pose(), pose).constant(pose.heading()));
    } //TODO: fix the logic of switching between this and field centric


    //Function for future builder of collecting paths for auto
    public Command goToTargetCommand(Pose pose){
        targetsHashSet.remove(pose);
        return new FollowPathCommand(follower, line(follower.pose(), pose).linear(follower.pose().heading(), pose.heading()));
    }
}
