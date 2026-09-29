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
    private static final double DELTA_TIME_MS = 50; // Reduced window for faster updates

    private double kV = 0.000176, kS = 0.09, kP = 0.00145;

    public static int RPM = 2500;

    private DcMotorEx rightMotor;
    private DcMotorEx leftMotor;

    private double lastTime;
    private int lastPosition;
    private double rpm = 0;
    private double tgtRpm = 0;

    public static final double GRAVITY_MM = 9810.0;
    public static final double SHOOTER_HEIGHT_MM = 153.0;
    public static final double SHOOTER_ANGLE_DEG = 70.0;
    public static final double WHEEL_RADIUS_MM = 48.0;

    // Goal coordinates converted from inches to mm (1 inch = 25.4 mm)
    public static final double GOAL_X_MM = 47.088952654232436;
    public static final double GOAL_Y_MM = 131.3845050215208 ;
    public static final double GOAL_Z_MM = 1400.0;

    public static double POLLEN_EFFICIENCY = 0.8;
    public static double NECTAR_EFFICIENCY = 1.0;

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
        // Fixed: Read leftMotor to match updateRPM()
        lastPosition = leftMotor.getCurrentPosition();
    }

    /**
     * Runs automatically every loop tick in SolversLib/WPILib architecture.
     */
    @Override
    public void periodic() {
        updateRPM();
    }

    public void setMotorPower(double power) {
        rightMotor.setPower(power);
        leftMotor.setPower(power);
    }

    public void updateRPM() {
        int currentPosition = leftMotor.getCurrentPosition();
        double currentTime = System.currentTimeMillis();
        double deltaTime = currentTime - lastTime;

        if (deltaTime >= DELTA_TIME_MS) {
            int deltaPosition = currentPosition - lastPosition;
            rpm = (deltaPosition * 60.0 * 1000.0) / (TICKS_PER_REVOLUTION * deltaTime);

            lastPosition = currentPosition;
            lastTime = currentTime;
        }
    }

    public double getRPM() {
        return rpm;
    }

    public void setMotorRPM(double rpmTarget) {
        this.tgtRpm = rpmTarget;

        if (rpmTarget <= 0) {
            setMotorPower(0);
            return;
        }

        double feedForward = kV * rpmTarget + kS;
        double error = rpmTarget - getRPM();
        double feedback = error * kP;

        setMotorPower(feedForward + feedback);
    }

    /**
     * Assumes robotXInches and robotYInches are passed in INCHES from Odometry/PedroPathing.
     */
    public static double calculatePollenRPM(double robotXInches, double robotYInches) {
        return calculateBaseRPM(robotXInches * 25.4, robotYInches * 25.4, POLLEN_EFFICIENCY);
    }

    public static double calculateNectarRPM(double robotXInches, double robotYInches) {
        return calculateBaseRPM(robotXInches * 25.4, robotYInches * 25.4, NECTAR_EFFICIENCY);
    }

    private static double calculateBaseRPM(double robotXMM, double robotYMM, double pieceEfficiency) {
        double deltaX = GOAL_X_MM - robotXMM;
        double deltaY = GOAL_Y_MM - robotYMM;
        double horizontalDistance = Math.hypot(deltaX, deltaY);

        double heightDifference = GOAL_Z_MM - SHOOTER_HEIGHT_MM;

        double angleRad = Math.toRadians(SHOOTER_ANGLE_DEG);
        double cosTheta = Math.cos(angleRad);
        double tanTheta = Math.tan(angleRad);

        double denominator = 2 * Math.pow(cosTheta, 2) * ((horizontalDistance * tanTheta) - heightDifference);

        if (denominator <= 0) {
            // Target is physically unreachable at this launch angle
            return 0.0;
        }

        double v0Squared = (GRAVITY_MM * Math.pow(horizontalDistance, 2)) / denominator;
        double v0 = Math.sqrt(v0Squared); // Velocity in mm/s

        double rawRpm = (v0 * 60.0) / (2 * Math.PI * WHEEL_RADIUS_MM);

        return rawRpm / pieceEfficiency;
    }

    public boolean isReady() {
        if (tgtRpm == 0) return false;
        return Math.abs(rpm - tgtRpm) < 50;
    }

    public void setShooterSpeed() {
        setMotorRPM(RPM);
    }

    public void setShooterSpeedDependsOnDist() {
        double currentX = BarnRobot.getInstance().drive.follower.pose().x();
        double currentY = BarnRobot.getInstance().drive.follower.pose().y();
        setMotorRPM(calculatePollenRPM(currentX, currentY));
    }

    public RunCommand operateShooter() {
        return new RunCommand(this::setShooterSpeed, this);
    }

    public RunCommand operateShooterDependsOnDist() {
        return new RunCommand(this::setShooterSpeedDependsOnDist, this);
    }

    public RunCommand turnOff() {
        return new RunCommand(() -> setMotorRPM(0), this);
    }
}