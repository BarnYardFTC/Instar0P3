package org.firstinspires.ftc.teamcode.subsystems;

import android.sax.StartElementListener;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
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
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LoggingPermission;

import kotlin.collections.UCollectionsKt;

public class Limelight extends SubsystemBase {
    private final Limelight3A limelight;
    private LLResult latestResult;
    public boolean isData = false;
    public boolean isValid = false;
    public int currentPipeline = 4;
    private double targetSizeCoefficient = 1.677;
    public boolean targetAchieved = false;
    public enum Colors {RED, BLUE}

    ArrayList<double[]> pythonOutputs = new ArrayList<>();
    double[] pythonOutput;

    private List<LLResultTypes.ColorResult> colorResults;
    public Limelight(HardwareMap hwMap, String limelightName) {
        limelight = hwMap.get(Limelight3A.class, limelightName);
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

    public void update() {
        if (!targetAchieved) latestResult = limelight.getLatestResult();
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


    public Limelight3A get() {
        return limelight;
    }

    /**
     * 0 - x offset from center of screen to center of target
     * 1 - y offset from center of screen to center of target
     * 2 - area in percentage of detected target
     * 3 - color ID (1 - yellow, 2 - blue, 3 - red)
     *
     * @return array of this 4 numbers for every target on the screen
     * PIPELINE WILL WORK ONLY WITH FULL LIGHT ON
     */
    public void getPythonResults(){
        pythonOutput = latestResult.getPythonOutput();
        if (pythonOutput != null && pythonOutput.length > 0) {
            pythonOutputs.add(pythonOutput);
        }
    }

    public ArrayList<double[]> detectOppositeColor(Colors color, ArrayList<double[]> pythonOutputs){
        ArrayList<double[]> oppositeTargets = new ArrayList<>();
        switch (color){
            case BLUE:
                for (double[] target : pythonOutputs) {
                    if (target[3] == 3){
                        oppositeTargets.add(target);
                    }
                }
                break;
            case RED:
                for (double[] target : pythonOutputs) {
                    if (target[3] == 2){
                        oppositeTargets.add(target);
                    }
                }
                break;
        }
        return oppositeTargets;
    }


    @Override
    public void periodic() {
        super.periodic();
        update();
    }
}