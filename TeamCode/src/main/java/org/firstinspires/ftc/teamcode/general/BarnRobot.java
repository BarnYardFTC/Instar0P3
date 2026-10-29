package org.firstinspires.ftc.teamcode.general;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.BarnTurret;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Gate;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;
import org.firstinspires.ftc.teamcode.subsystems.Pinpoint;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Transfer;
import org.firstinspires.ftc.teamcode.subsystems.Turret;

public class BarnRobot {
    private static BarnRobot instance;
    public Telemetry telemetry;
    public GamepadEx gamepadEx1;

    public Drivetrain drive;
    public Intake intake;
    public Transfer transfer;
    public Gate frontGate;
    public Gate backGate;
    public Shooter frontShooter;
    public Shooter backShooter;
    public Turret frontTurret;
    public Turret backTurret;
    public BarnTurret barnFrontTurret;
    public BarnTurret barnBackTurret;
    public Pinpoint pinpoint;
    public Limelight limelight;

    public enum HARDWARE {
        DRIVETRAIN_LB("leftBackDrivetrain"),
        DRIVETRAIN_LF("leftFrontDrivetrain"),
        DRIVETRAIN_RB("rightBackDrivetrain"),
        DRIVETRAIN_RF("rightFrontDrivetrain"),
        PINPOINT("pinpoint"),
        LIMELIGHT("limelight"),
        INTAKE("intake"),
        TRANSFER("transfer"),
        FRONT_GATE("frontGate"),
        BACK_GATE("backGate"),
        FRONT_TURRET("frontTurret"),
        BACK_TURRET("backTurret"),
        FRONT_TURRET_ENCODER("frontTurretEncoder"),
        BACK_TURRET_ENCODER("backTurretEncoder"),
        SHOOTER_FRONT("shooterMotorFront"),
        SHOOTER_BACK("shooterMotorBack");

        public final String key;

        HARDWARE(String key) {
            this.key = key;
        }
    }
    public static boolean isRobotInitialized = false;

    public static synchronized BarnRobot getInstance() {
        if (instance == null) {
            instance = new BarnRobot();
            isRobotInitialized = true;
        }
        return instance;
    }

    public void init(OpMode opMode) {
        gamepadEx1 = new GamepadEx(opMode.gamepad1);
        telemetry = opMode.telemetry;

        pinpoint = new Pinpoint(opMode.hardwareMap, HARDWARE.PINPOINT.key);
        limelight = new Limelight(opMode.hardwareMap, HARDWARE.LIMELIGHT.key);
        drive = new Drivetrain(
                opMode.hardwareMap,
                HARDWARE.DRIVETRAIN_LF.key,
                HARDWARE.DRIVETRAIN_RF.key,
                HARDWARE.DRIVETRAIN_LB.key,
                HARDWARE.DRIVETRAIN_RB.key
        );
        intake = new Intake(opMode.hardwareMap, HARDWARE.INTAKE.key);
        transfer = new Transfer(opMode.hardwareMap, HARDWARE.TRANSFER.key);
        frontGate = new Gate(opMode.hardwareMap, HARDWARE.FRONT_GATE.key, Servo.Direction.FORWARD); // TODO:Check servo directions
        backGate = new Gate(opMode.hardwareMap, HARDWARE.BACK_GATE.key, Servo.Direction.FORWARD);
        frontShooter = new Shooter(opMode.hardwareMap, HARDWARE.SHOOTER_FRONT.key, true); //Define shooter-bullet type position
        backShooter = new Shooter(opMode.hardwareMap, HARDWARE.SHOOTER_BACK.key, false);
        frontTurret = new Turret(opMode.hardwareMap, HARDWARE.FRONT_TURRET.key, HARDWARE.FRONT_TURRET_ENCODER.key);
        backTurret = new Turret(opMode.hardwareMap, HARDWARE.BACK_TURRET.key, HARDWARE.BACK_TURRET_ENCODER.key);
        barnFrontTurret = new BarnTurret(opMode.hardwareMap, HARDWARE.FRONT_TURRET.key);
        barnBackTurret = new BarnTurret(opMode.hardwareMap, HARDWARE.BACK_TURRET.key);
    }

    public void periodic() {
        telemetry.update();
        drive.follower.update();
    }

    public boolean sticksUsed() {
        return Math.abs(gamepadEx1.getLeftX()) > 0.05 ||
                Math.abs(gamepadEx1.getLeftY()) > 0.05 ||
                Math.abs(gamepadEx1.getRightX()) > 0.05;
    }

    public InstantCommand rumble(){
        return new InstantCommand(() -> gamepad1.rumble(100));
    }
}
