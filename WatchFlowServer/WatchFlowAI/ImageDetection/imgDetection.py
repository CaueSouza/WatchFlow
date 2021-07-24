import cv2
import numpy as np
import os
import time
from PIL import Image

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
    cap = cv2.VideoCapture(video_file)
    _, video = cap.read()

    width = video.shape[1]
    height = video.shape[0]

    # frame_counter = 0

    t_start = time.time()
    last_frame = None
    total_rec = resetVideoDetectionDict()
    # total_resets = 0

    while (True):
        conn, frame = cap.read()
        # frame_counter += 1

        if not conn:
            break
        # if total_resets == 2:
        #     break

        # print(frame_counter)

        # if frame_counter == cap.get(cv2.CAP_PROP_FRAME_COUNT):
        #     frame_counter = 0
        #     total_resets += 1
        #     cap = cv2.VideoCapture(video_file)
        #     _, frame = cap.read()

        frame = cv2.resize(frame, (width, height))
        last_frame = frame
        rec = runImgDetection(frame)
        updateVideoDetectionDict(total_rec, rec)

    cap.release()
    print("{:.2f}".format(time.time() - t_start))
    return total_rec, last_frame


def updateVideoDetectionDict(total_recognitions, recognitions):
    total_recognitions['articulated_truck'] += recognitions['articulated_truck']
    total_recognitions['bicycle'] += recognitions['bicycle']
    total_recognitions['bus'] += recognitions['bus']
    total_recognitions['car'] += recognitions['car']
    total_recognitions['motorcycle'] += recognitions['motorcycle']
    total_recognitions['motorized_vehicle'] += recognitions['motorized_vehicle']
    total_recognitions['non-motorized_vehicle'] += recognitions['non-motorized_vehicle']
    total_recognitions['pedestrian'] += recognitions['pedestrian']
    total_recognitions['pickup_truck'] += recognitions['pickup_truck']
    total_recognitions['single_unit_truck'] += recognitions['single_unit_truck']
    total_recognitions['work_van'] += recognitions['work_van']
    total_recognitions['total'] += recognitions['total']


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
        'work_van': 0,
        'total': 0
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
