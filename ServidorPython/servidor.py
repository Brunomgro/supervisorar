import snap7
from snap7.util import *

import firebase_admin
from firebase_admin import credentials, db

# Inicializar o Firebase
cred = credentials.Certificate("caminho/para/seu-arquivo.json")
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://seu-projeto.firebaseio.com'
})

# Enviar dados para o Firebase
ref = db.reference('/dados')
ref.set({'temperatura': 25, 'umidade': 60})

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