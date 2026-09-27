import limelight
import limelightresults
import json
import time

def limelightTuning():
    disc_limelight = limelight.discover_limelights(debug=False)
    if not disc_limelight:
        print("No limelights found")
    else:
        print("discovered limelights" + str(disc_limelight))

    llAddr = disc_limelight[0]
    ll = limelight.Limelight(llAddr)

    ll.enable_websocket()
    ll.pipeline_switch(1)

    time.sleep(1)

    base_pipeline = {
        "pipeline_type": "pipe_fiducial",
        "fiducial_type": "aprilClassic36h11"
    }

    bestExposure = 20
    bestGain = 0
    bestBlackLevel = 0
    bestScore = 0.0



    testExposures = [20, 100, 200, 300, 400, 500, 600, 800, 1000]
    testGains = [1, 2, 5, 6, 8 ,10, 13, 15, 20, 25, 30]
    testBlackLevels = [1, 3, 5 ,6, 8,10, 12]

    for exp in testExposures:
        for gain in testGains:
            for blk in testBlackLevels:
                pipeline_config = base_pipeline.copy()
                pipeline_config['exposure'] = exp
                pipeline_config['lcgain'] = gain
                pipeline_config['black_level'] = blk

                ll.update_pipeline(json.dumps(pipeline_config))
                time.sleep(0.0001)
                results = ll.get_results()
                parsed_results = limelightresults.parse_results(results)
                targetArea = 0

                for fiducial in parsed_results.fiducialResults:
                    if fiducial.fiducial_id == 22:
                        targetArea = fiducial.target_area

                        if targetArea > bestScore:
                            bestScore = targetArea
                            bestGain = gain
                            bestBlackLevel = blk
                            bestExposure = exp

                print("Testing exp: " + str(exp) + "\ngain: " + str(gain) + "\nblack level: " + str(blk) + "\nta: " + str(targetArea))

    finalPiplineJson = {
        'exposure': bestExposure,
        'lcgain': bestGain,
        'black_level': bestBlackLevel,
    }

    ll.update_pipeline(json.dumps(finalPiplineJson),flush=1)
    print("Best exp: " + str(bestExposure) + "\nBest gain: " + str(bestGain) + "\nBest black level: " + str(bestBlackLevel)  + "\nBest score " + str(bestScore))
    ll.disable_websocket()

    print(bestExposure)
    print(bestGain)
    print(bestBlackLevel)
    print(bestScore)

if __name__ == "__main__":
    limelightTuning()
