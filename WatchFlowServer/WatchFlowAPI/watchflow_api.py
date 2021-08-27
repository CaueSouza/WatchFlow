import flask
from flask import request
from .WatchFlowDatabase import database
from googlegeocoder import GoogleGeocoder
import socket

app = flask.Flask(__name__)
app.config["DEBUG"] = True

geocoder = GoogleGeocoder("AIzaSyCZEkcLehkTBSS0y3Mgx7_6aj8HHgtCK9s")

AUTHORIZATION = "Authorization"


@app.route('/allRunningCameras', methods=['GET'])
def allRunningCameras():
    if AUTHORIZATION not in request.headers:
        return 'Missing headers', 400

    headers = eval(request.headers.get(AUTHORIZATION))

    neededKeys = {'requesterUserId', 'requesterPwd'}

    if neededKeys <= headers.keys():

        shouldReturnIPs = headers['onlyIps'] if 'onlyIps' in headers else 0
        shouldReturnIPs = True if shouldReturnIPs == 1 else False

        success, message = database.getCamerasDatabaseAsJSON(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'],
            onlyIps=shouldReturnIPs)

        return message, 200 if success else 400
    else:
        return 'Missing data', 400

@app.route('/dashboardInformation', methods=['GET'])
def dashboardInformation():
    if AUTHORIZATION not in request.headers:
        return 'Missing headers', 400

    headers = eval(request.headers.get(AUTHORIZATION))

    neededHeadersKeys = {'requesterUserId', 'requesterPwd'}

    if neededHeadersKeys <= headers.keys():
        ok, message = database.getDashboardInformation(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'])

        return message, 200 if ok else 400
    else:
        return 'Missing data', 400

@app.route('/myDashboardCameras', methods=['GET'])
def myDashboardCameras():
    if AUTHORIZATION not in request.headers:
        return 'Missing headers', 400

    headers = eval(request.headers.get(AUTHORIZATION))

    neededKeys = {'requesterUserId', 'requesterPwd'}

    if neededKeys <= headers.keys():
        success, message = database.getDashboardCams(
            geocoder,
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'])

        return message, 200 if success else 400
    else:
        return 'Missing data', 400


@app.route('/saveDashboardSelectedIPs', methods=['POST'])
def saveDashboardSelectedIPs():
    if AUTHORIZATION not in request.headers or request.get_json() is None:
        return 'Missing headers', 400

    headers = eval(request.headers.get(AUTHORIZATION))
    body = request.get_json()

    neededHeadersKeys = {'requesterUserId', 'requesterPwd'}
    neededBodyKeys = {'selectedCameras'}

    if neededHeadersKeys <= headers.keys() and neededBodyKeys <= body.keys():
        success, message = database.saveDashboardSelectedCameras(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'],
            selectedCameras=body['selectedCameras'])

        return message, 200 if success else 400
    else:
        return 'Missing data', 400


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

        return message, 200

    else:
        return 'Missing data', 400


@app.route('/userInformations', methods=['GET'])
def userInformations():
    if AUTHORIZATION not in request.headers:
        return 'Missing headers', 400

    headers = eval(request.headers.get(AUTHORIZATION))

    neededHeadersKeys = {'requesterUserId', 'requesterPwd', 'userName'}

    if neededHeadersKeys <= headers.keys():
        _, message = database.userInformations(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'],
            username=headers['userName'])

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
    neededBodyKeys = {'newUserName', 'newUserPwd', 'newUserPhone'}

    if neededHeadersKeys <= headers.keys() and neededBodyKeys <= body.keys():
        success, message = database.createUser(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'],
            newUserName=body['newUserName'],
            newUserPwd=body['newUserPwd'],
            newUserPhone=body['newUserPhone'],
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

        try:
            search = geocoder.get(fullAddress)

            success, message = database.createCamera(
                requesterUserId=headers['requesterUserId'],
                requesterPwd=headers['requesterPwd'],
                cameraIp=cameraIp,
                latitude=search[0].geometry.location.lat,
                longitude=search[0].geometry.location.lng)

            return message, 200 if success else 400
        except Exception:
            return 'Invalid Address'
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


@app.route('/updatePhone', methods=['POST'])
def updatePhone():
    if AUTHORIZATION not in request.headers or request.get_json() is None:
        return 'Missing headers', 400

    headers = eval(request.headers.get(AUTHORIZATION))
    body = request.get_json()

    neededHeadersKeys = {'requesterUserId', 'requesterPwd'}
    neededBodyKeys = {'newUserPhone'}

    if neededHeadersKeys <= headers.keys() and neededBodyKeys <= body.keys():
        success, message = database.updatePhone(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'],
            newUserPhone=body['newUserPhone'])

        return message, 200 if success else 400
    else:
        return 'Missing params', 400


def run():
    database.resetDatabase()
    app.run(host=socket.gethostbyname(socket.gethostname()), port=5000)
