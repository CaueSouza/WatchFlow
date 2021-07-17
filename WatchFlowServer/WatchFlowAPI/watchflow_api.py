import flask
from flask import request
from .WatchFlowDatabase import database
from geopy.geocoders import Nominatim

app = flask.Flask(__name__)
app.config["DEBUG"] = True

locator = Nominatim(user_agent='WatchFlow')

AUTHORIZATION = "Authorization"


@app.route('/allRunningCameras', methods=['GET'])
def allRunningCameras():
    if AUTHORIZATION not in request.headers:
        return 'Missing headers', 400

    headers = eval(request.headers.get(AUTHORIZATION))

    neededKeys = {'requesterUserId', 'requesterPwd'}

    if neededKeys <= headers.keys():
        success, message = database.getCamerasDatabaseAsJSON(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'])

        return message, 200 if success else 400
    else:
        return 'Missing data', 400


@app.route('/allCamerasIps', methods=['GET'])
def allCamerasIps():
    return database.getCamerasIpsAsJSON()


@app.route('/cameraInformations', methods=['GET'])
def cameraInformations():
    if AUTHORIZATION not in request.headers:
        return 'Missing headers', 400

    headers = eval(request.headers.get(AUTHORIZATION))

    neededHeadersKeys = {'requesterUserId', 'requesterPwd', 'cameraIp'}

    if neededHeadersKeys <= headers.keys():
        _, message = database.cameraInformations(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'],
            cameraIp=headers['cameraIp'])

        # print(message)

        return message, 200

    else:
        return 'Missing data', 400


@app.route('/usersPositions', methods=['GET'])
def usersPositions():
    if AUTHORIZATION not in request.headers:
        return 'Missing headers', 400

    headers = eval(request.headers.get(AUTHORIZATION))

    neededKeys = {'requesterUserId', 'requesterPwd'}

    if neededKeys <= headers.keys():
        success, jsonOutput = database.getAllLoggedUsersPositions(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'])

        return jsonOutput, 200 if success else 400

    else:
        return 'Missing data', 400


@app.route('/userLogin', methods=['POST'])
def userLogin():

    if AUTHORIZATION not in request.headers or request.get_json() is None:
        return 'Missing headers', 400

    headers = eval(request.headers.get(AUTHORIZATION))
    body = request.get_json()

    neededHeadersKeys = {'userName', 'pwd'}
    neededBodyKeys = {'latitude', 'longitude'}

    if neededHeadersKeys <= headers.keys() and neededBodyKeys <= body.keys():
        success, message = database.userLogin(
            userName=headers['userName'],
            pwd=headers['pwd'],
            latitude=body['latitude'],
            longitude=body['longitude'])

        return message, 200 if success else 400
    else:
        return 'Missing data', 400


@app.route('/userLogout', methods=['POST'])
def userLogout():
    if AUTHORIZATION not in request.headers:
        return 'Missing headers', 400

    headers = eval(request.headers.get(AUTHORIZATION))

    neededKeys = {'requesterUserId', 'requesterPwd'}

    if neededKeys <= headers.keys():
        success, message = database.userLogout(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'])

        return message, 200 if success else 400
    else:
        return 'Missing data', 400


@app.route('/registerUser', methods=['POST'])
def registerUser():
    if AUTHORIZATION not in request.headers or request.get_json() is None:
        return 'Missing headers', 400

    headers = eval(request.headers.get(AUTHORIZATION))
    body = request.get_json()

    neededHeadersKeys = {'requesterUserId', 'requesterPwd'}
    neededBodyKeys = {'newUserName', 'newUserPwd'}

    if neededHeadersKeys <= headers.keys() and neededBodyKeys <= body.keys():
        success, message = database.createUser(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'],
            newUserName=body['newUserName'],
            newUserPwd=body['newUserPwd'],
            userType=body['type'] if ('type' in body) else 0)

        return message, 200 if success else 400

    else:
        return 'Missing data', 400


@app.route('/deleteUser', methods=['DELETE'])
def deleteUser():
    if AUTHORIZATION not in request.headers:
        return 'Missing headers', 400

    headers = eval(request.headers.get(AUTHORIZATION))

    neededHeadersKeys = {'requesterUserId', 'requesterPwd', 'oldUserName'}

    if neededHeadersKeys <= headers.keys():
        success, message = database.deleteUser(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'],
            oldUserName=headers['oldUserName'])

        return message, 200 if success else 400

    else:
        return 'Missing params', 400


@app.route('/registerCamera', methods=['POST'])
def registerCamera():
    if AUTHORIZATION not in request.headers or request.get_json() is None:
        return 'Missing headers', 400

    headers = eval(request.headers.get(AUTHORIZATION))
    body = request.get_json()

    neededHeadersKeys = {'requesterUserId', 'requesterPwd'}
    neededBodyKeys = {'cameraIp', 'street', 'number',
                      'neighborhood', 'city', 'country'}

    if neededHeadersKeys <= headers.keys() and neededBodyKeys <= body.keys():
        cameraIp = body.pop('cameraIp', None)
        fullAddress = ''

        for data in body:
            fullAddress += f' {body[data]}'

        location = locator.geocode(fullAddress)

        success, message = database.createCamera(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'],
            cameraIp=cameraIp,
            latitude=location.latitude,
            longitude=location.longitude)

        return message, 200 if success else 400
    else:
        return 'Missing params', 400


@app.route('/deleteCamera', methods=['DELETE'])
def deleteCamera():
    if AUTHORIZATION not in request.headers:
        return 'Missing headers', 400

    headers = eval(request.headers.get(AUTHORIZATION))

    neededHeadersKeys = {'requesterUserId', 'requesterPwd', 'cameraIp'}

    if neededHeadersKeys <= headers.keys():
        success, message = database.deleteCamera(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'],
            cameraIp=headers['cameraIp'])

        return message, 200 if success else 400

    else:
        return 'Missing params', 400


@app.route('/updateCamera', methods=['POST'])  # UNFINISHED REQUEST
def updateCamera():
    dataJson = request.get_json()
    dataArgs = request.args.to_dict()

    if dataJson is None and not bool(dataArgs):
        return 'Missing params', 400

    else:
        if not bool(dataArgs):
            data = dataJson
        else:
            data = dataArgs

        neededKeys = {'requesterUserId',
                      'requesterPwd', 'cameraIp', 'snapshot'}

        if neededKeys <= data.keys():
            success, message = database.updateCamera(
                requesterUserId=data['requesterUserId'],
                requesterPwd=data['requesterPwd'],
                cameraIp=data['cameraIp'],
                snapshot=data['snapshot'])

            return message, 200 if success else 400

        else:
            return 'Missing params', 400


def run():
    database.resetDatabase()
    app.run(host='192.168.0.17', port=5000)
