from cgi import print_arguments
from flask import Flask, url_for 
from flask import request
from flask import redirect 
from time import sleep

import requests

app = Flask(__name__) 

@app.route('/')
def main():
    return 'hihello'

@app.route('/send') 
def connect_hang():
    temp = request.args.get('num', "1")
    # sleep(2)
    print("index : ", temp )

    receive = requests.get('http://127.0.0.1:5000/receive?num=',params=temp, timeout=5)
    # r = requests.get('https://github.com', timeout=5)

    # return redirect(url_for('re'))
    return temp

@app.route('/receive')
def receive_hang():
    print("receive")
    return 'receive hang'

if __name__ == "__main__":
    app.run()