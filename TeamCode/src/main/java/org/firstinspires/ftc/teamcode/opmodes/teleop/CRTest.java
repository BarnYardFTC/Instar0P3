package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.subsystems.Turret;

@TeleOp(name = "CRTest", group = "Test")
public class CRTest extends CommandOpMode {
    Turret turret;
    GamepadEx gamepad;
    @Override
    public void initialize() {
        turret = new Turret(this.hardwareMap, "frontTurret", "frontTurretEncoder");
        gamepad = new GamepadEx(this.gamepad1);
        gamepad.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(turret.setTgtCommand(-90));
        gamepad.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(turret.setTgtCommand(90));
        gamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(turret.setTgtCommand(0));
        gamepad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(turret.setTgtCommand(180));
        gamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(turret.addTgtCommand(10));
        gamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(turret.addTgtCommand(-10));
    }

    @Override
    public void run() {
        super.run();
        this.telemetry.addData("Nominal: ", turret.getNominalAngle());
        this.telemetry.addData("Absolute: ", turret.getAbsoluteAngle());
        this.telemetry.addData("Last: ", turret.getLastRawAngle());
        this.telemetry.addData("Delta: ", turret.getAbsoluteAngle() - turret.getLastRawAngle());
        this.telemetry.update();
    }
}
