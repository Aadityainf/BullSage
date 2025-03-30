import requests
import pandas as pd

# Flask API endpoint
url = "http://127.0.0.1:5000/predict"

# List of stock tickers
tickers = ['RELIANCE.NS', 'TCS.NS', 'INFY.BO', 'HDFCBANK.BO', 'ICICIBANK.BO', 
           'ADANIPOWER.BO', 'APOLLOHOSP.BO', 'HEROMOTOCO.BO', 'MARUTI.BO', 
           'BHARTIARTL.NS', 'MRF.NS', 'WIPRO.NS','SBIN.NS', 'ITC.NS', 'KOTAKBANK.NS', 
          'BAJFINANCE.NS', 'ULTRACEMCO.NS', 'TITAN.NS', 'ASIANPAINT.NS', 'HCLTECH.NS']

# Store results
results = []

for ticker in tickers:
    data = {"ticker": ticker}
    
    try:
        response = requests.post(url, json=data)
        
        if response.status_code == 200:
            predicted_price = response.json().get("predicted_price", "N/A")
        else:
            predicted_price = "Failed"
        
        results.append({"Ticker": ticker, "Predicted Price": predicted_price})
    
    except requests.exceptions.RequestException as e:
        results.append({"Ticker": ticker, "Predicted Price": "Error"})

# Convert to DataFrame and display
df = pd.DataFrame(results)
print(df)
