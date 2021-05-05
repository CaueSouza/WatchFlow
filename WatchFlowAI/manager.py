import logging
import threading
import time
import requests

# GET ALL CAMERAS IPS ENDPOINTS
URL = "http://127.0.0.1:5000/allCamerasIps"


def thread_function(name):
    logging.info("Thread %s: starting", name)
    time.sleep(2)
    logging.info("Thread %s: finishing", name)


def run():

    r = requests.get(url=URL)
    r = r.json()

    response = r['response']
    cameras = response['cameras']

    format = "%(asctime)s: %(message)s"
    logging.basicConfig(format=format, level=logging.INFO,
                        datefmt="%H:%M:%S")

    threads = list()
    for ip in cameras:
        x = threading.Thread(target=thread_function, args=(ip,))
        threads.append(x)
        x.start()

    for ip, thread in enumerate(threads):
        thread.join()
