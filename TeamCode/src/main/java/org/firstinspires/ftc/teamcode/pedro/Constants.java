package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("leftFrontDrivetrain");
        c.frontRightName.set("rightFrontDrivetrain");
        c.backLeftName.set("leftBackDrivetrain");
        c.backRightName.set("rightBackDrivetrain");
        c.frontLeftDirection.set(DcMotor.Direction.REVERSE);
        c.frontRightDirection.set(DcMotor.Direction.FORWARD);
        c.backLeftDirection.set(DcMotor.Direction.REVERSE);
        c.backRightDirection.set(DcMotor.Direction.FORWARD);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-7.757240956223856);
        c.yPodOffset.set(-9.814638753575602);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.4217146891309132);
                Controller secondaryTranslationalForward = Controller.proportional(0.1558123092098453);
                Controller primaryTranslationalLateral = Controller.proportional(5.955605712458732);
                Controller secondaryTranslationalLateral = Controller.proportional(2.2004371740380018);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.013938454867169206));
                c.brake.set(Controller.proportionalFeedforward(0.011847686637093825));

                c.headingFeedback.set(Controller.proportional(10.362497357462551));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.08517496514446446, 0.0035046943120099817));

                c.linearBrakeCoefficients.set(Matrix.diag(0.10548001800003502, 0.0663305622992052));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0010407576965520513, 0.0016230666464132199));

                c.maxAchievableForwardVelocity.set(61.21141728123867);
                c.maxAchievableStrafeVelocity.set(24.90482128951645);
                c.naturalForwardDeceleration.set(34.36628545668252);
                c.naturalStrafeDeceleration.set(1198.656918625977);
            }
    );
}