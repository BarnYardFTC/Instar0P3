package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

public class Transfer extends SubsystemBase {

    private final DcMotor transferMotor;

    public Transfer(HardwareMap hwMap, String motorName){
        transferMotor = hwMap.get(DcMotor.class, motorName);
        transferMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        transferMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        transferMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public Command enableCommand(){
        return new InstantCommand(() -> transferMotor.setPower(1), this);
    }

    public Command disableCommand(){
        return new InstantCommand(() -> transferMotor.setPower(0), this);
    }

}
