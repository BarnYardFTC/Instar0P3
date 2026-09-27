package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.general.BarnRobot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class Drivetrain extends SubsystemBase {
    private final DcMotor leftFront;
    private final DcMotor rightFront;
    private final DcMotor leftBack;
    private final DcMotor rightBack;

    public Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    DrivePowers powers;
    private final double SLOW_SPEED = 0.3;
    private final double FAST_SPEED = 1.0;
    private double speedModifier;

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

    private void fieldCentric() {
        powers = ManualDrive.fieldCentric(
                -BarnRobot.getInstance().gamepadEx1.getLeftY() * speedModifier,
                BarnRobot.getInstance().gamepadEx1.getLeftX() * speedModifier,
                BarnRobot.getInstance().gamepadEx1.getRightX() * speedModifier * 0.7,
                follower.pose().heading()
        );
        follower.manual(powers);
        follower.update();
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
}
