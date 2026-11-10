package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@TeleOp(name = "Servo calibration", group = "test")
public class ShooterTuningTeleOp extends CommandOpMode {
    Shooter shooter;
    @Override
    public void initialize() {
        shooter = new Shooter(hardwareMap, "shooterMotorBack", true);
        shooter.setDefaultCommand(shooter.operateShooterDependsOnDist());
    }

    @Override
    public void run() {
        super.run();
        updateTelemetry();
    }

    private void updateTelemetry() {
        telemetry.addData("RPM", shooter.getRPM());
        telemetry.update();
    }
}