import snap7
from snap7.util import *
import firebase_admin
from firebase_admin import credentials, db

# Inicializar o Firebase
cred = credentials.Certificate("../../Firebase/supervisor-realidade-aumentada-firebase-adminsdk-fbsvc-e566db9f3a.json")
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://supervisor-realidade-aumentada-default-rtdb.firebaseio.com/'
})

# Enviar dados para o Firebase
db.reference('/Tanque/temperatura').set(80)
db.reference('/Tanque/nivel').set(80)

db.reference('/Tanque2/temperatura').set(50)
db.reference('/Tanque2/nivel').set(50)

db.reference('/motor123/temperatura').set(30)
db.reference('/motor123/velocidadeTerminal').set(30)
db.reference('/motor123/ligaDesligaTerminal').set(True)
db.reference('/motor123/setVelocidade').set(6)
db.reference('/motor123/setLigaDesliga').set(True)

db.reference('/ParadaDeEmergencia').set(False)


print("Dados enviados com sucesso!")

# client = snap7.client.Client()
# client.connect("", 0, 1, 102)
# client.get_connected()
# 
# contador = 0
# 
# while (contador < 50):
#     data = client.db_read(numeroDoDataBlock = 1, lerAPartirDoItem = 0, QuantidadeDeBytes = 2)
#     temperatura = get_int(data, indexDoByte = 0)
#     contador = contador + 1
#     print(temperatura)