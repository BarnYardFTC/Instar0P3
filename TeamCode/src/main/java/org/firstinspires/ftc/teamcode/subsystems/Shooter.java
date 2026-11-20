package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

@Configurable
public class Shooter extends SubsystemBase {
    private static final double TICKS_PER_REVOLUTION = 28;
    private static final double DELTA_TIME = 100;

    private double kV = 0.000176, kS = 0.09, kP = 0.00145;

    public static int CLOSE_RPM = 3300, MID_RPM = 3500, FAR_RPM = 3700;

    private DcMotorEx shooterMotor;

    private double lastTime;
    private int lastPosition;
    private double rpm = 0;
    public static double tgtRpm = 0;

    public Shooter(HardwareMap hwMap, String motorName, boolean shooterMode) {
        shooterMotor = hwMap.get(DcMotorEx.class, motorName);

        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lastTime = System.currentTimeMillis();
        lastPosition = shooterMotor.getCurrentPosition();
    }

    @Override
    public void periodic() {
        updateRPM();
        updateMotorPower();
    }

    public void setMotorPower(double power) {
        shooterMotor.setPower(power);
    }

    public void updateRPM() {
        int currentPosition = shooterMotor.getCurrentPosition();
        double currentTime = System.currentTimeMillis();

        double deltaTime = currentTime - lastTime;
        int deltaPosition = currentPosition - lastPosition;

        if (deltaTime >= DELTA_TIME) {
            rpm = (deltaPosition * 60.0 * 1000.0) / (TICKS_PER_REVOLUTION * deltaTime);

            lastPosition = currentPosition;
            lastTime = currentTime;
        }
    }

    public double getRPM() {
        return tgtRpm;
    }

    public double getTargetRPM() {
        return tgtRpm;
    }

    public void setTargetRPM(double rpmTarget) {
        this.tgtRpm = rpmTarget;
    }

    private void updateMotorPower() {
        if (tgtRpm <= 0) {
            setMotorPower(0);
            return;
        }

        double feedForward = kV * tgtRpm + kS;
        double error = tgtRpm - getRPM();
        double feedback = error * kP;

        setMotorPower(feedForward + feedback);
    }

    public boolean isReady() {
        if (tgtRpm == 0) return false;
        return (rpm > tgtRpm - 50 && rpm < tgtRpm + 50);
    }

    public Command setCustomRPMCommand(double rpm) {
        return new InstantCommand(() -> setTargetRPM(rpm));
    }

    public Command turnOff() {
        return new InstantCommand(() -> setTargetRPM(0));
    }
}
