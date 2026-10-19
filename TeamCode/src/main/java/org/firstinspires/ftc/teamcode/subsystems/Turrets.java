package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.AbsoluteAnalogEncoder;
import com.seattlesolvers.solverslib.hardware.motors.CRServoEx;

import org.firstinspires.ftc.teamcode.general.BarnRobot;


// At the moment when this class is written
// there is no turret on the robot yet

/** This class controls both turrets.
  * Set to a certain angle with simpleSetCommand().
  * Set to track a position with trackPositionCommand().
  * It tracks field center unless you set a goal with setGoalCommand. */
public class Turrets extends SubsystemBase { //TODO: parallax correction -> if needed <-
    private CRServoEx frontServo;
    private CRServoEx backServo;

    private AbsoluteAnalogEncoder frontEncoder;
    private AbsoluteAnalogEncoder backEncoder;

    private Pose goalPose = new Pose(72, 72);

    public Turrets() {
        //init with hardware once present, in hardware set ts to degrees

        frontServo.setPIDF(new PIDFCoefficients(0.01, 0.0, 0.001, 0.0));
        frontServo.setTargetPosition(0);

        backServo.setPIDF(new PIDFCoefficients(0.01, 0.0, 0.001, 0.0));
        backServo.setTargetPosition(0);
    }

    public void simpleSet(int degrees) {
        frontServo.setTargetPosition(degrees);
        backServo.setTargetPosition(degrees);
    }

    public void trackPosition(Pose targetPose) {
        Pose botPose = BarnRobot.getInstance().drive.follower.pose();
        double dx = targetPose.x() - botPose.x();
        double dy = targetPose.y() - botPose.y();

        double targetAngleRad = Math.atan2(dy, dx);
        double relativeAngleRad = targetAngleRad - botPose.heading();

        relativeAngleRad = Math.atan2(Math.sin(relativeAngleRad), Math.cos(relativeAngleRad));

        double targetDegrees = Math.toDegrees(relativeAngleRad);
        simpleSet((int) Math.round(targetDegrees));
    }

    public double[] getTurretPositions() {
        return new double[] {frontEncoder.getCurrentPosition(), backEncoder.getCurrentPosition()};
    }

    private void setGoal(Pose pose) {
        goalPose = pose;
    }

    public Command simpleSetCommand(int degrees) {
        return new InstantCommand(() -> simpleSet(degrees), this);
    }

    public Command trackPositionCommand(Pose targetPose) {
        return new RunCommand(() -> trackPosition(targetPose), this);
    }

    public Command setGoalCommand(Pose pose) {
        return new InstantCommand(() -> setGoal(pose), this);
    }

    public RunCommand trackPositionCommand() {
        return new RunCommand(() -> trackPosition(goalPose));
    }
}
