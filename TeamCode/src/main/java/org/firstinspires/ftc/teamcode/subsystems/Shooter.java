package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.general.BarnRobot;
import org.firstinspires.ftc.teamcode.opmodes.teleop.TeleopTemplate;

@Configurable
public class Shooter extends SubsystemBase {
    private static final double TICKS_PER_REVOLUTION = 28;
    private static final double DELTA_TIME = 100;

    private double kV = 0.000176, kS = 0.09, kP = 0.00145;

    public static int CLOSE_RPM = 2800, MID_RPM = 2900, FAR_RPM = 3000;

    private DcMotorEx shooterMotor;

    private double lastTime;
    private int lastPosition;
    private double rpm = 0;
    private double tgtRpm = 0;

    public enum ShooterMode {POLLEN, NECTAR}

    public enum GOAL_SIDES {RIGHT, LEFT}

    public ShooterMode shooterMode;

    public final double POLLEN_SHOOTER_ANGLE = 53.0;
    public final double NECTAR_SHOOTER_ANGLE = 47.0;

    public static final double GRAVITY = 9810.0;
    public static final double SHOOTER_HEIGHT = 153.0;
    public static final double WHEEL_RADIUS = 48.0;

    public final double RED_LEFT_GOAL_X = 1590.0;
    public final double RED_LEFT_GOAL_Y = 1463.0;

    public final double RED_RIGHT_GOAL_X = 1590.0;
    public final double RED_RIGHT_GOAL_Y = 2194.6;

    public final double BLUE_LEFT_GOAL_X = 2067.6;
    public final double BLUE_LEFT_GOAL_Y = 2194.6;

    public final double BLUE_RIGHT_GOAL_X = 2067.6;
    public final double BLUE_RIGHT_GOAL_Y = 1463.0;

    public final double GOAL_Z = 1512.6;


    public static double POLLEN_EFFICIENCY = 0.8;
    public static double NECTAR_EFFICIENCY = 1.0;

    public Shooter(HardwareMap hwMap, String motorName, ShooterMode shooterMode) {
        shooterMotor = hwMap.get(DcMotorEx.class, motorName);

        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lastTime = System.currentTimeMillis();
        lastPosition = shooterMotor.getCurrentPosition();
        this.shooterMode = shooterMode;
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
        //Function isnt working without encoder
        int currentPosition = shooterMotor.getCurrentPosition();
        double currentTime = System.currentTimeMillis();

        double deltaTime = currentTime - lastTime;
        int deltaPosition = currentPosition - lastPosition;

        if (deltaTime >= DELTA_TIME) {
            this.rpm = (deltaPosition * 60.0 * 1000.0) / (TICKS_PER_REVOLUTION * deltaTime);

            lastPosition = currentPosition;
            lastTime = currentTime;
        }
    }

    public double getRPM() {
        return this.rpm;
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

        //Requires an encoder on shooter motor
//        double error = tgtRpm - getRPM();
//        double feedback = error * kP;

        setMotorPower(Math.min(1.0, feedForward));
    }

    /**
     * Assumes robotXInches and robotYInches are passed in INCHES from Odometry/PedroPathing.
     */
    public double calculatePollenRPM(double robotXInches, double robotYInches, GOAL_SIDES goal_side, TeleopTemplate.ALLIANCE_COLOR alliance) {
        return calculateBaseRPM(robotXInches * 25.4, robotYInches * 25.4, POLLEN_EFFICIENCY, goal_side, alliance);
    }

    public double calculateNectarRPM(double robotXInches, double robotYInches, GOAL_SIDES goal_side, TeleopTemplate.ALLIANCE_COLOR alliance) {
        return calculateBaseRPM(robotXInches * 25.4, robotYInches * 25.4, NECTAR_EFFICIENCY, goal_side, alliance);
    }

    private double calculateBaseRPM(double robotXMM, double robotYMM, double pieceEfficiency, GOAL_SIDES goal_side, TeleopTemplate.ALLIANCE_COLOR alliance) {
        double GOAL_Y;
        double GOAL_X;
        double SHOOTER_ANGLE;

        if (alliance == TeleopTemplate.ALLIANCE_COLOR.RED) {
            GOAL_X = (goal_side == GOAL_SIDES.LEFT) ? RED_LEFT_GOAL_X : RED_RIGHT_GOAL_X;
            GOAL_Y = (goal_side == GOAL_SIDES.LEFT) ? RED_LEFT_GOAL_Y : RED_RIGHT_GOAL_Y;
        } else {
            GOAL_X = (goal_side == GOAL_SIDES.LEFT) ? BLUE_LEFT_GOAL_X : BLUE_RIGHT_GOAL_X;
            GOAL_Y = (goal_side == GOAL_SIDES.LEFT) ? BLUE_LEFT_GOAL_Y : BLUE_RIGHT_GOAL_Y;
        }

        SHOOTER_ANGLE = (shooterMode == ShooterMode.POLLEN) ? POLLEN_SHOOTER_ANGLE : NECTAR_SHOOTER_ANGLE;
        
        double deltaX = GOAL_X - robotXMM;
        double deltaY = GOAL_Y - robotYMM;
        double horizontalDistance = Math.hypot(deltaX, deltaY);

        double heightDifference = GOAL_Z - SHOOTER_HEIGHT;

        double angleRad = Math.toRadians(SHOOTER_ANGLE);
        double cosTheta = Math.cos(angleRad);
        double tanTheta = Math.tan(angleRad);

        double denominator = 2 * Math.pow(cosTheta, 2) * ((horizontalDistance * tanTheta) - heightDifference);

        if (denominator <= 0) {
            // Target is physically unreachable at this launch angle
            return 0.0;
        }

        double v0Squared = (GRAVITY * Math.pow(horizontalDistance, 2)) / denominator;
        double v0 = Math.sqrt(v0Squared); // Velocity in mm/s

        double rawRpm = (v0 * 60.0) / (2 * Math.PI * WHEEL_RADIUS);

        return rawRpm / pieceEfficiency;
    }

    public boolean isReady() {
        if (tgtRpm == 0) return false;
        return Math.abs(rpm - tgtRpm) < 50;
    }

    public Command setCustomRPMCommand(double rpm) {
        return new InstantCommand(() -> setTargetRPM(rpm));
    }

    public void setShooterSpeedDependsOnDist(GOAL_SIDES goal_side, TeleopTemplate.ALLIANCE_COLOR alliance) {
        double currentX = BarnRobot.getInstance().drive.follower.pose().x();
        double currentY = BarnRobot.getInstance().drive.follower.pose().y();
        if (shooterMode == ShooterMode.POLLEN) setTargetRPM(calculatePollenRPM(currentX, currentY, goal_side, alliance));
        else setTargetRPM(calculateNectarRPM(currentX, currentY, goal_side, alliance));
    }
}
