import warnings
warnings.filterwarnings("ignore", message="X does not have valid feature names")

from flask import Flask, request, jsonify
import numpy as np
import tensorflow as tf
import yfinance as yf
import pickle
from datetime import datetime

app = Flask(__name__)

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

        print(f"Ticker: {ticker} → Actual Price: {actual_price} | Adjusted Predicted Price: {predicted_price}")

        return jsonify({
            "ticker": ticker,
            "actual_price": actual_price,
            "predicted_price": predicted_price
        })

    except Exception as e:
        print(f"Error occurred: {e}")
        return jsonify({'error': str(e)})

if __name__ == '__main__':
    app.run(debug=True)
