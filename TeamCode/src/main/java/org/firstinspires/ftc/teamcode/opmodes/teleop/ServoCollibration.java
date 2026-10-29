package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.general.BarnRobot;

@TeleOp(name = "Servo calibration", group = "test")
public class ServoCollibration extends CommandOpMode {

    BarnRobot robot = BarnRobot.getInstance();

    @Override
    public void initialize() {
        robot.init(this);
    }

    @Override
    public void run() {
        super.run();
        robot.frontGate.setServoPosCommand();
        robot.backGate.setServoPosCommand();
        robot.periodic();
    }
}
