package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import org.firstinspires.ftc.teamcode.subsystems.Turret;

@TeleOp(name = "CRTest", group = "Test")
public class CRTest extends CommandOpMode {
    Turret backTurret;
    GamepadEx gamepad;
    @Override
    public void initialize() {
        backTurret = new Turret(this.hardwareMap, "backTurret", "backTurretEncoder");

        gamepad = new GamepadEx(this.gamepad1);
        gamepad.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(backTurret.trackPositionCommand(new Pose(0,0)));
        gamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(backTurret.addTgtCommand(10));
        gamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(backTurret.addTgtCommand(-10));
    }

    @Override
    public void run() {
        super.run();
        this.telemetry.addData("Back Turret", backTurret.getNominalAngle());
        this.telemetry.addData("Back Raw angle", backTurret.getAbsoluteAngle());
        this.telemetry.addData("Back Target angle", backTurret.getTgt());
        this.telemetry.update();
    }
}
