# from cv2 import data
from WatchFlowAPI.WatchFlowDatabase import database
import logging
import threading
from .ImageDetection import imgDetection
# import cv2
sem = threading.Semaphore()

IMAGE1 = 'D:\Projetos\TCC\WatchFlow\WatchFlowServer\WatchFlowAI\img1.jpg'
IMAGE2 = 'D:\Projetos\TCC\WatchFlow\WatchFlowServer\WatchFlowAI\img2.jpg'
VIDEO1 = 'D:\Projetos\TCC\WatchFlow\WatchFlowServer\WatchFlowAI\\video1.mp4'
VIDEO2 = 'D:\Projetos\TCC\WatchFlow\WatchFlowServer\WatchFlowAI\\video2.mp4'
TEST_VID = 'D:\Projetos\TCC\WatchFlow\WatchFlowServer\WatchFlowAI\\testvid.mp4'


def evalCamera(name):
    IP = name['ip']
    logging.info("Thread %s: starting", IP)

    # TODO RECEIVE SNAPSHOT FROM DB AND AS A IMG PASS THROUGH

    # CODE FOR MOCKED VIDEOS RECOGNITIONS
    # if IP == '127.0.0.1':
    #     recognitions, frame = imgDetection.runVideoDetection(TEST_VID)
    # else:
    #     recognitions, frame = imgDetection.runVideoDetection(TEST_VID)

    recognitions, frame = imgDetection.runVideoDetection(TEST_VID)
    database.saveReconToDatabase(IP, recognitions)
    database.saveFrameToDatabase(IP, frame)

    # # CODE FOR MOCKED IMAGES RECOGNITIONS
    # image = cv2.imread(IMAGE1 if name['ip'] == '127.0.0.1' else IMAGE2)
    # rec = imgDetection.runImgDetection(image)

    # print(rec)
    # # sem.acquire()
    # database.saveReconToDatabase(name['ip'], rec)
    # # sem.release()

    logging.info("Thread %s: finishing", IP)


def run():
    cameras = database.getCamerasIpsAsJSON()['cameras']

    format = "%(asctime)s: %(message)s"
    logging.basicConfig(format=format, level=logging.INFO,
                        datefmt="%H:%M:%S")

    threads = list()
    for ip in cameras:
        x = threading.Thread(target=evalCamera, args=(ip,))
        threads.append(x)
        x.start()

    for ip, thread in enumerate(threads):
        thread.join()


if __name__ == '__main__':
    run()
