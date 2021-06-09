'use strict'
const fs = require('fs')
function readDatas (fileName) {
    let datas = 'NO DATA'
    if (fs.existsSync(fileName)) {
        datas = JSON.parse(fs.readFileSync(fileName, { encoding: 'utf8' }))
    }
    return datas
}
function readData (fileName, id) {
    let data = ''
    if (fs.existsSync(fileName)) {
        const datas = readDatas(fileName)
        if (datas !== 'NO DATA') {
            data = datas.filter(data => data.id === id)
        }
    }
    if (data === '' || data.length === 0) data = null
    return data
}

function saveDatas (fileName, datas) {
    const stringData = JSON.stringify(datas, null, 2)
    fs.writeFileSync(fileName, stringData, { flag: 'w+' })
}

function addData (fileName, data) {
    const datas = readDatas(fileName)
    const datasFiltred = datas.filter(oneData => oneData.id === data.id)
    if (datasFiltred.length > 0) {
        data = null
    } else {
        datas.push(data)
        saveDatas(fileName, datas)
    }
}
function updateData (fileName, data) {
    const datas = readDatas(fileName)
    readData(fileName, data.id)
    const datasUpdated = datas.map(oneData => (oneData.id === data.id ? data : oneData))
    saveDatas(fileName, datasUpdated)
}
function deleteData (fileName, data) {
    const datas = readDatas(fileName)
    readData(fileName, data.id)
    const datasUpdated = datas.filter(oneData => oneData.id !== data.id)
    saveDatas(fileName, datasUpdated)
}
module.exports = {
    readDatas,
    readData,
    saveDatas,
    addData,
    updateData,
    deleteData
}
