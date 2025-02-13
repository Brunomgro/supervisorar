import snap7
from snap7.util import *
import firebase_admin
from firebase_admin import credentials, db
import time


# Inicializar o Firebase
cred = credentials.Certificate("../Firebase/firebasekey.json")
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://supervisor-realidade-aumentada-default-rtdb.firebaseio.com/'
})

print("Dados enviados com sucesso!")

if (False):
    client = snap7.client.Client()
    client.connect("192.168.0.16", 0, 1, 102)
    client.get_connected()

while (False):
    data = client.db_read(numeroDoDataBlock = 1, lerAPartirDoItem = 0, QuantidadeDeBytes = 4)
    temperatura = get_int(data, indexDoByte = 0)
    nivel = get_int(data, indexDoByte = 2)
    
    db.reference('/leitura/0').set({
        "id": "maquina1",
        "type": "temperature",
        "value": temperatura
        })
    
    db.reference('/leitura/1').set({
      "id": "maquina2",
      "type": "level",
      "value": nivel
    })

    print(temperatura)
    print(nivel)
    time.sleep(0.5)