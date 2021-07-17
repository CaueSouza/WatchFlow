from WatchFlowAPI.WatchFlowDatabase import database
import logging
import threading
from .ImageDetection import imgDetection
import cv2


def evalCamera(name):
    logging.info("Thread %s: starting", name['ip'])

    rec = imgDetection.runImgDetection(imagem=cv2.imread(
        'D:\Projetos\TCC\WatchFlow\WatchFlowServer\WatchFlowAI\img1.jpg'))
    print(rec)

    logging.info("Thread %s: finishing", name['ip'])

# def saveReconToDB():


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
