package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.general.BarnRobot;

public class Turret extends SubsystemBase { //TODO: parallax correction -> if needed <-

    private final CRServoEx servo;

    private static final double GEAR_RATIO = 2.75;
    private static final double MIN_ANGLE = 10;
    private static final double MAX_ANGLE = 350;

    public Turret(HardwareMap hwMap, String turretName, String encoderName) {
        servo = new CRServoEx(hwMap, turretName, encoderName, 3.3, AngleUnit.DEGREES, CRServoEx.RunMode.OptimizedPositionalControl);

        servo.setPIDF(new PIDFCoefficients(0.01, 0.0, 0.001, 0.0));
        setAngle(180);
    }

    public void setAngle(double degrees) {
        double clampedDegrees = Math.max(MIN_ANGLE, Math.min(MAX_ANGLE, degrees));
        int targetPosition = (int) Math.round(clampedDegrees * GEAR_RATIO);
        servo.setTargetPosition(targetPosition);
    }

    public void trackPosition(Pose targetPose) {
        Pose botPose = BarnRobot.getInstance().drive.follower.pose();
        double dx = targetPose.x() - botPose.x();
        double dy = targetPose.y() - botPose.y();

        double targetAngleRad = Math.atan2(dy, dx);
        double relativeAngleRad = Math.atan2(
                Math.sin(targetAngleRad - botPose.heading()),
                Math.cos(targetAngleRad - botPose.heading())
        );

        double targetDegrees = 180.0 + Math.toDegrees(relativeAngleRad);
        setAngle(targetDegrees);
    }

    public Command setAngleCommand(double degrees) {
        return new InstantCommand(() -> setAngle(degrees), this);
    }

    public RunCommand trackPositionCommand(Pose targetPose) {
        return new RunCommand(() -> trackPosition(targetPose), this);
    }
}