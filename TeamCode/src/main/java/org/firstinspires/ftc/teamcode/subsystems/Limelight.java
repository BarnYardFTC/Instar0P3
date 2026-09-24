package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.seattlesolvers.solverslib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.general.BarnRobot;

import java.util.List;

public class Limelight extends SubsystemBase {
    private final Limelight3A limelight;
    private LLResult latestResult;

    private List<LLResultTypes.FiducialResult> feducialResultates ;
    public Limelight() {
        limelight = BarnRobot.getInstance().hardware.limelight;
        limelight.setPollRateHz(100);
        start();
    }

    public void start() {
        limelight.start();
    }

    public void update() {
        LLResult result = limelight.getLatestResult();
        if (result != null) {
            latestResult = result;
            feducialResultates = latestResult.getFiducialResults();
        }
    }

    public LLResult getLatestResult() {
        return latestResult;
    }

    public boolean hasValidTarget() {
        return latestResult != null && latestResult.isValid();
    }

    public double getTx() {
        return hasValidTarget() ? latestResult.getTx() : 0.0;
    }

    public double getTy() {
        return hasValidTarget() ? latestResult.getTy() : 0.0;
    }

    public double getTa() {
        return hasValidTarget() ? latestResult.getTa() : 0.0;
    }

    public Pose3D getBotposeMT2() {
        return hasValidTarget() ? latestResult.getBotpose_MT2() : null;
    }

    public void updateRobotOrientation(double yawDegrees) {
        limelight.updateRobotOrientation(yawDegrees);
    }

    public Limelight3A get() {
        return limelight;
    }

    LLResultTypes.FiducialResult goalD = null;

    double goalDistance = 1;
    public double getDistance() {
        if (hasValidTarget() && feducialResultates != null && !feducialResultates.isEmpty()) {
            goalD = feducialResultates.get(0);
            for (LLResultTypes.FiducialResult fr : feducialResultates) {
                if (fr.getTargetArea() > goalD.getTargetArea()) {
                    goalD = fr;
                }
            }
            Pose3D pose = goalD.getTargetPoseCameraSpace();
        }
        return goalDistance;
    }

    @Override
    public void periodic() {
        super.periodic();
        update();
    }
}