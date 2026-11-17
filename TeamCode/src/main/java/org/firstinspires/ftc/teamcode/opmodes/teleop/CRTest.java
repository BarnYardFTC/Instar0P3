package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ParallelCommandGroup;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.subsystems.Turret;

@TeleOp(name = "CRTest", group = "Test")
public class CRTest extends CommandOpMode {
    Turret frontTurret;
    Turret backTurret;
    GamepadEx gamepad;
    @Override
    public void initialize() {
        frontTurret = new Turret(this.hardwareMap, "frontTurret", "frontTurretEncoder");
        backTurret = new Turret(this.hardwareMap, "backTurret", "backTurretEncoder");

        gamepad = new GamepadEx(this.gamepad1);
        gamepad.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(new ParallelCommandGroup(frontTurret.setAngleCommand(180), backTurret.setAngleCommand(180)));
        gamepad.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(new ParallelCommandGroup(frontTurret.setAngleCommand(360), backTurret.setAngleCommand(360)));
        gamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(new ParallelCommandGroup(frontTurret.setAngleCommand(0), backTurret.setAngleCommand(0)));
        gamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(new ParallelCommandGroup(frontTurret.adjustAngleCommand(10), backTurret.adjustAngleCommand(10)));
        gamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(new ParallelCommandGroup(frontTurret.adjustAngleCommand(-10), backTurret.adjustAngleCommand(-10)));
    }

    @Override
    public void run() {
        super.run();
        this.telemetry.addData("Front Turret", frontTurret.getAngle());
        this.telemetry.addData("Back Turret", backTurret.getAngle());
        this.telemetry.addData("Front Raw angle", frontTurret.getRawEncoder());
        this.telemetry.addData("Back Raw angle", backTurret.getRawEncoder());
        this.telemetry.addData("Front voltage", frontTurret.getVoltage());
        this.telemetry.addData("Back voltage", backTurret.getVoltage());

        this.telemetry.update();
    }
}
