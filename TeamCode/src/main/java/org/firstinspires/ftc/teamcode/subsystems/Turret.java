package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Turret extends SubsystemBase {

    CRServoEx servo;

    // nominal is basically a simulated encoder, we rely on it for setting power
    private double nominal = 180; // number is assumed from real turret starting direction
    private double tgt = nominal;
    private double lastRawAngle;
    private double currentRawAngle;
    private final double GEAR_RATIO = 2.75;


    private boolean isInitialized = false;
    private final double MAX_TICK_DELTA = 45.0;

    public Turret(HardwareMap hardwareMap, String servoName, String encoderName) {
        servo = new CRServoEx(hardwareMap, servoName, encoderName, 3.3, AngleUnit.DEGREES, CRServoEx.RunMode.RawPower);
        currentRawAngle = servo.getAbsoluteEncoder().getCurrentPosition();
        lastRawAngle = currentRawAngle;
    }

    @Override
    public void periodic() {
//        currentRawAngle = servo.getAbsoluteEncoder().getCurrentPosition();
//
//        double delta = currentRawAngle - lastRawAngle;
//
//        // error
//        if (delta > 180) {
//            delta -= 360;
//        } else if (delta < -180) {
//            delta += 360;
//        }
//
//
//
//        nominal += delta / GEAR_RATIO;
//        lastRawAngle = currentRawAngle;
//
//        if (tgt > nominal - 3 && tgt < nominal + 3) {
//            servo.set(0);
//        } else if (tgt > nominal) {
//            servo.set(0.1);
//        } else if (tgt < nominal) {
//            servo.set(-0.1);
//        }
        currentRawAngle = servo.getAbsoluteEncoder().getCurrentPosition();

        // 1. Guard against startup hardware garbage on frame 1
        if (!isInitialized) {
            lastRawAngle = currentRawAngle;
            isInitialized = true;
            return;
        }

        // 2. Trigonometric unwrapping: smoothly maps delta to [-180, 180] with zero floating-point edge cases
        double deltaRad = Math.toRadians(currentRawAngle - lastRawAngle);
        double delta = Math.toDegrees(Math.atan2(Math.sin(deltaRad), Math.cos(deltaRad)));

        // 3. Glitch & Slip Rejection: drop impossible single-tick jumps (>45° in 20ms) to protect nominal
        if (Math.abs(delta) < MAX_TICK_DELTA) {
            nominal += delta / GEAR_RATIO;
        }
        lastRawAngle = currentRawAngle;

        // 4. Smooth proportional deceleration near target to stop mechanical shaft slip
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
    }

    private void addTgt(double angle){
        tgt += angle;
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

    public Command setTgtCommand (double angle){
        return new InstantCommand(() -> setTgt(angle), this);
    }

    public Command addTgtCommand (double angle){
        return new InstantCommand(() -> addTgt(angle), this);
    }
}