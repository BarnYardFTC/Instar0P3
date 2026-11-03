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
    public static double CLOSE_POS = 0.9;
    public static double OPEN_POS = 0.7;
    public static double servoTestPos = 0.9;
    public boolean isOpened = false;

    public Gate(HardwareMap hwMap, String frontServoName){
        gateServo = hwMap.get(Servo.class, frontServoName);
        gateServo.setDirection(Servo.Direction.FORWARD);
        setClose();
    }

    @Override
    public void periodic() {
        setServoPos();
    }

    public void setServoPos(){
        double rawPos = servoTestPos > 1.0 ? servoTestPos / 180.0 : servoTestPos;
        double targetPos = Math.max(0.0, Math.min(1.0, rawPos));
        gateServo.setPosition(targetPos);
    }

    public void setServoPos(double pos){
        double rawPos = pos > 1.0 ? pos / 180.0 : pos;
        double targetPos = Math.max(0.0, Math.min(1.0, rawPos));
        gateServo.setPosition(targetPos);
    }

    public double getServoPos(){
        return gateServo.getPosition();
    }

    public void setOpen(){
        gateServo.setPosition(OPEN_POS);
        isOpened = true;
    }

    public void setClose(){
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
        setServoPos();
        return new InstantCommand(this::setServoPos, this);
    }
}
