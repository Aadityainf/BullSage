import requests
import pandas as pd

url = "http://127.0.0.1:5000/predict"  # Flask API endpoint

tickers_df = pd.read_csv('Tickers.csv')  # Ensure this file contains updated tickers
tickers = tickers_df['Ticker'].tolist()

for ticker in tickers:
    data = {"ticker": ticker}
    try:
        response = requests.post(url, json=data)
        if response.status_code == 200:
            print(f"Ticker: {ticker} → Predicted Price: {response.json().get('predicted_price')}")
        else:
            print(f"Ticker: {ticker} → Failed! Status Code: {response.status_code}")
    except requests.exceptions.RequestException as e:
        print(f"Ticker: {ticker} → Error:", e)
