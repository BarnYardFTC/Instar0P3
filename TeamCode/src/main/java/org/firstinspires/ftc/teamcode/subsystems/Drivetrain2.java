package org.firstinspires.ftc.teamcode.subsystems;//package org.firstinspires.ftc.teamcode.subsystems;
//
//import com.pedropathing.control.PIDFCoefficients;
//import com.pedropathing.control.PIDFController;
//import com.pedropathing.follower.Follower;
//import com.pedropathing.geometry.BezierLine;
//import com.pedropathing.geometry.Pose;
//import com.pedropathing.math.MathFunctions;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.seattlesolvers.solverslib.command.Command;
//import com.seattlesolvers.solverslib.command.InstantCommand;
//import com.seattlesolvers.solverslib.command.RunCommand;
//import com.seattlesolvers.solverslib.command.SubsystemBase;
//import com.seattlesolvers.solverslib.gamepad.GamepadEx;
//import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;
//
//import org.firstinspires.ftc.teamcode.general.BarnRobot;
//
//public class Drivetrain extends SubsystemBase {
//    private final DcMotor leftFront;
//    private final DcMotor rightFront;
//    private final DcMotor leftBack;
//    private final DcMotor rightBack;
//
//    public Follower follower;
//
//    private double speedModifier;
//    private final double SLOW_SPEED = 0.3;
//    private final double FAST_SPEED = 1.0;
//
//    private Double targetAngleDifference = null;
//    private final PIDFController trackingPIDF;
//    private final PIDFController secondaryTrackingPIDF;
//
//    private static Pose passPose = new Pose(0,0,0);
//
//    public Drivetrain(OpMode opMode) {
//        speedModifier = FAST_SPEED;
//        leftFront = BarnRobot.getInstance().hardware.leftFrontDrivetrain;
//        rightFront = BarnRobot.getInstance().hardware.rightFrontDrivetrain;
//        leftBack = BarnRobot.getInstance().hardware.leftBackDrivetrain;
//        rightBack = BarnRobot.getInstance().hardware.rightBackDrivetrain;
//
//        initMotor(DcMotorSimple.Direction.REVERSE, leftFront);
//        initMotor(DcMotorSimple.Direction.FORWARD, rightFront);
//        initMotor(DcMotorSimple.Direction.REVERSE, leftBack);
//        initMotor(DcMotorSimple.Direction.FORWARD, rightBack);
//
//        follower = Constants.createFollower(opMode.hardwareMap);
//        trackingPIDF = new PIDFController(Constants.followerConstants.coefficientsHeadingPIDF);
//        secondaryTrackingPIDF = new PIDFController(Constants.followerConstants.coefficientsSecondaryHeadingPIDF);
//    }
//
//    private void initMotor(DcMotorSimple.Direction direction, DcMotor motor) {
//        motor.setDirection(direction);
//        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//    }
//
//    private void drive() {
//        GamepadEx gamepadEx = BarnRobot.getInstance().gamepadEx1;
//        double lf_pw = gamepadEx.getLeftY() - gamepadEx.getLeftX() + gamepadEx.getRightX();
//        double rf_pw = gamepadEx.getLeftY() + gamepadEx.getLeftX() - gamepadEx.getRightX();
//        double lb_pw = gamepadEx.getLeftY() + gamepadEx.getLeftX() + gamepadEx.getRightX();
//        double rb_pw = gamepadEx.getLeftY() - gamepadEx.getLeftX() - gamepadEx.getRightX();
//        leftFront.setPower(lf_pw * speedModifier);
//        rightFront.setPower(rf_pw * speedModifier);
//        leftBack.setPower(lb_pw * speedModifier);
//        rightBack.setPower(rb_pw * speedModifier);
//    }
//
//    public RunCommand driveCommand() {
//        return new RunCommand(this::drive, this);
//    }
//
//     //PEDRO PATHING DRIVE SECTION
//     //This uses the Follower to drive and includes auto-alignment logic.
//    private void driveFollower() {
//        GamepadEx gp = BarnRobot.getInstance().gamepadEx1;
//        double x = gp.getLeftY() * speedModifier;
//        double y = -gp.getLeftX() * speedModifier;
//        double stickTurn = -gp.getRightX() * speedModifier * 0.7;
//
//        double turn;
//
//        // Auto-align logic: calculate heading if tracking is active and driver isn't turning
//        if (targetAngleDifference != null && Math.abs(stickTurn) < 0.1) {
//            turn = calculateAutoAlignTurn();
//        } else {
//            // Manual override: Clear tracking if the driver touches the right stick
//            if (targetAngleDifference != null) {
//                targetAngleDifference = null;
//                // Restore original follower coefficients when manual control resumes
//                follower.setHeadingPIDFCoefficients(Constants.followerConstants.coefficientsHeadingPIDF);
//            }
//            turn = stickTurn;
//        }
//
//        // Ensure follower is awake in Teleop mode if sticks are moved or tracking is active
//        if (!follower.getTeleopDrive() && (BarnRobot.getInstance().sticksUsed() || targetAngleDifference != null)) {
//            follower.startTeleopDrive(true);
//        }
//
//        try {
//            follower.setTeleOpDrive(x, y, turn, false);
//        } catch (Exception e) {
//            BarnRobot.getInstance().telemetry.addData("failed to set teleop", e);
//        }
//    }
//
//    private double calculateAutoAlignTurn() {
//        Pose currentPose = follower.getPose();
//        double headingError = MathFunctions.normalizeAngleSigned(targetAngleDifference);
//
//        // We zero out internal PID so it doesn't conflict with our manual control input
//        follower.setHeadingPIDFCoefficients(new PIDFCoefficients(0, 0, 0, 0));
//
//        // Switch between primary and aggressive secondary PID depending on error size
//        if (Math.abs(headingError) < Constants.followerConstants.headingPIDFSwitch && Constants.followerConstants.useSecondaryHeadingPIDF) {
//            secondaryTrackingPIDF.updateError(headingError);
//            secondaryTrackingPIDF.updateFeedForwardInput(MathFunctions.getTurnDirection(currentPose.getHeading(), currentPose.getHeading() + targetAngleDifference));
//            return secondaryTrackingPIDF.run();
//        } else {
//            trackingPIDF.updateError(headingError);
//            trackingPIDF.updateFeedForwardInput(MathFunctions.getTurnDirection(currentPose.getHeading(), currentPose.getHeading() + targetAngleDifference));
//            return trackingPIDF.run();
//        }
//    }
//
//    private void driveFieldOriented() {
//        GamepadEx gp = BarnRobot.getInstance().gamepadEx1;
//        double x = gp.getLeftY() * speedModifier;
//        double y = -gp.getLeftX() * speedModifier;
//        double turn = -gp.getRightX() * speedModifier * 0.7;
//
//        if (!follower.getTeleopDrive() && BarnRobot.getInstance().sticksUsed()) {
//            follower.startTeleopDrive(true);
//        }
//
//        try {
//            follower.setTeleOpDrive(x, y, turn, false);
//        } catch (Exception e) {
//            BarnRobot.getInstance().telemetry.addData("failed to set field oriented teleop", e);
//        }
//    }
//
//    public static void setPassPose(Pose pose){
//        passPose = pose;
//    }
//
//    public static Pose getPassPose() {
//        return passPose;
//    }
//
//    public Command goToCommand(Pose pose) {
//        return new FollowPathCommand(
//                follower,
//                follower.pathBuilder()
//                        .addPath(new BezierLine(follower.getPose(), pose))
//                        .setLinearHeadingInterpolation(follower.getHeading(), pose.getHeading())
//                        .build()
//        );
//    }
//
//    public RunCommand driveFollowerCommand() {
//        return new RunCommand(this::driveFollower, this);
//    }
//
//    public RunCommand driveFieldOrientedCommand() {
//        return new RunCommand(this::driveFieldOriented, this);
//    }
//
//    public Command setTargetAngleDifferenceCommand(double angleDiff) {
//        return new InstantCommand(() -> targetAngleDifference = angleDiff, this);
//    }
//
//    public Command clearTargetAngleDifferenceCommand() {
//        return new InstantCommand(() -> targetAngleDifference = null, this);
//    }
//
//
////    public Command updateLimelightDifferenceCommand() {
////        return new RunCommand(() -> {
////            Limelight9 limelight = BarnRobot.getInstance().limelight;
////            if (limelight != null && limelight.hasValidTarget()) {
////                targetAngleDifference = Math.toRadians(limelight.getTx());
////            }
////        }, this);
////    }
//
//    public double getDifference(){
//        return targetAngleDifference;
//    }
//
//    public Command setSlowModeCommand() {
//        return new InstantCommand(() -> speedModifier = SLOW_SPEED, this);
//    }
//
//    public Command setFastModeCommand() {
//        return new InstantCommand(() -> speedModifier = FAST_SPEED, this);
//    }
//}