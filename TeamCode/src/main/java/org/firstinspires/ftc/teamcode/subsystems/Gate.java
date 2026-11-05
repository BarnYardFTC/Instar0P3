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
    public static double CLOSE_POS;
    public static double OPEN_POS;
    public static double servoTestPos = 0.9;
    public static double servoPos = 0.9;
    public boolean isOpened = false;



    public Gate(HardwareMap hwMap, String frontServoName, double closePos, double openPos){
        gateServo = hwMap.get(Servo.class, frontServoName);
        gateServo.setDirection(Servo.Direction.FORWARD);
        OPEN_POS = openPos;
        CLOSE_POS = closePos;
        setClose();
    }

    @Override
    public void periodic() {
        gateServo.setPosition(servoPos);
    }

    public void setServoPos(double pos){
        servoPos = pos;
    }

    public double getServoPos(){
        return gateServo.getPosition();
    }

    public void setOpen(){
        servoPos = OPEN_POS;
        isOpened = true;
    }

    public void setClose(){
        servoPos = CLOSE_POS;
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
}
