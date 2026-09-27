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
        c.xPodOffset.set(-8.710609496109129);
        c.yPodOffset.set(-7.080514862781435);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.41174977442485033);
                Controller secondaryTranslationalForward = Controller.proportional(0.15213053949337993);
                Controller primaryTranslationalLateral = Controller.proportional(0.6881779823634235);
                Controller secondaryTranslationalLateral = Controller.proportional(0.25426337602893123);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.014043906858348217));
                c.brake.set(Controller.proportionalFeedforward(0.011937320829595985));

                c.headingFeedback.set(Controller.proportional(9.262743353297331));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.06513745188264064, 0.0087788379188871));

                c.linearBrakeCoefficients.set(Matrix.diag(0.07671278725664411, 0.04927062326946159));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0021970073791210227, 0.0021758419721566616));

                c.maxAchievableForwardVelocity.set(69.40900221176456);
                c.maxAchievableStrafeVelocity.set(53.81139069015402);
                c.naturalForwardDeceleration.set(34.70907381635403);
                c.naturalStrafeDeceleration.set(72.3327299696464);
            }
    );
}