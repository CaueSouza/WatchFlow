from Database import database


if __name__ == '__main__':
    database.resetDatabase()

    exec(open('watchflow_api.py').read())
