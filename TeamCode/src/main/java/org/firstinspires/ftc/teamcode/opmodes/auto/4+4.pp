{
  "startPoint": {
    "x": 8.831061692969868,
    "y": 14.654994619799133,
    "locked": false,
    "headingDeg": 90
  },
  "lines": [
    {
      "kind": "atomic",
      "id": "line-muzm5jbh-lenwr9",
      "endPoint": {
        "x": 36.74390243902438,
        "y": 15.742467718794828
      },
      "controlPoints": [],
      "heading": {
        "type": "constant",
        "reverse": true,
        "piecewiseHeading": {
          "segments": [
            {
              "startProgress": 0,
              "endProgress": 0.0001,
              "interpolationType": "tangential",
              "reversed": false,
              "continueFromPrevious": false
            },
            {
              "startProgress": 0.0001,
              "endProgress": 0.22444889779559118,
              "interpolationType": "linear",
              "reversed": false,
              "continueFromPrevious": false,
              "parameters": {
                "startDeg": 0,
                "endDeg": 0
              }
            },
            {
              "startProgress": 0.22444889779559118,
              "endProgress": 0.8877755511022044,
              "interpolationType": "linear",
              "reversed": false,
              "continueFromPrevious": false,
              "parameters": {
                "startDeg": 0,
                "endDeg": 0
              }
            },
            {
              "startProgress": 0.8877755511022044,
              "endProgress": 1,
              "interpolationType": "linear",
              "reversed": false,
              "continueFromPrevious": false,
              "parameters": {
                "startDeg": 0,
                "endDeg": 0
              }
            }
          ]
        },
        "degrees": 180
      },
      "color": "#AC79DD",
      "locked": false,
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "kind": "atomic",
      "id": "line-muzn2m2n-kz3m5s",
      "endPoint": {
        "x": 9.20229555236729,
        "y": 9.366571018651335
      },
      "controlPoints": [
        {
          "x": 35.149210903873744,
          "y": 7.726685796269727
        }
      ],
      "heading": {
        "type": "constant",
        "reverse": true,
        "startDeg": 0,
        "endDeg": 0,
        "degrees": 180,
        "piecewiseHeading": {
          "segments": [
            {
              "startProgress": 0,
              "endProgress": 1,
              "interpolationType": "constant",
              "reversed": false,
              "continueFromPrevious": false,
              "parameters": {
                "degrees": 0
              }
            }
          ]
        }
      },
      "color": "#A6B665",
      "locked": false,
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "kind": "atomic",
      "id": "line-muznr44m-c6z0kb",
      "endPoint": {
        "x": 36.74390243902438,
        "y": 15.742467718794828
      },
      "controlPoints": [],
      "heading": {
        "type": "constant",
        "reverse": true,
        "degrees": 180
      },
      "color": "#688B97",
      "locked": false,
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "kind": "atomic",
      "id": "line-muznult7-fuebz8",
      "endPoint": {
        "x": 10.45624103299857,
        "y": 91.03730272596844
      },
      "controlPoints": [
        {
          "x": 10.817790530846489,
          "y": 74.6692969870875
        }
      ],
      "heading": {
        "type": "tangential",
        "reverse": true
      },
      "color": "#8DAA8B",
      "locked": false,
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    }
  ],
  "shapes": [
    {
      "id": "triangle-1",
      "name": "Red Goal",
      "vertices": [
        {
          "x": 141.5,
          "y": 70
        },
        {
          "x": 141.5,
          "y": 141.5
        },
        {
          "x": 118.3,
          "y": 141.5
        },
        {
          "x": 135.5,
          "y": 118
        },
        {
          "x": 136.3,
          "y": 70.2
        }
      ],
      "color": "#dc2626",
      "fillColor": "#ff6b6b"
    },
    {
      "id": "triangle-2",
      "name": "Blue Goal",
      "vertices": [
        {
          "x": 6.2,
          "y": 116.9
        },
        {
          "x": 25,
          "y": 141.5
        },
        {
          "x": 0,
          "y": 141.5
        },
        {
          "x": 0,
          "y": 70
        },
        {
          "x": 6,
          "y": 70
        }
      ],
      "color": "#2563eb",
      "fillColor": "#60a5fa"
    }
  ],
  "sequence": [
    {
      "kind": "path",
      "lineId": "line-muzm5jbh-lenwr9"
    },
    {
      "kind": "path",
      "lineId": "line-muzn2m2n-kz3m5s"
    },
    {
      "kind": "path",
      "lineId": "line-muznr44m-c6z0kb"
    },
    {
      "kind": "path",
      "lineId": "line-muznult7-fuebz8"
    }
  ],
  "fieldPoints": [],
  "activePaths": [],
  "settings": {
    "xVelocity": 75,
    "yVelocity": 65,
    "aVelocity": 3.141592653589793,
    "kFriction": 0.1,
    "rWidth": 17.00814961,
    "rHeight": 17.9528,
    "safetyMargin": 1,
    "maxVelocity": 40,
    "maxAcceleration": 30,
    "maxDeceleration": 30,
    "fieldMap": "biobuzz.webp",
    "robotImage": "/robot.png",
    "showGhostPaths": false,
    "showOnionLayers": false,
    "onionLayerSpacing": 3,
    "onionColor": "#dc2626",
    "onionNextPointOnly": false,
    "showHeadingArrow": false,
    "showCurrentTValue": false,
    "leftPanelWidth": 0,
    "rightPanelWidth": 473,
    "headingArrowLength": 50,
    "headingArrowColor": "#ffffff",
    "headingArrowThickness": 2,
    "pathOpacity": 1,
    "leftPanelMinWidth": 0,
    "rightPanelMinWidth": 0,
    "penToolMaxPaths": 8,
    "curveThroughMaxPoints": 4,
    "experimentalFeatures": {
      "optimize": false,
      "curveThrough": false
    }
  },
  "version": "1.5.0",
  "timestamp": "2026-10-08T14:58:24.947Z"
}