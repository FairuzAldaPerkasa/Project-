# app.py

from flask import Flask, request, jsonify, render_template
from model import predict_rad, predict_condition
from keras.models import load_model
import joblib
import logging

app = Flask(__name__)

# Konfigurasi logging
logging.basicConfig(filename='api.log', level=logging.ERROR, 
                    format='%(asctime)s %(levelname)s: %(message)s')

# Load model dan scaler di sini
try:
    classification_model = load_model('Model/classification_model.h5')
    regression_model = load_model('Model/deep_learning_regression_model.h5')
    classification_scaler = joblib.load('Model/scaler_classification.pkl')
    regression_scaler = joblib.load('Model/scaler_deep_learning_regression.pkl')
    print("Model dan scaler berhasil di-load.")
except Exception as e:
    print(f"Error saat me-load model atau scaler: {e}")

@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == 'POST':
        try:
            data = request.form.to_dict()

            for key in data:
                data[key] = float(data[key])

            predicted_rad_value = predict_rad(data, regression_scaler, regression_model)

            data['rad_m'] = predicted_rad_value

            predicted_condition_value = predict_condition(data, classification_scaler, classification_model)

            return render_template('index.html', prediction=predicted_condition_value, rad=predicted_rad_value, data=data)

        except Exception as e:
            logging.error(f"Error: {e}")
            return "Terjadi error saat melakukan prediksi.", 500
    else:
        return render_template('index.html', prediction=None)

if __name__ == '__main__':
    app.run(debug=True)