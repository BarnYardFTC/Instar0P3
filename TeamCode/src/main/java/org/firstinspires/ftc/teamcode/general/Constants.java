package org.firstinspires.ftc.teamcode.general;

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
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-0.8737461210235836);
        c.yPodOffset.set(4.758815615195927);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.40789022772635675);
                Controller secondaryTranslationalForward = Controller.proportional(0.1507045401172737);
                Controller primaryTranslationalLateral = Controller.proportional(0.4352085855521774);
                Controller secondaryTranslationalLateral = Controller.proportional(0.1607979433739495);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.013632253663154592));
                c.brake.set(Controller.proportionalFeedforward(0.011587415613681403));

                c.headingFeedback.set(Controller.proportional(6.665629305517897));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.06648778744680216, 0.003891737048875456));

                c.linearBrakeCoefficients.set(Matrix.diag(0.12136524173961309, 0.05510693611834322));
                c.quadraticBrakeCoefficients.set(Matrix.diag(8.749049504225492E-4, 0.0020103956738338766));

                c.maxAchievableForwardVelocity.set(70.45584599414767);
                c.maxAchievableStrafeVelocity.set(55.55653496026936);
                c.naturalForwardDeceleration.set(30.16104236480404);
                c.naturalStrafeDeceleration.set(66.54900453761292);
            }
    );
}