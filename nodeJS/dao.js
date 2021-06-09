
const { Pool } = require('pg')

let client = {}
let pool = {}
async function connect () {
    const connectionString = 'postgres://lmcyzwkpekqgxv:90d926a3931a121986f84266659d42e391ae9e1ec4340e7c45e770e5bc9c11fe@ec2-52-205-3-3.compute-1.amazonaws.com:5432/d5eohgl7caul65'
    pool = new Pool({
        connectionString: connectionString
    })
    client = await pool.connect()
}

async function query (query, values, resultCallback) {
    console.log(values)
    await client.query(query, values)
        .then(result => {
            console.log('quering...')
            resultCallback(result)
        })
        .catch(error => console.log(error))
    // const result = await client.query('SELECT * FROM user_garage')
}

function disconnect () {
    pool.end()
}

module.exports = {
    connect: connect,
    disconnect: disconnect,
    query: query
}
