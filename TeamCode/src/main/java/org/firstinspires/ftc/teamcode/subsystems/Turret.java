package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Turret extends SubsystemBase {

    CRServoEx servo;

    // nominal is basically a simulated encoder, we rely on it for setting power
    private double nominal = 180; // number is assumed from real turret starting direction
    private double tgt = nominal;
    private double lastRawAngle;
    private double currentRawAngle;

    private final double GEAR_RATIO = 2.75; // oh how i want to make it a numero magico in periodic

    public Turret(HardwareMap hardwareMap, String servoName, String encoderName) {
        servo = new CRServoEx(hardwareMap, servoName, encoderName, 3.3, AngleUnit.DEGREES, CRServoEx.RunMode.RawPower);
        currentRawAngle = servo.getAbsoluteEncoder().getCurrentPosition();
        lastRawAngle = currentRawAngle;
    }

    @Override
    public void periodic() {
        currentRawAngle = servo.getAbsoluteEncoder().getCurrentPosition();
        // TODO: redo this bs with PID
        if (tgt > nominal - 3 && tgt < nominal + 3) { // adds error so it doesnt destroy itself
            servo.set(0);
        } else if (tgt > nominal) {
            servo.set(0.7);
        } else if (tgt < nominal) { // 0.7 instead of 1 just in case
            servo.set(-0.7);
            // syncs nominal with change in encoder
            // TODO: make it not break when going over 360 or 0
            nominal += (currentRawAngle - lastRawAngle) / GEAR_RATIO;
            lastRawAngle = currentRawAngle;
        }
    }

    private void setTgt ( double angle){
        tgt = angle;
    }

    private void addTgt(double angle){
        tgt += angle;
    }

    public double getNominalAngle(){
        return nominal;
    }

    public double getAbsoluteAngle(){
        return currentRawAngle;
    }

    public Command setTgtCommand (double angle){
        return new InstantCommand(() -> setTgt(angle), this);
    }

    public Command addTgtCommand (double angle){
        return new InstantCommand(() -> addTgt(angle), this);
    }
}