package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.DeferredCommand;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;
import com.seattlesolvers.solverslib.pedroCommand.TurnToCommand;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.firstinspires.ftc.teamcode.general.BarnRobot;
import org.firstinspires.ftc.teamcode.general.Constants;

public class Drivetrain extends SubsystemBase {

    // ==========================================
    // Constants
    // ==========================================

    private static final double SLOW_SPEED = 0.3;
    private static final double FAST_SPEED = 1.0;

    // Default target goal position on the field
    private final Pose goalPose = new Pose(47, 130, 0);

    // ==========================================
    // Hardware & Subsystems
    // ==========================================

    private final DcMotor leftFront;
    private final DcMotor rightFront;
    private final DcMotor leftBack;
    private final DcMotor rightBack;

    public final Follower follower;

    public final Limelight limelight = BarnRobot.getInstance().limelight;

    // ==========================================
    // State Variables
    // ==========================================

    public double speedModifier = FAST_SPEED;
    private DrivePowers powers;

    // Target collection tracking
    public Set<Pose> targetsHashSet = new LinkedHashSet<>();
    public List<Pose> targetsList = new ArrayList<>(targetsHashSet);

    // ==========================================
    // Constructor & Initialization
    // ==========================================

    public Drivetrain(HardwareMap hwMap, String leftFrontMotorName, String rightFrontMotorName,
                      String leftBackMotorName, String rightBackMotorName) {
        leftFront = hwMap.get(DcMotor.class, leftFrontMotorName);
        rightFront = hwMap.get(DcMotor.class, rightFrontMotorName);
        leftBack = hwMap.get(DcMotor.class, leftBackMotorName);
        rightBack = hwMap.get(DcMotor.class, rightBackMotorName);

        initMotor(DcMotorSimple.Direction.REVERSE, leftFront);
        initMotor(DcMotorSimple.Direction.FORWARD, rightFront);
        initMotor(DcMotorSimple.Direction.REVERSE, leftBack);
        initMotor(DcMotorSimple.Direction.FORWARD, rightBack);

        follower = Constants.create(hwMap);
    }

    private void initMotor(DcMotorSimple.Direction direction, DcMotor motor) {
        motor.setDirection(direction);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // ==========================================
    // Drive Methods
    // ==========================================

    public void fieldCentric() {
        GamepadEx gamepad = BarnRobot.getInstance().gamepadEx1;
        powers = new DrivePowers(
                gamepad.getLeftY() * speedModifier,
                -gamepad.getLeftX() * speedModifier,
                -gamepad.getRightX() * speedModifier * 0.7
        );
        follower.manual(powers);
        follower.update();
    }

    private void robotCentric() {
        GamepadEx gamepad = BarnRobot.getInstance().gamepadEx1;
        double leftY = gamepad.getLeftY();
        double leftX = gamepad.getLeftX();
        double rightX = gamepad.getRightX();

        double lfPower = leftY - leftX + rightX;
        double rfPower = leftY + leftX - rightX;
        double lbPower = leftY + leftX + rightX;
        double rbPower = leftY - leftX - rightX;

        leftFront.setPower(lfPower * speedModifier);
        rightFront.setPower(rfPower * speedModifier);
        leftBack.setPower(lbPower * speedModifier);
        rightBack.setPower(rbPower * speedModifier);
    }

    public void followPollen() {
//        Pose currentPose = follower.pose();
//        boolean inTargetZone = currentPose.x() + 5 > 72 && currentPose.heading() < 65 && currentPose.heading() > -65;
//        if (limelight == null || !limelight.hasValidTarget()){
//            powers = new DrivePowers(0, 0, 0.3);
//            BarnRobot.getInstance().telemetry.addData("Limelight", "NO TARGET");
//        } else {
//            double tx = limelight.getTx();
//            double turn = 0.0;
//            double forward = 0.25;
//            if (Math.abs(tx) > 1.2) {
//                turn = Math.max(-0.15, Math.min(0.15, tx * -0.006));
//            }
//            powers = new DrivePowers(forward, 0, turn);
//            BarnRobot.getInstance().telemetry.addData("Limelight", "TRACKING | tx: %.2f | turn: %.3f", tx, turn);
//        }

        if (!BarnRobot.getInstance().limelight.hasValidTarget()) powers = new DrivePowers(0, 0, 0.3);
        else {
            double tx = BarnRobot.getInstance().limelight.getTx();
            double turn = 0.0;
            double forward = 0.2;
            if (Math.abs(tx) > 1.2) {
                turn = tx * -0.015;
                turn = Math.max(-0.35, Math.min(0.35, turn));
            }
            powers = new DrivePowers(forward, 0, -turn);
        }
        follower.manual(powers);
        follower.update();
    }

     
//      Limelight limelight = BarnRobot.getInstance().limelight;
//        Pose currentPose = follower.pose();
//        double turn = 0.0;
//
//        boolean inTargetZone = currentPose.x() + 5 > 72 && currentPose.heading() < 65 && currentPose.heading() > -65;
//
//        if (limelight == null || !limelight.hasValidTarget()) {
//            double tx = limelight.getTx();
//            if (Math.abs(turn) < 0.1 && Math.abs(tx) > 0.5) {
//                turn = Math.max(-0.25, Math.min(0.25, -tx * 0.012));
//            }
//            powers = new DrivePowers(0.5, 0, turn);
//        }else {
//            powers = new DrivePowers(0, 0, 0.3);
//        }
//        follower.manual(powers);
//        follower.update();
//    }

//        if (limelight == null || !limelight.hasValidTarget() || inTargetZone) {
//            powers = new DrivePowers(0, 0, 0.2);
//            follower.manual(powers);
//            follower.update();
//            return;
//        }
//
//        List<LLResultTypes.ColorResult> colorResults = limelight.getLatestResult() != null ? limelight.getLatestResult().getColorResults() : null;
//
//        if (colorResults == null || colorResults.isEmpty()) {
//            powers = new DrivePowers(0, 0, 0.2);
//            follower.manual(powers);
//            follower.update();
//            return;
//        }

    // ==========================================
    // Command Factories
    // ==========================================

    public RunCommand fieldCentricCommand() {
        return new RunCommand(this::fieldCentric, this);
    }

    public RunCommand followPollenCommand() {
        return new RunCommand(this::followPollen, this);
    }

    public Command robotCentricCommand() {
        return new RunCommand(this::robotCentric, this);
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
                () -> new FollowPathCommand(
                        follower,
                        line(follower.pose(), pose).constant(pose.heading())
                ),
                List.of(this)
        );
    }

    public Command goToTargetCommand(Pose pose) {
        targetsHashSet.remove(pose);
        return new DeferredCommand(
                () -> new FollowPathCommand(
                        follower,
                        line(follower.pose(), pose).linear(follower.pose().heading(), pose.heading())
                ),
                List.of(this)
        );
    }
}