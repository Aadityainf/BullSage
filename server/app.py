'''
from flask import Flask, request, jsonify, send_file
from flask_mysqldb import MySQL
import yfinance as yf
import hashlib
import base64
import matplotlib.pyplot as plt
from io import BytesIO
import tensorflow as tf
import numpy as np
app = Flask(__name__)

# Load the trained LSTM model
model = tf.keras.models.load_model("stock_price_prediction.keras")

# Define the date range
start_date = "2014-09-30"
end_date = "2024-09-30"

def get_latest_stock_data(ticker,seq_length=1,num_features=4):
    stock = yf.Ticker(ticker)
    hist = stock.history(start=start_date, end=end_date)

    if hist.empty:
        raise ValueError("Invalid ticker or no data available.")

    latest_data = hist['Close'].values[-num_features:]  # Fetch last 4 closing prices

    if len(latest_data) < num_features:
        raise ValueError("Not enough data for the given ticker.")

        return np.array(latest_data).reshape(1,seq_length,num_features) # Ensure shape (1, num_features)

# Home route - renders HTML form
@app.route('/')
def home():
    return "Stock Price Prediction API is Running!"

@app.route('/predict', methods=['POST'])
def predict():
    try:
        stock_symbols = ["RELIANCE", "TCS", "HDFCBANK", "INFY", "ITC"]
        data = request.get_json()
        if "Relian" in data:
            features = get_latest_stock_data(data["ticker"])  # Fetch real-time data
        else:
            features = np.array(data['features']).reshape(1,1,4)  # Manual input
        
        prediction = model.predict(features)

        # Ensure predicted price is between 1000 and 10000
        predicted_price = np.clip(prediction, 1000, 10000)

        return jsonify({'predicted_price': predicted_price.tolist()}) , 200 # Convert to JSON
    
    except Exception as e:
        return jsonify({'error': str(e)}), 404

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
        #stock_symbols = ["RELIANCE"]
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
'''
import warnings
warnings.filterwarnings("ignore", message="X does not have valid feature names")
from flask import Flask, request, jsonify
import yfinance as yf
import hashlib
import base64
import matplotlib.pyplot as plt
from io import BytesIO
import tensorflow as tf
import numpy as np
import pickle
from flask_mysqldb import MySQL
from datetime import datetime
app = Flask(__name__)



# MySQL configuration
app.config['MYSQL_HOST'] = 'localhost'
app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = 'password'
app.config['MYSQL_DB'] = 'stock_analysis_db'
mysql = MySQL(app)  




# Load LSTM model
model = tf.keras.models.load_model("stock_price.keras")

# Load scalers
with open("scaler_x.pkl", "rb") as f:
    scaler_x = pickle.load(f)
with open("scaler_y.pkl", "rb") as f:
    scaler_y = pickle.load(f)

# Date range
end_date = datetime.today().strftime('%Y-%m-%d')
start_date = (datetime.today().replace(year=datetime.today().year - 10)).strftime('%Y-%m-%d')

# Define required features
required_features = [
    "Open", "High", "Low", "Close", "Daily Change", "% Daily Change", 
    "Smoothed Change", "MA_10", "MA_20", "MA_50", "EMA_10", "EMA_50", 
    "EMA_200", "RSI_14", "SMA_20", "BB_Upper", "BB_Lower", "MACD"
]
# Global Tickers List
tickers = ['RELIANCE.NS', 'TCS.NS', 'INFY.BO', 'HDFCBANK.BO', 'ICICIBANK.BO', 
           'ADANIPOWER.BO', 'APOLLOHOSP.BO', 'HEROMOTOCO.BO', 'MARUTI.BO', 
           'BHARTIARTL.NS', 'MRF.NS', 'WIPRO.NS','SBIN.NS', 'ITC.NS', 'KOTAKBANK.NS', 
           'BAJFINANCE.NS', 'ULTRACEMCO.NS', 'TITAN.NS', 'ASIANPAINT.NS', 'HCLTECH.NS']

def get_latest_stock_data(ticker, seq_length=1):
    stock = yf.Ticker(ticker)
    hist = stock.history(start=start_date, end=end_date)

    if hist.empty:
        raise ValueError("Invalid ticker or no data available.")

    # Calculate required technical indicators
    hist["Daily Change"] = hist["Close"] - hist["Open"]
    hist["% Daily Change"] = (hist["Daily Change"] / hist["Open"]) * 100
    hist["Smoothed Change"] = hist["Daily Change"].rolling(5).mean()
    hist["MA_10"] = hist["Close"].rolling(10).mean()
    hist["MA_20"] = hist["Close"].rolling(20).mean()
    hist["MA_50"] = hist["Close"].rolling(50).mean()
    hist["EMA_10"] = hist["Close"].ewm(span=10).mean()
    hist["EMA_50"] = hist["Close"].ewm(span=50).mean()
    hist["EMA_200"] = hist["Close"].ewm(span=200).mean()
    hist["RSI_14"] = 100 - (100 / (1 + (hist["Daily Change"].rolling(14).mean() / hist["Daily Change"].rolling(14).std())))
    hist["SMA_20"] = hist["Close"].rolling(20).mean()
    hist["BB_Upper"] = hist["SMA_20"] + (hist["Close"].rolling(20).std() * 2)
    hist["BB_Lower"] = hist["SMA_20"] - (hist["Close"].rolling(20).std() * 2)
    hist["MACD"] = hist["EMA_10"] - hist["EMA_50"]

    # Keep only necessary features
    hist = hist[required_features].dropna()

    if len(hist) < seq_length:
        raise ValueError("Not enough data for the given ticker.")

    latest_data = hist.iloc[-seq_length:].values  # Get latest seq_length rows

    # Scale input features
    scaled_data = scaler_x.transform(latest_data)

    return scaled_data.reshape(1, seq_length, len(required_features))



# Home route
@app.route('/')
def home():
    return "Stock Price Prediction API is Running!"

@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.get_json()
        print(f"Received data: {data}")

        if "ticker" in data:
            ticker = data["ticker"]
            features = get_latest_stock_data(ticker)
        else:
            raw_features = np.array(data['features']).reshape(1, -1)
            scaled_features = scaler_x.transform(raw_features).reshape(1, 1, len(required_features))
            features = scaled_features

        print(f"Processed features for {ticker}: {features}")

        prediction = model.predict(features)
        predicted_price = scaler_y.inverse_transform(prediction)[0][0]

        # Get latest actual stock price
        stock = yf.Ticker(ticker)
        hist = stock.history(period="1d")
        actual_price = hist["Close"].values[-1] if not hist.empty else None

        # Ensure predicted price is within ±800 range of actual price
        if actual_price:
            lower_bound = max(800, actual_price - 500)  # Ensuring price is at least 1000
            upper_bound = actual_price + 500
            predicted_price = np.clip(predicted_price, lower_bound, upper_bound)

        # Round final prediction
        predicted_price = round(float(predicted_price), 2)

       
        return jsonify({
            "ticker": ticker,
            
            "predicted_price": predicted_price
        })

    except Exception as e:
        print(f"Error occurred: {e}")
        return jsonify({'error': str(e)})

@app.route('/show_prices', methods=['GET'])
def show_prices():
    try:
        stock_data = []

        for ticker in tickers:
            try:
                features = get_latest_stock_data(ticker)
                prediction = model.predict(features)
                predicted_price = scaler_y.inverse_transform(prediction)[0][0]

                # Get actual stock price
                stock = yf.Ticker(ticker)
                hist = stock.history(period="1d")
                actual_price = hist["Close"].values[-1] if not hist.empty else None

                # Adjust predicted price within ±800 range
                if actual_price:
                    lower_bound = max(800, actual_price - 500)
                    upper_bound = actual_price + 500
                    predicted_price = np.clip(predicted_price, lower_bound, upper_bound)

                stock_data.append({
                    "ticker": ticker,
                   
                    "predicted_price": round(predicted_price, 2)
                })

            except Exception as e:
                stock_data.append({"ticker": ticker, "predicted_price": "Error"})

        return jsonify(stock_data)

    except Exception as e:
        return jsonify({"error": str(e)})



@app.route('/signup', methods=['POST'])
def signup():
    try:
        data = request.get_json()
        email = data.get('email')
        username = data.get('username')
        password = data.get('password')

        if not email or not username or not password:
            return jsonify({"error": "All fields are required"}), 400

        hashed_password = hashlib.sha256(password.encode()).hexdigest()
        cursor = mysql.connection.cursor()
        cursor.execute('INSERT INTO users (email, username, password) VALUES (%s, %s, %s)', (email, username, hashed_password))
        mysql.connection.commit()
        cursor.close()

        return jsonify({"status": "success", "message": "User registered successfully"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/login', methods=['POST'])
def login():
    try:
        data = request.get_json()
        email = data.get('email')
        password = data.get('password')

        if not email or not password:
            return jsonify({"error": "Email and password are required"}), 400

        hashed_password = hashlib.sha256(password.encode()).hexdigest()
        cursor = mysql.connection.cursor()
        cursor.execute('SELECT * FROM users WHERE email = %s', (email,))
        account = cursor.fetchone()

        if account and account[3] == hashed_password:
            return jsonify({"status": "success", "message": "Login successful", "email": email}), 200
        else:
            return jsonify({"error": "Invalid email or password"}), 401
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/stocks/recent-movements', methods=['GET'])
def recent_movements():
    try:
        stock_symbols = ["RELIANCE", "TCS", "HDFCBANK", "INFY", "ITC"]
        stocks_data = []

        for symbol in stock_symbols:
            stock = yf.Ticker(symbol + ".NS")
            hist = stock.history(period="2d")
            if len(hist) >= 2:
                prev_close, latest_close = hist['Close'].iloc[-2], hist['Close'].iloc[-1]
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
        return jsonify({"error": str(e)}), 500

@app.route('/stocks/search', methods=['GET'])
def search_stock():
    try:
        query = request.args.get('query', '').upper()
        if not query:
            return jsonify({"error": "Stock symbol is required"}), 400
        
        stock = yf.Ticker(query + ".NS")
        hist = stock.history(period="7d")
        if hist.empty or len(hist) < 2:
            return jsonify({"error": "Not enough historical data available for this stock."}), 404
        
        latest_close, prev_close = hist['Close'].iloc[-1], hist['Close'].iloc[-2]
        price_change = latest_close - prev_close
        percent_change = (price_change / prev_close) * 100

        stockN = str(stock).upper()
        stockInfo = yf.Ticker(stockN)
        hist = stockInfo.history(period="5d")

        data = {
            "dates": hist.index.tolist(),
            "close": hist["Close"].map(lambda x: round(x, 2)).tolist()
        }
        
        return jsonify({
            "data": {
                "symbol": query,
                "latest_close": round(latest_close, 2),
                "price_change": round(price_change, 2),
                "percent_change": round(percent_change, 2),
                "data":data
            }
        }), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True)

