from WatchFlowAPI import watchflow_api
from WatchFlowAI import manager

if __name__ == '__main__':
    # RUN BOTH IN DIFFERENT THREADS
    watchflow_api.run()
    # manager.run()
