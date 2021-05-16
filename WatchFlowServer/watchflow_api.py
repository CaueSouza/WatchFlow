import flask
from flask import request
import socket
from Database import database

app = flask.Flask(__name__)
app.config["DEBUG"] = True


@app.route('/allRunningCameras', methods=['GET'])
def allRunningCameras():
    if 'Authentication' not in request.headers:
        return 'Missing headers', 400

    headers = eval(request.headers.get("Authentication"))

    neededKeys = {'requesterUserId', 'requesterPwd'}

    if neededKeys <= headers.keys():
        success, message = database.getCamerasDatabaseAsJSON(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'])

        return message, 200 if success else 400
    else:
        return 'Missing data', 400


@app.route('/usersPositions', methods=['GET'])
def usersPositions():
    if 'Authentication' not in request.headers:
        return 'Missing headers', 400

    headers = eval(request.headers.get("Authentication"))

    neededKeys = {'requesterUserId', 'requesterPwd'}

    if neededKeys <= headers.keys():
        success, jsonOutput = database.getAllLoggedUsersPositions(
            requesterUserId=headers['requesterUserId'],
            requesterPwd=headers['requesterPwd'])

        return jsonOutput, 200 if success else 400

    else:
        return 'Missing data', 400

# @app.route('/allCamerasIps', methods=['POST'])
# def allCamerasIps():
#     dataJson = request.get_json()
#     dataArgs = request.args.to_dict()

#     if dataJson is None and not bool(dataArgs):
#         return 'Missing params', 400

#     else:
#         if not bool(dataArgs):
#             data = dataJson
#         else:
#             data = dataArgs

#         neededKeys = {'requesterUserId', 'requesterPwd'}

#         if neededKeys <= data.keys():
#             success, message = database.getCamerasDatabaseAsJSON(
#                 requesterUserId=data['requesterUserId'],
#                 requesterPwd=data['requesterPwd'],
#                 onlyIps=True)

#             return message, 200 if success else 400
#         else:
#             return 'Missing params', 400


@app.route('/userLogin', methods=['POST'])
def userLogin():

    if 'Authentication' not in request.headers or request.get_json() is None:
        return 'Missing headers', 400

    headers = eval(request.headers.get("Authentication"))
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
    if 'Authentication' not in request.headers:
        return 'Missing headers', 400

    headers = eval(request.headers.get("Authentication"))

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
    if 'Authentication' not in request.headers or request.get_json() is None:
        return 'Missing headers', 400

    headers = eval(request.headers.get("Authentication"))
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
    if 'Authentication' not in request.headers:
        return 'Missing headers', 400

    headers = eval(request.headers.get("Authentication"))

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
    dataJson = request.get_json()
    dataArgs = request.args.to_dict()

    if dataJson is None and not bool(dataArgs):
        return 'Missing params', 400

    else:
        if not bool(dataArgs):
            data = dataJson
        else:
            data = dataArgs

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
    dataJson = request.get_json()
    dataArgs = request.args.to_dict()

    if dataJson is None and not bool(dataArgs):
        return 'Missing params', 400

    else:
        if not bool(dataArgs):
            data = dataJson
        else:
            data = dataArgs

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


if __name__ == '__main__':
    hostname = socket.gethostname()
    local_ip = socket.gethostbyname(hostname)

    app.run(host=local_ip, port=5000)
    # app.run()
