from flask import Flask, request, render_template , jsonify
import numpy as np
import tensorflow as tf
import yfinance as yf
import pickle

app = Flask(__name__)

# Load the trained LSTM model
model = tf.keras.models.load_model("stock_price_prediction.keras")

# Load the trained Linear Regression model
'''
with open("svm_model.pkl", "rb") as file:
    model = pickle.load(file)  # Load the SVM model
'''

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
        data = request.get_json()
        if "ticker" in data:
            features = get_latest_stock_data(data["ticker"])  # Fetch real-time data
        else:
            features = np.array(data['features']).reshape(1,1,4)  # Manual input
        
        prediction = model.predict(features)

        # Ensure predicted price is between 1000 and 10000
        predicted_price = np.clip(prediction, 1000, 10000)

        return jsonify({'predicted_price': predicted_price.tolist()})  # Convert to JSON
    
    except Exception as e:
        return jsonify({'error': str(e)})

if __name__ == '__main__':
    app.run(debug=True)

