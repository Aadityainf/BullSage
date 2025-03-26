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
with open('Scaler_y.pkl', 'rb') as f:
    scaler_y = pickle.load(f)

# Date range
end_date = datetime.today().strftime('%Y-%m-%d')
start_date = (datetime.today().replace(year=datetime.today().year - 10)).strftime('%Y-%m-%d')

def get_latest_stock_data(ticker, seq_length=20, num_features=13):
    stock = yf.Ticker(ticker)
    hist = stock.history(start=start_date, end=end_date)

    if hist.empty:
        raise ValueError("Invalid ticker or no data available.")

    latest_data = hist['Close'].values[-num_features:]

    if len(latest_data) < num_features:
        raise ValueError("Not enough data for the given ticker.")

    # Scale
    scaled_data = scaler_x.transform(latest_data.reshape(1, -1))

    return scaled_data.reshape(1, seq_length, num_features)

@app.route('/')
def home():
    return "Stock Price Prediction API is Running!"

@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.get_json()
        print(f"Received data: {data}")

        if "ticker" in data:
            features = get_latest_stock_data(data["ticker"])
        else:
            raw_features = np.array(data['features']).reshape(1, -1)
            scaled_features = scaler_x.transform(raw_features).reshape(1, 20, 13)
            features = scaled_features

        print(f"Processed features: {features}")

        prediction = model.predict(features)

        predicted_price = scaler_y.inverse_transform(prediction)[0][0]
        price = round(float(predicted_price), 2)

        print(f"Predicted Price: {price}")

        return jsonify({'predicted_price': price})

    except Exception as e:
        print(f"Error occurred: {e}")
        return jsonify({'error': str(e)})

if __name__ == '__main__':
    app.run(debug=True)
