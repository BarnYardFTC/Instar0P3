package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.general.BarnRobot;

public class Turret extends SubsystemBase {

    CRServoEx servo;

    // nominal is basically a simulated encoder, we rely on it for setting power
    private double nominal = 0; // number is assumed from real turret starting direction
    private double tgt = nominal;
    private double lastRawAngle;
    private double currentRawAngle;
    private final double GEAR_RATIO = 2.75;

    private Pose tgtPose;
    private Pose robotPose;
    private boolean isTracking = false;

    private boolean isInitialized = false;
    private final double MAX_TICK_DELTA = 45.0;

    public Turret(HardwareMap hardwareMap, String servoName, String encoderName) {
        servo = new CRServoEx(hardwareMap, servoName, encoderName, 3.3, AngleUnit.DEGREES, CRServoEx.RunMode.RawPower);
        currentRawAngle = servo.getAbsoluteEncoder().getCurrentPosition();
        lastRawAngle = currentRawAngle;
    }

    @Override
    public void periodic() {

        // dont trust this yet (this is tracking)
        if (isTracking) {
//          robotPose = BarnRobot.getInstance().drive.follower.pose();
            robotPose = new Pose(72, 72, 0);
            double dx = tgtPose.x() - robotPose.x();
            double dy = tgtPose.y() - robotPose.y();
            double targetFieldAngleRad = Math.atan2(dy, dx);
            double targetRobotAngleRad = targetFieldAngleRad - robotPose.heading();
            double targetAngleDeg = Math.toDegrees(targetRobotAngleRad);

            double diffDeg = targetAngleDeg - nominal;
            double shortestDeltaDeg = Math.toDegrees(Math.atan2(Math.sin(Math.toRadians(diffDeg)), Math.cos(Math.toRadians(diffDeg))));
            tgt = nominal + shortestDeltaDeg;
        }

        currentRawAngle = servo.getAbsoluteEncoder().getCurrentPosition();

        // guard against startup hardware garbage on frame 1
        if (!isInitialized) {
            lastRawAngle = currentRawAngle;
            isInitialized = true;
            return;
        }

        // trigonometric unwrapping: smoothly maps delta to [-180, 180] with zero floating-point edge cases
        double deltaRad = Math.toRadians(currentRawAngle - lastRawAngle);
        double delta = Math.toDegrees(Math.atan2(Math.sin(deltaRad), Math.cos(deltaRad)));

        // glitch & Slip Rejection: drop impossible single-tick jumps (>45° in 20ms) to protect nominal
        if (Math.abs(delta) < MAX_TICK_DELTA) {
            nominal += delta / GEAR_RATIO;
        }
        lastRawAngle = currentRawAngle;

        // smooth proportional deceleration near target to stop mechanical shaft slip
        double error = tgt - nominal;
        if (Math.abs(error) < 3.0) {
            servo.set(0);
        } else {
            double power = Math.signum(error) * Math.min(0.1, Math.max(0.03, Math.abs(error) * 0.01));
            servo.set(power);
        }
    }

    private void setTgt ( double angle){
        tgt = angle;
        isTracking = false;

    }

    private void addTgt(double angle){
        tgt += angle;
        isTracking = false;
    }

    private void setTgtPose(Pose pose){
        tgtPose = pose;
        isTracking = true;
    }

    public double getNominalAngle(){
        return nominal;
    }

    public double getAbsoluteAngle(){
        return currentRawAngle;
    }

    public double getLastRawAngle(){
        return lastRawAngle;
    }

    public double getTgt() {
        return tgt;
    }

    public Command setTgtCommand (double angle){
        return new InstantCommand(() -> setTgt(angle), this);
    }

    public Command addTgtCommand (double angle){
        return new InstantCommand(() -> addTgt(angle), this);
    }

    public Command trackPositionCommand(Pose pose) {
        return new InstantCommand(() -> setTgtPose(pose), this);
    }
}