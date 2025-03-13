import requests

url = "http://127.0.0.1:5000/predict"  # Flask API endpoint
data = {"ticker": "RELIANCE.NS"}  # Replace with a valid ticker

try:
    response = requests.post(url, json=data)  # Send a POST request
    print(response.json())  # Print the predicted price response
except requests.exceptions.RequestException as e:
    print("Error:", e)
