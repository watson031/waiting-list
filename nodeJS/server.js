
// app.listen(process.env.PORT || PORT)
'use strict'
const bodyParser = require('body-parser')
const dao = require('./dao')
const express = require('express')
// const fetch = require('node-fetch')
const app = express()

// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: true }))
app.use(function (request, response, next) {
    response.header('Access-Control-Allow-Origin', '*')
    response.header('Access-Control-Allow-Headers', 'Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With')
    response.header('Access-Control-Allow-Methods', 'POST, PUT, GET, DELETE, OPTIONS')
    response.header('Access-Control-Allow-Credentials', 'false')
    next()
})
// parse application/json
app.use(bodyParser.json())

const PORT = process.env.PORT || 5000
const HTTP_OK = 200
const CONTENT_TYPE_JSON = 'application/json'
// eslint-disable-next-line no-undef
const { CarQuery } = require('car-query')
const carQuery = new CarQuery()
/** fonctions utiles */
function sendResponse (response, datas) {
    response.writeHead(HTTP_OK, { 'content-type': CONTENT_TYPE_JSON })
    response.end(JSON.stringify(datas))
}
/**
  * Gestion de la liste d'attente
 */

/** response aus differents requetes */
app.get('/users', async (req, res) => {
    await dao.connect()
    await dao.query('SELECT * FROM user_garage;', [], (result) => {
        console.log(result)
        sendResponse(res, result.rows)
    })
    dao.disconnect()
})

/**
 *recuperer un user selon id
 */
app.get('/user/:id', async (request, response) => {
    await dao.connect()
    await dao.query('Select * from user_garage where id=$1', [request.params.id], (result) => {
        sendResponse(response, result.rows)
    })
    dao.disconnect()
})
/**
 * ajouter un new  user
 *
 */
app.post('/user', async (request, response) => {
    await dao.connect()
    await dao.query('INSERT INTO user_garage(firstName, email, password, level, phone, lastName) VALUES ($1, $2, $3, $4, $5, $6)'
        , [request.body.firstName, request.body.email, request.body.password, request.body.level, request.body.phone, request.body.lastName], (result) => {
            sendResponse(response, result)
        })
    dao.disconnect()
})
/**
 * Recuperer tous les marques les Annnnes et les Models A partir de API et l'envoyer A watson
 */
app.get('/carYears', async (req, res) => {
    carQuery.getYears()
        .then(years => {
            sendResponse(res, years)
        })
        .catch(e => console.log(e))
})
app.get('/carMakes/:year', async (req, res) => {
    carQuery.getMakes(req.params.year)
        .then(makes => {
            sendResponse(res, makes)
        })
        .catch(e => console.log(e))
})
app.get('/carMakesName/:year', async (req, res) => {
    carQuery.getMakes(req.params.year)
        .then(makes => {
            const MakesNames = makes.map(make => make.display)
            sendResponse(res, MakesNames)
        })
        .catch(e => console.log(e))
})
app.get('/carModel/:year/:make', async (req, res) => {
    const searchCriteria = {
        year: req.params.year,
        make: req.params.make
    }
    carQuery.getModels(searchCriteria)
        .then(models => {
            const modelss = models.map(make => make.name)
            sendResponse(res, modelss)
        })
        .catch(e => console.log(e))
})

/**
 *Recuperer la vehicule selon id_user
 *
 */
app.get('/voiture/:id', async (request, response) => {
    await dao.connect()
    await dao.query('Select * from car where id=$1', [request.params.id], (result) => {
        sendResponse(response, result.rows)
    })
    dao.disconnect()
})
/**
 * Ajouter une Car dans La BD
 */
app.post('/car', async (request, response) => {
    await dao.connect()
    await dao.query('INSERT INTO car(brand, year, id_user) VALUES ($1, $2, $3);', [request.body.brand, request.body.year, request.body.idUser], result => {
        dao.query('SELECT MAX(id) from model', [], result => {
            const nextID = result.rows[0].id + 1
            dao.query('INSERT INTO model(id_car, model_name) VALUES ($1, $2);'
                , [nextID, request.body.model], (result) => {
                    sendResponse(response, result)
                })
        })
    })

    dao.disconnect()
})
/**
 *
 * update car Information
 */
app.put('/user', async (request, response) => {
    await dao.connect()
    await dao.query('UPDATE car SET  brand= $1, year=$2 WHERE id = $3 ; UPDATE model SET  model_name=? WHERE id_car;', [], (result) => {
        sendResponse(response, result)
    })
})
/// //******************Gestion de waiting list */
const nodeFs = require('./node-fs')
const WAITING_LIST_FILE = 'waiting-list.json'
/**
 * Employee Available
 */
app.get('/available/:idEmployee', (req, res) => {

})

// Estimated Time de 15min
const ESTIMATIED_WAITING_TIME = new Date(null, null, null, null, 15, 0, 0)
// conteneur de la liste d'attente des utilisateur
let waitingList = nodeFs.readDatas(WAITING_LIST_FILE)
/**
  * Waiting list
  */

/** envoyer toute la liste d'attente au cas ou l'utilsateur close l'application */
app.get('/waitingList', (req, res) => {
    sendResponse(res, nodeFs.readDatas(WAITING_LIST_FILE))
})
/** envoyer les informations d'un utilisateur */
app.get('/waitingList/:idUser', (req, res) => {
    sendResponse(res, nodeFs.readData(WAITING_LIST_FILE, parseInt(req.params.idUser)))
})
/**
  * ajouter un utilisateur au waiting list
  */
app.get('/addToWaitingList/:idUser', (req, res) => {
    waitingList = nodeFs.readDatas(WAITING_LIST_FILE)
    const userEstimatedTime = ESTIMATIED_WAITING_TIME.getMinutes() * (waitingList.length + 1)
    const userWaitingInfos = { id: parseInt(req.params.idUser), numDansLaListe: waitingList.length + 1, estimatedWaitingTime: userEstimatedTime }
    nodeFs.addData(WAITING_LIST_FILE, userWaitingInfos)
    sendResponse(res, nodeFs.readData(WAITING_LIST_FILE, parseInt(req.params.idUser)))
})
/**
 * delete a user from waitingList
 */
app.get('/deleteToWaitingList/:idUser', (req, res) => {
    waitingList = nodeFs.readDatas(WAITING_LIST_FILE)
    // mettre A jour le temps d'attente de chaque utilisateur
    waitingList.forEach(userInWaitingList => {
        const userEstimatedTime = userInWaitingList.estimatedWaitingTime - ESTIMATIED_WAITING_TIME.getMinutes()
        const userUpdated = { id: userInWaitingList.id, numDansLaListe: userInWaitingList.numDansLaListe - 1, estimatedWaitingTime: userEstimatedTime }
        nodeFs.updateData(WAITING_LIST_FILE, userUpdated)
    })
    // supprimer l'utilisateur de la liste
    const userToDeleteInWaitingList = waitingList.filter(userInWaitingList => userInWaitingList.id === parseInt(req.params.idUser))
    nodeFs.deleteData(WAITING_LIST_FILE, userToDeleteInWaitingList[0])
    waitingList = nodeFs.readData(WAITING_LIST_FILE, parseInt(req.params.idUser))
    sendResponse(res, waitingList)
})
app.listen(PORT, () => console.log(`Listening on ${PORT}`))
