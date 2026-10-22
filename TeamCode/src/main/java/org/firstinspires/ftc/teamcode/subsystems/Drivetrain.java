package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;
import com.seattlesolvers.solverslib.pedroCommand.TurnToCommand;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.firstinspires.ftc.teamcode.general.BarnRobot;
import org.firstinspires.ftc.teamcode.general.Constants;

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

    public Drivetrain(HardwareMap hwMap, String leftFrontName, String rightFrontName, String leftBackName, String rightBackName) {
        leftFront = hwMap.get(DcMotor.class, leftFrontName);
        rightFront = hwMap.get(DcMotor.class, rightFrontName);
        leftBack = hwMap.get(DcMotor.class, leftBackName);
        rightBack = hwMap.get(DcMotor.class, rightBackName);

        initMotor(DcMotorSimple.Direction.REVERSE, leftFront);
        initMotor(DcMotorSimple.Direction.FORWARD, rightFront);
        initMotor(DcMotorSimple.Direction.REVERSE, leftBack);
        initMotor(DcMotorSimple.Direction.FORWARD, rightBack);

        follower = Constants.create(hwMap);
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

    Pose pollenPose;
    public void followPollen() {
        double forward = BarnRobot.getInstance().limelight.getTa() > 0 ? 1.0/(1.0 + BarnRobot.getInstance().limelight.getTa()) : 0  ;
        powers = new DrivePowers(-1*forward, 0, BarnRobot.getInstance().limelight.getTx()*-0.05);

        follower.manual(powers);
        follower.update();
    }

    public boolean getInReach() {
        return pollenInReach;
    }

    public RunCommand fieldCentricCommand() {
        return new RunCommand(this::fieldCentric, this);
    }

    public RunCommand followPollenCommand(){
        return new RunCommand(this::followPollen, this);
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
