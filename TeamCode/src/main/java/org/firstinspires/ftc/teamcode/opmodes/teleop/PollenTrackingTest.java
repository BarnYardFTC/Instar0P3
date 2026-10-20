package org.firstinspires.ftc.teamcode.opmodes.teleop;



import com.pedropathing.revhub.localizers.ThreeWheelLocalizer;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.general.BarnRobot;
@TeleOp(name = "PolenTrackingTest", group = "test")
public class PollenTrackingTest extends CommandOpMode {
    BarnRobot robot = BarnRobot.getInstance();

    @Override
    public void initialize(){
        robot.init(this);
    }


    @Override
    public void run() {
        super.run();
        telemetry.addData("Ty", robot.limelight.getTx());
        telemetry.addData("Tx", robot.limelight.getTy());
        telemetry.addData("Distance", robot.limelight.getDistance());
    }


}
