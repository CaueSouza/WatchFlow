from WatchFlowAPI.WatchFlowDatabase import database
import logging
import threading
from .ImageDetection import imgDetection
import random
import os
import calendar
import time

sem = threading.Semaphore()

SRC_FILE_PATH = os.path.dirname(__file__) + '\\src'
IMAGE1 = '\\img1.jpg'
IMAGE2 = '\\img2.jpg'
FULL_VIDEO1 = '\\fullvideo1.mp4'
FULL_VIDEO2 = '\\fullvideo2.mp4'
VIDEO1_1 = '\\video1_1.mp4'
VIDEO2_1 = '\\video2_1.mp4'
VIDEO2_2 = '\\video2_2.mp4'
VIDEO2_3 = '\\video2_3.mp4'

TEST_VIDEOS = {VIDEO1_1, VIDEO2_1, VIDEO2_2, VIDEO2_3}


def evalCamera(name):
    IP = name['ip']
    logging.info("-Thread %s: starting", IP)

    chooseVid = random.choice(tuple(TEST_VIDEOS))
    print(IP + ': ' + chooseVid)

    recognitions, frame = imgDetection.runVideoDetection(
        SRC_FILE_PATH + chooseVid)

    saveDataToDatabase(IP, frame, recognitions)

    logging.info("-Thread %s: finishing", IP)

def saveDataToDatabase(IP, frame, recognitions):
    if database.doesCamExist(IP) :
        database.saveReconToCamerasDatabase(IP, recognitions)
        database.saveFrameToCameraDatabase(IP, frame)
        database.saveReconToHistoricDatabase(IP, recognitions, calendar.timegm(time.gmtime()))

def runThreads():
    threading.Timer(60.0, runThreads).start()

    cameras = database.getCamerasIpsAsJSON()['cameras']

    threads = list()
    for ip in cameras:
        x = threading.Thread(target=evalCamera, args=(ip,))
        threads.append(x)
        x.start()

    for ip, thread in enumerate(threads):
        thread.join()


def run():
    format = "%(asctime)s: %(message)s"
    logging.basicConfig(format=format, level=logging.INFO,
                        datefmt="%H:%M:%S")

    runThreads()
