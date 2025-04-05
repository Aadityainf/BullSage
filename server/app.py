
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
import jwt 
from functools import wraps
from flask_mysqldb import MySQL
from datetime import datetime,timedelta

app = Flask(__name__)
app.config['SECRET_KEY'] = 'your_secret_key'


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
def generate_token(email):
    payload = {
        'email': email,
        'exp': datetime.utcnow() + timedelta(days=1)
    }
    return jwt.encode(payload, app.config['SECRET_KEY'], algorithm='HS256')

def token_required(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        token = request.headers.get('Authorization')
        if not token:
            return jsonify({'error': 'Token is missing!'}), 401
        try:
            token = token.split(" ")[1]
            data = jwt.decode(token, app.config['SECRET_KEY'], algorithms=['HS256'])
            current_email = data['email']
        except jwt.ExpiredSignatureError:
            return jsonify({'error': 'Token has expired!'}), 401
        except Exception:
            return jsonify({'error': 'Invalid token!'}), 401
        return f(current_email, *args, **kwargs)
    return decorated

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
@token_required
def predict(current_email):
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
@app.route('/watchlist', methods=['POST'])
@token_required
def add_to_watchlist(current_email):
    try:
        data = request.get_json()
        ticker = data.get('ticker')
        if not ticker:
            return jsonify({'error': 'Ticker is required'}), 400

        cursor = mysql.connection.cursor()
        cursor.execute('INSERT IGNORE INTO watchlist (email, ticker) VALUES (%s, %s)', (current_email, ticker))
        mysql.connection.commit()
        cursor.close()

        return jsonify({'status': 'success', 'message': f'{ticker} added to watchlist'}), 201
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/watchlist', methods=['GET'])
@token_required
def get_watchlist(current_email):
    try:
        cursor = mysql.connection.cursor()
        cursor.execute('SELECT ticker FROM watchlist WHERE email = %s', (current_email,))
        results = cursor.fetchall()
        cursor.close()
        return jsonify({'status': 'success', 'watchlist': [r[0] for r in results]}), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/watchlist', methods=['DELETE'])
@token_required
def remove_from_watchlist(current_email):
    try:
        data = request.get_json()
        ticker = data.get('ticker')
        if not ticker:
            return jsonify({'error': 'Ticker is required'}), 400

        cursor = mysql.connection.cursor()
        cursor.execute('DELETE FROM watchlist WHERE email = %s AND ticker = %s', (current_email, ticker))
        mysql.connection.commit()
        cursor.close()

        return jsonify({'status': 'success', 'message': f'{ticker} removed from watchlist'}), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500    

if __name__ == '__main__':
    app.run(debug=True)

