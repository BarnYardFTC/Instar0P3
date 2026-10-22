package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private final DcMotor intakeMotor;

    public Intake(HardwareMap hwMap, String motorName) {
        intakeMotor = hwMap.get(DcMotor.class, motorName);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public Command enableCommand() {
        return new InstantCommand(() -> intakeMotor.setPower(1), this);
    }

    public Command disableCommand() {
        return new InstantCommand(() -> intakeMotor.setPower(0), this);
    }
}
