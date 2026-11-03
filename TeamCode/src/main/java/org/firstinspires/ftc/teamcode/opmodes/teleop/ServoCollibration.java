package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.bylazar.configurables.PanelsConfigurables;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Gate;

@TeleOp(name = "Servo calibration", group = "test")
public class ServoCollibration extends CommandOpMode {
    Gate frontGate;

    @Override
    public void initialize() {
        frontGate = new Gate(hardwareMap, "backGate");
    }

    @Override
    public void initialize_loop() {
        frontGate.setServoPos();
        updateTelemetry();
    }

    @Override
    public void run() {
        super.run();
        frontGate.setServoPos();
        updateTelemetry();
    }

    private void updateTelemetry() {
        telemetry.addData("Status", opModeIsActive() ? "Running" : "Init (ready)");
        telemetry.addData("servoTestPos (in Gate)", Gate.servoTestPos);
        telemetry.addData("Servo Position", frontGate.getServoPos());
        telemetry.update();
    }
}
