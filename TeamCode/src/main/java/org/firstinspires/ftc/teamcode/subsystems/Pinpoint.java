package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Pinpoint {
    private final GoBildaPinpointDriver pinpoint;

    public Pinpoint(HardwareMap hwMap, String deviceName) {
        pinpoint = hwMap.get(GoBildaPinpointDriver.class, deviceName);
    }

    public void update() {
        pinpoint.update();
    }

    public GoBildaPinpointDriver get() {
        return pinpoint;
    }
}
