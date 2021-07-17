import cv2
import numpy as np
import os

dir_path = os.path.dirname(os.path.realpath(__file__))

labels_path = os.path.sep.join([dir_path, 'watchflow.names'])
weigths_path = os.path.sep.join([dir_path, 'watchflow.weights'])
config_path = os.path.sep.join([dir_path, 'watchflow.cfg'])

LABELS = open(labels_path).read().strip().split('\n')
THRESHOLD = 0.5
THRESHOLD_NMS = 0.3


def runImgDetection(imagem):
    net = cv2.dnn.readNet(config_path, weigths_path)
    ln = net.getLayerNames()
    ln = [ln[i[0] - 1] for i in net.getUnconnectedOutLayers()]

    allDetections = np.zeros((len(LABELS), ), dtype=int)
    caixas = []
    confiancas = []
    IDclasses = []

    (h, w) = imagem.shape[:2]

    blob = cv2.dnn.blobFromImage(
        imagem, 1/255.0, (416, 416), swapRB=True, crop=False)
    net.setInput(blob)
    layer_outputs = net.forward(ln)

    for output in layer_outputs:
        for detection in output:
            scores = detection[5:]
            classeId = np.argmax(scores)
            confianca = scores[classeId]
            if confianca > THRESHOLD:

                caixa = detection[0:4] * np.array([w, h, w, h])
                (centerX, centerY, width, height) = caixa.astype('int')

                x = int(centerX - (width / 2))
                y = int(centerY - (height / 2))

                caixas.append([x, y, int(width), int(height)])
                confiancas.append(float(confianca))
                IDclasses.append(classeId)

    objs = cv2.dnn.NMSBoxes(caixas, confiancas, THRESHOLD, THRESHOLD_NMS)

    if len(objs) > 0:
        for i in objs.flatten():
            allDetections[IDclasses[i]] += 1

    recognitions = {name: 0 for name in LABELS}
    for key in recognitions:
        recognitions[key] = allDetections[LABELS.index(key)]

    recognitions['total'] = len(objs)

    return recognitions
