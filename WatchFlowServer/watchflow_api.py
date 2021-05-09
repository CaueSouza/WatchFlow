import flask
from flask import request
from Database import database
import socket

app = flask.Flask(__name__)
app.config["DEBUG"] = True


@app.route('/allRunningCameras', methods=['GET'])
def getAllRunningCameras():
    data = request.get_json()

    neededKeys = {'requesterUserId', 'requesterPwd'}

    if neededKeys <= data.keys():
        success, message = database.getCamerasDatabaseAsJSON(
            requesterUserId=data['requesterUserId'],
            requesterPwd=data['requesterPwd'])

        return message, 200 if success else 400
    else:
        return 'Missing params', 400


@app.route('/allCamerasIps', methods=['GET'])
def getAllCamerasIps():
    data = request.get_json()

    neededKeys = {'requesterUserId', 'requesterPwd'}

    if neededKeys <= data.keys():
        success, message = database.getCamerasDatabaseAsJSON(
            requesterUserId=data['requesterUserId'],
            requesterPwd=data['requesterPwd'],
            onlyIps=True)

        return message, 200 if success else 400
    else:
        return 'Missing params', 400


@app.route('/userLogin', methods=['POST'])
def userLogin():
    data = request.get_json()

    neededKeys = {'userName', 'pwd', 'latitude', 'longitude'}

    if neededKeys <= data.keys():
        success, message = database.userLogin(
            userName=data['userName'],
            pwd=data['pwd'],
            latitude=data['latitude'],
            longitude=data['longitude'])

        return message, 200 if success else 400
    else:
        return 'Missing params', 400


@app.route('/userLogout', methods=['POST'])
def userLogout():
    data = request.get_json()

    neededKeys = {'requesterUserId', 'requesterPwd'}

    if neededKeys <= data.keys():
        success, message = database.userLogout(
            requesterUserId=data['requesterUserId'],
            requesterPwd=data['requesterPwd'])

        return message, 200 if success else 400
    else:
        return 'Missing params', 400


@app.route('/registerUser', methods=['POST'])
def registerUser():
    data = request.get_json()

    neededKeys = {'requesterUserId',
                  'requesterPwd', 'newUserName', 'newUserPwd'}

    if neededKeys <= data.keys():
        success, message = database.createUser(
            requesterUserId=data['requesterUserId'],
            requesterPwd=data['requesterPwd'],
            newUserName=data['newUserName'],
            newUserPwd=data['newUserPwd'],
            userType=data['type'] if ('type' in data) else 0)

        return message, 200 if success else 400

    else:
        return 'Missing params', 400


@app.route('/deleteUser', methods=['DELETE'])
def deleteUser():
    data = request.get_json()

    neededKeys = {'requesterUserId', 'requesterPwd', 'oldUserId'}

    if neededKeys <= data.keys():
        success, message = database.deleteUser(
            requesterUserId=data['requesterUserId'],
            requesterPwd=data['requesterPwd'],
            oldUserName=data['oldUserName'])

        return message, 200 if success else 400

    else:
        return 'Missing params', 400


@app.route('/registerCamera', methods=['POST'])
def registerCamera():
    data = request.get_json()

    neededKeys = {'requesterUserId', 'requesterPwd',
                  'cameraIp', 'latitude', 'longitude'}

    if neededKeys <= data.keys():
        success, message = database.createCamera(
            requesterUserId=data['requesterUserId'],
            requesterPwd=data['requesterPwd'],
            cameraIp=data['cameraIp'],
            latitude=data['latitude'],
            longitude=data['longitude'])

        return message, 200 if success else 400
    else:
        return 'Missing params', 400


@app.route('/deleteCamera', methods=['DELETE'])
def deleteCamera():
    data = request.get_json()

    neededKeys = {'requesterUserId', 'requesterPwd', 'cameraIp'}

    if neededKeys <= data.keys():
        success, message = database.deleteCamera(
            requesterUserId=data['requesterUserId'],
            requesterPwd=data['requesterPwd'],
            cameraIp=data['cameraIp'])

        return message, 200 if success else 400

    else:
        return 'Missing params', 400


@app.route('/updateCamera', methods=['POST'])
def updateCamera():
    data = request.get_json()

    neededKeys = {'requesterUserId', 'requesterPwd', 'cameraIp', 'snapshot'}

    if neededKeys <= data.keys():
        success, message = database.updateCamera(
            requesterUserId=data['requesterUserId'],
            requesterPwd=data['requesterPwd'],
            cameraIp=data['cameraIp'],
            snapshot=data['snapshot'])

        return message, 200 if success else 400

    else:
        return 'Missing params', 400


@app.route('/usersPositions', methods=['POST'])
def usersPositions():
    data = request.get_json()

    neededKeys = {'requesterUserId', 'requesterPwd'}

    if neededKeys <= data.keys():
        success, jsonOutput = database.getAllLoggedUsersPositions(
            requesterUserId=data['requesterUserId'],
            requesterPwd=data['requesterPwd'])

        return jsonOutput, 200 if success else 400

    else:
        return 'Missing params', 400


if __name__ == '__main__':
    hostname = socket.gethostname()
    local_ip = socket.gethostbyname(hostname)

    app.run(host=local_ip, port=5000)
    # app.run()
