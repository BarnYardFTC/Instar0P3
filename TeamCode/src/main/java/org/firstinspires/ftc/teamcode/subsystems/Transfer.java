package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.teamcode.general.BarnRobot;

public class Transfer extends SubsystemBase {
    private Servo dock;
    public Servo guard;

    private final double DOCK_COLLECT_POS = 1;
    private final double DOCK_PASS_POS = 0.3;

    private final double GUARD_COLLECT_POS = 0.91234;
    private final double GUARD_PASS_POS = 0;

    public boolean isDocCollect = true;
    public boolean isGuardCollect = true;
    BarnRobot robot = BarnRobot.getInstance();

    public Transfer(){
        dock = robot.hardware.dock;
        guard = robot.hardware.guard;
        dock.setDirection(Servo.Direction.FORWARD);
        guard.setDirection(Servo.Direction.REVERSE);
    }

    private void setDockPosition(double pos){
        dock.setPosition(pos);
    }

    private void setGuardPosition(double pos){
        guard.setPosition(pos);
    }

    private void setCollect(){
        setDockPosition(DOCK_COLLECT_POS);
        setGuardPosition(GUARD_COLLECT_POS);
        isDocCollect = true;
        isGuardCollect = true;
    }

    private void setPass() {
        setDockPosition(DOCK_PASS_POS);
        setGuardPosition(GUARD_PASS_POS);
        isDocCollect = false;
        isGuardCollect = false;
    }

    public double getDockPos(){
        return dock.getPosition();
    }

    public double getGuardPos(){
        return guard.getPosition();
    }

    public Command setCollectCommand(){
        return new InstantCommand(() -> setCollect(), this);
    }

    public Command setPassCommand(){

        return new InstantCommand(() -> setPass(), this);
    }

//    public ConditionalCommand setPassNShootCommand(){
//        return new ConditionalCommand(
//                setPassCommand(),
//                BarnRobot.getInstance().rumb(),
//                () -> BarnRobot.getInstance().shooter.isReady()
//        );
//    }

    public Command setDockPositionCommand(double pos) {
        return new InstantCommand(() -> setDockPosition(pos));
    }
    public Command setGuardPositionCommand(double pos) {
        return new InstantCommand(() -> setGuardPosition(pos));
    }
}
