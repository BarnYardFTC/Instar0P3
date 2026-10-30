package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.general.BarnRobot;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

@TeleOp(name = "Main Teleop", group = "main")
public class MainTeleop extends CommandOpMode {

    @Override
    public void initialize() {
        TeleopTemplate.apply(this, Drivetrain.DriveMode.MANUAL);
    }

    @Override
    public void run() {
        super.run();
        TeleopTemplate.periodic();
    }

    @Override
    public void end() {
        super.end();
        TeleopTemplate.end();
    }
}
