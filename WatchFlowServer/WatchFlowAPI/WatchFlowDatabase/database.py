import io
import sqlite3 as sqlite
from pathlib import Path
import csv
import os
import uuid
from json import dumps
from base64 import b64encode
from PIL import Image
import cv2

ENCODING = 'utf-8'
IMAGE_NAME = 'snapshot'
JSON_NAME = 'output.json'
FILES_PATH = 'WatchFlowAPI/WatchFlowDatabase/'


def convertToBinaryData(filename):
    with open(filename, 'rb') as file:
        blobData = file.read()
    return blobData


def convertFrameToBinaryData(frame):
    color_converted = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
    snapshot = Image.fromarray(color_converted)
    b = io.BytesIO()
    snapshot.save(b, 'jpeg')
    im_bytes = b.getvalue()
    return im_bytes


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
                phone INTEGER NOT NULL,
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
                snapshot BLOB,
                total INTEGER,
                articulated_truck INTEGER,
                bicycle INTEGER,
                bus INTEGER,
                car INTEGER,
                motorcycle INTEGER,
                motorized_vehicle INTEGER,
                non_motorized_vehicle INTEGER,
                pedestrian INTEGER,
                pickup_truck INTEGER,
                single_unit_truck INTEGER,
                work_van INTEGER
        );
        """)

        cursor.execute("""
        CREATE TABLE historic (
                id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                ip TEXT NOT NULL,
                timestamp REAL NOT NULL,
                total INTEGER,
                articulated_truck INTEGER,
                bicycle INTEGER,
                bus INTEGER,
                car INTEGER,
                motorcycle INTEGER,
                motorized_vehicle INTEGER,
                non_motorized_vehicle INTEGER,
                pedestrian INTEGER,
                pickup_truck INTEGER,
                single_unit_truck INTEGER,
                work_van INTEGER
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

    sql = """INSERT INTO users (userName, pwd, phone, type, latitude, longitude, logged)
    VALUES (?, ?, ?, ?, ?, ?, 0)"""

    cursor.executemany(sql, users_rows)

    cams_file = open(FILES_PATH + 'cameras.csv')
    cams_rows = csv.reader(cams_file)

    cursor.executemany(
        "INSERT INTO cameras (ip, latitude, longitude) VALUES (?, ?, ?)",
        cams_rows)

    cursor.execute("""
        UPDATE cameras
        SET total = 0,
            articulated_truck = 0,
            bicycle = 0,
            bus = 0,
            car = 0,
            motorcycle = 0,
            motorized_vehicle = 0,
            non_motorized_vehicle = 0,
            pedestrian = 0,
            pickup_truck = 0,
            single_unit_truck = 0,
            work_van = 0
    """)

    conn.commit()

    img_data1 = convertToBinaryData(FILES_PATH + 'img1.jpg')
    img_data2 = convertToBinaryData(FILES_PATH + 'img2.jpg')
    _, retorno = userLogin('admin', 'admin', -19.9167, -43.9345)
    _, retorno = userLogin('Joe', 'senha1', -23.5535, -46.6767)

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
                # Checking if snapshot is available for that camera
                if row[4] is not None:
                    json_dict = {'ip': row[1],
                                 'latitude': row[2],
                                 'longitude': row[3]}
                    json_list.append(json_dict)

        return (True, {'message': json_output})
    else:
        return (False, {'message': 'Invalid credentials'})


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
               newUserPhone, userType):
    if validateUser(requesterUserId, requesterPwd, shouldValidateAdmin=True):
        query = """
                INSERT INTO
                    users
                    (userName, pwd, phone, type, logged)
                VALUES
                    (?,?,?,?,0)
        """

        data = (newUserName, newUserPwd, newUserPhone, userType)

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


def ipAlreadyRegistered(cameraIp):
    query = """SELECT * FROM cameras WHERE ip=?"""

    data = (cameraIp, )

    queryResult = executeFetchallQuery(query, data)

    if len(queryResult) > 0:
        return True
    else:
        return False


def createCamera(requesterUserId, requesterPwd, cameraIp, latitude, longitude):
    if validateUser(requesterUserId, requesterPwd, shouldValidateAdmin=True):
        if ipAlreadyRegistered(cameraIp):
            return (False, {'message': 'Ip already registered'})
        else:
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
                    snapshot,
                    latitude,
                    longitude,
                    total,
                    articulated_truck,
                    bicycle,
                    bus,
                    car,
                    motorcycle,
                    motorized_vehicle,
                    non_motorized_vehicle,
                    pedestrian,
                    pickup_truck,
                    single_unit_truck,
                    work_van
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

        raw_data['latitude'] = queryResult[0][1]
        raw_data['longitude'] = queryResult[0][2]

        recognitions = {}
        recognitions['total'] = queryResult[0][3]
        recognitions['articulated_truck'] = queryResult[0][4]
        recognitions['bicycle'] = queryResult[0][5]
        recognitions['bus'] = queryResult[0][6]
        recognitions['car'] = queryResult[0][7]
        recognitions['motorcycle'] = queryResult[0][8]
        recognitions['motorized_vehicle'] = queryResult[0][9]
        recognitions['non_motorized_vehicle'] = queryResult[0][10]
        recognitions['pedestrian'] = queryResult[0][11]
        recognitions['pickup_truck'] = queryResult[0][12]
        recognitions['single_unit_truck'] = queryResult[0][13]
        recognitions['work_van'] = queryResult[0][14]

        raw_data['recognitions'] = recognitions

        json_data = dumps(raw_data)

        return (True, json_data)
    else:
        return (False, {'message': 'Invalid credentials'})


def userInformations(requesterUserId, requesterPwd, username):
    if validateUser(requesterUserId, requesterPwd):

        query = """
                SELECT
                    phone,
                    latitude,
                    longitude
                FROM
                    users
                WHERE
                    username=? AND logged=1
        """

        data = (username,)

        queryResult = executeFetchallQuery(query, data)[0]

        json_dict = {'phone': queryResult[0],
                     'latitude': queryResult[1],
                     'longitude': queryResult[2]}

        return (True, json_dict)
    else:
        return (False, {'message': 'Invalid credentials'})


def updatePhone(requesterUserId, requesterPwd, newUserPhone):
    if validateUser(requesterUserId, requesterPwd):
        query = """
                UPDATE
                    users
                SET
                    phone=?
                WHERE
                    userId=? AND pwd=?
        """

        data = (newUserPhone, requesterUserId, requesterPwd)

        executeQuery(query, data)

        return (True, {'message': 'Phone updated'})
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


def saveReconToCamerasDatabase(cameraIp, recognitions):
    query = """
            UPDATE
                cameras
            SET
                total=?,
                articulated_truck=?,
                bicycle=?,
                bus=?,
                car=?,
                motorcycle=?,
                motorized_vehicle=?,
                non_motorized_vehicle=?,
                pedestrian=?,
                pickup_truck=?,
                single_unit_truck=?,
                work_van=?
            WHERE
                ip=?
    """

    data = (int(recognitions['total']),
            int(recognitions['articulated_truck']),
            int(recognitions['bicycle']),
            int(recognitions['bus']),
            int(recognitions['car']),
            int(recognitions['motorcycle']),
            int(recognitions['motorized_vehicle']),
            int(recognitions['non-motorized_vehicle']),
            int(recognitions['pedestrian']),
            int(recognitions['pickup_truck']),
            int(recognitions['single_unit_truck']),
            int(recognitions['work_van']),
            cameraIp)

    executeQuery(query, data)

def saveReconToHistoricDatabase(cameraIp, recognitions, timestamp):
    query = """
            INSERT INTO
                historic
                (   
                    ip, 
                    timestamp,
                    total,
                    articulated_truck,
                    bicycle,
                    bus,
                    car,
                    motorcycle,
                    motorized_vehicle,
                    non_motorized_vehicle,
                    pedestrian,
                    pickup_truck,
                    single_unit_truck,
                    work_van
                )
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)
    """

    data = (cameraIp,
            timestamp,
            int(recognitions['total']),
            int(recognitions['articulated_truck']),
            int(recognitions['bicycle']),
            int(recognitions['bus']),
            int(recognitions['car']),
            int(recognitions['motorcycle']),
            int(recognitions['motorized_vehicle']),
            int(recognitions['non-motorized_vehicle']),
            int(recognitions['pedestrian']),
            int(recognitions['pickup_truck']),
            int(recognitions['single_unit_truck']),
            int(recognitions['work_van']))

    executeQuery(query, data)


def saveFrameToCameraDatabase(cameraIp, frame):
    # Converting frame to image
    binary_snapshot = convertFrameToBinaryData(frame)

    query = """
            UPDATE
                cameras
            SET
                snapshot=?
            WHERE
                ip=?
    """

    data = (b64encode(binary_snapshot), cameraIp)

    executeQuery(query, data)
