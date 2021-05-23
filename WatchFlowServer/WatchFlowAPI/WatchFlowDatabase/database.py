import sqlite3 as sqlite
from pathlib import Path
import csv
import os
import uuid
from json import dumps
from base64 import b64encode

ENCODING = 'utf-8'
IMAGE_NAME = 'snapshot'
JSON_NAME = 'output.json'
FILES_PATH = 'WatchFlowAPI/WatchFlowDatabase/'


def convertToBinaryData(filename):
    with open(filename, 'rb') as file:
        blobData = file.read()
    return blobData


def executeQuery(query, data):
    conn = sqlite.connect(FILES_PATH + 'database.db')
    cursor = conn.cursor()

    cursor.execute(query, data)

    conn.commit()
    cursor.close()


def executeFetchallQuery(query, data=None):
    conn = sqlite.connect(FILES_PATH + 'database.db')
    cursor = conn.cursor()

    if data is not None:
        cursor.execute(query, data)
    else:
        cursor.execute(query)

    queryData = cursor.fetchall()
    cursor.close()

    return queryData


def createDatabases():

    if Path(FILES_PATH + 'database.db').is_file():
        pass

    else:
        # conectando...
        conn = sqlite.connect(FILES_PATH + 'database.db')

        # definindo um cursor
        cursor = conn.cursor()

        # criando a tabela (schema)
        cursor.execute("""
        CREATE TABLE users (
                id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                userId TEXT,
                userName TEXT NOT NULL,
                pwd TEXT NOT NULL,
                type INTEGER NOT NULL,
                latitude REAL,
                longitude REAL,
                logged INTEGER NOT NULL
        );
        """)

        cursor.execute("""
        CREATE TABLE cameras (
                id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                ip TEXT NOT NULL,
                latitude REAL NOT NULL,
                longitude REAL NOT NULL,
                snapshot BLOB
        );
        """)

        # desconectando...
        cursor.close()


def resetDatabase():
    os.remove(FILES_PATH + 'database.db')
    createDatabases()

    conn = sqlite.connect(FILES_PATH + 'database.db')
    cursor = conn.cursor()

    users_file = open(FILES_PATH + 'users_tcc.csv')
    users_rows = csv.reader(users_file)

    sql = """INSERT INTO users (userName, pwd, type, latitude, longitude, logged)
    VALUES (?, ?, ?, ?, ?, 0)"""

    cursor.executemany(sql, users_rows)

    cams_file = open(FILES_PATH + 'cameras.csv')
    cams_rows = csv.reader(cams_file)

    cursor.executemany(
        "INSERT INTO cameras (ip, latitude, longitude) VALUES (?, ?, ?)",
        cams_rows)

    conn.commit()

    img_data1 = convertToBinaryData(FILES_PATH + 'cat.jpg')
    img_data2 = convertToBinaryData(FILES_PATH + 'dog.jpg')
    _, retorno = userLogin('admin', 'admin', 23.5505, 46.6333)

    updateCamera(retorno['userId'], 'admin',
                 '127.0.0.1', b64encode(img_data1))
    updateCamera(retorno['userId'], 'admin',
                 '127.0.0.2', b64encode(img_data2))

    conn.commit()
    cursor.close()


def getCamerasDatabaseAsJSON(requesterUserId, requesterPwd, onlyIps=False):

    if validateUser(requesterUserId, requesterPwd):

        json_list = []
        json_output = {'cameras': json_list}

        query = "SELECT * FROM cameras"

        queryResult = executeFetchallQuery(query)

        if onlyIps:
            for row in queryResult:
                json_dict = {'ip': row[1]}
                json_list.append(json_dict)

        else:
            for row in queryResult:
                json_dict = {'ip': row[1],
                             'latitude': row[2],
                             'longitude': row[3]}
                json_list.append(json_dict)

        return (True, {'message': json_output})
    else:
        return (False, {'message': 'Invalid credentials'})


def getCamerasIpsAsJSON():

    json_list = []
    json_output = {'cameras': json_list}

    query = "SELECT * FROM cameras"

    queryResult = executeFetchallQuery(query)
    for row in queryResult:
        json_dict = {'ip': row[1]}
        json_list.append(json_dict)

    return json_output


def validateUserName(userName, pwd):
    query = """
            SELECT
                type
            FROM
                users
            WHERE
                userName=? AND pwd=?
    """

    data = (userName, pwd)

    user = executeFetchallQuery(query, data)

    if len(user) == 1:
        return True
    else:
        return False


def validateUser(userId, pwd, shouldValidateAdmin=False):

    query = """
            SELECT
                type
            FROM
                users
            WHERE
                userId=? AND pwd=? AND logged=1
    """

    data = (userId, pwd)

    user = executeFetchallQuery(query, data)

    if len(user) == 1:
        if shouldValidateAdmin:
            if user[0][0] == 1:
                return True
            else:
                return False
        else:
            return True
    else:
        return False


def createUser(requesterUserId, requesterPwd, newUserName, newUserPwd,
               userType):

    if validateUser(requesterUserId, requesterPwd, shouldValidateAdmin=True):
        query = """
                INSERT INTO
                    users
                    (userName, pwd, type, logged)
                VALUES
                    (?,?,?,0)
        """

        data = (newUserName, newUserPwd, userType)

        executeQuery(query, data)

        return (True, {'message': 'User registered'})
    else:
        return (False, {'message': 'Invalid credentials'})


def deleteUser(requesterUserId, requesterPwd, oldUserName):

    if validateUser(requesterUserId, requesterPwd, shouldValidateAdmin=True):
        query = """
                DELETE FROM
                    users
                WHERE
                    userName=?
        """

        data = (oldUserName,)

        executeQuery(query, data)

        return (True, {'message': 'User deleted'})
    else:

        return (False, {'message': 'Invalid credentials'})


def userLogin(userName, pwd, latitude, longitude):

    if validateUserName(userName, pwd):
        userId = str(uuid.uuid4())

        query = """
                UPDATE
                    users
                SET
                    userId = ?,
                    latitude = ?,
                    longitude = ?,
                    logged = 1
                WHERE
                    userName=? AND pwd=?
        """

        data = (userId, latitude, longitude, userName, pwd)

        executeQuery(query, data)

        isAdm = validateUser(userId, pwd, True)

        return (True, {'userId': userId, 'pwd': pwd,
                       'userType': 1 if isAdm else 0})
    else:
        return (False, {'message': 'Login failed'})


def userLogout(requesterUserId, requesterPwd):
    if validateUser(requesterUserId, requesterPwd):
        query = """
                UPDATE
                    users
                SET
                    logged = 0
                WHERE
                    userId=? AND pwd=?
        """

        data = (requesterUserId, requesterPwd)

        executeQuery(query, data)

        return (True, {'message': 'Logout successfull'})
    else:
        return (False, {'message': 'Logout failed'})


def createCamera(requesterUserId, requesterPwd, cameraIp, latitude, longitude):

    if validateUser(requesterUserId, requesterPwd, shouldValidateAdmin=True):
        query = """
                INSERT INTO
                    cameras
                    (ip, latitude, longitude)
                VALUES
                    (?,?,?)
        """

        data = (cameraIp, latitude, longitude)

        executeQuery(query, data)

        return (True, {'message': 'Camera registered'})
    else:
        return (False, {'message': 'Invalid user'})


def deleteCamera(requesterUserId, requesterPwd, cameraIp):

    if validateUser(requesterUserId, requesterPwd, shouldValidateAdmin=True):
        query = "DELETE FROM cameras WHERE ip=?"

        data = (cameraIp,)

        executeQuery(query, data)

        return (True, {'message': 'Camera deleted'})
    else:
        return (False, {'message': 'Invalid credentials'})


def updateCamera(requesterUserId, requesterPwd, cameraIp, snapshot):
    if validateUser(requesterUserId, requesterPwd, shouldValidateAdmin=True):
        query = """
                UPDATE
                    cameras
                SET
                    snapshot=?
                WHERE
                    ip=?
        """

        data = (snapshot, cameraIp)

        executeQuery(query, data)

        return (True, {'message': 'Camera updated'})
    else:
        return (False, {'message': 'Invalid credentials'})


def getAllLoggedUsersPositions(requesterUserId, requesterPwd):
    if validateUser(requesterUserId, requesterPwd):

        json_list = []
        json_output = {'locations': json_list}

        query = """
                SELECT
                    userName,
                    latitude,
                    longitude
                FROM
                    users
                WHERE
                    userId!=? AND logged=1
        """

        data = (requesterUserId,)

        queryResult = executeFetchallQuery(query, data)

        for row in queryResult:
            json_dict = {'userName': row[0],
                         'latitude': row[1],
                         'longitude': row[2]}
            json_list.append(json_dict)

        return (True, {'message': json_output})
    else:
        return (False, {'message': 'Invalid credentials'})


def cameraInformations(requesterUserId, requesterPwd, cameraIp):
    if validateUser(requesterUserId, requesterPwd):

        query = """
                SELECT
                    snapshot
                FROM
                    cameras
                WHERE
                    ip=?
        """

        data = (cameraIp,)

        queryResult = executeFetchallQuery(query, data)

        response = queryResult[0][0]

        base64_string = response.decode(ENCODING)
        raw_data = {IMAGE_NAME: base64_string}
        json_data = dumps(raw_data)

        return (True, json_data)
    else:
        return (False, {'message': 'Invalid credentials'})