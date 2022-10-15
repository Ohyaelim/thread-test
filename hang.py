from flask import Flask, render_template

app = Flask(__name__)

@app.route('/')
def hello():
    return 'hello'

@app.route('/test')
def autocoder():
    return 'test'

def main():
    app.run(host='127.0.0.1', debug=False, port=80)

if __name__== '__main__':
    main()