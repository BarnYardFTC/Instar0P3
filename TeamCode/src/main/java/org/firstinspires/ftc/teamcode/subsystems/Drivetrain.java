package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.DeferredCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.geometry.Pose2d;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;
import com.seattlesolvers.solverslib.pedroCommand.TurnToCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.general.BarnRobot;
import org.firstinspires.ftc.teamcode.general.Constants;

public class Drivetrain extends SubsystemBase {
    public static enum DriveMode {
        MANUAL,
        FOLLOWING_POLLEN,
        FOLLOWING_TRAJECTORY
    }
    public DriveMode driveMode = DriveMode.MANUAL;

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


    //TODO: Find min and max tx where we can intake pollens
    private final double maxTx = 1;
    private final double minTx = 0.5;
    private boolean isCollecting = false;

    public Set<Pose> targetsHashSet = new LinkedHashSet<>();
    public List<Pose> targetsList = new ArrayList<>(targetsHashSet);


    public Drivetrain(HardwareMap hwMap, String leftFrontMotorName, String rightFrontMotorName, String leftBackMotorName, String rightBackMotorName) {
        leftFront = hwMap.get(DcMotor.class, leftFrontMotorName);
        rightFront = hwMap.get(DcMotor.class, rightFrontMotorName);
        leftBack = hwMap.get(DcMotor.class, leftBackMotorName);
        rightBack = hwMap.get(DcMotor.class, rightBackMotorName);

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
        if (driveMode != DriveMode.MANUAL) {
            BarnRobot.getInstance().telemetry.addData("Drive Mode", "Not Manual");
            return;
        }

        powers = new DrivePowers(
                BarnRobot.getInstance().gamepadEx1.getLeftY() * speedModifier,
                -BarnRobot.getInstance().gamepadEx1.getLeftX() * speedModifier,
                -BarnRobot.getInstance().gamepadEx1.getRightX() * speedModifier * 0.7
        );
        follower.manual(powers);
        follower.update();
    }

    private void drive() {
        GamepadEx gamepadEx = BarnRobot.getInstance().gamepadEx1;
        double lf_pw = gamepadEx.getLeftY() - gamepadEx.getLeftX() + gamepadEx.getRightX();
        double rf_pw = gamepadEx.getLeftY() + gamepadEx.getLeftX() - gamepadEx.getRightX();
        double lb_pw = gamepadEx.getLeftY() + gamepadEx.getLeftX() + gamepadEx.getRightX();
        double rb_pw = gamepadEx.getLeftY() - gamepadEx.getLeftX() - gamepadEx.getRightX();
        leftFront.setPower(lf_pw * speedModifier);
        rightFront.setPower(rf_pw * speedModifier);
        leftBack.setPower(lb_pw * speedModifier);
        rightBack.setPower(rb_pw * speedModifier);
    }

    //Converts robot position and target position from limelight view to the target's field position
//    public Pose targetToField(Pose robotPose, Pose2D targetPoseRobotSpace){
//        Pose targetPose = new Pose(targetPoseRobotSpace.getX(DistanceUnit.INCH), targetPoseRobotSpace.getY(DistanceUnit.INCH));
//        double forward = targetPose.y();
//        double left = -targetPose.x();
//
//        double heading = robotPose.heading();
//        double cos = Math.cos(heading);
//        double sin = Math.sin(heading);
//
//        double fieldX = robotPose.x() + forward * cos - left * sin;
//        double fieldY = robotPose.y() + forward * sin + left * cos;
//
//        return new Pose(fieldX, fieldY);
//    }

    public void followPollen() {
        if (driveMode != DriveMode.FOLLOWING_POLLEN) {
            BarnRobot.getInstance().telemetry.addData("Drive Mode", "Not Following Pollen");
            return;
        }

        if (!BarnRobot.getInstance().limelight.hasValidTarget() || (follower.pose().x() + 5 > 72 && follower.pose().heading() < 65 && follower.pose().heading() > -65)) powers = new DrivePowers(0, 0, 0.3);
        else {
            isCollecting = true;
            double tx = BarnRobot.getInstance().limelight.getTx();
            double turn = 0.0;
            double forward = 0.3;
            if (Math.abs(tx) > 1.2) {
                turn = tx * -0.015;
                turn = Math.max(-0.35, Math.min(0.35, turn));
            }
            powers = new DrivePowers(-1 * forward, 0, turn);
        }
//        if(BarnRobot.getInstance().limelight.targetAchieved)

        follower.manual(powers);
        follower.update();
    }

    public void followTrajectory() {
        if (driveMode != DriveMode.FOLLOWING_TRAJECTORY) return;

        // Type code here pls
    }


    public RunCommand fieldCentricCommand() {
        return new RunCommand(this::fieldCentric, this);
    }

    public Command followPollenCommand() {
        return new CommandBase() {
            {
                addRequirements(Drivetrain.this);
            }

            @Override
            public void initialize() {
                setDriveMode(DriveMode.FOLLOWING_POLLEN);
            }

            @Override
            public void execute() {
                followPollen();
            }

            @Override
            public void end(boolean interrupted) {
                setDriveMode(DriveMode.MANUAL);
            }

            @Override
            public boolean isFinished() {
                return false;
            }
        };
    }

    public Command followTrajectoryCommand() {
        return new CommandBase() {
            {
                addRequirements(Drivetrain.this);
            }

            @Override
            public void initialize() {
                setDriveMode(DriveMode.FOLLOWING_TRAJECTORY);
            }

            @Override
            public void execute() {
                followTrajectory();
            }

            @Override
            public void end(boolean interrupted) {
                setDriveMode(DriveMode.MANUAL);
            }

            @Override
            public boolean isFinished() {
                return false;
            }
        };
    }

    public void setDriveMode(DriveMode mode) {
        driveMode = mode;
    }

    public Command robotCentricCommand() {
        return new InstantCommand(this::drive, this);
    }

    public Command setSlowModeCommand() {
        return new InstantCommand(() -> speedModifier = SLOW_SPEED, this);
    }

    public Command setFastModeCommand() {
        return new InstantCommand(() -> speedModifier = FAST_SPEED, this);
    }

    public Command turnToGoal() {
        return new DeferredCommand(
                () -> new TurnToCommand(
                        follower,
                        Math.atan2(
                                goalPose.y() - follower.pose().y(),
                                goalPose.x() - follower.pose().x()
                        )
                ),
                List.of(this)
        );
    }

    public Command goToCommand(Pose pose) {
        return new DeferredCommand(
                () -> {
                    Pose start = follower.pose();

                    return new FollowPathCommand(
                            follower,
                            line(follower.pose(), pose)
                                    .constant(pose.heading())
                    );
                },
                List.of(this)
        );
    }

    //Function for future builder of collecting paths for auto
    public Command goToTargetCommand(Pose pose){
        targetsHashSet.remove(pose);
        return new DeferredCommand(
                () -> {
                    Pose start = follower.pose();

                    return new FollowPathCommand(
                            follower,
                            line(follower.pose(), pose)
                                    .linear(follower.pose().heading(), pose.heading())
                    );
                },
                List.of(this)
        );
    }
}