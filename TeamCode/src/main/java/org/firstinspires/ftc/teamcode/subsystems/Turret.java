package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.AbsoluteAnalogEncoder;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;


// At the moment when this class is written
// there is no turret on the robot yet
public class Turret extends SubsystemBase {
    private CRServoEx frontServo;
    private CRServoEx backServo;

    private AbsoluteAnalogEncoder frontEncoder;
    private AbsoluteAnalogEncoder backEncoder;

    public Turret() {
        //init with hardware once present, in hardware set ts to degrees

        frontServo.setPIDF(new PIDFCoefficients(0.01, 0.0, 0.001, 0.0));
        frontServo.setTargetPosition(0);

        backServo.setPIDF(new PIDFCoefficients(0.01, 0.0, 0.001, 0.0));
        backServo.setTargetPosition(0);
    }

    private void simpleSet(int degrees) {
        frontServo.setTargetPosition(degrees);
        backServo.setTargetPosition(degrees);
    }

    public double[] getTurretPositions() {
        return new double[] {frontEncoder.getCurrentPosition(), backEncoder.getCurrentPosition()};
    }

    public Command simpleSetCommand(int degrees) {
        return new InstantCommand(() -> simpleSet(degrees));
    }
}
