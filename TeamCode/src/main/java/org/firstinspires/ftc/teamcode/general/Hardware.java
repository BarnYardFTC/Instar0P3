package org.firstinspires.ftc.teamcode.general;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class  Hardware {
    public HardwareMap hwMap = hardwareMap;

    public enum CONFIG {
        DRIVETRAIN_LB("leftBackDrivetrain"),
        DRIVETRAIN_LF("leftFrontDrivetrain"),
        DRIVETRAIN_RB("rightBackDrivetrain"),
        DRIVETRAIN_RF("rightFrontDrivetrain"),
        PINPOINT("pinpoint"),
        LIMELIGHT("limelight"),
        INTAKE("intakeMotor"),
        DOCK("docServo"),
        GUARD("guardServo"),
        SHOOTER_RIGHT("shooterMotorRight"),
        SHOOTER_LEFT("shooterMotorLeft");

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
    public DcMotorEx shooterMotorRight;
    public DcMotorEx shooterMotorLeft;

    public DcMotor intake;
    public Servo dock;
    public Servo guard;

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

        shooterMotorRight = hwMap.get(DcMotorEx.class, CONFIG.SHOOTER_RIGHT.key);
        shooterMotorLeft = hwMap.get(DcMotorEx.class, CONFIG.SHOOTER_LEFT.key);

        intake = hwMap.get(DcMotor.class, CONFIG.INTAKE.key);
    }

    private void initServos(){
        dock = hwMap.get(Servo.class, CONFIG.DOCK.key);
        guard = hwMap.get(Servo.class, CONFIG.GUARD.key);
    }

    private void initSensors(){
        pinpoint = hwMap.get(GoBildaPinpointDriver.class, CONFIG.PINPOINT.key);
        limelight = hwMap.get(Limelight3A.class, CONFIG.LIMELIGHT.key);
    }
}