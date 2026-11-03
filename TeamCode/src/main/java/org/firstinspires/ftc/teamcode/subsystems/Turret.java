package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.general.BarnRobot;
public class Turret extends SubsystemBase {

    private final CRServoEx servo;
    public static double targetAngleDegrees = 180.0;
    private static final double GEAR_RATIO = 2.75;
    private static final double MIN_ANGLE = 0.0;
    private static final double MAX_ANGLE = 360.0;

    public static double kP = 0.01, kD = 0.0001;
    private double lastRawEncoderAngle = 0.0;
    private double totalServoDegrees = 180.0 * GEAR_RATIO;
    private boolean initialized = false;

    private double lastError = 0.0;
    private final ElapsedTime timer = new ElapsedTime();

    public Turret(HardwareMap hwMap, String turretName, String encoderName) {
        servo = new CRServoEx(hwMap, turretName, encoderName, 3.3, AngleUnit.DEGREES, CRServoEx.RunMode.RawPower);
    }

    private void setAngle(double degrees) {
        targetAngleDegrees = Math.max(MIN_ANGLE, Math.min(MAX_ANGLE, degrees));
    }

    private static final double CENTER_RAW_ENCODER = 270.0;

    private void updateMultiTurnEncoder() {
        double currentRaw = servo.getAbsoluteEncoder().getCurrentPosition();

        if (!initialized) {
            lastRawEncoderAngle = currentRaw;

            double offsetFromCenter = currentRaw - CENTER_RAW_ENCODER;
            if (offsetFromCenter > 180.0) {
                offsetFromCenter -= 360.0;
            } else if (offsetFromCenter < -180.0) {
                offsetFromCenter += 360.0;
            }
            totalServoDegrees = (180.0 * GEAR_RATIO) + offsetFromCenter;

            timer.reset();
            initialized = true;
            return;
        }

        double delta = currentRaw - lastRawEncoderAngle;
        if (delta > 180.0) {
            delta -= 360.0;
        } else if (delta < -180.0) {
            delta += 360.0;
        }

        if (Math.abs(delta) > 90.0) {
            return;
        }

        totalServoDegrees += delta;
        lastRawEncoderAngle = currentRaw;
    }

    public double getAngle() {
        return totalServoDegrees / GEAR_RATIO;
    }

    @Override
    public void periodic() {
        updateMultiTurnEncoder();

        double targetServoDegrees = targetAngleDegrees * GEAR_RATIO;
        double error = targetServoDegrees - totalServoDegrees;

        double dt = timer.seconds();
        timer.reset();
        double derivative = (dt > 0.0001) ? (error - lastError) / dt : 0.0;
        lastError = error;

        double power = Range.clip((error * kP) + (derivative * kD), -1.0, 1.0);

        servo.set(power);
    }

    public Command setAngleCommand(double degrees) {
        return new InstantCommand(() -> setAngle(degrees), this);
    }

    public Command adjustAngleCommand(double deltaDegrees) {
        return new InstantCommand(() -> setAngle(targetAngleDegrees + deltaDegrees), this);
    }
}