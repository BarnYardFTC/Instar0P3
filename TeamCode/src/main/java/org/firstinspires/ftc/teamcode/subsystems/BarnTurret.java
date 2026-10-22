package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.general.BarnRobot;

public class BarnTurret extends SubsystemBase {

    private final Servo servo;

    private static final double GEAR_RATIO = 2.75;
    private static final double SERVO_RANGE_DEGREES = 180.0;
    private static final double MIN_ANGLE = 0.0;
    private static final double MAX_ANGLE = SERVO_RANGE_DEGREES / GEAR_RATIO;

    public BarnTurret(HardwareMap hwMap, String turretName) {
        servo = hwMap.get(Servo.class, turretName);
        setAngle(MAX_ANGLE / 2);
    }

    private void setAngle(double degrees) {
        double clampedDegrees = Math.max(MIN_ANGLE, Math.min(MAX_ANGLE, degrees));
        double position = (clampedDegrees * GEAR_RATIO) / SERVO_RANGE_DEGREES;
        servo.setPosition(position);
    }

    private double getAngle() {
        return (servo.getPosition() * SERVO_RANGE_DEGREES) / GEAR_RATIO;
    }

    private void trackPosition(Pose targetPose) {
        Pose botPose = BarnRobot.getInstance().drive.follower.pose();
        double dx = targetPose.x() - botPose.x();
        double dy = targetPose.y() - botPose.y();

        double targetAngleRad = Math.atan2(dy, dx);
        double relativeAngleRad = Math.atan2(
                Math.sin(targetAngleRad - botPose.heading()),
                Math.cos(targetAngleRad - botPose.heading())
        );

        double centerAngle = (MIN_ANGLE + MAX_ANGLE) / 2.0;
        double targetDegrees = centerAngle + Math.toDegrees(relativeAngleRad);
        setAngle(targetDegrees);
    }

    public Command setAngleCommand(double degrees) {
        return new InstantCommand(() -> setAngle(degrees), this);
    }

    public RunCommand trackPositionCommand(Pose targetPose) {
        return new RunCommand(() -> trackPosition(targetPose), this);
    }
}
