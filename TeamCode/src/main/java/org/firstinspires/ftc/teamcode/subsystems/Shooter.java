package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.general.BarnRobot;

@Configurable
public class Shooter extends SubsystemBase {
    private static final double TICKS_PER_REVOLUTION = 28;
    private static final int RPM_MAX = 3030;

    private static final double DELTA_TIME = 100;

    private double kV = 0.000176, kS = 0.09, kP = 0.00145;

    public static int RPM = 1500 ;

    private DcMotorEx rightMotor;
    private DcMotorEx leftMotor;

    private double lastTime;

    private int lastPosition;
    private double rpm;

    private double tgtRpm;

    public Shooter() {

        rightMotor = BarnRobot.getInstance().hardware.shooterMotorRight;
        leftMotor = BarnRobot.getInstance().hardware.shooterMotorLeft;

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lastTime = System.currentTimeMillis();
        lastPosition = rightMotor.getCurrentPosition();
        rpm = 0;
    }

    public void setMotorPower(double power) {
        rightMotor.setPower(power);
        leftMotor.setPower(power);
    }

    public void updateRPM() {
        int currentPosition = leftMotor.getCurrentPosition();
        double currentTime = System.currentTimeMillis();

        double deltaTime = currentTime - lastTime;
        int deltaPosition = currentPosition - lastPosition;

        if(deltaTime >= DELTA_TIME) {
            rpm = (deltaPosition * 60 * 1000) / (TICKS_PER_REVOLUTION * deltaTime);

            lastPosition = currentPosition;
            lastTime = currentTime;
        }
    }

    public double getRPM(){
        return rpm;
    }

    public void setMotorRPM(int rpm) {
        // call it every loop pls
        tgtRpm = rpm;

        double feedForward = kV * rpm + kS;
        double error = rpm - getRPM();
        double feedback = error * kP;

        setMotorPower(feedForward + feedback);
    }

    public boolean isReady() {
        if (tgtRpm == 0) return false;
        return (rpm > tgtRpm - 50 && rpm < tgtRpm + 50);
    }

    public void setShooterSpeed(){
        setMotorRPM(RPM);
        updateRPM();
    }

    public RunCommand operateShooter() {
        return new RunCommand(() -> setShooterSpeed(), this);
    }

    public RunCommand turnOff() {
        return new RunCommand(() -> setMotorPower(0), this);
    }

}
