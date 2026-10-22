package org.firstinspires.ftc.teamcode.general;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;

public class  Hardware {
    public HardwareMap hwMap = hardwareMap;

    public enum CONFIG {
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
        SHOOTER_FRONT("shooterMotorFront"),
        SHOOTER_BACK("shooterMotorBack");

        public final String key;

        CONFIG(String key) {
            this.key = key;
        }
    };

    public Limelight3A limelight;

    public GoBildaPinpointDriver pinpoint;

    public DcMotor leftFrontDrivetrain;
    public DcMotor rightFrontDrivetrain;
    public DcMotor leftBackDrivetrain;
    public DcMotor rightBackDrivetrain;
    public DcMotor transferMotor;
    public DcMotor intakeMotor;
    public DcMotorEx shooterMotorFront;
    public DcMotorEx shooterMotorBack;
    public Servo frontGateServo;
    public Servo backGateServo;
    public CRServoEx frontTurretServo;
    public CRServoEx backTurretServo;

    public Hardware(HardwareMap hwMap) {
        this.hwMap = hwMap;
        initMotors();
        initServos();
        initSensors();
    }

    private void initMotors() {
        leftFrontDrivetrain = hwMap.get(DcMotor.class, CONFIG.DRIVETRAIN_LF.key);
        rightFrontDrivetrain = hwMap.get(DcMotor.class, CONFIG.DRIVETRAIN_RF.key);
        leftBackDrivetrain = hwMap.get(DcMotor.class, CONFIG.DRIVETRAIN_LB.key);
        rightBackDrivetrain = hwMap.get(DcMotor.class, CONFIG.DRIVETRAIN_RB.key);

        shooterMotorFront = hwMap.get(DcMotorEx.class, CONFIG.SHOOTER_FRONT.key);
        shooterMotorBack = hwMap.get(DcMotorEx.class, CONFIG.SHOOTER_BACK.key);

        intakeMotor = hwMap.get(DcMotor.class, CONFIG.INTAKE.key);
        transferMotor = hwMap.get(DcMotor.class, CONFIG.TRANSFER.key);
    }

    private void initServos(){
        backGateServo = hwMap.get(Servo.class, CONFIG.BACK_GATE.key);
        frontGateServo = hwMap.get(Servo.class, CONFIG.FRONT_GATE.key);

    }

    private void initSensors(){
        pinpoint = hwMap.get(GoBildaPinpointDriver.class, CONFIG.PINPOINT.key);
        limelight = hwMap.get(Limelight3A.class, CONFIG.LIMELIGHT.key);
    }
}