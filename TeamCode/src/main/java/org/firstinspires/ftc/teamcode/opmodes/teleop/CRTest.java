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
        turret = new Turret(this.hardwareMap, "turret", "encoder");
        gamepad = new GamepadEx(this.gamepad1);
        gamepad.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(turret.setAngleCommand(20));
        gamepad.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(turret.setAngleCommand(340));
        gamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(turret.setAngleCommand(0));
        gamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(turret.adjustAngleCommand(10));
        gamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(turret.adjustAngleCommand(-10));
    }

    @Override
    public void run() {
        super.run();
        this.telemetry.addData(   "Turret", turret.getAngle());
        this.telemetry.update();
    }
}
