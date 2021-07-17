from WatchFlowAPI.WatchFlowDatabase import database
import logging
import threading
from .ImageDetection import imgDetection
import cv2
sem = threading.Semaphore()


def evalCamera(name):
    logging.info("Thread %s: starting", name['ip'])

    # TODO RECEIVE SNAPSHOT FROM DB AND AS A IMG PASS THROUGH]
    if name['ip'] == '127.0.0.1':
        rec = imgDetection.runImgDetection(imagem=cv2.imread(
            'D:\Projetos\TCC\WatchFlow\WatchFlowServer\WatchFlowAI\img1.jpg'))
    elif name['ip'] == '127.0.0.2':
        rec = imgDetection.runImgDetection(imagem=cv2.imread(
            'D:\Projetos\TCC\WatchFlow\WatchFlowServer\WatchFlowAI\img2.jpg'))

    print(rec)
    # sem.acquire()
    database.saveReconToDatabase(name['ip'], rec)
    # sem.release()

    logging.info("Thread %s: finishing", name['ip'])


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
