//X Axis is vertical in image

class vector2d {
  constructor(x, y) {
    this.x = x;
    this.y = y;
  }
}

let splinePathsList = document.getElementById("splinePathsList");
let bezierPathsList = document.getElementById("bezierPathsList");

var mouseIsDown = false;
var lastMousePos = null;

var valueUpdated = false;

var activeSplinePathIndex = 0;
var splinePaths = [];
var targetPoint = {
  targetPathIndex : 0,
  targetKeyPath : null
}

var imageWidth = null;
var fieldWidth = 10;

function mouseDown(event) {
  mouseIsDown = true;
  findMouseClickTarget(event);
}

function mouseMove(event) {
  if (mouseIsDown) {
    if (lastMousePos == null) {
      lastMousePos = new vector2d(event.clientX, event.clientY)
    }

    var mouseMovement = new vector2d(pixToLength(event.clientY - lastMousePos.y), pixToLength(event.clientX - lastMousePos.x));

    lastMousePos.x = event.clientX;
    lastMousePos.y = event.clientY;

    if (splinePaths.length > 0 && targetPoint.targetKeyPath != null) {
      switch (targetPoint.targetKeyPath) {
        case controlPoint.anchor1:
          splinePaths[activeSplinePathIndex].addToTargetValue(mouseMovement);
          break;
        case controlPoint.anchor2:
          splinePaths[activeSplinePathIndex].addToTargetValue(mouseMovement);
          break;
        case controlPoint.control1:
          splinePaths[activeSplinePathIndex].addToTargetValue(mouseMovement);
          break;
        case controlPoint.control2:
          splinePaths[activeSplinePathIndex].addToTargetValue(mouseMovement);
          break;
        default:
          alert("Error1");
      }
    }
    canvasUpdate();
  }
  updateValuePanel();
}

function mouseUp() {
  mouseIsDown = false;
  lastMousePos = null;
}

function updateValues() {
  valueUpdated = false;

  fieldWidth = nullSafe(parseFloat(document.getElementById("fieldWidth").value));

  splinePaths[activeSplinePathIndex].setTargetValue(controlPoint.anchor1, new vector2d(nullSafe(parseFloat(document.getElementById("startX").value)), nullSafe(parseFloat(document.getElementById("startY").value))));
  splinePaths[activeSplinePathIndex].setTargetValue(controlPoint.anchor2, new vector2d(nullSafe(parseFloat(document.getElementById("endX").value)), nullSafe(parseFloat(document.getElementById("endY").value))));
  splinePaths[activeSplinePathIndex].setTargetValue(controlPoint.control1, new vector2d(nullSafe(parseFloat(document.getElementById("p1X").value)), nullSafe(parseFloat(document.getElementById("p1Y").value))));
  splinePaths[activeSplinePathIndex].setTargetValue(controlPoint.control2, new vector2d(nullSafe(parseFloat(document.getElementById("p2X").value)), nullSafe(parseFloat(document.getElementById("p2Y").value))));

  splinePaths[activeSplinePathIndex].paths[targetPoint.targetPathIndex].power = nullSafe(parseFloat(document.getElementById("power").value));
  splinePaths[activeSplinePathIndex].paths[targetPoint.targetPathIndex].forward = !document.getElementById("reverseDirection").checked;

  updateValuePanel();
  canvasUpdate();
}

function updateValuePanel() {
  console.log("update");
  if (targetPoint.targetPathIndex != null) {
    document.getElementById("startX").value = splinePaths[activeSplinePathIndex].paths[targetPoint.targetPathIndex].anchor1.x;
    document.getElementById("startY").value = splinePaths[activeSplinePathIndex].paths[targetPoint.targetPathIndex].anchor1.y;
    document.getElementById("endX").value = splinePaths[activeSplinePathIndex].paths[targetPoint.targetPathIndex].anchor2.x;
    document.getElementById("endY").value = splinePaths[activeSplinePathIndex].paths[targetPoint.targetPathIndex].anchor2.y;
    document.getElementById("p1X").value = splinePaths[activeSplinePathIndex].paths[targetPoint.targetPathIndex].control1.x;
    document.getElementById("p1Y").value = splinePaths[activeSplinePathIndex].paths[targetPoint.targetPathIndex].control1.y;
    document.getElementById("p2X").value = splinePaths[activeSplinePathIndex].paths[targetPoint.targetPathIndex].control2.x;
    document.getElementById("p2Y").value = splinePaths[activeSplinePathIndex].paths[targetPoint.targetPathIndex].control2.y;

    document.getElementById("power").value = splinePaths[activeSplinePathIndex].paths[targetPoint.targetPathIndex].power;
    document.getElementById("reverseDirection").checked = !splinePaths[activeSplinePathIndex].paths[targetPoint.targetPathIndex].forward;
  }
}

function addSplinePath() {
  splinePaths.push(new bezierSplinePath("Path " + splinePaths.length.toString(), [new bezierPath(new vector2d(5,5), new vector2d(3,3), new vector2d(5,2), new vector2d(2,3))]));
  activeSplinePathIndex = splinePaths.length-1;

  canvasUpdate();
  updateSplinePathsList();
  updateBezierPathsList();
}

function addBezierPath() {
  var anchor1 = clone(splinePaths[activeSplinePathIndex].paths[splinePaths[activeSplinePathIndex].paths.length-1].anchor2);
  var control1 = getOppositePoint(splinePaths[activeSplinePathIndex].paths[splinePaths[activeSplinePathIndex].paths.length-1].control2, splinePaths[activeSplinePathIndex].paths[splinePaths[activeSplinePathIndex].paths.length-1].anchor2, fieldWidth/7);
  splinePaths[activeSplinePathIndex].paths.push(new bezierPath(anchor1, new vector2d(5,2), control1, new vector2d(4,3)));

  canvasUpdate();
  updateBezierPathsList();
}

function downloadSpline() {
  let splineJSON = JSON.stringify(splinePaths[activeSplinePathIndex]);
  let filename = splinePaths[activeSplinePathIndex].name + ".json";

  download(filename, splineJSON);
}

function updateSplinePathsList() {
  //let splinePathsList = document.getElementById("splinePathsList");
  splinePathsList.innerHTML = ''; //Clear splinePathsList
  var i;
  for (i = 0; i < splinePaths.length; i++) {
    let button = document.createElement("button");
    button.textContent = clone(splinePaths[i].name);
    button.id = "sp" + i.toString();
    button.onclick = function() {
      targetPoint.targetPathIndex = null;
      activeSplinePathIndex = parseFloat(button.id.split("")[2]); //Find a better solution

      canvasUpdate();
      updateBezierPathsList();
    };
    splinePathsList.appendChild(button);
  };
}

function updateBezierPathsList() {
  //let bezierPathsList = document.getElementById("bezierPathsList");
  bezierPathsList.innerHTML = ''; //Clear splinePathsList
  var i;
  for (i = 0; i < splinePaths[activeSplinePathIndex].paths.length; i++) {
    let button = document.createElement("button");
    button.textContent = "Curve " + (i+1).toString();
    button.id = "bp" + i.toString();
    button.onclick = function() {
      targetPoint.targetPathIndex = parseFloat(button.id.split("")[2]);
      updateValuePanel();
    };
    bezierPathsList.appendChild(button);
  };
}

function canvasSetUpAndMaintenance() {
  splinePaths = [new bezierSplinePath("Test Path", [new bezierPath(new vector2d(5,5), new vector2d(3,3), new vector2d(5,2), new vector2d(2,3))])];
  var canvas = document.getElementById("mainCanvas");
  var contentWidth = document.getElementById("content").offsetWidth;
  var contentHeight = document.getElementById("content").offsetHeight;

  if (canvas.getContext) {
    var ctx = canvas.getContext("2d");

    var fieldImage = new Image();
    fieldImage.src = "FieldView.png"

    var fieldImageRatio = fieldImage.width / fieldImage.height;
    var contentRatio = contentWidth / contentHeight;

    if (fieldImageRatio >= contentRatio) {
      var canvasWidth = contentWidth;
      var canvasHeight = contentWidth / fieldImageRatio;
    }
    if (fieldImageRatio < contentRatio) {
      var canvasHeight = contentHeight;
      var canvasWidth = contentWidth / fieldImageRatio;
    }

    canvas.width = canvasWidth;
    canvas.height = canvasHeight;

    imageWidth = canvasHeight;

    /*fieldImage.onload = function(){
      ctx.drawImage(this,0,0,canvasWidth,canvasHeight);
    }*/
    canvasUpdate();
    updateSplinePathsList();
    updateBezierPathsList();

  } else {
    alert('You need Safari or Firefox 1.5+ to see this.');
  }


}

function canvasUpdate() {
  var canvas = document.getElementById("mainCanvas");
  var context = canvas.getContext("2d");

  context.clearRect(0, 0, canvas.width, canvas.height);

  for (var i = 0; i < splinePaths[activeSplinePathIndex].paths.length; i++) {
    drawBezierPath(splinePaths[activeSplinePathIndex].paths[i]);
  }
}

function drawBezierPath(curve) {
  var canvas = document.getElementById("mainCanvas");

  if (canvas.getContext) {
    var ctx = canvas.getContext("2d");

    ctx.lineWidth = 1;
    ctx.beginPath();
    ctx.moveTo(lengthToPix(curve.anchor1.y), lengthToPix(curve.anchor1.x));

    var t;
    var subdivisions = 30;
    for (t = 0; t <= 1; t = t + 1/subdivisions) {
      ctx.lineTo(lengthToPix(curve.getPoint(t).y), lengthToPix(curve.getPoint(t).x));
    }
    ctx.lineTo(lengthToPix(curve.anchor2.y), lengthToPix(curve.anchor2.x));

    ctx.strokeStyle = '#66ff66';
    ctx.stroke();

    ctx.beginPath();

    ctx.moveTo(lengthToPix(curve.anchor1.y), lengthToPix(curve.anchor1.x));
    ctx.lineTo(lengthToPix(curve.control1.y), lengthToPix(curve.control1.x));
    ctx.moveTo(lengthToPix(curve.anchor2.y), lengthToPix(curve.anchor2.x));
    ctx.lineTo(lengthToPix(curve.control2.y), lengthToPix(curve.control2.x));

    ctx.strokeStyle = '#000000';
    ctx.stroke();

  } else {
    alert('You need Safari or Firefox 1.5+ to see this.');
  }
}

const controlPoint = {
  anchor1 : 1,
  anchor2 : 2,
  control1 : 3,
  control2 : 4
};
Object.freeze(controlPoint);

function checkPointClick(radius, point1, point2) {
  var distance = Math.sqrt(((point2.x - point1.x) ** 2) + ((point2.y - point1.y) ** 2));
  if (distance <= radius) {
    return true;
  } else {
    return false;
  }
}

function findMouseClickTarget(event) {
  var relativeRadius = 5
  var mainCanvas = document.getElementById("mainCanvas");
  var mouseCoord = new vector2d(pixToLength(event.clientY - (mainCanvas.offsetTop + mainCanvas.offsetParent.offsetTop)), pixToLength(event.clientX - (mainCanvas.offsetLeft + mainCanvas.offsetParent.offsetLeft)));
  var pointClicked = false;

  for (var i = 0; i < splinePaths[activeSplinePathIndex].paths.length; i++) {
    if (checkPointClick(fieldWidth*relativeRadius/100, splinePaths[activeSplinePathIndex].paths[i].anchor1, mouseCoord)) {
      targetPoint.targetPathIndex = i;
      targetPoint.targetKeyPath = controlPoint.anchor1;
      pointClicked = true;
    } else if (checkPointClick(fieldWidth*relativeRadius/100, splinePaths[activeSplinePathIndex].paths[i].anchor2, mouseCoord)) {
      targetPoint.targetPathIndex = i;
      targetPoint.targetKeyPath = controlPoint.anchor2;
      pointClicked = true;
    } else if (checkPointClick(fieldWidth*relativeRadius/100, splinePaths[activeSplinePathIndex].paths[i].control1, mouseCoord)) {
      targetPoint.targetPathIndex = i;
      targetPoint.targetKeyPath = controlPoint.control1;
      pointClicked = true;
    } else if (checkPointClick(fieldWidth*relativeRadius/100, splinePaths[activeSplinePathIndex].paths[i].control2, mouseCoord)) {
      targetPoint.targetPathIndex = i;
      targetPoint.targetKeyPath = controlPoint.control2;
      pointClicked = true;
    }
  }
  if (!pointClicked) {
    targetPoint.targetKeyPath = null;
  }
}

class bezierPath {
  constructor(start, end, control1, control2) {
    this.anchor1 = start;
    this.anchor2 = end;
    this.control1 = control1;
    this.control2 = control2;
    this.power = 0.5;
    this.forward = true;
  }
  getPoint(t) {
    var x = (this.anchor1.x * Math.pow((1 - t), 3)) + (this.control1.x * 3 * t * Math.pow((1 - t), 2)) + (this.control2.x * 3 * Math.pow(t, 2) * (1 - t)) + (this.anchor2.x * Math.pow(t, 3));
    var y = (this.anchor1.y * Math.pow((1 - t), 3)) + (this.control1.y * 3 * t * Math.pow((1 - t), 2)) + (this.control2.y * 3 * Math.pow(t, 2) * (1 - t)) + (this.anchor2.y * Math.pow(t, 3));
    return new vector2d(x,y);
  }
}

class bezierSplinePath {
  constructor(name, paths) {
    this.name = name;
    this.paths = paths;
  }
  setTargetValue(pointType, coords) {
    if (valueUpdated == false) { //Find a better solution
      var vector;
      switch (pointType) {
        case controlPoint.anchor1:
          vector = new vector2d(coords.x - this.paths[targetPoint.targetPathIndex].anchor1.x, coords.y - this.paths[targetPoint.targetPathIndex].anchor1.y);
          if (vector.x != 0 || vector.y != 0) {
            valueUpdated = true;

            this.paths[targetPoint.targetPathIndex].anchor1.x += vector.x;
            this.paths[targetPoint.targetPathIndex].anchor1.y += vector.y;
            this.paths[targetPoint.targetPathIndex].control1.x += vector.x;
            this.paths[targetPoint.targetPathIndex].control1.y += vector.y;
            if (targetPoint.targetPathIndex > 0) {
              this.paths[targetPoint.targetPathIndex-1].anchor2.x += vector.x;
              this.paths[targetPoint.targetPathIndex-1].anchor2.y += vector.y;
              this.paths[targetPoint.targetPathIndex-1].control2.x += vector.x;
              this.paths[targetPoint.targetPathIndex-1].control2.y += vector.y;
            }
          }
          break;
        case controlPoint.anchor2:
          vector = new vector2d(coords.x - this.paths[targetPoint.targetPathIndex].anchor2.x, coords.y - this.paths[targetPoint.targetPathIndex].anchor2.y);
          if (vector.x != 0 || vector.y != 0) {
            valueUpdated = true;

            this.paths[targetPoint.targetPathIndex].anchor2.x += vector.x;
            this.paths[targetPoint.targetPathIndex].anchor2.y += vector.y;
            this.paths[targetPoint.targetPathIndex].control2.x += vector.x;
            this.paths[targetPoint.targetPathIndex].control2.y += vector.y;
            if (targetPoint.targetPathIndex < this.paths.length-1) {
              this.paths[targetPoint.targetPathIndex+1].anchor1.x += vector.x;
              this.paths[targetPoint.targetPathIndex+1].anchor1.y += vector.y;
              this.paths[targetPoint.targetPathIndex+1].control1.x += vector.x;
              this.paths[targetPoint.targetPathIndex+1].control1.y += vector.y;
            }
          }
          break;
        case controlPoint.control1:
          vector = new vector2d(coords.x - this.paths[targetPoint.targetPathIndex].control1.x, coords.y - this.paths[targetPoint.targetPathIndex].control1.y);

          if (vector.x != 0 || vector.y != 0) {
            valueUpdated = true;

            var startPoint = clone(this.paths[targetPoint.targetPathIndex].control1);

            this.paths[targetPoint.targetPathIndex].control1.x += vector.x;
            this.paths[targetPoint.targetPathIndex].control1.y += vector.y;

            var endPoint = clone(this.paths[targetPoint.targetPathIndex].control1);
            var rotation = getAngleRotated(clone(this.paths[targetPoint.targetPathIndex].anchor1), startPoint, endPoint);

            if (targetPoint.targetPathIndex > 0) {
              this.paths[targetPoint.targetPathIndex-1].control2 = rotatePoint(this.paths[targetPoint.targetPathIndex-1].control2, this.paths[targetPoint.targetPathIndex].anchor1, rotation);
            }
          }
          break;
        case controlPoint.control2:
          vector = new vector2d(coords.x - this.paths[targetPoint.targetPathIndex].control2.x, coords.y - this.paths[targetPoint.targetPathIndex].control2.y);

          if (vector.x != 0 || vector.y != 0) {
            valueUpdated = true;

            var startPoint = clone(this.paths[targetPoint.targetPathIndex].control2);

            this.paths[targetPoint.targetPathIndex].control2.x += vector.x;
            this.paths[targetPoint.targetPathIndex].control2.y += vector.y;

            var endPoint = clone(this.paths[targetPoint.targetPathIndex].control2);
            var rotation = getAngleRotated(clone(this.paths[targetPoint.targetPathIndex].anchor2), startPoint, endPoint);

            if (targetPoint.targetPathIndex < this.paths.length-1) {
              this.paths[targetPoint.targetPathIndex+1].control1 = rotatePoint(this.paths[targetPoint.targetPathIndex+1].control1, this.paths[targetPoint.targetPathIndex].anchor2, rotation);
            }
          }
          break;
        default:
          alert("Error2");
      }
    }
  }
  addToTargetValue(vector) {
    switch (targetPoint.targetKeyPath) {
      case controlPoint.anchor1:
        var initialPos = clone(this.paths[targetPoint.targetPathIndex].anchor1);
        this.paths[targetPoint.targetPathIndex].anchor1.x += vector.x;
        this.paths[targetPoint.targetPathIndex].anchor1.y += vector.y;
        this.paths[targetPoint.targetPathIndex].control1.x += vector.x;
        this.paths[targetPoint.targetPathIndex].control1.y += vector.y;
        if (targetPoint.targetPathIndex > 0) {
          this.paths[targetPoint.targetPathIndex-1].anchor2.x += vector.x;
          this.paths[targetPoint.targetPathIndex-1].anchor2.y += vector.y;
          this.paths[targetPoint.targetPathIndex-1].control2.x += vector.x;
          this.paths[targetPoint.targetPathIndex-1].control2.y += vector.y;
        }
        break;
      case controlPoint.anchor2:
        this.paths[targetPoint.targetPathIndex].anchor2.x += vector.x;
        this.paths[targetPoint.targetPathIndex].anchor2.y += vector.y;
        this.paths[targetPoint.targetPathIndex].control2.x += vector.x;
        this.paths[targetPoint.targetPathIndex].control2.y += vector.y;
        if (targetPoint.targetPathIndex < this.paths.length-1) {
          this.paths[targetPoint.targetPathIndex+1].anchor1.x += vector.x;
          this.paths[targetPoint.targetPathIndex+1].anchor1.y += vector.y;
          this.paths[targetPoint.targetPathIndex+1].control1.x += vector.x;
          this.paths[targetPoint.targetPathIndex+1].control1.y += vector.y;
        }
        break;
      case controlPoint.control1:
        var startPoint = clone(this.paths[targetPoint.targetPathIndex].control1);

        this.paths[targetPoint.targetPathIndex].control1.x += vector.x;
        this.paths[targetPoint.targetPathIndex].control1.y += vector.y;

        var endPoint = clone(this.paths[targetPoint.targetPathIndex].control1);
        var rotation = getAngleRotated(clone(this.paths[targetPoint.targetPathIndex].anchor1), startPoint, endPoint);

        if (targetPoint.targetPathIndex > 0) {
          this.paths[targetPoint.targetPathIndex-1].control2 = rotatePoint(this.paths[targetPoint.targetPathIndex-1].control2, this.paths[targetPoint.targetPathIndex].anchor1, rotation);
        }
        break;
      case controlPoint.control2:
        var startPoint = clone(this.paths[targetPoint.targetPathIndex].control2);

        this.paths[targetPoint.targetPathIndex].control2.x += vector.x;
        this.paths[targetPoint.targetPathIndex].control2.y += vector.y;

        var endPoint = clone(this.paths[targetPoint.targetPathIndex].control2);
        var rotation = getAngleRotated(clone(this.paths[targetPoint.targetPathIndex].anchor2), startPoint, endPoint);

        if (targetPoint.targetPathIndex < this.paths.length-1) {
          this.paths[targetPoint.targetPathIndex+1].control1 = rotatePoint(this.paths[targetPoint.targetPathIndex+1].control1, this.paths[targetPoint.targetPathIndex].anchor2, rotation);
        }
        break;
      default:
        Alert("Error3");
    }
  }
}

function rotatePoint(point, center, angle) {
  var x1 = point.x - center.x;
  var y1 = point.y - center.y;

  var x2 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
  var y2 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

  return new vector2d(x2 + center.x, y2 + center.y);
}

function getAngleRotated(center, point1, point2) {
  var pointOneSlope = (point1.y - center.y) / (point1.x - center.x);
  var pointTwoSlope = (point2.y - center.y) / (point2.x - center.x);

  var pointOneAngle = Math.atan(pointOneSlope) + ((point1.x >= center.x) ? 0 : Math.PI);
  var pointTwoAngle = Math.atan(pointTwoSlope) + ((point2.x >= center.x) ? 0 : Math.PI);

  return pointTwoAngle - pointOneAngle;
}

function getOppositePoint(point, center, distance) {
  var slope = (point.y - center.y) / (point.x - center.x);
  var xOffset = Math.sqrt(Math.pow(distance, 2) / (Math.pow(slope, 2) + 1));
  var yOffset = slope * xOffset;

  return new vector2d(center.x + xOffset * ((point.x <= center.x) ? 1 : -1), center.y + yOffset * ((point.x <= center.x) ? 1 : -1));
}

function clone(obj) {
    if(obj == null || typeof(obj) != 'object')
        return obj;

    var temp = new obj.constructor();
    for(var key in obj)
        temp[key] = clone(obj[key]);

    return temp;
}

function nullSafe(input) {
  if (isNaN(input)) {
    return 0;
  } else {
    return input;
  }
}

function lengthToPix(length) {
  console.log(imageWidth);
  return length * (imageWidth / fieldWidth);
}

function pixToLength(pix) {
  return pix * (fieldWidth / imageWidth);
}

function download(filename, text) {
  var element = document.createElement('a');
  element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
  element.setAttribute('download', filename);

  element.style.display = 'none';
  document.body.appendChild(element);

  element.click();

  document.body.removeChild(element);
}
