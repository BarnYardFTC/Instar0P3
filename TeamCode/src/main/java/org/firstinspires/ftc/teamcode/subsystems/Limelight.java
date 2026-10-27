package org.firstinspires.ftc.teamcode.subsystems;

import android.sax.StartElementListener;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.sun.source.doctree.StartElementTree;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.general.BarnRobot;
import org.opencv.core.Mat;

import java.io.ByteArrayInputStream;
import java.util.List;

import kotlin.collections.UCollectionsKt;

public class Limelight extends SubsystemBase {
    private final Limelight3A limelight;
    private LLResult latestResult;
    public boolean isData = false;
    public boolean isValid = false;
    public int currentPipeline = 1;
    private double targetSizeCoefficient = 1.677;
    public boolean targetAchieved;

    private List<LLResultTypes.ColorResult> colorResults;
    public Limelight() {
        limelight = BarnRobot.getInstance().hardware.limelight;
        limelight.setPollRateHz(100);
        start();
    }

    public void start() {
        limelight.start();
        limelight.pipelineSwitch(currentPipeline);
    }
    public void switchPipeline(int num) {
        limelight.pipelineSwitch(num);
        currentPipeline = num;
    }
    public void setBestTargetPipeline(){
//        results[currentPipeline - 1] = limelight.getLatestResult();

        LLResult pipeline1;
        LLResult pipeline2;

        pipeline1 = limelight.getLatestResult();
        switchPipeline((currentPipeline == 1) ? 2 : 1);
        pipeline2 = limelight.getLatestResult();

        if (pipeline1 != null && pipeline2 != null){
            if (pipeline1.getTa() * targetSizeCoefficient >= pipeline2.getTa()) switchPipeline(1);
            else switchPipeline(2);
        }
        else {
            if (pipeline1!=null) switchPipeline(1);
            else if (pipeline2!=null) switchPipeline(2);
        }
        targetAchieved = false;
    }

    //

    public void update() {
        if (!targetAchieved){
            latestResult = limelight.getLatestResult();
            if (latestResult.isValid() && latestResult != null){
                colorResults = latestResult.getColorResults();
            }
        }
        else setBestTargetPipeline();
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

    LLResultTypes.ColorResult target_D;
    LLResultTypes.ColorResult target_S;
    double targetDistance = -1;
    public double getDistance() {
        if (hasValidTarget() && colorResults != null && !colorResults.isEmpty()) {
            target_D = colorResults.get(0);
            for (LLResultTypes.ColorResult fr : colorResults) {
                if (fr.getTargetArea() > target_D.getTargetArea()) {
                    target_D = fr;
                }
            }
            targetDistance = target_D.getTargetPoseCameraSpace().getPosition().z;

        }else BarnRobot.getInstance().telemetry.addLine("No valid targets");
        return targetDistance;
    }

    //Gets target(pollen) position relying on robot position = 0,0,0

    LLResultTypes.ColorResult target2;
    Pose3D targetPose = new Pose3D(new Position(DistanceUnit.INCH, 0, 0, 0, 0), new YawPitchRollAngles(AngleUnit.DEGREES, 0, 0, 0, 0));
    public Pose3D getTargetRobotSpace() {
        if (hasValidTarget() && colorResults != null && !colorResults.isEmpty()) {
            isValid = true;
            target_S = colorResults.get(0);
            for(LLResultTypes.ColorResult cl : colorResults){
                if (cl.getTargetPoseCameraSpace().getPosition().z < target_S.getTargetPoseCameraSpace().getPosition().z){
                    target_S = cl;
                }
            }
            targetPose = target_S.getTargetPoseCameraSpace();
        }else isValid = false;

        return targetPose;
    }

    //For future builder of collecting paths for auto
//    private void targetFinder(){
//        BarnRobot.getInstance().drive.targetsHashSet.add(new Pose(getTargetRobotSpace().getPosition().x,getTargetRobotSpace().getPosition().y, getTargetRobotSpace().getOrientation().getYaw()));
//    }

    @Override
    public void periodic() {
        super.periodic();
        update();
    }
}