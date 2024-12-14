from flask import Flask, request, jsonify, send_file
from flask_mysqldb import MySQL
import yfinance as yf
import hashlib
import base64
import matplotlib.pyplot as plt
from io import BytesIO

app = Flask(__name__)

# MySQL configuration
app.config['MYSQL_HOST'] = 'localhost'
app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = 'password'
app.config['MYSQL_DB'] = 'stock_analysis_db'

mysql = MySQL(app)

# User signup endpoint
@app.route('/signup', methods=['POST'])
def signup():
    try:
        data = request.get_json()
        email = data.get('email')
        username = data.get('username')
        password = data.get('password')

        if not email or not username or not password:
            return jsonify({"status": "error", "message": "All fields are required"}), 400

        hashed_password = hashlib.sha256(password.encode()).hexdigest()

        cursor = mysql.connection.cursor()
        cursor.execute('INSERT INTO users (email, username, password) VALUES (%s, %s, %s)', (email, username, hashed_password))
        mysql.connection.commit()
        cursor.close()

        return jsonify({"status": "success", "message": "User registered successfully"}), 201
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500

# User login endpoint
@app.route('/login', methods=['POST'])
def login():
    try:
        data = request.get_json()
        email = data.get('email')
        password = data.get('password')

        if not email or not password:
            return jsonify({"status": "error", "message": "Email and password are required"}), 400

        hashed_password = hashlib.sha256(password.encode()).hexdigest()

        cursor = mysql.connection.cursor()
        cursor.execute('SELECT * FROM users WHERE email = %s', (email,))
        account = cursor.fetchone()

        if account and account[3] == hashed_password:  # account[3] is the password column
            return jsonify({"status": "success", "message": "Login successful", "email": email}), 200
        else:
            return jsonify({"status": "error", "message": "Invalid email or password"}), 401
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500

# Fetch recent stock movements for home screen
@app.route('/stocks/recent-movements', methods=['GET'])
def recent_movements():
    try:
        stock_symbols = ["RELIANCE", "TCS", "HDFCBANK", "INFY", "ITC"]
        stocks_data = []

        for symbol in stock_symbols:
            stock = yf.Ticker(symbol + ".NS")
            hist = stock.history(period="2d")
            if len(hist) >= 2:
                prev_close = hist.iloc[-2]["Close"]
                latest_close = hist.iloc[-1]["Close"]
                price_change = latest_close - prev_close
                percent_change = (price_change / prev_close) * 100

                stocks_data.append({
                    "symbol": symbol,
                    "latest_close": round(latest_close, 2),
                    "price_change": round(price_change, 2),
                    "percent_change": round(percent_change, 2),
                })

        return jsonify({"status": "success", "data": stocks_data}), 200
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500

# Search stock and display details with graph
@app.route('/stocks/search', methods=['GET'])
def search_stock():
    try:
        query = request.args.get('query', '').upper()
        if not query:
            return jsonify({"status": "error", "message": "Stock symbol or name is required"}), 400

        stock = yf.Ticker(query + ".NS")
        hist = stock.history(period="7d")
        if hist.empty:
            return jsonify({"status": "error", "message": "Stock not found"}), 404

        latest_close = hist['Close'].iloc[-1]
        prev_close = hist['Close'].iloc[-2]
        price_change = latest_close - prev_close
        percent_change = (price_change / prev_close) * 100

        plt.figure(figsize=(10, 5))
        plt.plot(hist['Close'], marker='o', linestyle='-', color='blue', label='Close Price')
        plt.title(f'{stock.info["shortName"]} (Last 7 Days)')
        plt.xlabel('Date')
        plt.ylabel('Close Price (INR)')
        plt.grid(True)
        plt.legend()

        buffer = BytesIO()
        plt.savefig(buffer, format='png')
        buffer.seek(0)
        plt.close()
        graph_base64 = base64.b64encode(buffer.getvalue()).decode('utf-8')

        return jsonify({
            "status": "success",
            "data": {
                "name": stock.info.get("longName", query),
                "symbol": query,
                "latest_close": round(latest_close, 2),
                "price_change": round(price_change, 2),
                "percent_change": round(percent_change, 2),
                "graph_base64": graph_base64
            }
        }), 200
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True)