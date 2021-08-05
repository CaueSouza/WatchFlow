import cv2
import numpy as np
import os
import math
import time

dir_path = os.path.dirname(os.path.realpath(__file__))

labels_path = os.path.sep.join([dir_path, 'watchflow.names'])
weigths_path = os.path.sep.join([dir_path, 'watchflow.weights'])
config_path = os.path.sep.join([dir_path, 'watchflow.cfg'])

LABELS = open(labels_path).read().strip().split('\n')
THRESHOLD = 0.5
THRESHOLD_NMS = 0.3


def redimension(image, maxWidth=600):
    if image.shape[1] > maxWidth:
        proportion = image.shape[1] / image.shape[0]
        imgWidth = maxWidth
        imgHeight = int(imgWidth / proportion)
    else:
        imgWidth = image.shape[1]
        imgHeight = image.shape[0]

    image = cv2.resize(image, (imgWidth, imgHeight))
    return image


def blob_image(net, ln, image):
    blob = cv2.dnn.blobFromImage(
        image, 1/255.0, (416, 416), swapRB=True, crop=False)
    net.setInput(blob)
    layerOutputs = net.forward(ln)

    return net, image, layerOutputs


def runVideoDetection(video_file):
    firstSec = math.ceil(time.time())

    cap = cv2.VideoCapture(video_file)
    _, video = cap.read()

    width = video.shape[1]
    height = video.shape[0]

    frame_counter = 0

    last_frame = None
    total_rec = resetVideoDetectionDict()

    while (True):
        conn, frame = cap.read()
        frame_counter += 1

        if not conn:
            break

        if math.ceil(time.time()) - firstSec > 30:  # STOP AFTER 60 SECONDS
            break

        frame = cv2.resize(frame, (width, height))
        last_frame = frame
        rec = runImgDetection(frame)
        total_rec = updateVideoDetectionDict(total_rec, rec)

    cap.release()

    total_rec = calculateMeanRecognitions(total_rec, frame_counter)
    total_rec['total'] = sum(total_rec.values())

    return total_rec, last_frame


def updateVideoDetectionDict(total_rec, recognitions):
    return {name: value + recognitions[name]
            for name, value in total_rec.items()}


def calculateMeanRecognitions(total_rec, total_frames):
    return {name: math.ceil(value/total_frames)
            for name, value in total_rec.items()}


def resetVideoDetectionDict():
    return {
        'articulated_truck': 0,
        'bicycle': 0,
        'bus': 0,
        'car': 0,
        'motorcycle': 0,
        'motorized_vehicle': 0,
        'non-motorized_vehicle': 0,
        'pedestrian': 0,
        'pickup_truck': 0,
        'single_unit_truck': 0,
        'work_van': 0
    }


def runImgDetection(image):
    net = cv2.dnn.readNet(config_path, weigths_path)
    ln = net.getLayerNames()
    ln = [ln[i[0] - 1] for i in net.getUnconnectedOutLayers()]

    allDetections = np.zeros((len(LABELS), ), dtype=int)
    boxes = []
    trusts = []
    IDclasses = []

    image = redimension(image)

    (h, w) = image.shape[:2]

    net, image, layerOutputs = blob_image(net, ln, image)

    for output in layerOutputs:
        for detection in output:
            scores = detection[5:]
            classeId = np.argmax(scores)
            confianca = scores[classeId]
            if confianca > THRESHOLD:

                box = detection[0:4] * np.array([w, h, w, h])
                (centerX, centerY, width, height) = box.astype('int')

                x = int(centerX - (width / 2))
                y = int(centerY - (height / 2))

                boxes.append([x, y, int(width), int(height)])
                trusts.append(float(confianca))
                IDclasses.append(classeId)

    objs = cv2.dnn.NMSBoxes(boxes, trusts, THRESHOLD, THRESHOLD_NMS)

    if len(objs) > 0:
        for i in objs.flatten():
            allDetections[IDclasses[i]] += 1

    recognitions = {name: 0 for name in LABELS}
    for key in recognitions:
        recognitions[key] = allDetections[LABELS.index(key)]

    recognitions['total'] = len(objs)

    return recognitions
