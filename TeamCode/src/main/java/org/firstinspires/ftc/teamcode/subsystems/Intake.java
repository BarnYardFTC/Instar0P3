package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.general.BarnRobot;

public class Intake extends SubsystemBase {
    private final DcMotor intakeMotor;

    public Intake() {
        intakeMotor = BarnRobot.getInstance().hardware.intake;
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public Command enableCommand(){
        return new InstantCommand(() -> intakeMotor.setPower(1), this);
    }

    public Command disableCommand(){
        return new InstantCommand(() -> intakeMotor.setPower(0), this);
    }
}
