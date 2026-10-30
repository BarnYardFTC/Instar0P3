package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

@Configurable
public class Gate extends SubsystemBase {

    private final Servo gateServo;
    public final double CLOSE_POS = 0.8;
    public final double OPEN_POS = 0.2;
    public static double servoTestPos = 0.1;
    public boolean isOpened = false;

    public Gate(HardwareMap hwMap, String frontServoName, Servo.Direction direction){
        gateServo = hwMap.get(Servo.class, frontServoName);
        gateServo.setDirection(direction);
        setClose();
    }

    public void setServoPos(){
        gateServo.setPosition(servoTestPos);
    }

    private void setOpen(){
        gateServo.setPosition(OPEN_POS);
        isOpened = true;
    }

    private void setClose(){
        gateServo.setPosition(CLOSE_POS);
        isOpened = false;
    }

    public boolean getGatePosition(){
        return isOpened;
    }

    public Command closeCommand(){
        return new InstantCommand(this::setClose, this);
    }

    public Command openCommand(){
        return new InstantCommand(this::setOpen, this);
    }

    public Command setServoPosCommand(){
        return new InstantCommand(this::setServoPos, this);
    }
}
